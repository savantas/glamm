package gov.lbl.glamm.client.model.interfaces;

import gov.lbl.glamm.client.model.util.Xref;

import java.util.Collection;
import java.util.Set;

public interface HasXrefs {
	public void addXref(final Xref xref);
	public Set<Xref> getXrefs();
	public Xref getXrefForDbName(final String dbName);
	public Xref getXrefForDbNames(final Collection<String> dbNames);
}
