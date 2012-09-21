package gov.lbl.glamm.deprecated;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Model object representing an experiment, containing a list of samples.
 * @author DHAP Digital, Inc - angie
 *
 */
@Deprecated
public class Experiment {
	// Information
	private String id = null;
	private HashMap<String,String> attributesMap = null;

	// Children
	private ArrayList<Sample> samples = null;

	// Representation
	private boolean htmlDirty = false;
	private String html = null;

	// Description methods
	public String toHtml() {
		if ( htmlDirty ) {
		StringBuilder builder = new StringBuilder();
		builder.append("<span class='title'>").append(id).append("</span>");
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

	// Bean methods
	public String getId() {
		if ( id == null ) {
			id = "";
		}
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
		this.htmlDirty = true;
		this.attributesMap = attributesMap;
	}
	public ArrayList<Sample> getSamples() {
		if ( samples == null ) {
			samples = new ArrayList<Sample>();
		}
		return samples;
	}
	public void setSamples(ArrayList<Sample> samples) {
		this.samples = samples;
	}
}
