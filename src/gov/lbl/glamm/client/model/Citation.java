package gov.lbl.glamm.client.model;


import java.io.Serializable;

/**
 * Representation of an external data source's citation
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Citation
implements Serializable {
	
	private String dbVersion;	
	private String description;	
	private String text;
	
	@SuppressWarnings("unused")
	private Citation() {}
	
	/**
	 * Constructor
	 * @param dbVersion	The version of the cited data source
	 * @param description A terse description of the data source
	 * @param text The full text of the citation. 
	 */
	public Citation(String dbVersion, String description, String text) {
		super();
		this.dbVersion=dbVersion;
		this.description=description;
		this.text=text;
	}

	/**
	 * Gets the database version.
	 * @return The version string.
	 */
	public String getDbVersion() {
		return dbVersion;
	}

	/** Gets the description of this citation.
	 * @return The description of this citation (e.g. "KEGG Reaction Database")
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the full text of this citation.
	 * @return The full text of the citation.
	 */
	public String getText() {
		return text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dbVersion == null) ? 0 : dbVersion.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citation other = (Citation) obj;
		if (dbVersion == null) {
			if (other.dbVersion != null)
				return false;
		} else if (!dbVersion.equals(other.dbVersion))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
}
