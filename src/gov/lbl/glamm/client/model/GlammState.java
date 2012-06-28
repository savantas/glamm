package gov.lbl.glamm.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GlammState implements Serializable {

	private Organism organism = null;
	private String amdId = null;

	private GlammState() {
		setOrganism(Organism.globalMap());
		amdId = "map01100"; 
	}
	
	public void setOrganism(Organism organism) {
		if (organism == null)
			this.organism = Organism.globalMap();
		
		this.organism = organism;
	}
	
	public Organism getOrganism() {
		return organism;
	}
	
	public static GlammState defaultState() {
		return new GlammState();
	}
	
	public void setAMDId(String id) {
		if (id != null)
			amdId = id;
	}
	
	public String getAMDId() {
		return amdId;
	}
}
