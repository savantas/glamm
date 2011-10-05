package gov.lbl.glamm.client.model.util;

import gov.lbl.glamm.client.model.interfaces.HasXrefs;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class XrefSet
implements HasXrefs, Serializable {
	
	private Set<Xref> xrefs;
	
	public XrefSet() {
		xrefs = new HashSet<Xref>();
	}

	@Override
	public void addXref(Xref xref) {
		xrefs.add(xref);
	}

	@Override
	public Set<Xref> getXrefs() {
		return xrefs;
	}

	@Override
	public Xref getXrefForDbName(String dbName) {
		for(Xref xref : xrefs) {
			if(xref.getXrefDbName().equals(dbName))
				return xref;
		}
		return null;
	}
	
	@Override
	public Xref getXrefForDbNames(Collection<String> dbNames) {
		for(Xref xref : xrefs) {
			if(dbNames.contains(xref.getXrefDbName()))
				return xref;
		}
		return null;
	}

}
