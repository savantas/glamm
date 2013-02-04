package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;

@SuppressWarnings("serial")
public class KBFBAConstraintTerm implements Serializable {

    private Float coefficient;
    private String varType;
    private String variable;
    
    public KBFBAConstraintTerm() {
    	
    }
    
	public Float getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(Float coefficient) {
		this.coefficient = coefficient;
	}
	public String getVariableType() {
		return varType;
	}
	public void setVariableType(String varType) {
		this.varType = varType;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}

}
