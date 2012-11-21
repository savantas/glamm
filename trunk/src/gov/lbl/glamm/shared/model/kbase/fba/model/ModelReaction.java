package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelReaction {
	private String modelReactionId;
	private String reactionId;
	private String compartmentId;
	private String name;
	private String direction;
	private List<String> features;
	
	@JsonProperty("id")
	public String getModelReactionId() {
		return modelReactionId;
	}
	@JsonProperty("id")
	public void setModelReactionId(String modelReactionId) {
		this.modelReactionId = modelReactionId;
	}

	@JsonProperty("reaction")
	public String getReactionId() {
		return reactionId;
	}
	@JsonProperty("reaction")
	public void setReactionId(String reactionId) {
		this.reactionId = reactionId;
	}

	@JsonProperty("compartment")
	public String getCompartmentId() {
		return compartmentId;
	}
	@JsonProperty("compartment")
	public void setCompartmentId(String compartmentId) {
		this.compartmentId = compartmentId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("direction")
	public String getDirection() {
		return direction;
	}
	@JsonProperty("direction")
	public void setDirection(String direction) {
		this.direction = direction;
	}

	@JsonProperty("features")
	public List<String> getFeatures() {
		return features;
	}
	@JsonProperty("features")
	public void setFeatures(List<String> features) {
		this.features = features;
	}
}
