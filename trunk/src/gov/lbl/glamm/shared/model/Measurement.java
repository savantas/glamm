package gov.lbl.glamm.shared.model;

import java.io.Serializable;

/**
 * A measurement, typically associated with a sample.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Measurement implements Serializable {
		
	private String expId;
	private String sampleId;
	private float value;
	private float confidence;
	
	private String targetId;
	
	
	@SuppressWarnings("unused")
	private Measurement() {}
	
	/**
	 * Constructor
	 * @param expId The experiment id.
	 * @param sampleId The sample id.
	 * @param value The value of the measurement.
	 * @param confidence The confidence of this measurement.
	 * @param targetId The target id associated with this measurement - may soon be deprecated.
	 */
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

	/**
	 * Copy constructor
	 * @param measurement The measurement. 
	 */
	public Measurement(final Measurement measurement) {
		this.expId 		= measurement.expId;
		this.sampleId 	= measurement.sampleId;
		this.value 		= measurement.value;
		this.confidence = measurement.confidence;
		this.targetId 	= measurement.targetId;
	}
	

	/**
	 * Gets the experiment id for this measurement.
	 * @return The experiment id.
	 */
	public String getExpId() {
		return expId;
	}

	/**
	 * Gets the sample id for this measurement.
	 * @return The sample id.
	 */
	public String getSampleId() {
		return sampleId;
	}

	/**
	 * Gets the value of this measurement.
	 * @return The value.
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Gets the confidence of this measurement.
	 * @return The confidence.
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Gets the target id for this measurement.
	 * @return The target id.
	 */
	public String getTargetId() {
		return targetId;
	}


}