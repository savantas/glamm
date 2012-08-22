package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Model class for Organism.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Organism implements Serializable {
	
	public static transient final String GLOBAL_MAP_NAME			= "Global Map";
	public static transient final String GLOBAL_MAP_TAXONOMY_ID		= "0";
	public static transient final long MIN_METAGENOME_TAXID = 1000000000000l;
		
	/**
	 * Comparator for listing Organism names alphabetically.
	 * @author jtbates
	 *
	 */
	public static class OrganismComparator implements Comparator<Organism> {

		@Override
		public int compare(Organism o0, Organism o1) {
			String name0 = o0.getName();
			String name1 = o1.getName();
			return name0.compareTo(name1);
		}
		
	}
	
	private String taxonomyId;
	private String name;
	private boolean isSessionOrganism;
	
	private transient Set<String> molTaxonomyIds;
	

	@SuppressWarnings("unused")
	private Organism() {}
	

	/**
	 * Factory method returning an Organism representing the global map (i.e. the map containing all reactions
	 * in a AnnotatedMapDescriptor object's metabolic network.)
	 */
	public static Organism globalMap() {
		return new Organism(GLOBAL_MAP_TAXONOMY_ID, GLOBAL_MAP_NAME, false);
	}
	
	
	/**
	 * Constructor
	 * @param taxonomyId The organism's taxonomy Id.  Typically a MicrobesOnline taxonomy id.
	 * @param name The name of the organism.
	 * @param isSessionOrganism Flag indicating whether or not this organism was uploaded during a user session.
	 */
	public Organism(String taxonomyId, String name, boolean isSessionOrganism) {		
		this.taxonomyId = taxonomyId;
		this.name = name;
		this.isSessionOrganism = isSessionOrganism;
		this.molTaxonomyIds = new HashSet<String>();
	}
	
	
	/**
	 * Adds a MicrobesOnline taxonomy id to the set of taxonomy ids for this organism.
	 * @param taxonomyId The taxonomy id.
	 */
	public void addMolTaxonomyId(String taxonomyId) {
		molTaxonomyIds.add(taxonomyId);
	}
	
	/**
	 * Adds a collection of MicrobesOnline taxonomy ids to the set of taxonomy ids for this organism.
	 * @param taxonomyIds The collection of taxonomy ids.
	 */
	public void addMolTaxonomyIds(Collection<String> taxonomyIds) {
		molTaxonomyIds.addAll(taxonomyIds);
	}
	
	/**
	 * Gets the set of MicrobesOnline taxonomy ids.
	 * @return The set of taxonomyIds.
	 */
	public Set<String> getMolTaxonomyIds() {
		return molTaxonomyIds;
	}
	
	/**
	 * Gets the name of the organism.
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}
	

	/**
	 * Gets the taxonomy id of the organism.
	 * @return The name.
	 */
	public String getTaxonomyId() {
		return this.taxonomyId;
	}
	

	/**
	 * Convenience method to determine if this organism is the global map.
	 * @return Flag indicating if this organism is the global map.
	 */
	public final boolean isGlobalMap() {
		return taxonomyId.equals(GLOBAL_MAP_TAXONOMY_ID);
	}
	

	/**
	 * Convenience method to determine if this organism is a metagenome.
	 * @return Flag indicating if this organism is a metagenome.
	 */
	public final boolean isMetagenome() {
		return Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID;
	}
	

	/**
	 * Convenience method to determine if this organism is a session (i.e. user-uploaded) organism.
	 * @return Flag indicating if this organism is a session organism.
	 */
	public final boolean isSessionOrganism() {
		return isSessionOrganism;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((taxonomyId == null) ? 0 : taxonomyId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Organism))
			return false;
		Organism other = (Organism) obj;
		if (this == other)
			return true;
		if (!super.equals(other))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taxonomyId == null) {
			if (other.taxonomyId != null)
				return false;
		} else if (!taxonomyId.equals(other.taxonomyId))
			return false;
		return true;
	}
	
	
	

}