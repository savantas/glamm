package gov.lbl.glamm.client.model.util;

import java.io.Serializable;

/**
 * Model class for Synonyms.  Synonyms have both a name and a type.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Synonym implements Serializable {
	
	private String name;
	private String type;
	
	@SuppressWarnings("unused")
	private Synonym() {}
	
	/**
	 * Constructor
	 * @param name The name of the synonym.
	 * @param type The type of the synonym (e.g. NCBI, VIMSS, etc.)
	 */
	public Synonym(final String name, final String type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the type.
	 * @return The type.
	 */
	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Synonym other = (Synonym) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
