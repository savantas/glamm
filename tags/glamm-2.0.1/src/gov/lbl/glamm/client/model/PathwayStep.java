package gov.lbl.glamm.client.model;


import java.io.Serializable;

@SuppressWarnings(value={ "unused", "serial" })
public final class PathwayStep extends GlammPrimitive implements Serializable {
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	
	public static final String DIRECTION_BOTH			= "both";
	public static final String DIRECTION_FORWARD 		= "forward";
	public static final String DIRECTION_REVERSE 		= "reverse";
	public static final String DIRECTION_UNSPECIFIED	= "unspecified";
	
	private String rxnId;
	private String direction = DIRECTION_BOTH;
	
	private PathwayStep() {}
	
	public PathwayStep(String rxnId, String direction) {
		this.rxnId = rxnId;
		this.direction = direction;
	}
	
	@Override
	public Type getType() {
		return TYPE;
	}
}
