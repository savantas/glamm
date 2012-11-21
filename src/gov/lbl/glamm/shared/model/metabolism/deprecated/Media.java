package gov.lbl.glamm.shared.model.metabolism.deprecated;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Media {
	private String id;
	private String name;
	private List<String> compoundIds;
	private List<Float> concentrations;
	private float pH;
	private float temperature;
	
	@JsonProperty("media_id")
	public String getMediaId() {
		return id;
	}
}
