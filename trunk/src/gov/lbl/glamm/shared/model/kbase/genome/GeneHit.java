package gov.lbl.glamm.shared.model.kbase.genome;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneHit {
	private String featureId;
	private float blastScore;
	
	@JsonProperty("gene")
	public String getFeatureId() {
		return featureId;
	}
	@JsonProperty("gene")
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	@JsonProperty("blast_score")
	public float getBlastScore() {
		return blastScore;
	}
	@JsonProperty("blast_score")
	public void setBlastScore(float blastScore) {
		this.blastScore = blastScore;
	}
}
