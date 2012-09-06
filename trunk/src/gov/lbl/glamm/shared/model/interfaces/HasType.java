package gov.lbl.glamm.shared.model.interfaces;

import gov.lbl.glamm.shared.model.util.Type;

/**
 * Interface indicating this object has a gov.lbl.glamm.client.model.util.Type.
 * @author jtbates
 *
 */
public interface HasType {
	/**
	 * Gets the type.
	 * @return The type.
	 */
	public Type getType();
}
