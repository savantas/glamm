package gov.lbl.glamm.client.model.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Type implements Serializable {
	private static transient int nextHashCode;
	private final int index;

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

