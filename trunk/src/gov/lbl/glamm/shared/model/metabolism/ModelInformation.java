package gov.lbl.glamm.shared.model.metabolism;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInformation {

	private String modelID,
				   biochemistryID,
				   experimentID;
	
	public ModelInformation() {
		modelID = "";
		biochemistryID = "";
		experimentID = "";
	}
	
	@JsonProperty("modelID")
	public void setModelID(String modelID) { this.modelID = modelID; }
	@JsonProperty("modelID")
	public String getModelID() { return modelID; }
	
	@JsonProperty("biochemistryID")
	public void setBiochemistryID(String biochemistryID) { this.biochemistryID = biochemistryID; }
	@JsonProperty("biochemistryID")
	public String getBiochemistryID() { return biochemistryID; }
	
	@JsonProperty("experimentID")
	public void setExperimentID(String experimentID) { this.experimentID = experimentID; }
	@JsonProperty("experimentID")
	public String getExperimentID() { return experimentID; }
	
}
