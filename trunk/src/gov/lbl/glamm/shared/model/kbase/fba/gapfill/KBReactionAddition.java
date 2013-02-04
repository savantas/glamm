package gov.lbl.glamm.shared.model.kbase.fba.gapfill;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBReactionAddition {
	private String reactionId;
	private String direction;
	
	@JsonProperty("reaction_id")
	public String getReactionId() {
		return reactionId;
	}
	@JsonProperty("reaction_id")
	public void setReactionId(String reactionId) {
		this.reactionId = reactionId;
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
