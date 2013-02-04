package gov.lbl.glamm.shared.model.kbase.fba;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBFBA {
	private String fbaId;
	private KBFBAFormulation formulation;
	private List<KBMinimalMediaPrediction> minimalMediaPredictions;
	private List<KBMetaboliteProduction> metaboliteProductions;
	private List<KBReactionFlux> reactionFluxes;
	private List<KBCompoundFlux> compoundFluxes;
	private List<KBGeneAssertion> geneAssertions;
	
	@JsonProperty("fba_id")
	public String getFbaId() {
		return fbaId;
	}
	@JsonProperty("fba_id")
	public void setFbaId(String fbaId) {
		this.fbaId = fbaId;
	}

	@JsonProperty("formulation")
	public KBFBAFormulation getFormulation() {
		return formulation;
	}
	@JsonProperty("formulation")
	public void setFormulation(KBFBAFormulation formulation) {
		this.formulation = formulation;
	}

	@JsonProperty("minimalMediaPrediction")
	public List<KBMinimalMediaPrediction> getMinimalMediaPredictions() {
		return minimalMediaPredictions;
	}
	@JsonProperty("minimalMediaPrediction")
	public void setMinimalMediaPredictions(
			List<KBMinimalMediaPrediction> minimalMediaPredictions) {
		this.minimalMediaPredictions = minimalMediaPredictions;
	}

	@JsonProperty("metaboliteProductions")
	public List<KBMetaboliteProduction> getMetaboliteProductions() {
		return metaboliteProductions;
	}
	@JsonProperty("metaboliteProductions")
	public void setMetaboliteProductions(
			List<KBMetaboliteProduction> metaboliteProductions) {
		this.metaboliteProductions = metaboliteProductions;
	}

	@JsonProperty("reactionFluxes")
	public List<KBReactionFlux> getReactionFluxes() {
		return reactionFluxes;
	}
	@JsonProperty("reactionFluxes")
	public void setReactionFluxes(List<KBReactionFlux> reactionFluxes) {
		this.reactionFluxes = reactionFluxes;
	}
	
	@JsonProperty("compoundFluxes")
	public List<KBCompoundFlux> getCompoundFluxes() {
		return compoundFluxes;
	}
	@JsonProperty("compoundFluxes")
	public void setCompoundFluxes(List<KBCompoundFlux> compoundFluxes) {
		this.compoundFluxes = compoundFluxes;
	}
	
	@JsonProperty("geneAssertions")
	public List<KBGeneAssertion> getGeneAssertions() {
		return geneAssertions;
	}
	@JsonProperty("geneAssertions")
	public void setGeneAssertions(List<KBGeneAssertion> geneAssertions) {
		this.geneAssertions = geneAssertions;
	}
}
