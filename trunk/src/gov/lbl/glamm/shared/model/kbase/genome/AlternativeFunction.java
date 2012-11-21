package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlternativeFunction {
	private String function;
	private float probability;
	private List<GeneHit> geneHits;
	
	@JsonProperty("function")
	public String getFunction() {
		return function;
	}
	
	@JsonProperty("function")
	public void setFunction(String function) {
		this.function = function;
	}

	@JsonProperty("probability")
	public float getProbability() {
		return probability;
	}
	@JsonProperty("probability")
	public void setProbability(float probability) {
		this.probability = probability;
	}
	
	@JsonProperty("gene_hits")
	public List<GeneHit> getGeneHits() {
		return geneHits;
	}
	@JsonProperty("gene_hits")
	public void setGeneHits(List<GeneHit> geneHits) {
		this.geneHits = geneHits;
	}
}
