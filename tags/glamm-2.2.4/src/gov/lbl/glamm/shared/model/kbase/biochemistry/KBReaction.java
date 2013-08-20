package gov.lbl.glamm.shared.model.kbase.biochemistry;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBReaction {
	private String id;
	private String name;
	private String abbrev;
	private List<String> enzymes;
	private String direction;
	private String reversibility;
	private float deltaG;
	private float deltaGErr;
	private String equation;
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("abbrev")
	public String getAbbreviation() {
		return abbrev;
	}
	@JsonProperty("abbrev")
	public void setAbbreviation(String abbrev) {
		this.abbrev = abbrev;
	}
	
	@JsonProperty("enzymes")
	public List<String> getEnzymes() {
		return enzymes;
	}
	@JsonProperty("enzymes")
	public void setEnzymes(List<String> enzymes) {
		this.enzymes = enzymes;
	}
	
	@JsonProperty("direction")
	public String getDirection() {
		return direction;
	}
	@JsonProperty("direction")
	public void setDirection(String direction) {
		this.direction = direction;
	}

	@JsonProperty("reaction_id")
	public String getId() {
		return id;
	}
	@JsonProperty("reaction_id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("reversibility")
	public String getReversibility() {
		return reversibility;
	}
	@JsonProperty("reversibility")
	public void setReversibility(String reversibility) {
		this.reversibility = reversibility;
	}

	@JsonProperty("deltaG")
	public float getDeltaG() {
		return deltaG;
	}
	@JsonProperty("deltaG")
	public void setDeltaG(float deltaG) {
		this.deltaG = deltaG;
	}

	@JsonProperty("deltaGErr")
	public float getDeltaGErr() {
		return deltaGErr;
	}
	@JsonProperty("deltaGErr")
	public void setDeltaGErr(float deltaGErr) {
		this.deltaGErr = deltaGErr;
	}

	@JsonProperty("equation")
	public String getEquation() {
		return equation;
	}
	@JsonProperty("equation")
	public void setEquation(String equation) {
		this.equation = equation;
	}
}
