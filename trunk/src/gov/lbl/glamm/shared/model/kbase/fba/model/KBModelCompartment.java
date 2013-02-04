package gov.lbl.glamm.shared.model.kbase.fba.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBModelCompartment {
	private String id;
	private String name;
	private float pH;
	private float potential;
	private int index;
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("pH")
	public float getPh() {
		return pH;
	}
	@JsonProperty("pH")
	public void setPh(float pH) {
		this.pH = pH;
	}
	
	@JsonProperty("potential")
	public float getPotential() {
		return potential;
	}
	@JsonProperty("potential")
	public void setPotential(float potential) {
		this.potential = potential;
	}
	
	@JsonProperty("index")
	public int getIndex() {
		return index;
	}
	@JsonProperty("index")
	public void setIndex(int index) {
		this.index = index;
	}
}
