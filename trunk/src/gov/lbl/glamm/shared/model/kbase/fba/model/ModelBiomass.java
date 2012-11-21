package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelBiomass {
	private String id;
	private String name;
	private List<BiomassCompound> biomassCompounds;
	
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
	public List<BiomassCompound> getBiomassCompounds() {
		return biomassCompounds;
	}
	@JsonProperty("biomass_compounds")
	public void setBiomassCompounds(List<BiomassCompound> biomassCompounds) {
		this.biomassCompounds = biomassCompounds;
	}
}
