package gov.lbl.glamm.client.model.interfaces;

import gov.lbl.glamm.client.model.util.XrefSet;

/**
 * Interface indicating this object has references in external databases.
 * @author jtbates
 *
 */
public interface HasXrefs {
	/**
	 * Gets the XrefSet associated with this object.
	 * @return The XrefSet.
	 */
	public XrefSet getXrefSet();
}
