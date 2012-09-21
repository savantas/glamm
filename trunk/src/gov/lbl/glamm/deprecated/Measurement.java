package gov.lbl.glamm.deprecated;


/**
 * A measurement in a a sample.
 * Currently, this object does not have a direct reference to the element
 * to which the measurement applies - that data is stored elsewhere for now.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
@Deprecated
public class Measurement {
	// Groupings
	private Sample sample = null;

	// Information
	private String id = null;
	private double value = 0.0;
	private float confidence = 0.0f;
	// TODO: make other attributes dynamic

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

	// Bean methods
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
}
