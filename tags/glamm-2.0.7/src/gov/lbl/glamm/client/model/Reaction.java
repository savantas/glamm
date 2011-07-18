package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.util.ReactionColor;
import gov.lbl.glamm.client.util.RowDependentSelectionCell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Reaction extends GlammPrimitive implements Serializable, RowDependentSelectionCell.HasOptions {
	
	public enum Direction {
		BOTH("both"),
		FORWARD("forward"),
		REVERSE("reverse"),
		UNSPECIFIED("unspecified");
		
		private String theString = null;
		
		private Direction(final String theString) {
			this.theString = theString;
		}
		
		@Override
		public String toString() {
			return theString;
		}
	}

	public static class Participant {
		
		public enum KeggRpairRole {
			MAIN("main"),
			OTHER("other");
			
			private String theString = null;
			
			private KeggRpairRole(final String theString) {
				this.theString = theString;
			}
			
			@Override
			public String toString() {
				return theString;
			}
		}
		
		private String coefficient = null;
		private Compound compound = null;
		private KeggRpairRole role = null;
		
		public Participant(final Compound compound, final String coefficient, final KeggRpairRole role) {
			this.compound = compound;
			this.coefficient = coefficient;
			this.role = role;
		}

		public final String getCoefficient() {
			return coefficient;
		}

		public final Compound getCompound() {
			return compound;
		}

		public final KeggRpairRole getRole() {
			return role;
		}
		
	}
	//********************************************************************************

	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	private Set<String> 	ecNums = null;
	private String definition = null;
	private Direction direction = Direction.BOTH;
	private boolean isNative = true;
	private Set<Participant>	products 	= null;
	private Set<Participant> 	reactants 	= null;
	private Set<GlammPrimitive.Reference> geneRefs = null;
	private transient Organism selectedTransgenicCandidate = null;
	private List<Organism> transgenicCandidates = null;
	private Map<String, Organism> name2TransgenicCandidate = null;
	private Map<Organism, Set<String>> transgenicCandidate2EcNums = null;
	private transient ReactionColor color; // ReactionColor depends on resources from the client bundle - don't instantiate or try to set server-side.

	public static final transient ProvidesKey<Reaction> KEY_PROVIDER = new ProvidesKey<Reaction>() {
		public Object getKey(Reaction item) {
			return item == null ? null : new Integer(item.hashCode());
		}
	};

	//********************************************************************************

	public Reaction() {}

	//********************************************************************************

	public void addEcNum(String ecNum) {
		if(ecNum != null && !ecNum.isEmpty()) {
			if(ecNums == null)
				ecNums = new HashSet<String>();
			ecNums.add(ecNum);
		}
	}

	//********************************************************************************

	public void addGeneReference(GlammPrimitive.Reference reference) {
		if(reference != null) {
			if(geneRefs == null) 
				geneRefs = new HashSet<GlammPrimitive.Reference>();
			geneRefs.add(reference);
		}
	}

	//********************************************************************************

	public void addProduct(Participant rp) {
		if(rp == null)
			return;
		if(products == null)
			products = new HashSet<Participant>();
		products.add(rp);
	}

	//********************************************************************************

	public void addReactant(Participant rp) {
		if(rp == null)
			return;
		if(reactants == null)
			reactants = new HashSet<Participant>();
		reactants.add(rp);
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
			transgenicCandidate2EcNums = new HashMap<Organism, Set<String>>();
		Set<String> ecNums = transgenicCandidate2EcNums.get(organism);
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

	//********************************************************************************

	public String getDefinition() {
		return definition;
	}

	//********************************************************************************

	public final Direction getDirection() {
		return direction;
	}

	//********************************************************************************

	public Set<String> getEcNums() {
		return ecNums;
	}

	//********************************************************************************

	public Set<String> getEcNumsForTransgenicCandidate(Organism organism) {
		return transgenicCandidate2EcNums.get(organism);
	}

	//********************************************************************************

	@Override
	public String getNoOptionsString() {
		if(ecNums == null || ecNums.isEmpty())
			return "No EC";
		if(!isNative && (transgenicCandidates == null || transgenicCandidates.isEmpty()))
			return "No candidates";
		return "Native";
	}

	//********************************************************************************

	@Override
	public List<String> getOptions() {
		ArrayList<String> options = new ArrayList<String>();
		for(Organism organism : transgenicCandidates)
			options.add(organism.getName());
		return options;
	}
	
	//********************************************************************************

	@Override
	public List<String> getOptionsPreamble() {
		if(!this.hasOptions())
			return null;
		List<String> optionsPreamble = new ArrayList<String>();
		optionsPreamble.add(Integer.toString(transgenicCandidates.size()) + " candidates");
		optionsPreamble.add("-");
		return optionsPreamble;
	}

	//********************************************************************************

	public Set<Participant> getProducts() {
		return products;
	}

	//********************************************************************************

	public Set<Participant> getReactants() {
		return reactants;
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

	public List<Organism> getTransgenicCandidates() {
		return transgenicCandidates;
	}

	//********************************************************************************


	@Override
	public Type getType() {
		return TYPE;
	}

	//********************************************************************************

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
	public boolean hasOptions() {
		return (transgenicCandidates != null && !transgenicCandidates.isEmpty());
	}

	public boolean isNative() {
		return isNative;
	}

	public void setColor(ReactionColor color) {
		this.color = color;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	public void setSelectedTransgenicCandidate(String name) {
		selectedTransgenicCandidate = name2TransgenicCandidate.get(name);
	}

	public void sortTransgenicCandidates() {
		if(transgenicCandidates != null) 
			Collections.sort(transgenicCandidates, new Organism.OrganismComparator());
	}

	//********************************************************************************
}