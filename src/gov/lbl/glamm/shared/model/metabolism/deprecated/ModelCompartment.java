package gov.lbl.glamm.shared.model.metabolism.deprecated;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelCompartment {
	private String isLocked = null,
				   pH = null,
				   compartmentIndex = null,
				   potential = null,
				   modDate = null,
				   compartmentUUID = null,
				   label = null,
				   uuid = null;
	
	public ModelCompartment() { }
	
	@JsonProperty("compartmentIndex")
	public String getCompartmentIndex() { return compartmentIndex; }
	@JsonProperty("compartmentIndex")
	public void setCompartmentIndex(String compartmentIndex) { this.compartmentIndex = compartmentIndex; }

	@JsonProperty("modDate")
	public String getModDate() { return modDate; }
	@JsonProperty("modDate")
	public void setModDate(String modDate) { this.modDate = modDate; }
	
	@JsonProperty("locked")
	public String getLocked() { return isLocked; }
	@JsonProperty("locked")
	public void setLocked(String isLocked) { this.isLocked = isLocked; }

	@JsonProperty("pH")
	public String getPH() { return pH; }
	@JsonProperty("pH")
	public void setPH(String pH) { this.pH = pH; }

	@JsonProperty("potential")
	public String getPotential() { return potential; }
	@JsonProperty("potential")
	public void setPotential(String potential) { this.potential = potential; }

	@JsonProperty("compartment_uuid")
	public String getCompartmentUUID() { return compartmentUUID; }
	@JsonProperty("compartment_uuid")
	public void setCompartmentUUID(String compartmentUUID) { this.compartmentUUID = compartmentUUID; }

	@JsonProperty("label")
	public String getLabel() { return label; }
	@JsonProperty("label")
	public void setLabel(String label) { this.label = label; }

	@JsonProperty("uuid")
	public String getUUID() { return uuid; }
	@JsonProperty("uuid")
	public void setUUID(String uuid) { this.uuid = uuid; }
}
