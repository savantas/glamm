package gov.lbl.glamm.shared.model.kbase.biochemistry;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reaction {
	private String id;
	private String reversibility;
	private float deltaG;
	private float deltaGErr;
	private String equation;
	
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
