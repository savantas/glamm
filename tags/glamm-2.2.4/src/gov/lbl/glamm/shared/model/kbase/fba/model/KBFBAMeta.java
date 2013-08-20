package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBFBAMeta {
	private String fbaId;
	private String mediaId;
	private float objective;
	private List<String> featureIds;
	
	@JsonProperty("id")
	public String getFbaId() {
		return fbaId;
	}
	@JsonProperty("id")
	public void setFbaId(String fbaId) {
		this.fbaId = fbaId;
	}
	
	@JsonProperty("media")
	public String getMediaId() {
		return mediaId;
	}
	@JsonProperty("media")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@JsonProperty("objective")
	public float getObjective() {
		return objective;
	}
	@JsonProperty("objective")
	public void setObjective(float objective) {
		this.objective = objective;
	}
	
	@JsonProperty("ko")
	public List<String> getFeatureIds() {
		return featureIds;
	}
	@JsonProperty("ko")
	public void setFeatureIds(List<String> featureIds) {
		this.featureIds = featureIds;
	}
}
