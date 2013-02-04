package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBAlternativeFunction {
	private String function;
	private float probability;
	private List<KBGeneHit> geneHits;
	
	public KBAlternativeFunction() {
		function = null;
		geneHits = null;
	}
	
	@SuppressWarnings("unchecked")
	public KBAlternativeFunction(List<Object> altFunc) {
		this();

		if (altFunc.size() != 3)
			return;
		else {
			function = (String)altFunc.get(0);
			probability = (Float)altFunc.get(1);
			geneHits = (List<KBGeneHit>)altFunc.get(2);
		}
	}
	
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
	public List<KBGeneHit> getGeneHits() {
		return geneHits;
	}
	@JsonProperty("gene_hits")
	public void setGeneHits(List<KBGeneHit> geneHits) {
		this.geneHits = geneHits;
	}
}
