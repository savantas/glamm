package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Pathway extends GlammPrimitive implements Serializable {

	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	
	private String name		= null;
	private String mapId	= null;
	private List<Reaction> reactions = null;
	
	public Pathway() {}
	
	//********************************************************************************
	
	public void addReaction(Reaction reaction) {
		if(reactions == null)
			reactions = new ArrayList<Reaction>();
		reactions.add(reaction);
	}
	
	//********************************************************************************

	public String getName() {
		return name;
	}
	
	public List<Reaction> getReactions() {
		return reactions;
	}
	
	@Override
	public Type getType() {
		return TYPE;
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