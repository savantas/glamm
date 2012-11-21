package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GapGenMeta {
	private String gapGenId;
	private String mediaId;
	private List<String> featureKo;
	
	@JsonProperty("id")
	public String getGapGenId() {
		return gapGenId;
	}
	@JsonProperty("id")
	public void setGapGenId(String gapGenId) {
		this.gapGenId = gapGenId;
	}
	
	@JsonProperty("media")
	public String getMediaId() {
		return mediaId;
	}
	@JsonProperty("media")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@JsonProperty("ko")
	public List<String> getFeatureKo() {
		return featureKo;
	}
	@JsonProperty("ko")
	public void setFeatureKo(List<String> featureKo) {
		this.featureKo = featureKo;
	}
}
