package gov.lbl.glamm.client.model;


@SuppressWarnings("serial")
public class PathwayLink extends GlammPrimitive {
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	
	public PathwayLink() {}
	
	//********************************************************************************
	
	@Override
	public Type getType() {
		return TYPE;
	}

}
