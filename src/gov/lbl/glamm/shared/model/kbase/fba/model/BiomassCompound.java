package gov.lbl.glamm.shared.model.kbase.fba.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BiomassCompound {
	private String modelCompoundId;
	private float coefficient;

	@JsonProperty("modelcompound")
	public String getModelCompoundId() {
		return modelCompoundId;
	}
	@JsonProperty("modelcompound")
	public void setModelCompoundId(String modelCompoundId) {
		this.modelCompoundId = modelCompoundId;
	}
	
	@JsonProperty("coefficient")
	public float getCoefficient() {
		return coefficient;
	}
	@JsonProperty("coefficient")
	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
}
