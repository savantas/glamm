package gov.lbl.glamm.shared.model.kbase.biochemistry;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBBiochemistry {
	private String id;
	private String name;
	private List<String> compoundIds;
	private List<String> reactionIds;
	private List<String> mediaIds;
	
	@JsonProperty("biochemistry_id")
	public String getId() {
		return id;
	}
	@JsonProperty("biochemistry_id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("compounds")
	public List<String> getCompoundIds() {
		return compoundIds;
	}
	@JsonProperty("compounds")
	public void setCompoundIds(List<String> compoundIds) {
		this.compoundIds = compoundIds;
	}

	@JsonProperty("reactions")
	public List<String> getReactionIds() {
		return reactionIds;
	}
	@JsonProperty("reactions")
	public void setReactionIds(List<String> reactionIds) {
		this.reactionIds = reactionIds;
	}

	@JsonProperty("media")
	public List<String> getMediaIds() {
		return mediaIds;
	}
	@JsonProperty("media")
	public void setMediaIds(List<String> mediaIds) {
		this.mediaIds = mediaIds;
	}
}
