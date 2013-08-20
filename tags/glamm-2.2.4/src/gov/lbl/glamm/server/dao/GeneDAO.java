package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Gene;

import java.util.Collection;
import java.util.Set;

/**
 * Data access object interface for genes.
 * @author jtbates
 *
 */
public interface GeneDAO {
	
	/**
	 * Gets the set of all EC numbers for the genes of an organism specified by the given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The set of EC numbers.
	 */
	public Set<String> getEcNumsForOrganism(String taxonomyId);
	
	/**
	 * Gets the set of genes for a given taxonomy id and collection of EC numbers. 
	 * @param taxonomyId The taxonomy id.
	 * @param ecNums The collection of EC numbers.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForEcNums(String taxonomyId, Collection<String> ecNums);
	
	/**
	 * Gets the set of genes for a collection of VIMSS ids (MicrobesOnline locus ids.)
	 * @param taxonomyId The taxonomy id - TODO: Not actually necessary - VIMSS ids are taxon-specific.
	 * @param vimssIds The collection of VIMSS ids.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> vimssIds);

	public Set<Gene> getGenesForVimssIds(Collection<String> vimssIds); 
	
	/**
	 * Gets the set of all genes for a given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForOrganism(String taxonomyId);
	
	/**
	 * Gets the set of genes for a given taxonomy id and array of reaction ids.
	 * @param taxonomyId The taxonomy id.
	 * @param rxnIds The array of reaction ids.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds);
	
	/**
	 * Gets the set of genes for a given taxonomy ids and collection of gene synonyms.
	 * @param taxonomyId The taxonomy id.
	 * @param synonyms The collection of gene synonyms.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms);

}
