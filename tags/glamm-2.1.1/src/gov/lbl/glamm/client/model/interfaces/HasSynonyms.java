package gov.lbl.glamm.client.model.interfaces;

import gov.lbl.glamm.client.model.util.Synonym;

import java.util.Set;

public interface HasSynonyms {
	public void addSynonym(final Synonym synonym);
	public Set<Synonym> getSynonyms();
	public void setSynonyms(final Set<Synonym> synonyms);
}
