package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBModelBiomass {
	private String id;
	private String name;
	private List<KBBiomassCompound> biomassCompounds;
	
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
	
	@JsonProperty("biomass_compounds")
	public List<KBBiomassCompound> getBiomassCompounds() {
		return biomassCompounds;
	}
	@JsonProperty("biomass_compounds")
	public void setBiomassCompounds(List<KBBiomassCompound> biomassCompounds) {
		this.biomassCompounds = biomassCompounds;
	}
}
