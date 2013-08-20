package gov.lbl.glamm.shared.model;

import gov.lbl.glamm.shared.model.interfaces.HasType;

public class DataGroupElement {
	
	private String genomeName,
				   id,
				   taxId,
				   callbackUrl;
	private float strength;
	private HasType element;
				   
	
	private DataGroupElement() {
		genomeName = "";
		id = "";
		taxId = "";
		callbackUrl = "";
		element = null;
		strength = 0;
	}
	
	public DataGroupElement(String id) {
		this();
		this.id = id;
	}
	
	public String getGenomeName() {
		return genomeName;
	}
	
	public void setGenomeName(String genomeName) {
		this.genomeName = genomeName;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTaxonomyId() {
		return taxId;
	}
	
	public void setTaxonomyId(String taxId) {
		this.taxId = taxId;
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}
	
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	public float getStrength() {
		return strength;
	}
	
	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	public HasType getElement() {
		return element;
	}
	
	public void setElement(HasType element) {
		this.element = element;
	}

}
