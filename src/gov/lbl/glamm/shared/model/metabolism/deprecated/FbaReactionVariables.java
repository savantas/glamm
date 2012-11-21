package gov.lbl.glamm.shared.model.metabolism.deprecated;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FbaReactionVariables {
	private float lowerBound,
				  upperBound,
				  min,
				  value,
				  max;
	
	private String reactionUUID = null,
				   variableType = null;
	
	@JsonProperty("lowerBound")
	public float getLowerBound() { return lowerBound; }
	@JsonProperty("lowerBound")
	public void setLowerBound(float lowerBound) { this.lowerBound = lowerBound; }
	
	@JsonProperty("upperBound")
	public float getUpperBound() { return upperBound; }
	@JsonProperty("upperBound")
	public void setUpperBound(float upperBound) { this.upperBound = upperBound; }
	
	@JsonProperty("min")
	public float getMin() { return min; }
	@JsonProperty("min")
	public void setMin(float min) { this.min = min; }
	
	@JsonProperty("max")
	public float getMax() { return max; }
	@JsonProperty("max")
	public void setMax(float max) { this.max = max; }
	
	@JsonProperty("value")
	public float getValue() { return value; }
	@JsonProperty("value")
	public void setValue(float value) { this.value = value; }
	
	@JsonProperty("variableType")
	public String getVariableType() { return variableType; }
	@JsonProperty("variableType")
	public void setVariableType(String variableType) { this.variableType = variableType; }
	
	@JsonProperty("modelreaction_uuid")
	public String getReactionUUID() { return reactionUUID; }
	@JsonProperty("modelreaction_uuid")
	public void setReactionUUID(String reactionUUID) { this.reactionUUID = reactionUUID; }
	
}
