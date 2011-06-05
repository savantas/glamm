package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Reaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public interface ReactionDAO {
	public ArrayList<Reaction> getReactions(Collection<String> rxnIds, HashSet<String> dbNames);
	public ArrayList<Reaction> getReactionsForEcNums(Collection<String> ecNums, HashSet<String> dbNames);
	public ArrayList<Reaction> getReactionsForSearch(HashSet<String> dbNames);
	public ArrayList<Reaction> getRxn2EcMapping(String mapId, HashSet<String> dbNames);
	
	public HashSet<String> getRxnIdsForEcNums(Collection<String> ecNums, HashSet<String> dbNames);
}
