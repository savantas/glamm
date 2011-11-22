package gov.lbl.glamm.client.model.util;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Xref implements Serializable {
	//********************************************************************************

	private String xrefId		= null;
	private String xrefDbName 	= null;

	//********************************************************************************

	@SuppressWarnings("unused")
	private Xref() {}

	public Xref(final String xrefId, final String xrefDbName) {
		this.xrefId = xrefId;
		this.xrefDbName = xrefDbName;
	}

	//********************************************************************************

	public String getXrefId() {
		return xrefId;
	}

	//********************************************************************************

	public String getXrefDbName() {
		return xrefDbName;
	}

	//********************************************************************************

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((xrefDbName == null) ? 0 : xrefDbName.hashCode());
		result = prime * result
		+ ((xrefId == null) ? 0 : xrefId.hashCode());
		return result;
	}

	//********************************************************************************

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Xref other = (Xref) obj;
		if (xrefDbName == null) {
			if (other.xrefDbName != null)
				return false;
		} else if (!xrefDbName.equals(other.xrefDbName))
			return false;
		if (xrefId == null) {
			if (other.xrefId != null)
				return false;
		} else if (!xrefId.equals(other.xrefId))
			return false;
		return true;
	}

}
