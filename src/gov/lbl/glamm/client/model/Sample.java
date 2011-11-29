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

	public static enum TargetType {

		GENE("gene", "Gene", true),
		REACTION("rxn", "Reaction", false),
		COMPOUND("cpd", "Compound", false);

		private String targetType;
		private String caption;
		private boolean isDefault;

		private TargetType(final String targetType, final String caption, final boolean isDefault) {
			this.targetType = targetType;
			this.caption = caption;
			this.isDefault = isDefault;
		}

		public static TargetType fromString(final String theString) {
			TargetType targetType = null;
			if(theString != null) {
				for(TargetType value : TargetType.values()) {
					if(theString.equals(value.toString())) {
						targetType = value;
						break;
					}
				}
			}
			if(targetType == null)
				throw new IllegalArgumentException("Type request null or undefined.");

			return targetType;
		}

		@Override
		public String toString() {
			return targetType;
		}

		public String getCaption() {
			return caption;
		}

		public boolean isDefault() {
			return isDefault;
		}
	}

	private float clampMin;
	private float clampMid;
	private float clampMax;
	private String confidenceType;
	private	String control;
	private	String cTime;
	private DataType dataType;
	private String experimentId;
	private	String factorUnit;
	private	String sampleId;
	private	String stress;
	private TargetType targetType;
	private	String treatment;
	private	String tTime;
	private String units;

	public static final transient ProvidesKey<Sample> KEY_PROVIDER = new ProvidesKey<Sample>() {
		public Object getKey(Sample item) {
			return item == null ? null : item.getExperimentId() + "_" + item.getSampleId();
		}
	};

	@SuppressWarnings("unused")
	private Sample() {}

	public Sample(final String expId, final String sampleId, final DataType dataType) {
		super();
		this.experimentId		= expId;
		this.sampleId 			= sampleId;
		this.dataType			= dataType;
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

	public final TargetType getTargetType() {
		return targetType;
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

	public void setConfidenceType(final String confidenceType) {
		this.confidenceType = confidenceType;
	}

	public void setFactorUnits(final String factorUnit) {
		this.factorUnit = factorUnit;
	}

	public void setUnits(final String units) {
		this.units = units;
	}

	public void setStress(final String stress) {
		this.stress = stress;
	}

	public void setTargetType(final TargetType targetType) {
		this.targetType = targetType;
	}

	public void setTreatment(final String treatment, final String tTime) {
		this.treatment 	= treatment;
		this.tTime		= tTime;
	}

	public void setControl(final String control, final String cTime) {
		this.control 	= control;
		this.cTime		= cTime;
	}

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

}