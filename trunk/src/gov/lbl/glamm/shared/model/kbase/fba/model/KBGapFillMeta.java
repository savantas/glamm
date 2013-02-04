package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBGapFillMeta {
	private String gapFillId;
	private String mediaId;
	private List<String> featureKo;
	
	@JsonProperty("id")
	public String getGapFillId() {
		return gapFillId;
	}
	@JsonProperty("id")
	public void setGapFillId(String gapFillId) {
		this.gapFillId = gapFillId;
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
