package gov.lbl.glamm.shared.model.kbase.fba;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FBA {
	private String fbaId;
	private FBAFormulation formulation;
	private List<MinimalMediaPrediction> minimalMediaPredictions;
	private List<MetaboliteProduction> metaboliteProductions;
	private List<ReactionFlux> reactionFluxes;
	private List<CompoundFlux> compoundFluxes;
	private List<GeneAssertion> geneAssertions;
	
	@JsonProperty("fba_id")
	public String getFbaId() {
		return fbaId;
	}
	@JsonProperty("fba_id")
	public void setFbaId(String fbaId) {
		this.fbaId = fbaId;
	}

	@JsonProperty("formulation")
	public FBAFormulation getFormulation() {
		return formulation;
	}
	@JsonProperty("formulation")
	public void setFormulation(FBAFormulation formulation) {
		this.formulation = formulation;
	}

	@JsonProperty("minimalMediaPrediction")
	public List<MinimalMediaPrediction> getMinimalMediaPredictions() {
		return minimalMediaPredictions;
	}
	@JsonProperty("minimalMediaPrediction")
	public void setMinimalMediaPredictions(
			List<MinimalMediaPrediction> minimalMediaPredictions) {
		this.minimalMediaPredictions = minimalMediaPredictions;
	}

	@JsonProperty("metaboliteProductions")
	public List<MetaboliteProduction> getMetaboliteProductions() {
		return metaboliteProductions;
	}
	@JsonProperty("metaboliteProductions")
	public void setMetaboliteProductions(
			List<MetaboliteProduction> metaboliteProductions) {
		this.metaboliteProductions = metaboliteProductions;
	}

	@JsonProperty("reactionFluxes")
	public List<ReactionFlux> getReactionFluxes() {
		return reactionFluxes;
	}
	@JsonProperty("reactionFluxes")
	public void setReactionFluxes(List<ReactionFlux> reactionFluxes) {
		this.reactionFluxes = reactionFluxes;
	}
	
	@JsonProperty("compoundFluxes")
	public List<CompoundFlux> getCompoundFluxes() {
		return compoundFluxes;
	}
	@JsonProperty("compoundFluxes")
	public void setCompoundFluxes(List<CompoundFlux> compoundFluxes) {
		this.compoundFluxes = compoundFluxes;
	}
	
	@JsonProperty("geneAssertions")
	public List<GeneAssertion> getGeneAssertions() {
		return geneAssertions;
	}
	@JsonProperty("geneAssertions")
	public void setGeneAssertions(List<GeneAssertion> geneAssertions) {
		this.geneAssertions = geneAssertions;
	}
}
