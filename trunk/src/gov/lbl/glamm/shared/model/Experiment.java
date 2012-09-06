package gov.lbl.glamm.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	
	
	@SuppressWarnings("unused")
	private Experiment() {}
	
	/**
	 * Constructor
	 * @param expId The experiment id.
	 */
	public Experiment(final String expId) {
		this.expId = expId;
		this.samples = new ArrayList<Sample>();
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
	
	/**
	 * Gets the experiment id.
	 * @return The experimentid.
	 */
	public String getExperimentId() {
		return expId;
	}
	
	/**
	 * Gets the list of samples associated with this experiment.
	 * @return The list of samples.
	 */
	public List<Sample> getSamples() {
		return samples;
	}
	

}