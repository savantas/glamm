package gov.lbl.glamm.shared;

import java.io.Serializable;

/**
 * This is just a little class with three components that describe an external service parameter
 * - a human readable name for form inputs
 * - a registered name with the external service
 * - an abbreviated name for the GLAMM state URL
 * It also holds the current parameter value.
 * 
 * @author wjriehl
 */
@SuppressWarnings("serial")
public class ExternalServiceParameter implements Serializable {

	public enum ParameterType {
		STRING,						// parameters set by the user
		BOOLEAN;					// hidden parameter set programmatically
		
		public static ParameterType fromString(String s) {
			for (ParameterType type : ParameterType.values()) {
				if (type.toString().equalsIgnoreCase(s))
					return type;
			}
			return STRING;
		}
	}
	
	private String hrName,
				   extName,
				   stateName,
				   value;
	private ParameterType type;
	private boolean visible;
	
	public ExternalServiceParameter(String hrName, String extName, String stateName) {
		this();
		setHumanReadableName(hrName);
		setExternalUrlName(extName);
		setStateUrlName(stateName);
	}
	
	public ExternalServiceParameter() {
		hrName = "";
		extName = "";
		stateName = "";
		value = "";
		visible = true;
	}
	
	public void setHumanReadableName(String hrName) {
		if (hrName != null)
			this.hrName = hrName;
	}
	
	public String getHumanReadableName() {
		return hrName;
	}
	
	public void setExternalUrlName(String extName) {
		if (extName != null)
			this.extName = extName;
	}
	
	public String getExternalUrlName() {
		return extName;
	}
	
	public void setStateUrlName(String stateName) {
		if (stateName != null)
			this.stateName = stateName;
	}
	
	public String getStateUrlName() {
		return stateName;
	}
	
	public void setValue(String value) {
		if (value != null)
			this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public ParameterType getParameterType() {
		return type;
	}
	
	public void setParameterType(ParameterType type) {
		this.type = type;
	}
	
	public void setTypeFromString(String s) {
		this.type = ParameterType.fromString(s);
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisibilityFromString(String s) {
		visible = s.equalsIgnoreCase("user") || s.equalsIgnoreCase("true");
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}