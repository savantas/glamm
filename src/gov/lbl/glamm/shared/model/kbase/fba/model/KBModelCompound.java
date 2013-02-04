package gov.lbl.glamm.shared.model.kbase.fba.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBModelCompound {
	private String compoundId;
	private String modelCompoundId;
	private String name;
	private String compartmentId;
	
	@JsonProperty("compound")
	public String getCompoundId() {
		return compoundId;
	}
	@JsonProperty("compound")
	public void setCompoundId(String compoundId) {
		this.compoundId = compoundId;
	}

	@JsonProperty("id")
	public String getModelCompoundId() {
		return modelCompoundId;
	}
	@JsonProperty("id")
	public void setModelCompoundId(String modelCompoundId) {
		this.modelCompoundId = modelCompoundId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("compartment")
	public String getCompartmentId() {
		return compartmentId;
	}
	@JsonProperty("compartment")
	public void setCompartmentId(String compartmentId) {
		this.compartmentId = compartmentId;
	}
}
