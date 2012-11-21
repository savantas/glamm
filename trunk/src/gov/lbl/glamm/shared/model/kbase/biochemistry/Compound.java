package gov.lbl.glamm.shared.model.kbase.biochemistry;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Compound {
	private String id;
	private String name;
	private List<String> aliases;
	private float charge;
	private String formula;
	
	@JsonProperty("compound_id")
	public String getId() {
		return id;
	}
	@JsonProperty("compound_id")
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

	@JsonProperty("aliases")
	public List<String> getAliases() {
		return aliases;
	}
	@JsonProperty("aliases")
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	@JsonProperty("charge")
	public float getCharge() {
		return charge;
	}
	@JsonProperty("charge")
	public void setCharge(float charge) {
		this.charge = charge;
	}
	
	@JsonProperty("formula")
	public String getFormula() {
		return formula;
	}
	@JsonProperty("formula")
	public void setFormula(String formula) {
		this.formula = formula;
	}
}
