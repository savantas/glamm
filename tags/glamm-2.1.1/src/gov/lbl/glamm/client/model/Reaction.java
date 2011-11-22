package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasType;
import gov.lbl.glamm.client.model.interfaces.HasXrefs;
import gov.lbl.glamm.client.model.util.Type;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.client.model.util.XrefSet;
import gov.lbl.glamm.client.util.ReactionColor;
import gov.lbl.glamm.client.util.RowDependentSelectionCell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Reaction
implements Serializable, RowDependentSelectionCell.HasOptions, HasMeasurements, HasType, HasXrefs {
	
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

	public static class Participant implements Serializable {
		
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
		
		public Participant(final Compound compound, final String coefficient, final KeggRpairRole role) {
			this.compound = compound;
			this.coefficient = coefficient;
			setRole(role);
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
		
		public final void setRole(final KeggRpairRole role) {
			this.role = role;
		}
		
		public final void setRole(final String role) {
			setRole(KeggRpairRole.fromString(role));
		}
		
	}
	//********************************************************************************

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
	private XrefSet xrefs;

	public static final transient ProvidesKey<Reaction> KEY_PROVIDER = new ProvidesKey<Reaction>() {
		public Object getKey(Reaction item) {
			return item == null ? null : new Integer(item.hashCode());
		}
	};

	//********************************************************************************

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
		xrefs = new XrefSet();
	}

	//********************************************************************************

	public void addEcNum(String ecNum) {
		if(ecNum != null && !ecNum.isEmpty()) {
			ecNums.add(ecNum);
		}
	}

	public void addGene(final Gene gene) {
		if(gene != null)
			genes.add(gene);
	}
	
	//********************************************************************************

	public void addProduct(final Participant rp) {
		if(rp == null)
			return;
		products.add(rp);
	}

	//********************************************************************************

	public void addSubstrate(final Participant rp) {
		if(rp == null)
			return;
		substrates.add(rp);
	}

	//********************************************************************************

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
	
	public Set<Gene> getGenes() {
		return genes;
	}

	//********************************************************************************

	public String getGuid() {
		return guid;
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

	public Set<Participant> getSubstrates() {
		return substrates;
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

	public void setGuid(final String guid) {
		if(guid == null)
			throw new IllegalArgumentException("Attempting to set null guid");
		this.guid = guid;
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

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void addXref(Xref xref) {
		xrefs.addXref(xref);
	}

	@Override
	public Set<Xref> getXrefs() {
		return xrefs.getXrefs();
	}

	@Override
	public Xref getXrefForDbName(String dbName) {
		return xrefs.getXrefForDbName(dbName);
	}

	@Override
	public Xref getXrefForDbNames(Collection<String> dbNames) {
		return xrefs.getXrefForDbNames(dbNames);
	}

	@Override
	public void addMeasurement(Measurement measurement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Measurement> getMeasurements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMeasurements(Set<Measurement> measurements) {
		// TODO Auto-generated method stub
		
	}

	//********************************************************************************
}