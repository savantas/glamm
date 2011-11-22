package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Reaction;

import java.util.Collection;
import java.util.Set;

public interface ReactionDAO {
	public Set<Reaction> getReactions(Collection<String> rxnIds);
	public Set<Reaction> getReactionsForEcNums(Collection<String> ecNums);
	public Set<Reaction> getReactionsForSearch(String mapId);
	public Set<String> getRxnIdsForEcNums(Collection<String> ecNums);
}
