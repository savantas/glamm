package gov.lbl.glamm.client.model;


import java.io.Serializable;

@SuppressWarnings("serial")
public class MNNode extends GlammPrimitive implements Serializable {
	
	//********************************************************************************
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	private String cpd0ExtId = null;
	private String cpd1ExtId = null;
	private String rxnExtId = null;	
	private boolean isNative = true;

	//********************************************************************************
	
	@SuppressWarnings("unused")
	private MNNode() {}

	public MNNode(String rxnExtId, String cpd0ExtId, String cpd1ExtId) {
		this.rxnExtId = rxnExtId;
		this.cpd0ExtId = cpd0ExtId;
		this.cpd1ExtId = cpd1ExtId;
	}

	//********************************************************************************

	public MNNode(String rxnExtId, String cpd0ExtId, String cpd1ExtId, boolean isNative) {
		this(rxnExtId, cpd0ExtId, cpd1ExtId);
		setNative(isNative);
	}

	//********************************************************************************

	public String getCpd0ExtId() {
		return cpd0ExtId;
	}

	//********************************************************************************

	public String getCpd1ExtId() {
		return cpd1ExtId;
	}

	//********************************************************************************

	public String getRxnExtId() {
		return rxnExtId;
	}

	@Override
	public Type getType() {
		return TYPE;
	}
	
	//********************************************************************************

	public boolean isNative() {
		return isNative;
	}

	//********************************************************************************

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	//********************************************************************************

	public String toString() {
		return rxnExtId + " (" + cpd0ExtId + ", " + cpd1ExtId + ")";
	}
	
	//********************************************************************************

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((cpd0ExtId == null) ? 0 : cpd0ExtId.hashCode());
		result = prime * result
				+ ((cpd1ExtId == null) ? 0 : cpd1ExtId.hashCode());
		result = prime * result + (isNative ? 1231 : 1237);
		result = prime * result
				+ ((rxnExtId == null) ? 0 : rxnExtId.hashCode());
		return result;
	}

	//********************************************************************************

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MNNode other = (MNNode) obj;
		if (cpd0ExtId == null) {
			if (other.cpd0ExtId != null)
				return false;
		} else if (!cpd0ExtId.equals(other.cpd0ExtId))
			return false;
		if (cpd1ExtId == null) {
			if (other.cpd1ExtId != null)
				return false;
		} else if (!cpd1ExtId.equals(other.cpd1ExtId))
			return false;
		if (isNative != other.isNative)
			return false;
		if (rxnExtId == null) {
			if (other.rxnExtId != null)
				return false;
		} else if (!rxnExtId.equals(other.rxnExtId))
			return false;
		return true;
	}
	
	//********************************************************************************
		
}
