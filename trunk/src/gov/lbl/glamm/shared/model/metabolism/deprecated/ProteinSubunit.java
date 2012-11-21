package gov.lbl.glamm.shared.model.metabolism.deprecated;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProteinSubunit {
	private String roleUUID = null,
				   triggering = null,
				   isOptional = null,
				   note = null;
	private List<ProteinSubunitGenes> subunitGenes;
	
	public ProteinSubunit() { }
	
	@JsonProperty("triggering")
	public String getTriggering() { return triggering; }
	@JsonProperty("triggering")
	public void setTriggering(String triggering) { this.triggering = triggering; }
	
	@JsonProperty("optional")
	public String getOptional() { return isOptional; }
	@JsonProperty("optional")
	public void setOptional(String isOptional) { this.isOptional = isOptional; }
	
	@JsonProperty("role_uuid")
	public String getRoleUUID() { return roleUUID; }
	@JsonProperty("role_uuid")
	public void setRoleUUID(String roleUUID) { this.roleUUID = roleUUID; }

	@JsonProperty("note")
	public String getNote() { return note; }
	@JsonProperty("note")
	public void setNote(String note) { this.note = note; }

	@JsonProperty("modelReactionProteinSubunitGenes")
	public List<ProteinSubunitGenes> getSubunitGenes() { return subunitGenes; }
	@JsonProperty("modelReactionProteinSubunitGenes")
	public void setSubunitGenes(List<ProteinSubunitGenes> subunitGenes) { this.subunitGenes = subunitGenes; }
}
