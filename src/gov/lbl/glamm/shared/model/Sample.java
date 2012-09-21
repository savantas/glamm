package gov.lbl.glamm.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Model class for Sample - the basic unit of experimental data.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Sample
implements Serializable {

	public static transient final String DEFAULT_SAMPLE_ID 	= "-1";

	/**
	 * Enum for the various data types: those explicitly supported by the GLAMM database (Fitness, Protein, RNA, RNASeq)
	 * and those uploaded by the user (Session.)
	 * @author jtbates
	 *
	 */
	public enum DataType {

		NONE("None"),
		RNA("RNA"),
		FITNESS("Fitness"),
		FBA("FBA"),
		PROTEIN("Protein"),
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

		/**
		 * Gets the DataType from the MicrobesOnline experiment type string.
		 * @param molExpType The MicrobesOnline experiment type string.
		 * @return The data type.
		 */
		public static DataType dataTypeForMolExpType(final String molExpType) {
			return molExpType2DataType.get(molExpType);
		}

		/**
		 * Get the MicrobesOnline experiment type string for this DataType.
		 * @return The experiment type string.
		 */
		public String getMolExpType() {
			return molExpType;
		}
	}

	/**
	 * Gets the target type on to which the Sample data will be projected.
	 * @author jtbates
	 *
	 */
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

		/**
		 * Get the target type from a string - typically a CSS class.
		 * @param theString The string.
		 * @return The target type.
		 */
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

		/**
		 * Gets the caption string for this data type.
		 * @return The caption string.
		 */
		public String getCaption() {
			return caption;
		}

		/**
		 * Gets the flag indicating whether or not this is the default data type
		 * for uploaded experiments (currently GENE.)
		 * @return The flag.
		 */
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

	
	// Groupings
	private Experiment experiment = null;

	// Information
	private String id = null;
	private HashMap<String,String> attributesMap = null;

	// Representation
	private boolean htmlDirty = false;
	private String html = null;

	// Children
	/** map from elementId (e.g. gene id or compound id)
	 * concatenated with experiment data type
	 * to measurement object */
	private HashMap<String,Measurement> elementIdDataTypeMeasurementMap = null;

	// Description methods
	public String toHtml() {
		if ( htmlDirty ) {
		StringBuilder builder = new StringBuilder();
		builder.append("<span class='title'>").append(id).append("</span>");
		builder.append("<br/><span class='attributeName'>")
			.append( "dataType" )
			.append("</span>")
			.append(": <span class='attribute'>")
			.append( dataType.toString() )
			.append("</span>");
		;
		for ( Entry<String,String> attribute : this.getAttributesMap().entrySet() ) {
			builder.append("<br/><span class='attributeName'>")
				.append(attribute.getKey())
				.append("</span>")
				.append(": <span class='attribute'>")
				.append(attribute.getValue())
				.append("</span>");
			;
		}
		html = builder.toString();
		htmlDirty = false;
		}
		return html;
	}

	// Bean methods
	public Experiment getExperiment() {
		return experiment;
	}
	public void setExperiment(Experiment experiment) {
		this.htmlDirty = true;
		this.experiment = experiment;
	}

	public void setDataType( DataType dataType ) {
		this.dataType = dataType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.htmlDirty = true;
		this.id = id;
	}
	public HashMap<String, String> getAttributesMap() {
		if ( attributesMap == null ) {
			attributesMap = new HashMap<String,String>();
		}
		return attributesMap;
	}
	public void setAttributesMap(HashMap<String, String> attributesMap) {
		this.attributesMap = attributesMap;
	}
	/** map from elementId (e.g. gene id or compound id)
	 * concatenated with experiment data type
	 * to measurement object */
	public HashMap<String,Measurement> getElementIdDataTypeMeasurementMap() {
		if ( elementIdDataTypeMeasurementMap == null ) {
			elementIdDataTypeMeasurementMap = new HashMap<String,Measurement>();
		}
		return elementIdDataTypeMeasurementMap;
	}
	
	/**
	 * Key provider. 
	 */
	public static final transient ProvidesKey<Sample> KEY_PROVIDER = new ProvidesKey<Sample>() {
		public Object getKey(Sample item) {
			return item == null ? null : item.getExperimentId() + "_" + item.getSampleId();
		}
	};

	public Sample() {
		super();
		clampMin 		= 0.0f;
		clampMid 		= 0.0f;
		clampMax 		= 0.0f;
		confidenceType	= "";
		control 		= "";
		cTime 			= "";
		factorUnit 		= "";
		stress			= "";
		targetType		= TargetType.GENE;
		treatment		= "";
		tTime	 		= "";
		units			= "";
	}

	/**
	 * Constructor
	 * @param expId The experiment id.
	 * @param sampleId The sample id.
	 * @param dataType The data type for this sample.
	 */
	public Sample(final String expId, final String sampleId, final DataType dataType) {
		this();
		this.experimentId		= expId;
		this.sampleId 			= sampleId;
		this.dataType			= dataType;
		
	}

	/**
	 * Gets the confidence type.
	 * @return The confidence type.
	 */
	public String getConfidenceType() {
		return confidenceType;
	}

	/**
	 * Gets the control time (cTime in the MicrobesOnline microarray database.)
	 * @return The control time.
	 */
	public String getcTime() {
		return cTime;
	}

	/**
	 * Gets the factor unit (factorUnit in the MicrobesOnline microarray database.)
	 * @return The factor unit.
	 */
	public String getFactorUnit() {
		return factorUnit;
	}

	/**
	 * Gets the treatment time (tTime in the MicrobesOnline microarray database.)
	 * @return The treatment time.
	 */
	public String gettTime() {
		return tTime;
	}

	/**
	 * Gets the experiment id.
	 * @return The id.
	 */
	public String getExperimentId() {
		return experimentId;
	}

	/**
	 * Gets the sample id.
	 * @return The sample id.
	 */
	public String getSampleId() {
		return sampleId;
	}

	/**
	 * Gets the maximum value to which measurements are clamped.
	 * @return The value.
	 */
	public float getClampMax() {
		return clampMax;
	}

	/**
	 * Gets the value midway between clampMin and clampMax.
	 * @return The value.
	 */
	public float getClampMid() {
		return clampMid;
	}

	/**
	 * Gets the minimum value to which measurements are clamped.
	 * @return The value.
	 */
	public float getClampMin() {
		return clampMin;
	}

	/**
	 * Gets the description of the control.
	 * @return The description.
	 */
	public final String getControl() {
		return control;
	}

	/**
	 * Gets the data type for this sample.
	 * @return The data type.
	 */
	public final DataType getDataType() {
		return dataType;
	}

	/**
	 * Gets the description of the stress.
	 * @return The stress description.
	 */
	public final String getStress() {
		return stress;
	}

	/**
	 * Generates a summary of the sample suitable for display in a table.
	 * @return The summary.
	 */
	public final String getSummary() {
		String unitString = getUnitString();
		return stress + " - " + treatment + unitString + " - " + control + unitString;
	}

	/**
	 * Gets the target type (i.e., the type of element for which this sample applies.)
	 * @return The target type.
	 */
	public final TargetType getTargetType() {
		return targetType;
	}

	/**
	 * Gets the description of the treatment.
	 * @return The description.
	 */
	public final String getTreatment() {
		return treatment;
	}

	/**
	 * Gets the units for the measurements.
	 * @return The units.
	 */
	public final String getUnits() {
		return units;
	}

	/**
	 * Generates a minimally formatted description of the units suitable for display.
	 * @return The formatted string.
	 */
	public final String getUnitString() {
		String unitString = "";

		if(factorUnit != null && !factorUnit.isEmpty())
			unitString = "(" + factorUnit + ")";
		else if(units != null && !units.isEmpty())
			unitString = "(" + units + ")";

		return unitString;
	}

	/**
	 * Sets the clamping values for display.
	 * @param clampMin The minimum displayable value.
	 * @param clampMid The midpoint between clampMin and clampMax, as determined by the user.
	 * @param clampMax The maximum displayable value.
	 */
	public void setClampValues(final float clampMin, final float clampMid, final float clampMax) {
		this.clampMin = clampMin;
		this.clampMid = clampMid;
		this.clampMax = clampMax;
	}

	/**
	 * Sets the confidence type.
	 * @param confidenceType The confidence type.
	 */
	public void setConfidenceType(final String confidenceType) {
		this.confidenceType = (confidenceType == null) ? "" : confidenceType;
	}

	/**
	 * Sets the factor units.
	 * @param factorUnit The units.
	 */
	public void setFactorUnits(final String factorUnit) {
		this.factorUnit = (factorUnit == null) ? "" : factorUnit;
	}

	/**
	 * Sets the units for display.
	 * @param units The units.
	 */
	public void setUnits(final String units) {
		this.units = (units == null) ? "" : units;
	}

	/**
	 * Sets the description of the stress.
	 * @param stress The description.
	 */
	public void setStress(final String stress) {
		this.stress = (stress == null) ? "" : stress;
	}

	/**
	 * Sets the target type.
	 * @param targetType The type.
	 */
	public void setTargetType(final TargetType targetType) {
		if(targetType == null)
			throw new IllegalArgumentException("targetType may not be null.");
		this.targetType = targetType;
	}

	/**
	 * Sets the treatment and the time for which the treatment was administered.
	 * @param treatment The description of the treatment.
	 * @param tTime The time.
	 */
	public void setTreatment(final String treatment, final String tTime) {
		this.treatment = (treatment == null) ? "" : treatment;
		this.tTime = (tTime == null) ? "" : tTime;
	}

	/**
	 * Sets the control and the time for which the control was administered.
	 * @param control The description of the control.
	 * @param cTime The time.
	 */
	public void setControl(final String control, final String cTime) {
		this.control = (control == null) ? "" : control;
		this.cTime = (cTime == null) ? "" : cTime;
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