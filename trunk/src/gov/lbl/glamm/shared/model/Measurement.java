package gov.lbl.glamm.shared.model;

import java.io.Serializable;

/**
 * A measurement, typically associated with a sample.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Measurement implements Serializable {
		
	private String id;
	private String expId;
	private String sampleId;
	private float value = 0.0f;
	private float confidence = 0.0f;
	
	private String targetId;
	
	public Measurement() {}

	// Groupings
	private Sample sample = null;

	// Description methods
	public String toHtml() {
		return toHtml(false);
	}

	// Description methods
	public String toHtml( boolean includeSampleAndExperiment ) {
		StringBuilder builder = new StringBuilder();
		if ( includeSampleAndExperiment ) {
			builder.append("<span class='type'>Measurement</span>: ");
		}
		builder.append("<span class='title'>").append(id)
			.append("</span><br/><span class='attributeName'>value</span>: <span class='attribute'>")
			.append(value)
			.append("</span><br/><span class='attributeName'>confidence</span>: <span class='attribute'>")
			.append(confidence)
			.append("</span>")
		;
		if ( includeSampleAndExperiment ) {
			builder.append("<br/><div class='child'><span class='type'>Sample</span>: ")
				.append(sample.toHtml());
			if ( sample != null ) {
				builder.append("<br/><span class='type'>Experiment</span>: ")
					.append(sample.getExperiment().toHtml())
					;
			}
			builder.append("</div>");
		}

		return builder.toString();
	}

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
	
	public void setExpId(String expId) {
		this.expId = expId;
	}

	/**
	 * Gets the sample id for this measurement.
	 * @return The sample id.
	 */
	public String getSampleId() {
		return sampleId;
	}
	
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	
	public Sample getSample() {
		return sample;
	}
	
	public void setSample(Sample sample) {
		this.sample = sample;
	}

	/**
	 * Gets the value of this measurement.
	 * @return The value.
	 */
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * Gets the confidence of this measurement.
	 * @return The confidence.
	 */
	public float getConfidence() {
		return confidence;
	}
	
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	/**
	 * Gets the target id for this measurement.
	 * @return The target id.
	 */
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

}