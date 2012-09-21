package gov.lbl.glamm.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Representation of an Experiment - wraps a collection of Samples with the same experiment id.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Experiment 
implements Serializable {
	
	public static transient final String DEFAULT_EXPERIMENT_ID = "-1";
	
	private String expId;
	private List<Sample> samples;
	
	private Map<String,String> attributesMap = null;

	public Experiment() {
		htmlDirty = true;
		this.samples = new ArrayList<Sample>();
		expId = "";
	}
	
	private boolean htmlDirty = false;
	private String html = null;
	
	/**
	 * Constructor
	 * @param expId The experiment id.
	 */
	public Experiment(final String expId) {
		this();
		this.expId = expId;
	}

	/**
	 * Adds a sample to this experiment.
	 * @param sample The sample.
	 */
	public void addSample(final Sample sample) {

		if(sample != null) {
			samples.add(sample);
		}
	}
	
	public Map<String, String> getAttributesMap() {
		if (attributesMap == null)
			attributesMap = new HashMap<String, String>();
		
		return attributesMap;
	}
	
	public void setAttributesMap(Map<String, String> attributesMap) {
		this.htmlDirty = true;
		this.attributesMap = attributesMap;
	}
	
	/**
	 * Gets the experiment id.
	 * @return The experimentid.
	 */
	public String getExperimentId() {
		return expId;
	}
	
	public void setExperimentId(String expId) {
		this.htmlDirty = true;
		this.expId = expId;
	}
	
	/**
	 * Gets the list of samples associated with this experiment.
	 * @return The list of samples.
	 */
	public List<Sample> getSamples() {
		if (samples == null)
			samples = new ArrayList<Sample>();
		return samples;
	}
	
	public void setSamples(List<Sample> samples) {
		this.samples = samples;
	}
	
	public String toHtml() {
		if ( htmlDirty ) {
		StringBuilder builder = new StringBuilder();
		builder.append("<span class='title'>").append(expId).append("</span>");
		for ( Entry<String,String> attribute : this.getAttributesMap().entrySet() ) {
			builder.append("<br/><span class='attributeName'>")
				.append(attribute.getKey())
				.append("</span>: <span class='attribute'>")
				.append(attribute.getValue())
				.append("</span>");
				;
		}
		html = builder.toString();
		htmlDirty = false;
		}
		return html;
	}

}