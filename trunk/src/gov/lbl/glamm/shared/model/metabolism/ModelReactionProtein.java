package gov.lbl.glamm.shared.model.metabolism;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelReactionProtein {
	private String complexUUID = null,
				   note = null;
	private List<ProteinSubunit> subunits;
	
	public ModelReactionProtein() { }
	
	@JsonProperty("complex_uuid")
	public String getComplexUUID() { return complexUUID; }
	@JsonProperty("complex_uuid")
	public void setComplexUUID(String complexUUID) { this.complexUUID = complexUUID; }

	@JsonProperty("note")
	public String getNote() { return note; }
	@JsonProperty("note")
	public void setNote(String note) { this.note = note; }

	@JsonProperty("modelReactionProteinSubunits")
	public List<ProteinSubunit> getSubunits() { return subunits; }
	@JsonProperty("modelReactionProteinSubunits")
	public void setSubunits(List<ProteinSubunit> subunits) { this.subunits = subunits; }
}

