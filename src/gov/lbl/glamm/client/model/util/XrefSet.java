package gov.lbl.glamm.client.model.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class XrefSet
implements Serializable {
	
	private Set<Xref> xrefs;
	
	public XrefSet() {
		xrefs = new HashSet<Xref>();
	}

	public void addXref(Xref xref) {
		xrefs.add(xref);
	}
	
	public void addXrefs(Collection<Xref> xrefs) {
		if(xrefs != null && !xrefs.isEmpty())
			xrefs.addAll(xrefs);
	}

	public Set<Xref> getXrefs() {
		return xrefs;
	}

	public Xref getXrefForDbName(String dbName) {
		for(Xref xref : xrefs) {
			if(xref.getXrefDbName().equals(dbName))
				return xref;
		}
		return null;
	}
	
	public Xref getXrefForDbNames(Collection<String> dbNames) {
		for(Xref xref : xrefs) {
			if(dbNames.contains(xref.getXrefDbName()))
				return xref;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((xrefs == null) ? 0 : xrefs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XrefSet other = (XrefSet) obj;
		if (xrefs == null) {
			if (other.xrefs != null)
				return false;
		} else if (!xrefs.equals(other.xrefs))
			return false;
		return true;
	}
	
	

}
