package gov.lbl.glamm.client.model;




import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an Experiment - wraps a collection of Samples with the same experiment id
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Experiment 
implements Serializable {
	
	public static transient final String DEFAULT_EXPERIMENT_ID = "-1";
	
	private String expId;
	private List<Sample> samples;
	
	//********************************************************************************
	
	@SuppressWarnings("unused")
	private Experiment() {}
	
	/**
	 * Constructor
	 * @param expId The experiment id
	 * @param taxonomyId The taxonomy id of the Organism that is the subject of this experiment
	 * @param source A description of the experiment's source
	 */
	public Experiment(final String expId) {
		this.expId = expId;
		this.samples = new ArrayList<Sample>();
	}

	//********************************************************************************

	/**
	 * @param Adds a Sample to this Experiment
	 */
	public void addSample(final Sample sample) {

		if(sample != null) {
			samples.add(sample);
		}
	}
		
	//********************************************************************************

	/**
	 * Accessor
	 * @return The experiment id
	 */
	public String getExperimentId() {
		return expId;
	}
	
	//********************************************************************************
	
	/**
	 * Accessor
	 * @return The list of Samples associated with this Experiment
	 */
	public List<Sample> getSamples() {
		return samples;
	}
	

}