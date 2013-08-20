package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class KBMetaboliteProduction implements Serializable {
	private float maximumProduction;
	private String modelCompoundId;
	
	@JsonProperty("maximumProduction")
	public float getMaximumProduction() {
		return maximumProduction;
	}
	@JsonProperty("maximumProduction")
	public void setMaximumProduction(float maximumProduction) {
		this.maximumProduction = maximumProduction;
	}
	
	@JsonProperty("modelCompoundId")
	public String getModelCompoundId() {
		return modelCompoundId;
	}
	@JsonProperty("modelCompoundId")
	public void setModelCompoundId(String modelCompoundId) {
		this.modelCompoundId = modelCompoundId;
	}
}
