package gov.lbl.glamm.client.model;



import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings({ "unused", "serial" })

public class Sample extends GlammPrimitive implements Serializable {

	public static transient final String DEFAULT_SAMPLE_ID 	= "-1";

	public static transient final String DATA_TYPE_FLUX			= "flux";
	public static transient final String DATA_TYPE_METABOLITE	= "metabolite";
	public static transient final String DATA_TYPE_MRNA			= "mRNA";
	public static transient final String DATA_TYPE_NONE			= "none";
	public static transient final String DATA_TYPE_PROTEIN		= "protein";

	private float clampMin			= 0f;
	private float clampMid			= 0f;
	private float clampMax			= 0f;
	private String confidenceType	= null;
	private	String control			= null;
	private	String cTime			= null;
	private String experimentId		= null;
	private	String factorUnit		= null;
	private	String sampleId			= null;
	private	String stress			= null;
	private String taxonomyId		= null;
	private	String treatment		= null;
	private	String tTime			= null;
	private String units			= null;


	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();

	public static final transient ProvidesKey<Sample> KEY_PROVIDER = new ProvidesKey<Sample>() {
		public Object getKey(Sample item) {
			return item == null ? null : item.getExperimentId() + "_" + item.getSampleId();
		}
	};


	//********************************************************************************

	private Sample() {}

	public Sample(final String expId, final String sampleId, final String taxonomyId, final String source) {
		super();
		this.experimentId		= expId;
		this.sampleId 	= sampleId;
		this.taxonomyId = taxonomyId;
		setSource(source);
	}

	//********************************************************************************

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((experimentId == null) ? 0 : experimentId.hashCode());
		result = prime * result
		+ ((sampleId == null) ? 0 : sampleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sample other = (Sample) obj;
		if (experimentId == null) {
			if (other.experimentId != null)
				return false;
		} else if (!experimentId.equals(other.experimentId))
			return false;
		if (sampleId == null) {
			if (other.sampleId != null)
				return false;
		} else if (!sampleId.equals(other.sampleId))
			return false;
		return true;
	}

	public String getExperimentId() {
		return experimentId;
	}

	public String getSampleId() {
		return sampleId;
	}

	public float getClampMax() {
		return clampMax;
	}

	public float getClampMid() {
		return clampMid;
	}

	public float getClampMin() {
		return clampMin;
	}

	public final String getControl() {
		return control;
	}

	public final String getStress() {
		return stress;
	}

	public final String getSummary() {
		return stress + " - " + treatment + " - " + control;
	}

	public final String getTaxonomyId() {
		return taxonomyId;
	}

	public final String getTreatment() {
		return treatment;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public final String getUnits() {
		return units;
	}

	public void setClampValues(final float clampMin, final float clampMid, final float clampMax) {
		this.clampMin = clampMin;
		this.clampMid = clampMid;
		this.clampMax = clampMax;
	}

	//********************************************************************************

	public void setConfidenceType(final String confidenceType) {
		this.confidenceType = confidenceType;
	}

	//********************************************************************************

	public void setFactorUnits(final String factorUnit) {
		this.factorUnit = factorUnit;
	}

	//********************************************************************************

	public void setUnits(final String units) {
		this.units = units;
	}

	//********************************************************************************

	public void setStress(final String stress) {
		this.stress = stress;
	}

	//********************************************************************************

	public void setTreatment(final String treatment, final String tTime) {
		this.treatment 	= treatment;
		this.tTime		= tTime;
	}

	//********************************************************************************

	public void setControl(final String control, final String cTime) {
		this.control 	= control;
		this.cTime		= cTime;
	}

	//********************************************************************************

}