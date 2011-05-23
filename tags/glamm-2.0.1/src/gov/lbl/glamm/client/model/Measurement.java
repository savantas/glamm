package gov.lbl.glamm.client.model;

import java.io.Serializable;

@SuppressWarnings({"unused", "serial"})
public class Measurement extends GlammPrimitive implements Serializable {
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	
	private String expId 			= null;
	private String sampleId 		= null;
	private float value 			= 0f;
	private float confidence 		= 0f;
	
	private String targetId			= null;
	
	//********************************************************************************
	
	private Measurement() {}
	
	public Measurement(final String expId, 
			final String sampleId, 
			final float value, 
			final float confidence, 
			final String targetId) {
		this.expId 		= expId;
		this.sampleId 	= sampleId;
		this.value 		= value;
		this.confidence = confidence;
		this.targetId 	= targetId;
	}

	//********************************************************************************

	public String getExpId() {
		return expId;
	}

	//********************************************************************************

	public String getSampleId() {
		return sampleId;
	}

	//********************************************************************************

	public float getValue() {
		return value;
	}

	//********************************************************************************

	public float getConfidence() {
		return confidence;
	}

	//********************************************************************************

	public String getTargetId() {
		return targetId;
	}

	//********************************************************************************
	
	@Override
	public Type getType() {
		return TYPE;
	}
	
	//********************************************************************************
	
	

}