package gov.lbl.glamm.shared.model.kbase.fba.gapgen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactionRemoval {
	private String modelReactionId;
	private String direction;
	
	@JsonProperty("modelreaction_id")
	public String getModelReactionId() {
		return modelReactionId;
	}
	@JsonProperty("modelreaction_id")
	public void setModelReactionId(String modelReactionId) {
		this.modelReactionId = modelReactionId;
	}

	@JsonProperty("direction")
	public String getDirection() {
		return direction;
	}
	@JsonProperty("direction")
	public void setDirection(String direction) {
		this.direction = direction;
	}
}
