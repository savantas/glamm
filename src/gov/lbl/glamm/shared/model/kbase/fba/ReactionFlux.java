package gov.lbl.glamm.shared.model.kbase.fba;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactionFlux {
	private String modelReactionId;
	private float value;
	private float upperBound;
	private float lowerBound;
	private float max;
	private float min;
	private String type;
	
	@JsonProperty("modelreaction_id")
	public String getModelReactionId() {
		return modelReactionId;
	}
	@JsonProperty("modelreaction_id")
	public void setModelReactionId(String modelReactionId) {
		this.modelReactionId = modelReactionId;
	}
	
	@JsonProperty("value")
	public float getValue() {
		return value;
	}
	@JsonProperty("value")
	public void setValue(float value) {
		this.value = value;
	}
	
	@JsonProperty("upperBound")
	public float getUpperBound() {
		return upperBound;
	}
	@JsonProperty("upperBound")
	public void setUpperBound(float upperBound) {
		this.upperBound = upperBound;
	}
	
	@JsonProperty("lowerBound")
	public float getLowerBound() {
		return lowerBound;
	}
	@JsonProperty("lowerBound")
	public void setLowerBound(float lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	@JsonProperty("max")
	public float getMax() {
		return max;
	}
	@JsonProperty("max")
	public void setMax(float max) {
		this.max = max;
	}
	
	@JsonProperty("min")
	public float getMin() {
		return min;
	}
	@JsonProperty("min")
	public void setMin(float min) {
		this.min = min;
	}
	
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
}
