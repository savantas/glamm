package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
	private List<RegionOfDna> location;

	@JsonProperty("location")
	public List<RegionOfDna> getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation(List<RegionOfDna> location) {
		this.location = location;
	}
}
