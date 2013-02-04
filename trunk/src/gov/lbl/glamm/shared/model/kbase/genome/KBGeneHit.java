package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBGeneHit {
	private String featureId;
	private float blastScore;
	
	public KBGeneHit() {
		featureId = null;
	}
	
	public KBGeneHit(List<Object> geneHit) {
		this();
		if (geneHit.size() != 2) {
			return;
		}
		else {
			featureId = (String)geneHit.get(0);
			blastScore = (Float)geneHit.get(1);
		}
	}
	
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
