package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class KBGeneAssertion implements Serializable {
	private String featureId;
	private float growthFraction;
	private float growth;
	private boolean isEssential;
	
	@JsonProperty("feature_id")
	public String getFeatureId() {
		return featureId;
	}
	@JsonProperty("feature_id")
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	@JsonProperty("growthFraction")
	public float getGrowthFraction() {
		return growthFraction;
	}
	@JsonProperty("growthFraction")
	public void setGrowthFraction(float growthFraction) {
		this.growthFraction = growthFraction;
	}
	
	@JsonProperty("getGrowth")
	public float getGrowth() {
		return growth;
	}
	@JsonProperty("getGrowth")
	public void setGrowth(float growth) {
		this.growth = growth;
	}
	
	@JsonProperty("isEssential")
	public boolean isEssential() {
		return isEssential;
	}
	@JsonProperty("isEssential")
	public void setEssential(boolean isEssential) {
		this.isEssential = isEssential;
	}
}
