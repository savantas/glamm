package gov.lbl.glamm.shared.model.interfaces;

import gov.lbl.glamm.shared.model.util.XrefSet;

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
