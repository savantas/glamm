package gov.lbl.glamm.client.model;




import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings({"unused", "serial"})
public class Experiment extends GlammPrimitive implements Serializable {
	
	public static transient final String DEFAULT_EXPERIMENT_ID = "-1";
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	private String expId = null;
	private String taxonomyId = null;
	private ArrayList<Sample> samples	= null;
	
	//********************************************************************************
	
	private Experiment() {}
	
	public Experiment(final String expId, final String taxonomyId, final String source) {
		super();
		this.expId = expId;
		this.taxonomyId = taxonomyId;
		setSource(source);
	}

	//********************************************************************************

	public void addSample(final Sample sample) {

		if(sample != null) {
			if( samples == null )
				samples = new ArrayList<Sample>();
			samples.add(sample);
		}
	}
		
	//********************************************************************************

	public String getExperimentId() {
		return expId;
	}
	
	//********************************************************************************
	
	public ArrayList<Sample> getSamples() {
		return samples;
	}
	
	//********************************************************************************
	
	public String getTaxonomyId() {
		return taxonomyId;
	}
	
	@Override
	public Type getType() {
		return TYPE;
	}

}