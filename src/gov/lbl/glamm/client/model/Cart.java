package gov.lbl.glamm.client.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class Cart implements Serializable {
	
	private Set<Compound>	compounds;
	private Set<Gene> 		genes;
	private Organism 		organism;
	private Set<Pathway> 	pathways;
	private Set<Reaction> 	reactions;
	private Set<Sample> 	samples;
	private User			user;
	
	private Cart() {
		this.compounds = new LinkedHashSet<Compound>();
		this.genes = new LinkedHashSet<Gene>();
		this.organism = null;
		this.pathways = new LinkedHashSet<Pathway>();
		this.reactions = new LinkedHashSet<Reaction>();
		this.samples = new LinkedHashSet<Sample>();
		this.user = null;
	}
	
	public void addCompound(final Compound compound) {
		if(compound == null)
			throw new IllegalArgumentException("Attempting to add null Compound.");
		compounds.add(compound);
	}
	
	public void addGene(final Gene gene) {
		if(gene == null)
			throw new IllegalArgumentException("Attempting to add null Gene.");
		genes.add(gene);
	}
	
	public void addPathway(final Pathway pathway) {
		if(pathway == null)
			throw new IllegalArgumentException("Attempting to add null Pathway.");
		pathways.add(pathway);
	}
	
	public void addReaction(final Reaction reaction) {
		if(reaction == null)
			throw new IllegalArgumentException("Attempting to add null Reaction.");
		reactions.add(reaction);
	}
	
	public void addSample(final Sample sample) {
		if(sample == null)
			throw new IllegalArgumentException("Attempted to add null Sample.");
		samples.add(sample);
	}

	public Set<Compound> getCompounds() {
		return compounds;
	}

	public Set<Gene> getGenes() {
		return genes;
	}

	public Organism getOrganism() {
		return organism;
	}

	public Set<Pathway> getPathways() {
		return pathways;
	}

	public Set<Reaction> getReactions() {
		return reactions;
	}

	public Set<Sample> getSamples() {
		return samples;
	}

	public User getUser() {
		return user;
	}

	public void setOrganism(Organism organism) {
		this.organism = organism;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
