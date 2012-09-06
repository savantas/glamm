package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Citation;

import java.util.List;

/**
 * Data access object interface for citations data.
 * @author jtbates
 *
 */
public interface CitationsDAO {
	/**
	 * Gets the citations database table name.
	 * Should probably be deprecated soon.
	 * @return The citations database table name, null if unavailable.
	 */
	public String getCitationsTableName();
	
	/**
	 * Gets the list of database citations.
	 * @return The list of database citations.
	 */
	public List<Citation> getCitations();
}
