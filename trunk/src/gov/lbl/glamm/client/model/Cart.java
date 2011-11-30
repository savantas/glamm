package gov.lbl.glamm.client.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A cart for holding ordered sets of compounds, genes, pathways, reactions, and samples, as selected by the user.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Cart implements Serializable {
	
	private Set<Compound>	compounds;
	private Set<Gene> 		genes;
	private Organism 		organism;
	private Set<Pathway> 	pathways;
	private Set<Reaction> 	reactions;
	private Set<Sample> 	samples;
	private User			user;
	
	/**
	 * Constructor
	 */
	private Cart() {
		this.compounds = new LinkedHashSet<Compound>();
		this.genes = new LinkedHashSet<Gene>();
		this.organism = null;
		this.pathways = new LinkedHashSet<Pathway>();
		this.reactions = new LinkedHashSet<Reaction>();
		this.samples = new LinkedHashSet<Sample>();
		this.user = null;
	}
	
	/**
	 * Adds a compound to the cart.
	 * @param compound The compound.
	 */
	public void addCompound(final Compound compound) {
		if(compound == null)
			throw new IllegalArgumentException("Attempting to add null Compound.");
		compounds.add(compound);
	}
	
	/**
	 * Adds a gene to the cart.
	 * @param gene The gene.
	 */
	public void addGene(final Gene gene) {
		if(gene == null)
			throw new IllegalArgumentException("Attempting to add null Gene.");
		genes.add(gene);
	}
	
	/**
	 * Adds a pathway to the cart.
	 * @param pathway The pathway.
	 */
	public void addPathway(final Pathway pathway) {
		if(pathway == null)
			throw new IllegalArgumentException("Attempting to add null Pathway.");
		pathways.add(pathway);
	}
	
	/**
	 * Adds a reaction to the cart.
	 * @param reaction The reaction.
	 */
	public void addReaction(final Reaction reaction) {
		if(reaction == null)
			throw new IllegalArgumentException("Attempting to add null Reaction.");
		reactions.add(reaction);
	}
	
	/**
	 * Adds a sample to the cart.
	 * @param sample The sample.
	 */
	public void addSample(final Sample sample) {
		if(sample == null)
			throw new IllegalArgumentException("Attempted to add null Sample.");
		samples.add(sample);
	}

	/**
	 * Gets the set of compounds in the cart.
	 * @return The set of compounds.
	 */
	public Set<Compound> getCompounds() {
		return compounds;
	}

	/**
	 * Gets the set of genes in the cart.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenes() {
		return genes;
	}

	/**
	 * Gets the organism in the cart.
	 * @return The organism.
	 */
	public Organism getOrganism() {
		return organism;
	}

	/**
	 * Gets the set of pathways in the cart.
	 * @return The set of pathways.
	 */
	public Set<Pathway> getPathways() {
		return pathways;
	}

	/**
	 * Gets the set of reactions in the cart.
	 * @return The set of reactions.
	 */
	public Set<Reaction> getReactions() {
		return reactions;
	}

	/**
	 * Gets the set of samples in the cart.
	 * @return The set of samples.
	 */
	public Set<Sample> getSamples() {
		return samples;
	}

	/**
	 * Gets the user associated with the cart.
	 * @return The user.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the organism in the cart.
	 * @param organism The organism.
	 */
	public void setOrganism(Organism organism) {
		this.organism = organism;
	}

	/**
	 * Sets the user associated with this cart.
	 * @param user The user.
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
