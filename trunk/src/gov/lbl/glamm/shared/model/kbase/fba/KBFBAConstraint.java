package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class KBFBAConstraint implements Serializable {
    private Float rhs;
    private String sign;
    private List<KBFBAConstraintTerm> terms;
    private String name;

    public KBFBAConstraint() {
    	
    }

	public Float getRhs() {
		return rhs;
	}

	public void setRhs(Float rhs) {
		this.rhs = rhs;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public List<KBFBAConstraintTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<KBFBAConstraintTerm> terms) {
		this.terms = terms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
