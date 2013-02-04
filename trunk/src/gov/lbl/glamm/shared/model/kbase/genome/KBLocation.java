package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBLocation {
	private List<KBRegionOfDna> location;

	@JsonProperty("location")
	public List<KBRegionOfDna> getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation(List<List<Object>> location) {
		this.location = new ArrayList<KBRegionOfDna>();
		for (List<Object> region : location) {
			this.location.add(new KBRegionOfDna(region));
		}
	}
}
