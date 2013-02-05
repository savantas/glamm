package gov.lbl.glamm.server.kbase.dao.impl;

import gov.doe.kbase.fba.Biochemistry;
import gov.doe.kbase.fba.CompoundFlux;
import gov.doe.kbase.fba.FBA;
import gov.doe.kbase.fba.FBAFormulation;
import gov.doe.kbase.fba.FBAModel;
import gov.doe.kbase.fba.GeneAssertion;
import gov.doe.kbase.fba.MetaboliteProduction;
import gov.doe.kbase.fba.MinimalMediaPrediction;
import gov.doe.kbase.fba.ModelCompound;
import gov.doe.kbase.fba.ModelReaction;
import gov.doe.kbase.fba.ReactionFlux;
import gov.doe.kbase.fba.bound;
import gov.doe.kbase.fba.constraint;
import gov.doe.kbase.fba.fbaModelServices;
import gov.doe.kbase.fba.get_biochemistry_params;
import gov.doe.kbase.fba.get_fbas_params;
import gov.doe.kbase.fba.get_models_params;
import gov.doe.kbase.fba.term;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;
import gov.lbl.glamm.server.kbase.dao.KBMetabolicModelDAO;
import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.User;
import gov.lbl.glamm.shared.model.kbase.fba.KBBound;
import gov.lbl.glamm.shared.model.kbase.fba.KBCompoundFlux;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAConstraint;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAConstraintTerm;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAFormulation;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;
import gov.lbl.glamm.shared.model.kbase.fba.KBGeneAssertion;
import gov.lbl.glamm.shared.model.kbase.fba.KBMetaboliteProduction;
import gov.lbl.glamm.shared.model.kbase.fba.KBMinimalMediaPrediction;
import gov.lbl.glamm.shared.model.kbase.fba.KBReactionFlux;
import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;
import gov.lbl.glamm.shared.model.util.Xref;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KBMetabolicModelDAOImpl implements KBMetabolicModelDAO {
	private static final String fbaURL = "http://bio-data-1.mcs.anl.gov/services/fba";
	private GlammSession sm;
	private static fbaModelServices fbaClient;

	/**
	 * THINGS FOR CACHING
	 * ------------------
	 * Eventually, some of this should be moved somewhere more... transient. Like a user session store
	 * or something. (part of the User class? And only when logged in? I dunno.)
	 * 
	 * For now, dump it all here, and hope it doesn't swell up to the GB range.
	 */
	// key = "biochemistry-biochemistry_workspace"
	// value = Map<seed_id, kegg_id>
	private static Map<String, Map<String, String>> biochemistrySeed2Kegg;
	// key = "model-model_workspace" 
	// value = "biochemistry-biochemistry-workspace"
	private static Map<String, String> model2Biochemistry;
	
	static {
		biochemistrySeed2Kegg = new HashMap<String, Map<String, String>>();
		model2Biochemistry = new HashMap<String, String>();
		
		try {
			fbaClient = new fbaModelServices(fbaURL);
		} catch (MalformedURLException e) {
			fbaClient = null;
		}
	}
	
	public KBMetabolicModelDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}
	
	public KBMetabolicModel getModel(final String modelId, final String workspaceId) {
		if (modelId == null || workspaceId == null)
			return null;

		List<String> workspaceList = new ArrayList<String>();
		List<String> modelIdList = new ArrayList<String>();
		
		modelIdList.add(modelId);
		workspaceList.add(workspaceId);
		
		List<FBAModel> modelList = getModels(modelIdList, workspaceList);
		
		if (modelList != null && !modelList.isEmpty())
			return processModel(modelList.get(0));
		
		return null;
	}
	
	private List<FBAModel> getModels(List<String> modelIds, List<String> workspaceIds) {
		if (modelIds == null || workspaceIds == null || modelIds.size() != workspaceIds.size())
			return null;

		get_models_params params = new get_models_params();
		if (sm.getUser() != User.guestUser())
			params.auth = sm.getUser().getAuth();

		params.workspaces = workspaceIds;
		params.models = modelIds;
		try {
			List<FBAModel> modelList = fbaClient.get_models(params);
			if (modelList == null || modelList.isEmpty())
				return null;
			else {
				for (FBAModel model : modelList) {
					if (model.id != null && model.biochemistry != null && 
						model.workspace != null && model.biochemistry_workspace != null)
						model2Biochemistry.put(model.id + "-" + model.workspace, model.biochemistry + "-" + model.biochemistry_workspace);
				}
				return modelList;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
	public KBMetabolicModel getModel(KBWorkspaceObjectData modelData) {
		return getModel(modelData.getId(), modelData.getWorkspace());
	}
	
	private KBMetabolicModel processModel(FBAModel kbModel) {
		KBMetabolicModel model = new KBMetabolicModel();
		model.setId(kbModel.id);
		model.setName(kbModel.name);
		model.setWorkspace(kbModel.workspace);
		model.setBiochemistryName(kbModel.biochemistry);
		model.setBiochemistryWorkspace(kbModel.biochemistry_workspace);
		model.setGenomeName(kbModel.genome);
		model.setGenomeWorkspace(kbModel.genome_workspace);
		model.setMapName(kbModel.map);
		model.setMapWorkspace(kbModel.map_workspace);
		model.setStatus(kbModel.status);
		model.setType(kbModel.type);
		
		Map<String, String> biochem = getBiochemistryMap(kbModel);

		Set<String> cpdIds = new HashSet<String>();
		for(ModelCompound mCpd : kbModel.compounds) {
			cpdIds.add(biochem.get(mCpd.compound));
		}
		
		List<String> rxnIds = new ArrayList<String>();
		for(ModelReaction mRxn : kbModel.reactions) {
			rxnIds.add(biochem.get(mRxn.reaction));
//			System.out.println(mRxn.id + "\t" + mRxn.name + "\t" + mRxn.reaction);
		}
	
		ReactionDAO reactionDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> reactions = reactionDao.getReactions(rxnIds);
		model.addReactions(reactions);

		CompoundDAO compoundDao = new CompoundGlammDAOImpl(sm);
		Set<Compound> compounds = compoundDao.getCompounds(cpdIds);
		model.addCompounds(compounds);
		
		return model;
	}
	
	public KBFBAResult getFBAResult(final String fbaId, final String workspaceId) {
		if (fbaId == null || workspaceId == null)
			return null;
		
		get_fbas_params params = new get_fbas_params();
		
		if (sm.getUser() != User.guestUser())
			params.auth = sm.getUser().getAuth();

		List<String> workspaceList = new ArrayList<String>();
		List<String> fbaIdList = new ArrayList<String>();
		
		fbaIdList.add(fbaId);
		workspaceList.add(workspaceId);
		params.workspaces = workspaceList;
		params.fbas = fbaIdList;
		try {
			List<FBA> fbaList = fbaClient.get_fbas(params);
			if (fbaList == null || fbaList.isEmpty())
				return null;
			else
				return processFBA(fbaList.get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private KBFBAResult processFBA(FBA fba) {
//		System.out.println("PROCESSING FBA RESULTS");
//		System.out.println(fba.id);
//		System.out.println(fba.model);
//		System.out.println(fba.workspace);
//		System.out.println(fba.objective);
		
		KBFBAResult result = new KBFBAResult();
		
		result.setId(fba.id);
		result.setWorkspace(fba.workspace);
		result.setModel(fba.model);
		result.setModelWorkspace(fba.model_workspace);
		result.setObjective(fba.objective);
		result.setComplete(fba.isComplete);
		
		FBAFormulation form = fba.formulation;
		
	    // more stuff.
		KBFBAFormulation formulation = new KBFBAFormulation();
		formulation.setMediaId(form.media);
		formulation.setMediaWorkspace(form.media_workspace);
		formulation.setPromModelId(form.prommodel);
		formulation.setPromModelWorkspace(form.prommodel_workspace);
		formulation.setObjectiveFraction(form.objfraction);
		formulation.setAllReversible(form.allreversible != 0);
		formulation.setMaximizeObjective(form.allreversible != 0);
		formulation.setDefaultMaxFlux(form.defaultmaxflux);
		formulation.setDefaultMinUptake(form.defaultminuptake);
		formulation.setDefaultMaxUptake(form.defaultmaxuptake);
		formulation.setUseSimpleThermodynamicConstraints(form.simplethermoconst != 0);
		formulation.setUseThermodynamicConstraints(form.simplethermoconst != 0);
		formulation.setNoThermodynamicsError(form.nothermoerror != 0);
		formulation.setMinThermodynamicsError(form.minthermoerror != 0);
		formulation.setAdditionalCompounds(form.additionalcpds);
		//TODO - see if we need to translate names.
		formulation.setGeneKO(form.geneko);
		//TODO - see if we need to translate names.
		formulation.setReactionKO(form.rxnko);
		//TODO - see if we need to translate names.
		formulation.setUptakeLimits(form.uptakelim);

		List<KBFBAConstraintTerm> terms = new ArrayList<KBFBAConstraintTerm>();
		for (term t : form.objectiveTerms) {
			//TODO - see if we need to translate names.
			KBFBAConstraintTerm tNew = new KBFBAConstraintTerm();
			tNew.setCoefficient(t.coefficient);
			tNew.setVariable(t.variable);
			tNew.setVariableType(t.varType);
			terms.add(tNew);
		}
		formulation.setObjectiveTerms(terms);

		List<KBBound> bounds = new ArrayList<KBBound>();
		for (bound b : form.bounds) {
			//TODO - see if we need to translate names.
			KBBound bNew = new KBBound();
			bNew.setMax(b.max);
			bNew.setMin(b.min);
			bNew.setVariable(b.variable);
			bNew.setVariableType(b.varType);
			bounds.add(bNew);
		}
		formulation.setBounds(bounds);

		List<KBFBAConstraint> constraints = new ArrayList<KBFBAConstraint>();
		for (constraint c : form.constraints) {
			//TODO - see if we need to translate names.
			KBFBAConstraint cNew = new KBFBAConstraint();
			cNew.setName(c.name);
			cNew.setRhs(c.rhs);
			cNew.setSign(c.sign);
			List<KBFBAConstraintTerm> cTerms = new ArrayList<KBFBAConstraintTerm>();
			for (term t : c.terms) {
				//TODO - see if we need to translate names.
				KBFBAConstraintTerm tNew = new KBFBAConstraintTerm();
				tNew.setCoefficient(t.coefficient);
				tNew.setVariable(t.variable);
				tNew.setVariableType(t.varType);
				cTerms.add(tNew);
			}
			cNew.setTerms(cTerms);
			constraints.add(cNew);
		}
		formulation.setConstraints(constraints);

		result.setFormulation(formulation);
		
		List<KBMinimalMediaPrediction> minimalMediaPredictions = new ArrayList<KBMinimalMediaPrediction>();
		for (MinimalMediaPrediction p : fba.minimalMediaPredictions) {
			//TODO - see if we need to translate names.
			KBMinimalMediaPrediction pNew = new KBMinimalMediaPrediction();
			pNew.setEssentialNutrients(p.essentialNutrients);
			pNew.setOptionalNutrients(p.optionalNutrients);
			minimalMediaPredictions.add(pNew);
		}
		
		result.setMinimalMediaPredictions(minimalMediaPredictions);
		
		List<KBMetaboliteProduction> metaboliteProductions = new ArrayList<KBMetaboliteProduction>();
		for (MetaboliteProduction p : fba.metaboliteProductions) {
			//TODO - see if we need to translate names.
			KBMetaboliteProduction pNew = new KBMetaboliteProduction();
			pNew.setMaximumProduction(p.maximumProduction);
			pNew.setModelCompoundId(p.modelcompound);
			metaboliteProductions.add(pNew);
		}
		
		result.setMetaboliteProductions(metaboliteProductions);
		
	    List<KBReactionFlux> reactionFluxes = new ArrayList<KBReactionFlux>();
	    for (ReactionFlux f : fba.reactionFluxes) {
	    	//TODO - see if we need to translate names
	    	KBReactionFlux fNew = new KBReactionFlux();
	    	fNew.setDefintion(f.definition);
	    	fNew.setLowerBound(f.lowerBound);
	    	fNew.setMax(f.max);
	    	fNew.setMin(f.min);
	    	fNew.setModelReactionId(f.reaction);
	    	fNew.setType(f.type);
	    	fNew.setUpperBound(f.upperBound);
	    	fNew.setValue(f.value);
	    	reactionFluxes.add(fNew);
	    }

	    result.setReactionFluxes(reactionFluxes);
	    
	    List<KBCompoundFlux> compoundFluxes = new ArrayList<KBCompoundFlux>();
	    for (CompoundFlux f : fba.compoundFluxes) {
	    	//TODO - see if we need to translate names
	    	KBCompoundFlux fNew = new KBCompoundFlux();
	    	fNew.setLowerBound(f.lowerBound);
	    	fNew.setMax(f.max);
	    	fNew.setMin(f.min);
	    	fNew.setModelCompoundId(f.compound);
	    	fNew.setName(f.name);
	    	fNew.setType(f.type);
	    	fNew.setUpperBound(f.upperBound);
	    	fNew.setValue(f.value);
	    	compoundFluxes.add(fNew);
	    }
	    
	    result.setCompoundFluxes(compoundFluxes);
	    
	    List<KBGeneAssertion> geneAssertions = new ArrayList<KBGeneAssertion>();
	    for (GeneAssertion a : fba.geneAssertions) {
	    	//TODO - see (again) if we need to translate names
	    	KBGeneAssertion aNew = new KBGeneAssertion();
	    	aNew.setEssential(a.isEssential != 0);
	    	aNew.setFeatureId(a.feature);
	    	aNew.setGrowth(a.growth);
	    	aNew.setGrowthFraction(a.growthFraction);
	    	geneAssertions.add(aNew);
	    }
	    
	    result.setGeneAssertions(geneAssertions);

	    /**
	     * Okay, so,
	     * We're rolling like this for now:
	     * 1. Get FBA from workspace
	     * 2. Translate FBA into something Serializable 
	     *    (might be undone eventually, if I can fix the Java client maker, but it's fast enough to do)
	     * 3. Get the Model the FBA is based on from workspace
	     * 4. Use the Model to get the Biochemistry from workspace
	     * 5. Use the Model to get the mapping from ModelReactions to Reactions
	     * 6. Use the biochemistries to map from Reactions -> KEGG ids
	     * 7. Map the KEGG ids back to the ReactionFluxes
	     * 8. Use the KEGG ids to grab the GLAMM reactions from the DB.
	     * 9. Use the Reaction's Xrefs to get the KEGG ids
	     * 10. FINALLY we can map the value from the ReactionFlux into the GLAMM Reaction that the 
	     *     AnnotatedMapPresenter can use.
	     * 11. Return this whole mess.
	     * 12. Cry a little.
	     */
	    
	    List<String> modelId = new ArrayList<String>();
		List<String> workspaceId = new ArrayList<String>();
		modelId.add(fba.model);
		workspaceId.add(fba.model_workspace);
		
		FBAModel model = getRawWorkspaceModel(fba.model, fba.model_workspace);
		
		// model_thingy_id -> biochem_id mapping.
		Map<String, String> componentId2BiochemId = new HashMap<String, String>();
		for (ModelCompound cpd : model.compounds)
			componentId2BiochemId.put(cpd.id, cpd.compound);
		for (ModelReaction rxn : model.reactions)
			componentId2BiochemId.put(rxn.id, rxn.reaction);

		Map<String, String> biochem = getBiochemistryMap(model);
		if (biochem == null) // if, after all that, we don't have the biochemistry, just give up already.
			return null;

		float maxFluxBound = Float.MIN_VALUE;
		float minFluxBound = Float.MAX_VALUE;
		float maxFluxValue = Float.MIN_VALUE;
		float minFluxValue = Float.MAX_VALUE;
		Map<String, KBReactionFlux> kegg2ReactionFlux = new HashMap<String, KBReactionFlux>();
	    for (KBReactionFlux flux : result.getReactionFluxes()) {
	    	if (maxFluxValue < flux.getValue())
	    		maxFluxValue = flux.getValue();
	    	if (minFluxValue > flux.getValue())
	    		minFluxValue = flux.getValue();
	    	if (maxFluxBound < flux.getUpperBound())
	    		maxFluxBound = flux.getUpperBound();
	    	if (minFluxBound > flux.getLowerBound())
	    		minFluxBound = flux.getLowerBound();
	    	kegg2ReactionFlux.put(biochem.get(componentId2BiochemId.get(flux.getModelReactionId())), flux);
//	    	System.out.println(flux.getModelReactionId() + "\t" + biochem.get(flux.getModelReactionId()) + "\t" + componentId2BiochemId.get(flux.getModelReactionId()) + "\t" + biochem.get(componentId2BiochemId.get(flux.getModelReactionId())));
	    }
	    
	    result.setMaxFluxValue(maxFluxValue);
	    result.setMinFluxValue(minFluxValue);
	    result.setGlobalUpperBound(maxFluxBound);
	    result.setGlobalLowerBound(minFluxBound);
	    
		ReactionDAO reactionDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> reactions = reactionDao.getReactions(kegg2ReactionFlux.keySet());

		for (Reaction rxn : reactions) {
			Set<Measurement> measurements = new HashSet<Measurement>();
			Xref xref = rxn.getXrefSet().getXrefForDbName("LIGAND-RXN");
			if (xref != null) {
				String keggId = rxn.getXrefSet().getXrefForDbName("LIGAND-RXN").getXrefId();
//				System.out.println(rxn.getGuid() + " - " + keggId);
				Measurement m = new Measurement(fba.id, "", kegg2ReactionFlux.get(keggId).getValue(), 1, rxn.getGuid());
				measurements.add(m);
				rxn.getMeasurementSet().setMeasurements(measurements);
			}
		}
		result.setReactionValues(reactions);
		

//		System.out.println("-----------------");
//	    System.out.println("done processing!!");

		
		
		
		return result;
	
	}

	private FBAModel getRawWorkspaceModel(String model, String workspace) {
		List<String> workspaceList = new ArrayList<String>();
		List<String> modelIdList = new ArrayList<String>();
		
		modelIdList.add(model);
		workspaceList.add(workspace);
		
		get_models_params params = new get_models_params();
		if (sm.getUser() != User.guestUser())
			params.auth = sm.getUser().getAuth();
		
		params.models = modelIdList;
		params.workspaces = workspaceList;
		
		try {
			List<FBAModel> models = fbaClient.get_models(params);
			if (models == null || models.isEmpty())
				return null;
			else
				return models.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Map<String, String> getBiochemistryForFBA(FBA fba) {
		String modelLookupKey = fba.model + "-" + fba.model_workspace;

		if (model2Biochemistry.containsKey(modelLookupKey) &&
			biochemistrySeed2Kegg.containsKey(model2Biochemistry.get(modelLookupKey))) {
				return biochemistrySeed2Kegg.get(model2Biochemistry.get(modelLookupKey));
		}
		else {
			List<String> ids = new ArrayList<String>();
			ids.add(fba.model);
			List<String> workspaces = new ArrayList<String>();
			workspaces.add(fba.model_workspace);

			List<FBAModel> modelList = getModels(ids, workspaces);
			if (modelList != null && !modelList.isEmpty()) {
				return getBiochemistryMap(modelList.get(0));
			}
			return null;
		}
	}
	
	// If it doesn't find it, downloads the chemistry and sets up the relationships for caching it.
	private Map<String, String> getBiochemistryMap(FBAModel model) {

		String mapId = model.biochemistry + "-" + model.biochemistry_workspace;

		// need to fetch biochemistry, if we don't have it already.
		if (!biochemistrySeed2Kegg.containsKey(mapId)) {
			try {
				get_biochemistry_params params = new get_biochemistry_params();
				if (sm.getUser() != User.guestUser())
					params.auth = sm.getUser().getAuth();
				params.biochemistry = model.biochemistry;
				params.biochemistry_workspace = model.biochemistry_workspace;
				params.id_type = "ModelSEED";

				Biochemistry biochemSEED = fbaClient.get_biochemistry(params);
				
				params.id_type = "KEGG";
				Biochemistry biochemKEGG = fbaClient.get_biochemistry(params);
				
				Map<String, String> lookup = new HashMap<String, String>();

				for (int i=0; i<biochemSEED.compounds.size(); i++) {
					lookup.put(biochemSEED.compounds.get(i), biochemKEGG.compounds.get(i));
				}

				for (int i=0; i<biochemSEED.reactions.size(); i++) {
					lookup.put(biochemSEED.reactions.get(i), biochemKEGG.reactions.get(i));
				}
				
				biochemistrySeed2Kegg.put(mapId, lookup);
				model2Biochemistry.put(model.id + "-" + model.workspace, mapId);
			} catch (Exception e) {
				System.out.println("Unable to fetch biochemistry to resolve model");
				return null;
			}
		}

		return biochemistrySeed2Kegg.get(mapId);
	}
	
}