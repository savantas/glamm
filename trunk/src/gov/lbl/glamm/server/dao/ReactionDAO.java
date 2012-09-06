package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Reaction;

import java.util.Collection;
import java.util.Set;

/**
 * Data access object interface for reactions.
 * @author jtbates
 *
 */
public interface ReactionDAO {
	/**
	 * Gets the set of reactions for a collection of reaction ids.
	 * @param rxnIds The reaction ids.
	 * @return The set of reactions.
	 */
	public Set<Reaction> getReactions(Collection<String> rxnIds);
	
	/**
	 * Gets the set of reactinos for a collection of EC numbers.
	 * @param ecNums The EC numbers.
	 * @return The set of reactions.
	 */
	public Set<Reaction> getReactionsForEcNums(Collection<String> ecNums);
	
	/**
	 * Gets the set of all reactions for a given map id.
	 * @param mapId The map id.
	 * @return The set of all reactions.
	 */
	public Set<Reaction> getReactionsForSearch(String mapId);
	
	/**
	 * Gets the set of reaction ids for a collection of EC numbers.
	 * @param ecNums The collection of EC numbers.
	 * @return The set of reaction ids.
	 */
	public Set<String> getRxnIdsForEcNums(Collection<String> ecNums);
}
