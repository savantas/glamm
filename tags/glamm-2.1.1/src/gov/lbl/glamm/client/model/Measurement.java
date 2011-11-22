package gov.lbl.glamm.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Measurement implements Serializable {
		
	private String expId;
	private String sampleId;
	private float value;
	private float confidence;
	
	private String targetId;
	
	//********************************************************************************
	
	@SuppressWarnings("unused")
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

	public Measurement(final Measurement measurement) {
		this.expId 		= measurement.expId;
		this.sampleId 	= measurement.sampleId;
		this.value 		= measurement.value;
		this.confidence = measurement.confidence;
		this.targetId 	= measurement.targetId;
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

}