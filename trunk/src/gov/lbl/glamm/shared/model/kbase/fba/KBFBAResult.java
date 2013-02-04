package gov.lbl.glamm.shared.model.kbase.fba;

import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.util.MeasurementSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class KBFBAResult implements Serializable {
    private String id;
    private String workspace;
	private String model;
    private String model_workspace;
    private Float objective;
    private Integer isComplete;
    private KBFBAFormulation formulation;
    private List<KBMinimalMediaPrediction> minimalMediaPredictions;
    private List<KBMetaboliteProduction> metaboliteProductions;
    private List<KBReactionFlux> reactionFluxes;
    private List<KBCompoundFlux> compoundFluxes;
    private List<KBGeneAssertion> geneAssertions;
    
    private Map<String, Compound> compoundValues;
    private Map<String, Reaction> reactionValues;
    
    private Float minFluxValue;
    private Float maxFluxValue;
    private Float globalUpperBound;
    private Float globalLowerBound;
    
	public KBFBAResult() {
    	id = "";
    	workspace = "";
    	model = "";
    	model_workspace = "";
    	objective = 0f;
    	isComplete = 0;
    	formulation = null;
    	minimalMediaPredictions = null;
    	metaboliteProductions = null;
    	reactionFluxes = null;
    	compoundFluxes = null;
    	geneAssertions = null;
    	
    	// these are both mapped from GLAMM's guid -> compound or reaction
    	compoundValues = new HashMap<String, Compound>();
    	reactionValues = new HashMap<String, Reaction>();
    }

    
    public Float getMinFluxValue() {
		return minFluxValue;
	}


	public void setMinFluxValue(Float minFluxValue) {
		this.minFluxValue = minFluxValue;
	}


	public Float getMaxFluxValue() {
		return maxFluxValue;
	}


	public void setMaxFluxValue(Float maxFluxValue) {
		this.maxFluxValue = maxFluxValue;
	}


	public Float getGlobalUpperBound() {
		return globalUpperBound;
	}


	public void setGlobalUpperBound(Float globalUpperBound) {
		this.globalUpperBound = globalUpperBound;
	}


	public Float getGlobalLowerBound() {
		return globalLowerBound;
	}


	public void setGlobalLowerBound(Float globalLowerBound) {
		this.globalLowerBound = globalLowerBound;
	}

    public Collection<Compound> getCompoundValues() {
    	return compoundValues.values();
    }
    public void addCompoundValues(Collection<Compound> compoundValues) {
    	for (Compound cpd : compoundValues) {
    		this.compoundValues.put(cpd.getGuid(), cpd);
    	}
    }
    
    public Collection<Reaction> getReactionValues() {
    	return reactionValues.values();
    }
    public void setReactionValues(Collection<Reaction> reactionValues) {
    	for (Reaction rxn : reactionValues) {
    		this.reactionValues.put(rxn.getGuid(), rxn);
    	}
    }
    
    public boolean hasReaction(String guid) {
    	return reactionValues.containsKey(guid);
    }
    
    public boolean hasCompound(String guid) {
    	return compoundValues.containsKey(guid);
    }
    
    public float getFluxForReaction(Reaction rxn) {
    	if (rxn == null || rxn.getGuid() == null || !reactionValues.containsKey(rxn.getGuid()))
    		return 0;  // not sure what's best to do here. throw an exception? maybe later.
    	
    	float value = 0;
    	
    	MeasurementSet measurements = reactionValues.get(rxn.getGuid()).getMeasurementSet();
		for(Measurement measurement : measurements.getMeasurements()) {
			value += measurement.getValue();
		}

		return value /= (float) measurements.getMeasurements().size();
    }
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelWorkspace() {
		return model_workspace;
	}
	public void setModelWorkspace(String model_workspace) {
		this.model_workspace = model_workspace;
	}
	public Float getObjective() {
		return objective;
	}
	public void setObjective(Float objective) {
		this.objective = objective;
	}
	public Integer isComplete() {
		return isComplete;
	}
	public void setComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}
	public KBFBAFormulation getFormulation() {
		return formulation;
	}
	public void setFormulation(KBFBAFormulation formulation) {
		this.formulation = formulation;
	}
	public List<KBMinimalMediaPrediction> getMinimalMediaPredictions() {
		return minimalMediaPredictions;
	}
	public void setMinimalMediaPredictions(
			List<KBMinimalMediaPrediction> minimalMediaPredictions) {
		this.minimalMediaPredictions = minimalMediaPredictions;
	}
	public List<KBMetaboliteProduction> getMetaboliteProductions() {
		return metaboliteProductions;
	}
	public void setMetaboliteProductions(
			List<KBMetaboliteProduction> metaboliteProductions) {
		this.metaboliteProductions = metaboliteProductions;
	}
	public List<KBReactionFlux> getReactionFluxes() {
		return reactionFluxes;
	}
	public void setReactionFluxes(List<KBReactionFlux> reactionFluxes) {
		this.reactionFluxes = reactionFluxes;
	}
	public List<KBCompoundFlux> getCompoundFluxes() {
		return compoundFluxes;
	}
	public void setCompoundFluxes(List<KBCompoundFlux> compoundFluxes) {
		this.compoundFluxes = compoundFluxes;
	}
	public List<KBGeneAssertion> getGeneAssertions() {
		return geneAssertions;
	}
	public void setGeneAssertions(List<KBGeneAssertion> geneAssertions) {
		this.geneAssertions = geneAssertions;
	}
}
