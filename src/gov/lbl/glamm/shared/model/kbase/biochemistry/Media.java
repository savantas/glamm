package gov.lbl.glamm.shared.model.kbase.biochemistry;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Media {
	private String id;
	private String name;
	private List<String> compoundId;
	private List<Float> concentrations;
	private float pH;
	private float temperature;
	
	@JsonProperty("media_id")
	public String getId() {
		return id;
	}
	@JsonProperty("media_id")
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

	@JsonProperty("compounds")
	public List<String> getCompoundId() {
		return compoundId;
	}
	@JsonProperty("compounds")
	public void setCompoundId(List<String> compoundId) {
		this.compoundId = compoundId;
	}

	@JsonProperty("concentrations")
	public List<Float> getConcentrations() {
		return concentrations;
	}
	@JsonProperty("concentrations")
	public void setConcentrations(List<Float> concentrations) {
		this.concentrations = concentrations;
	}

	@JsonProperty("pH")
	public float getPh() {
		return pH;
	}
	@JsonProperty("pH")
	public void setPh(float pH) {
		this.pH = pH;
	}

	@JsonProperty("temperature")
	public float getTemperature() {
		return temperature;
	}
	@JsonProperty("temperature")
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
}
