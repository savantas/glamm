package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
/*
@SuppressWarnings("serial")
public class ReactionFunData extends GlammPrimitive implements Serializable {
	
	private static transient final String ID_PREFIX = "p";
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	private ArrayList<GlammPrimitive> primitives = null;
		
	//********************************************************************************
	
	public ReactionFunData() {
		super();
		primitives = new ArrayList<GlammPrimitive>();
	}
	
	//********************************************************************************
	
	public String addPrimitive(GlammPrimitive primitive) {
		String id = null;
		if(primitive != null) {
			// assign a generated id only if the incoming primitive lacks one
			if(primitive.getId() == null) {
				id = ID_PREFIX + primitives.size();
				primitive.setId(id);
			}
			else 
				id = primitive.getId();
			primitives.add(primitive);
		}
		return id;
	}
	
	//********************************************************************************	
	
	@Override
	public Type getType() {
		return TYPE;
	}
}
*/