package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Sample;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data access object interface for genes.
 * @author jtbates
 *
 */
public interface OrganismDAO {
	
	/**
	 * Gets a list of all organisms.
	 * @return The list of all organisms, sorted alphabetically.
	 */
	public List<Organism> getAllOrganisms();
	
	/**
	 * Gets the list of all organisms with experiment data of given type.
	 * @param dataType The data type.
	 * @return The list of organisms, sorted alphabetically.
	 */
	public List<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType);
	
	/**
	 * Gets the organism for a given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The organism.
	 */
	public Organism getOrganismForTaxonomyId(final String taxonomyId);
	
	/**
	 * Gets the EC number to organism mapping for the given set of EC numbers.  That is, for each EC number,
	 * a set of organisms whose genomes contain the loci corresponding to those EC numbers is returned and associated
	 * with that EC number in the resulting map.
	 * @param ecNums The EC numbers.
	 * @return THe EC number to organism mapping.
	 */
	public Map<String, Set<Organism>> getTransgenicCandidatesForEcNums(Set<String> ecNums);
}
