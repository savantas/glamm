package gov.lbl.glamm.shared.model.metabolism.deprecated;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProteinSubunitGenes {
	private String featureUUID = null;
	
	public ProteinSubunitGenes() { }
	
	@JsonProperty("feature_uuid")
	public String getFeatureUUID() { return featureUUID; }
	@JsonProperty("feature_uuid")	
	public void setFeatureUUID(String featureUUID) { this.featureUUID = featureUUID; }
}

