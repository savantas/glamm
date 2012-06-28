package gov.lbl.glamm.client.model.interfaces;

import gov.lbl.glamm.client.model.util.Synonym;

import java.util.Set;

/**
 * Interface indicating the implementor has synonyms.
 * @author jtbates
 *
 */
public interface HasSynonyms {
	/**
	 * Adds a synonym for this object.
	 * @param synonym The synonym.
	 */
	public void addSynonym(final Synonym synonym);
	
	/**
	 * Gets the set of synonyms for this object.
	 * @return The set of synonyms.
	 */
	public Set<Synonym> getSynonyms();
	
	/**
	 * Sets the synonyms for this object to those passed as a parameter to this method.
	 * @param synonyms The set of synonyms.
	 */
	public void setSynonyms(final Set<Synonym> synonyms);
}
