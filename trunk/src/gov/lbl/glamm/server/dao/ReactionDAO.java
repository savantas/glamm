package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Reaction;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ReactionDAO {
	public List<Reaction> getReactions(Collection<String> rxnIds, Set<String> dbNames);
	public List<Reaction> getReactionsForEcNums(Collection<String> ecNums, Set<String> dbNames);
	public List<Reaction> getReactionsForSearch(Set<String> dbNames);
	public List<Reaction> getRxn2EcMapping(String mapId, Set<String> dbNames);
	
	public Set<String> getRxnIdsForEcNums(Collection<String> ecNums, Set<String> dbNames);
}
