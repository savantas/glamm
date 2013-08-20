package gov.lbl.glamm.shared.model.kbase.fba.gapfill;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBGapFill {
	private String id;
	private KBGapFillingFormulation formulation;
	private List<String> biomassRemovals;
	private List<String> mediaAdditions;
	private List<KBReactionAddition> reactionAdditions;
	
	@JsonProperty("gapfill_id")
	public String getId() {
		return id;
	}
	@JsonProperty("gapfill_id")
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("formulation")
	public KBGapFillingFormulation getFormulation() {
		return formulation;
	}
	@JsonProperty("formulation")
	public void setFormulation(KBGapFillingFormulation formulation) {
		this.formulation = formulation;
	}
	
	@JsonProperty("biomassRemovals")
	public List<String> getBiomassRemovals() {
		return biomassRemovals;
	}
	@JsonProperty("biomassRemovals")
	public void setBiomassRemovals(List<String> biomassRemovals) {
		this.biomassRemovals = biomassRemovals;
	}
	
	@JsonProperty("mediaAdditions")
	public List<String> getMediaAdditions() {
		return mediaAdditions;
	}
	@JsonProperty("mediaAdditions")
	public void setMediaAdditions(List<String> mediaAdditions) {
		this.mediaAdditions = mediaAdditions;
	}
	
	@JsonProperty("reactionAdditions")
	public List<KBReactionAddition> getReactionAdditions() {
		return reactionAdditions;
	}
	@JsonProperty("reactionAdditions")
	public void setReactionAdditions(List<KBReactionAddition> reactionAdditions) {
		this.reactionAdditions = reactionAdditions;
	}
}
