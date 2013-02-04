package gov.lbl.glamm.shared.model.metabolism.deprecated;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Media {
	private String id;
	private String name;
	private List<String> compoundIds;
	private List<Float> concentrations;
	private float pH;
	private float temperature;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getCompoundIds() {
		return compoundIds;
	}

	public void setCompoundIds(List<String> compoundIds) {
		this.compoundIds = compoundIds;
	}

	public List<Float> getConcentrations() {
		return concentrations;
	}

	public void setConcentrations(List<Float> concentrations) {
		this.concentrations = concentrations;
	}

	public float getpH() {
		return pH;
	}

	public void setpH(float pH) {
		this.pH = pH;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	@JsonProperty("media_id")
	public String getMediaId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
