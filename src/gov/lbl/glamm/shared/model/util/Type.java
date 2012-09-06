package gov.lbl.glamm.shared.model.util;

import java.io.Serializable;

/**
 * Convenience class that assigns a GLAMM-specific type to model classes implementing the HasType interface.
 * This avoids the use of Java reflection which is not implemented in the GWT.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Type implements Serializable {
	private static transient int nextHashCode;
	private final int index;

	/**
	 * Constructor
	 */
	public Type() {
		index = ++nextHashCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Type other = (Type) obj;
		if (index != other.index)
			return false;
		return true;
	}
}

