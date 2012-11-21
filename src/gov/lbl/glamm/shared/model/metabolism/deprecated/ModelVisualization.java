package gov.lbl.glamm.shared.model.metabolism.deprecated;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelVisualization {

	private String visID,
				   visName,
				   visNotes;
	
	private boolean showAllReactions;
	
	private ModelInformation modelInfo;
	
	public ModelVisualization() {
		visID = "";
		visName = "";
		visNotes = "";
		
		showAllReactions = false;
		
		modelInfo = new ModelInformation();
	}

	@JsonProperty("modelInfo")
	public void setModelInfo(ModelInformation modelInfo) { this.modelInfo = modelInfo; }
	@JsonProperty("modelInfo")
	public ModelInformation getModelInfo() { return modelInfo; }
	
	@JsonProperty("visID")
	public void setVisID(String visID) { this.visID = visID; }
	@JsonProperty("visID")
	public String getVisID() { return visID; }
	
	@JsonProperty("visName")
	public void setVisName(String visName) { this.visName = visName; }
	@JsonProperty("visName")
	public String getVisName() { return visName; }
	
	@JsonProperty("visNotes")
	public void setVisNotes(String visNotes) { this.visNotes = visNotes; }
	@JsonProperty("visNotes")
	public String getVisNotes() { return visNotes; }
	
	@JsonProperty("showAllReactions")
	public void showAllReactions(boolean showAllReactions) { this.showAllReactions = showAllReactions; }
	@JsonProperty("showAllReactions")
	public boolean showAllReactions() { return showAllReactions; }
	
	
}