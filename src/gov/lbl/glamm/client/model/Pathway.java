package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Pathway implements Serializable {
	
	private String name		= null;
	private String mapId	= null;
	private List<Reaction> reactions = null;
	
	public Pathway() {
		reactions = new ArrayList<Reaction>();
	}
	
	//********************************************************************************
	
	public void addReaction(Reaction reaction) {
		reactions.add(reaction);
	}
	
	//********************************************************************************

	public String getName() {
		return name;
	}
	
	public List<Reaction> getReactions() {
		return reactions;
	}
	
	//********************************************************************************

	public void setName(String name) {
		this.name = name;
	}

	//********************************************************************************

	public String getMapId() {
		return mapId;
	}
	
	//********************************************************************************

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	//********************************************************************************
	
}