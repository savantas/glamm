package gov.lbl.glamm.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Media implements Serializable {
	private String id;
	private String name;
	Map<Compound, Float> compound2Concs;
	private float pH;
	private float temperature;

	public Media() {
		id = "";
		name = "";
		compound2Concs = new HashMap<Compound, Float>();
		pH = 0;
		temperature = 0;
	}
	
	public Media(String id, String name) {
		this();
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public float getPh() {
		return pH;
	}
	
	public float getTemperature() {
		return temperature;
	}
	
	public Map<Compound, Float> getMediaMap() {
		return compound2Concs;
	}
	
	public void addMediaComponent(Compound cpd, float concentration) {
		compound2Concs.put(cpd, concentration);
	}
	
	public float getConcentration(Compound cpd) {
		if (!compound2Concs.containsKey(cpd))
			return 0;
		return compound2Concs.get(cpd);
	}
}
