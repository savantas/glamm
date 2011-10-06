package gov.lbl.glamm.client.model;



import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")

public class Sample
implements Serializable {

	public static transient final String DEFAULT_SAMPLE_ID 	= "-1";

	public enum DataType {

		NONE("None"),
		FITNESS("Fitness"),
		PROTEIN("Protein"),
		RNA("RNA"),
		RNASEQ("RNASeq"),
		SESSION("Session");

		private static HashMap<String, DataType> molExpType2DataType = new HashMap<String, DataType>();
		
		static {
			for(DataType dataType : DataType.values())
				molExpType2DataType.put(dataType.molExpType, dataType);
		}

		private String molExpType = null;

		private DataType(final String molExpType) {
			this.molExpType = molExpType;
		}
		
		public static DataType dataTypeForMolExpType(final String molExpType) {
			return molExpType2DataType.get(molExpType);
		}
		
		public String getMolExpType() {
			return molExpType;
		}
	}

	private float clampMin			= 0f;
	private float clampMid			= 0f;
	private float clampMax			= 0f;
	private String confidenceType	= null;
	private	String control			= null;
	private	String cTime			= null;
	private DataType dataType;
	private String experimentId		= null;
	private	String factorUnit		= null;
	private	String sampleId			= null;
	private	String stress			= null;
	private	String treatment		= null;
	private	String tTime			= null;
	private String units			= null;

	public static final transient ProvidesKey<Sample> KEY_PROVIDER = new ProvidesKey<Sample>() {
		public Object getKey(Sample item) {
			return item == null ? null : item.getExperimentId() + "_" + item.getSampleId();
		}
	};


	//********************************************************************************

	@SuppressWarnings("unused")
	private Sample() {}

	public Sample(final String expId, final String sampleId, final DataType dataType) {
		super();
		this.experimentId		= expId;
		this.sampleId 			= sampleId;
		this.dataType			= dataType;
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

	public String getConfidenceType() {
		return confidenceType;
	}

	public String getcTime() {
		return cTime;
	}

	public String getFactorUnit() {
		return factorUnit;
	}

	public String gettTime() {
		return tTime;
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
	
	public final DataType getDataType() {
		return dataType;
	}

	public final String getStress() {
		return stress;
	}

	public final String getSummary() {
		String unitString = getUnitString();
		return stress + " - " + treatment + unitString + " - " + control + unitString;
	}

	public final String getTreatment() {
		return treatment;
	}

	public final String getUnits() {
		return units;
	}
	
	public final String getUnitString() {
		String unitString = "";
		
		if(factorUnit != null && !factorUnit.isEmpty())
			unitString = "(" + factorUnit + ")";
		else if(units != null && !units.isEmpty())
			unitString = "(" + units + ")";
		
		return unitString;
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