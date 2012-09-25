package gov.lbl.glamm.shared.model.metabolism.flux;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FbaBiomassVariables {
	private float lowerBound,
				  upperBound;
	private String min,
				   max,
				   value,
				   variableType,
				   biomassUUID;
	
	@JsonProperty("lowerBound")
	public float getLowerBound() { return lowerBound; }
	@JsonProperty("lowerBound")
	public void setLowerBound(float lowerBound) { this.lowerBound = lowerBound; }
	
	@JsonProperty("upperBound")
	public float getUpperBound() { return upperBound; }
	@JsonProperty("upperBound")
	public void setUpperBound(float upperBound) { this.upperBound = upperBound; }
	
	@JsonProperty("min")
	public String getMin() { return min; }
	@JsonProperty("min")
	public void setMin(String min) { this.min = min; }
	
	@JsonProperty("max")
	public String getMax() { return max; }
	@JsonProperty("max")
	public void setMax(String max) { this.max = max; }
	
	@JsonProperty("value")
	public String getValue() { return value; }
	@JsonProperty("value")
	public void setValue(String value) { this.value = value; }
	
	@JsonProperty("variableType")
	public String getVariableType() { return variableType; }
	@JsonProperty("variableType")
	public void setVariableType(String variableType) { this.variableType = variableType; }
	
	@JsonProperty("biomass_uuid")
	public String getBiomassUUID() { return biomassUUID; }
	@JsonProperty("biomass_uuid")
	public void setBiomassUUID(String biomassUUID) { this.biomassUUID = biomassUUID; }

}
