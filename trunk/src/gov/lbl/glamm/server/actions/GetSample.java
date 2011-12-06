package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasSynonyms;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service class for getting the primitives (genes, reactions, or compounds) along with their measurements for a given sample.
 * @author jtbates
 *
 */
public class GetSample  {
	
	/**
	 * Gets the set of primitives and measurements for a given sample.
	 * @param sm The GLAMM session.
	 * @param sample The sample.
	 * @return The set of primitives and measurements for the sample.
	 */
	public static Set<? extends HasMeasurements> getMeasurementsForSample(final GlammSession sm, final Sample sample) {
		
		if(sample == null)
			return new HashSet<HasMeasurements>();
		
		// set up DAO
		ExperimentDAO expDao = new ExperimentDAOImpl(sm);
		
		// get id2Measurements mapping for experiment
		Map<String, Set<Measurement>> id2Measurements = expDao.getMeasurements(sample.getExperimentId(), sample.getSampleId());
		
		switch(sample.getTargetType()) {
		case COMPOUND:
			return getCompoundsForMeasurements(sm, id2Measurements);
		case GENE:
			return getReactionsForGenesForMeasurements(sm, expDao, sample, id2Measurements);
		case REACTION:
			return getReactionsForMeasurements(sm, id2Measurements);
		}
		
		return new HashSet<HasMeasurements>();
	}
	
	private static Set<? extends HasMeasurements> getCompoundsForMeasurements(final GlammSession sm, 
			final Map<String, Set<Measurement>> id2Measurements) {
		
		// set up DAO
		CompoundDAO cpdDao = new CompoundGlammDAOImpl(sm);
		Set<Compound> compounds = cpdDao.getCompounds(id2Measurements.keySet());
		
		for(Compound compound : compounds) {
			for(Xref xref : compound.getXrefSet().getXrefs()) {
				Set<Measurement> measurements = id2Measurements.get(xref.getXrefId());
				if(measurements != null) {
					compound.getMeasurementSet().setMeasurements(measurements);
					break;
				}
			}
		}
		
		return compounds;
	}
	
	private static Set<? extends HasMeasurements> getReactionsForGenesForMeasurements(final GlammSession sm, 
			final ExperimentDAO expDao,
			final Sample sample, 
			final Map<String, Set<Measurement>> id2Measurements) {
		
		// set up DAOs
		GeneDAO geneDao = new GeneDAOImpl(sm);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		
		// get taxonomyId
		String taxonomyId = expDao.getTaxonomyIdForExperimentId(sample.getExperimentId()); 
		
		// get the genes corresponding to these measurements
		Set<Gene> genes = null;
		if(sm.isSessionExperiment(sample.getExperimentId()))
			genes = geneDao.getGenesForSynonyms(taxonomyId, id2Measurements.keySet());
		else
			genes = geneDao.getGenesForVimssIds(taxonomyId, id2Measurements.keySet());
		
		// add measurements to genes
		for(Gene gene : genes) {
			Set<Synonym> synonyms = ((HasSynonyms) gene).getSynonyms();
			for(Synonym synonym : synonyms) {
				Set<Measurement> measurements = id2Measurements.get(synonym.getName());
				if(measurements != null)
					for(Measurement measurement : measurements)
						gene.getMeasurementSet().addMeasurement(measurement);
			}
		}
		
		// assemble map of ecNums to Genes
		Map<String, Set<Gene>> ecNum2Genes = new HashMap<String, Set<Gene>>();
		for(Gene gene : genes) {
			for(String ecNum : gene.getEcNums()) {
				Set<Gene> genes4EcNum = ecNum2Genes.get(ecNum);
				if(genes4EcNum == null) {
					genes4EcNum = new HashSet<Gene>();
					ecNum2Genes.put(ecNum, genes4EcNum);
				}
				genes4EcNum.add(gene);
			}
		}
		
		// get set of reactions corresponding to those ecNums
		Set<Reaction> rxns = rxnDao.getReactionsForEcNums(ecNum2Genes.keySet());
		
		// add genes to rxns
		for(Reaction rxn : rxns) {
			for(String ecNum : rxn.getEcNums()) {
				Set<Gene> genes4EcNum = ecNum2Genes.get(ecNum);
				if(genes4EcNum != null)
					for(Gene gene : genes4EcNum)
						rxn.addGene(gene);
			}
		}
		
		return rxns;
	}
	
	private static Set<? extends HasMeasurements> getReactionsForMeasurements(final GlammSession sm, 
			final Map<String, Set<Measurement>> id2Measurements) {
		
		ReactionDAO reactionDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> reactions = reactionDao.getReactions(id2Measurements.keySet());
		
		for(Reaction reaction : reactions) {
			for(Xref xref : reaction.getXrefSet().getXrefs()) {
				Set<Measurement> measurements = id2Measurements.get(xref.getXrefId());
				if(measurements != null) {
					reaction.getMeasurementSet().setMeasurements(measurements);
					break;
				}
			}
		}
		
		return reactions;
	}
	
}
