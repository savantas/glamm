package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.util.ReactionColor;
import gov.lbl.glamm.client.util.RowDependentSelectionCell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Reaction extends GlammPrimitive implements Serializable, RowDependentSelectionCell.HasOptions {

	//********************************************************************************

	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	private HashSet<String> 	ecNums = null;
	private String definition = null;
	private boolean isNative = true;
	private HashSet<Product>	products 	= null;
	private HashSet<Reactant> 	reactants 	= null;
	private HashSet<GlammPrimitive.Reference> geneRefs = null;
	private transient Organism selectedTransgenicCandidate = null;
	private ArrayList<Organism> transgenicCandidates = null;
	private HashMap<String, Organism> name2TransgenicCandidate = null;
	private HashMap<Organism, HashSet<String>> transgenicCandidate2EcNums = null;
	private transient ReactionColor color; // ReactionColor depends on resources from the client bundle - don't instantiate or try to set server-side.

	public static final transient ProvidesKey<Reaction> KEY_PROVIDER = new ProvidesKey<Reaction>() {
		public Object getKey(Reaction item) {
			return item == null ? null : new Integer(item.hashCode());
		}
	};

	//********************************************************************************

	public Reaction() {}

	//********************************************************************************

	public void addGeneReference(GlammPrimitive.Reference reference) {
		if(reference != null) {
			if(geneRefs == null) 
				geneRefs = new HashSet<GlammPrimitive.Reference>();
			geneRefs.add(reference);
		}
	}

	//********************************************************************************

	public void addProduct(Product rp) {
		if(rp != null) {
			if(products == null)
				products = new HashSet<Product>();
			products.add( rp );
		}
	}

	//********************************************************************************

	public void addReactant(Reactant rp) {
		if(rp != null) {
			if(reactants == null)
				reactants = new HashSet<Reactant>();
			reactants.add(rp);
		}
	}

	//********************************************************************************

	public void addTransgenicCandidate(String ecNum, Organism organism) {
		if(organism == null)
			return;
		// add to list
		if(transgenicCandidates == null)
			transgenicCandidates = new ArrayList<Organism>();
		if(!transgenicCandidates.contains(organism))
			transgenicCandidates.add(organism);

		// add to map
		if(transgenicCandidate2EcNums == null)
			transgenicCandidate2EcNums = new HashMap<Organism, HashSet<String>>();
		HashSet<String> ecNums = transgenicCandidate2EcNums.get(organism);
		if(ecNums == null) {
			ecNums = new HashSet<String>();
			transgenicCandidate2EcNums.put(organism, ecNums);
		}

		if(name2TransgenicCandidate == null)
			name2TransgenicCandidate = new HashMap<String, Organism>();
		name2TransgenicCandidate.put(organism.getName(), organism);
		ecNums.add(ecNum);

	}

	//********************************************************************************

	public void addEcNum(String ecNum) {
		if(ecNum != null && !ecNum.isEmpty()) {
			if(ecNums == null)
				ecNums = new HashSet<String>();
			ecNums.add(ecNum);
		}
	}

	//********************************************************************************

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	//********************************************************************************

	public HashSet<String> getEcNums() {
		return ecNums;
	}

	//********************************************************************************

	public String getDefinition() {
		return definition;
	}

	//********************************************************************************

	public HashSet<String> getEcNumsForTransgenicCandidate(Organism organism) {
		return transgenicCandidate2EcNums.get(organism);
	}

	//********************************************************************************

	public ReactionColor getReactionColor() {
		return color;
	}

	//********************************************************************************

	public Organism getSelectedTransgenicCandidate() {
		return selectedTransgenicCandidate;
	}

	//********************************************************************************

	public ArrayList<Organism> getTransgenicCandidates() {
		return transgenicCandidates;
	}

	//********************************************************************************

	public boolean isNative() {
		return isNative;
	}

	//********************************************************************************

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	//********************************************************************************

	public void setColor(ReactionColor color) {
		this.color = color;
	}

	//********************************************************************************

	public void setSelectedTransgenicCandidate(String name) {
		selectedTransgenicCandidate = name2TransgenicCandidate.get(name);
	}

	//********************************************************************************

	public void sortTransgenicCandidates() {
		if(transgenicCandidates != null) 
			Collections.sort(transgenicCandidates, new Organism.OrganismComparator());
	}

	//********************************************************************************


	public HashSet<Reactant> getReactants() {
		return reactants;
	}

	//********************************************************************************

	public HashSet<Product> getProducts() {
		return products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((ecNums == null) ? 0 : ecNums.hashCode());
		result = prime * result
		+ ((geneRefs == null) ? 0 : geneRefs.hashCode());
		result = prime * result
		+ ((products == null) ? 0 : products.hashCode());
		result = prime * result
		+ ((reactants == null) ? 0 : reactants.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reaction other = (Reaction) obj;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (ecNums == null) {
			if (other.ecNums != null)
				return false;
		} else if (!ecNums.equals(other.ecNums))
			return false;
		if (geneRefs == null) {
			if (other.geneRefs != null)
				return false;
		} else if (!geneRefs.equals(other.geneRefs))
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		if (reactants == null) {
			if (other.reactants != null)
				return false;
		} else if (!reactants.equals(other.reactants))
			return false;
		return true;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getNoOptionsString() {
		return "Native";
	}

	@Override
	public List<String> getOptions() {
		ArrayList<String> options = new ArrayList<String>();
		for(Organism organism : transgenicCandidates)
			options.add(organism.getName());
		return options;
	}

	@Override
	public boolean hasOptions() {
		return (transgenicCandidates != null && !transgenicCandidates.isEmpty());
	}

	//********************************************************************************
}