package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasType;
import gov.lbl.glamm.client.model.interfaces.HasXrefs;
import gov.lbl.glamm.client.model.util.MeasurementSet;
import gov.lbl.glamm.client.model.util.Type;
import gov.lbl.glamm.client.model.util.XrefSet;
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

/**
 * Model class for reactions.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Reaction
implements Serializable, RowDependentSelectionCell.HasOptions, HasMeasurements, HasType, HasXrefs {
	
	/**
	 * Enum specifying reaction direction.
	 * @author jtbates
	 *
	 */
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

	/**
	 * Internal class for reaction participants.
	 * @author jtbates
	 *
	 */
	public static class Participant implements Serializable {
		
		/**
		 * Enum for KEGG Rpair role.
		 * Currently, only "main" and "other" roles apply.  "main" compounds are all those
		 * involved in the movement of bulk carbon - those specified by the KEGG rpair roles "main" and "trans".
		 * "other" compounds are all others.
		 * @author jtbates
		 *
		 */
		public enum KeggRpairRole {
			MAIN("main"),
			OTHER("other");
			
			private String theString = null;
			
			public static KeggRpairRole fromString(final String theString) {
				if(theString == null)
					return OTHER;
				else if(theString.equals("main") || theString.equals("trans"))
					return MAIN;
				return OTHER;
			}
			
			private KeggRpairRole(final String theString) {
				this.theString = theString;
			}
			
			@Override
			public String toString() {
				return theString;
			}
		}
		
		private String coefficient;
		private Compound compound;
		private KeggRpairRole role;
		
		@SuppressWarnings("unused")
		private Participant() {}
		
		/**
		 * Constructor
		 * @param compound The compound.
		 * @param coefficient The stoichiometric coefficient.  Not necessarily numerical (e.g. n, n+1)
		 * @param role The rpair role.
		 */
		public Participant(final Compound compound, final String coefficient, final KeggRpairRole role) {
			this.compound = compound;
			this.coefficient = coefficient;
			setRole(role);
		}

		/**
		 * Gets the stoichiometric coefficient.
		 * @return The coefficient.
		 */
		public final String getCoefficient() {
			return coefficient;
		}

		/**
		 * Gets the compound.
		 * @return The compound.
		 */
		public final Compound getCompound() {
			return compound;
		}

		/**
		 * Gets the KEGG rpair role.
		 * @return The role.
		 */
		public final KeggRpairRole getRole() {
			return role;
		}
		
		/**
		 * Sets the KEGG rpair role.
		 * @param role The role as KeggRpairRole enum.
		 */
		public final void setRole(final KeggRpairRole role) {
			this.role = role;
		}
		
		/**
		 * Sets the KEGG rpair role.
		 * @param role The role as string.
		 */
		public final void setRole(final String role) {
			setRole(KeggRpairRole.fromString(role));
		}
		
	}

	public static transient final Type TYPE = new Type();
	private String guid;
	private Set<String> 	ecNums;
	private String definition;
	private Direction direction;
	private Set<Gene> genes;
	private boolean isNative;
	private Set<Participant>	products;
	private Set<Participant> 	substrates;
	private transient Organism selectedTransgenicCandidate;
	private List<Organism> transgenicCandidates;
	private Map<String, Organism> name2TransgenicCandidate;
	private Map<Organism, Set<String>> transgenicCandidate2EcNums;
	private transient ReactionColor color; // ReactionColor depends on resources from the client bundle - don't instantiate or try to set server-side.
	private MeasurementSet measurementSet;
	private XrefSet xrefSet;

	/**
	 * Key provider for Reactions.
	 */
	public static final transient ProvidesKey<Reaction> KEY_PROVIDER = new ProvidesKey<Reaction>() {
		public Object getKey(Reaction item) {
			return item == null ? null : new Integer(item.hashCode());
		}
	};

	/**
	 * Constructor
	 */
	public Reaction() {
		direction = Direction.BOTH;
		isNative = true;
		ecNums = new HashSet<String>();
		genes = new HashSet<Gene>();
		products = new HashSet<Participant>();
		substrates = new HashSet<Participant>();
		transgenicCandidates = new ArrayList<Organism>();
		name2TransgenicCandidate = new HashMap<String, Organism>();
		transgenicCandidate2EcNums = new HashMap<Organism, Set<String>>();
		measurementSet = new MeasurementSet();
		xrefSet = new XrefSet();
	}

	/**
	 * Adds an EC (Enzyme Commission) number to this reaction.
	 * @param ecNum The EC number.
	 */
	public void addEcNum(String ecNum) {
		if(ecNum != null && !ecNum.isEmpty()) {
			ecNums.add(ecNum);
		}
	}

	/**
	 * Adds a gene to this reaction.
	 * @param gene The gene.
	 */
	public void addGene(final Gene gene) {
		if(gene != null)
			genes.add(gene);
	}
	
	/**
	 * Adds a product to this reaction - products are those reaction participants that appear on the right
	 * side of the reaction equation as stored in the GLAMM database.
	 * @param rp The product.
	 */
	public void addProduct(final Participant rp) {
		if(rp == null)
			return;
		products.add(rp);
	}

	/**
	 * Adds a substrate to this reaction - substrates are those reaction participants that appear on the left
	 * side of the reaction equation as stored in the GLAMM database.
	 * @param rp The substrate.
	 */
	public void addSubstrate(final Participant rp) {
		if(rp == null)
			return;
		substrates.add(rp);
	}

	/**
	 * Adds a candidate organism for a given EC number to this non-native reaction.
	 * @param ecNum The EC number
	 * @param organism The organism.
	 */
	public void addTransgenicCandidate(final String ecNum, final Organism organism) {
		if(organism == null)
			return;
		// add to list
		if(!transgenicCandidates.contains(organism))
			transgenicCandidates.add(organism);

		// add to map
		Set<String> ecNums = transgenicCandidate2EcNums.get(organism);
		if(ecNums == null) {
			ecNums = new HashSet<String>();
			transgenicCandidate2EcNums.put(organism, ecNums);
		}

		name2TransgenicCandidate.put(organism.getName(), organism);
		ecNums.add(ecNum);

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
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		if (substrates == null) {
			if (other.substrates != null)
				return false;
		} else if (!substrates.equals(other.substrates))
			return false;
		return true;
	}

	/**
	 * Gets the human-readable definition of the reaction.
	 * @return The definition.
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Gets the direction of the reaction.
	 * @return The direction.
	 */
	public final Direction getDirection() {
		return direction;
	}

	/**
	 * Gets the set of EC (enzyme commission) numbers for this reaction.
	 * @return The set of EC numbers.
	 */
	public Set<String> getEcNums() {
		return ecNums;
	}

	/**
	 * Gets the set of EC (enzyme commission) numbers for a transgenic candidate organism for this non-native reaction.
	 * @param organism
	 * @return The set of EC numbers.
	 */
	public Set<String> getEcNumsForTransgenicCandidate(Organism organism) {
		return transgenicCandidate2EcNums.get(organism);
	}
	
	/**
	 * Gets the set of genes associated with this reaction.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenes() {
		return genes;
	}

	/**
	 * Gets the GLAMM GUID associated with this reaction - soon to be deprecated.
	 * @return The GUID.
	 */
	public String getGuid() {
		return guid;
	}
	
	@Override
	public String getNoOptionsString() {
		if(ecNums == null || ecNums.isEmpty())
			return "No EC";
		if(!isNative && (transgenicCandidates == null || transgenicCandidates.isEmpty()))
			return "No candidates";
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
	public List<String> getOptionsPreamble() {
		if(!this.hasOptions())
			return null;
		List<String> optionsPreamble = new ArrayList<String>();
		optionsPreamble.add(Integer.toString(transgenicCandidates.size()) + " candidates");
		optionsPreamble.add("-");
		return optionsPreamble;
	}

	/**
	 * Gets the set of products.
	 * @return The set of products.
	 */
	public Set<Participant> getProducts() {
		return products;
	}

	/**
	 * Gets the set of substrates.
	 * @return The set of substrates.
	 */
	public Set<Participant> getSubstrates() {
		return substrates;
	}

	/**
	 * Gets the ReactionColor enum associated with this non-native reaction (used in Route steps.)
	 * @return The color.
	 */
	public ReactionColor getReactionColor() {
		return color;
	}

	/**
	 * Gets the selected transgenic candidate for this non-native reaction.
	 * @return The candidate.
	 */
	public Organism getSelectedTransgenicCandidate() {
		return selectedTransgenicCandidate;
	}

	/**
	 * Gets ths list of transgenic candidate organisms for this non-native reaction.
	 * @return The list or transgenic candidates.
	 */
	public List<Organism> getTransgenicCandidates() {
		return transgenicCandidates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((ecNums == null) ? 0 : ecNums.hashCode());
		result = prime * result
		+ ((products == null) ? 0 : products.hashCode());
		result = prime * result
		+ ((substrates == null) ? 0 : substrates.hashCode());
		return result;
	}
	
	@Override
	public boolean hasOptions() {
		return (transgenicCandidates != null && !transgenicCandidates.isEmpty());
	}

	/**
	 * Indicates whether or not the reaction is native.
	 * @return Flag indicating whether or not the reaction is native.
	 */
	public boolean isNative() {
		return isNative;
	}

	/**
	 * Sets the reaction color.
	 * @param color The color.
	 */
	public void setColor(ReactionColor color) {
		this.color = color;
	}

	/**
	 * Sets the reaction definition.
	 * @param definition The definition.
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * Sets the reaction direction.
	 * @param direction The direction.
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * The reaction GLAMM GUID.
	 * @param guid The guid.
	 */
	public void setGuid(final String guid) {
		if(guid == null)
			throw new IllegalArgumentException("Attempting to set null guid");
		this.guid = guid;
	}
	
	/**
	 * Sets the flag indicating whether or not the reaction is native.
	 * @param isNative The flag.
	 */
	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	/**
	 * Sets the selected transgenic candidate.
	 * @param name The name of the transgenic candidate organism.
	 */
	public void setSelectedTransgenicCandidate(String name) {
		selectedTransgenicCandidate = name2TransgenicCandidate.get(name);
	}

	/**
	 * Sorts the list of transgenic candidate organisms alphabetically.
	 */
	public void sortTransgenicCandidates() {
		if(transgenicCandidates != null) 
			Collections.sort(transgenicCandidates, new Organism.OrganismComparator());
	}

	@Override
	public Type getType() {
		return TYPE;
	}
	
	@Override
	public MeasurementSet getMeasurementSet() {
		return measurementSet;
	}

	@Override
	public XrefSet getXrefSet() {
		return xrefSet;
	}

}