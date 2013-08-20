package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;

@SuppressWarnings("serial")
public class KBBound implements Serializable {
    private Float min;
    private Float max;
    private String variableType;
    private String variable;

	public KBBound() {
    	
    }

	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}

	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

}
