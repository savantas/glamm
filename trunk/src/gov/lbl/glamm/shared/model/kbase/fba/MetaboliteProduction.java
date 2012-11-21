package gov.lbl.glamm.shared.model.kbase.fba;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaboliteProduction {
	private float maximumProduction;
	private float modelCompoundId;
	
	@JsonProperty("maximumProduction")
	public float getMaximumProduction() {
		return maximumProduction;
	}
	@JsonProperty("maximumProduction")
	public void setMaximumProduction(float maximumProduction) {
		this.maximumProduction = maximumProduction;
	}
	
	@JsonProperty("modelCompoundId")
	public float getModelCompoundId() {
		return modelCompoundId;
	}
	@JsonProperty("modelCompoundId")
	public void setModelCompoundId(float modelCompoundId) {
		this.modelCompoundId = modelCompoundId;
	}
}
