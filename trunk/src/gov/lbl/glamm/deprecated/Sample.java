package gov.lbl.glamm.deprecated;

import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Measurement;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A sample in an experiment.
 * Contains a map containing the measurement values associated with this sample,
 * keyed by element id and experiment data type.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
@Deprecated
public class Sample {

	/**
	 * Enum for the various data types: those explicitly supported by the GLAMM database (Fitness, Protein, RNA, RNASeq)
	 * and those uploaded by the user (Session.)
	 * @author jtbates
	 *
	 */
	public enum DataType {
		NONE("None")
		, FITNESS("Fitness")
		, PROTEIN("Protein")
		, RNA("RNA")
		, RNASeq("RNASeq")
		, SESSION("Session")
		;

		private static Map<String,DataType> molExpType2DataType = new HashMap<String,DataType>();
		static {
			for ( DataType dataType : DataType.values() ) {
				molExpType2DataType.put( dataType.molExpType(), dataType );
			}
		}
		public static DataType dataTypeForMolExpType( String molExpType ) {
			return molExpType2DataType.get( molExpType );
		}

		private String molExpType = null;
		private DataType( final String molExpType ) {
			this.molExpType = molExpType;
		}
		public String molExpType() {
			return molExpType;
		}
	}

	// Groupings
	private Experiment experiment = null;
	private DataType dataType = null;

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
	public DataType getDataType() {
		return dataType;
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
}
