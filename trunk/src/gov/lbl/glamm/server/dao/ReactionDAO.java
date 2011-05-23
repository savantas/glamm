package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Reaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public interface ReactionDAO {
	public ArrayList<Reaction> getReactions(Collection<String> rxnIds, String dbName);
//	public ArrayList<Reaction> getReactionsForOrganism(String taxonomyId, String dbName);
	public ArrayList<Reaction> getReactionsForEcNums(Collection<String> ecNums, String dbName);
	public ArrayList<Reaction> getReactionsForSearch(String dbName);
	public ArrayList<Reaction> getRxn2EcMapping(String mapId, String dbName);
	
	public HashSet<String> getRxnIdsForEcNums(Collection<String> ecNums, String dbName);
}
