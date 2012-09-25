package gov.lbl.glamm.shared.model.metabolism.flux;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FbaCompoundVariables {
	
	private float lowerBound,
				  upperBound,
				  min,
				  max,
				  value;
	private String compoundUUID,
				   variableType;
	
	
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
	
	@JsonProperty("modelcompound_uuid")
	public String getCompoundUUID() { return compoundUUID; }
	@JsonProperty("modelcompound_uuid")
	public void setCompoundUUID(String compoundUUID) { this.compoundUUID = compoundUUID; }
}
