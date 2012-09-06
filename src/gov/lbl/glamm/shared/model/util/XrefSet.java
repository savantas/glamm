package gov.lbl.glamm.shared.model.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of cross references to external databases, with methods for returning cross references from 
 * specific databases.  This is returned by all objects implementing the HasXrefs interface.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class XrefSet
implements Serializable {
	
	private Set<Xref> xrefs;
	
	/**
	 * Constructor
	 */
	public XrefSet() {
		xrefs = new HashSet<Xref>();
	}

	/**
	 * Adds cross reference to the set.
	 * @param xref The cross reference.
	 */
	public void addXref(Xref xref) {
		xrefs.add(xref);
	}

	/**
	 * Adds a collection of cross references to the set.
	 * @param xrefs The collection.
	 */
	public void addXrefs(Collection<Xref> xrefs) {
		if(xrefs != null && !xrefs.isEmpty())
			xrefs.addAll(xrefs);
	}

	/**
	 * Gets all cross references from the set.
	 * @return The set of cross references.
	 */
	public Set<Xref> getXrefs() {
		return xrefs;
	}

	/**
	 * Gets a cross reference from a specific database.
	 * @param dbName The name of the database.
	 * @return The cross reference.
	 */
	public Xref getXrefForDbName(String dbName) {
		for(Xref xref : xrefs) {
			if(xref.getXrefDbName().equals(dbName))
				return xref;
		}
		return null;
	}
	
	/**
	 * Gets a cross reference from a set of databases.
	 * @param dbNames The collection of database names.
	 * @return The cross reference
	 */
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
