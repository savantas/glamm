package gov.lbl.glamm.shared.model.kbase.fba.gapgen;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBGapGen {
	private String id;
	private KBGapGenFormulation formulation;
	private List<String> biomassAdditions;
	private List<String> mediaRemovals;
	private List<KBReactionRemoval> reactionRemovals;
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("formulation")
	public KBGapGenFormulation getFormulation() {
		return formulation;
	}
	@JsonProperty("formulation")
	public void setFormulation(KBGapGenFormulation formulation) {
		this.formulation = formulation;
	}
	
	@JsonProperty("biomassAdditions")
	public List<String> getBiomassAdditions() {
		return biomassAdditions;
	}
	@JsonProperty("biomassAdditions")
	public void setBiomassAdditions(List<String> biomassAdditions) {
		this.biomassAdditions = biomassAdditions;
	}
	
	@JsonProperty("mediaRemovals")
	public List<String> getMediaRemovals() {
		return mediaRemovals;
	}
	@JsonProperty("mediaRemovals")
	public void setMediaRemovals(List<String> mediaRemovals) {
		this.mediaRemovals = mediaRemovals;
	}
	
	@JsonProperty("reactionRemovals")
	public List<KBReactionRemoval> getReactionRemovals() {
		return reactionRemovals;
	}
	@JsonProperty("reactionRemovals")
	public void setReactionRemovals(List<KBReactionRemoval> reactionRemovals) {
		this.reactionRemovals = reactionRemovals;
	}
}
