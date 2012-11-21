package gov.lbl.glamm.shared.model;

import gov.lbl.glamm.shared.model.interfaces.HasMeasurements;
import gov.lbl.glamm.shared.model.interfaces.HasType;
import gov.lbl.glamm.shared.model.util.MeasurementSet;
import gov.lbl.glamm.shared.model.util.Type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Model class for a generic Metabolic Model. This will primarily (at first) be used as a container
 * for information about FBA models used with the KBase, but may eventually grow into use for generic modeling
 * (whether FBA-related, kinetic, experimental flux, and so on).
 * 
 * For now, most of its functionality deals with common FBA model tropes, such as reaction and metabolite compartments,
 * upper and lower bounds, and biomass reactions.
 * @author wjriehl
 *
 */
@SuppressWarnings("serial")
public class MetabolicModel implements Serializable, HasType {

	/**
	 *  Metabolic models contain...
	 *    a set of ModelReactions
	 *    a set of compounds
	 *    a set of genes (maybe)
	 *    locations of reactions
	 *    reaction bounds
	 *    a biomass reaction
	 *    media conditions
	 *    a set of fluxes
	 *   
	 */
	
	public static transient final Type TYPE = new Type();
	
	public enum Compartment {
		CYTOSOL("cytosol", "c"),
		EXTRACELLULAR("extracellular", "e"),
		PERIPLASM("periplasm", "p"),
		NUCLEUS("nucleus", "n"),
		GOLGI("golgi", "g"),
		ENDOPLASMIC_RETICULUM("endoplasmic reticulum", "r"),
		MITOCHONDRIA("mitochondria", "m"),
		PEROXISOME("peroxisome", "x"),
		VACUOLE("vacuole", "v");

		private static Map<String, Compartment> name2Compartment = new HashMap<String, Compartment>();
		private static Map<String, Compartment> abbrev2Compartment = new HashMap<String, Compartment>();
		
		static {
			for (Compartment compartment : Compartment.values()) {
				name2Compartment.put(compartment.name, compartment);
				abbrev2Compartment.put(compartment.abbreviation, compartment);
			}
		}
		
		private String name;
		private String abbreviation;

		private Compartment(final String name, final String abbreviation) {
			this.name = name;
			this.abbreviation = abbreviation;
		}
		
		public String getName() {
			return name;
		}
		
		public String getAbbreviation() {
			return abbreviation;
		}
		
		public Compartment compartmentForName(String name) {
			return name2Compartment.get(name);
		}
		
		public Compartment compartmentForAbbreviation(String abbrev) {
			return abbrev2Compartment.get(abbrev);
		}
	}
	
	public enum ReactionType {
		NORMAL,
		EXCHANGE,
		TRANSPORT
	}
	
	// this should probably be in some kind of configuration file, or from the DB as an Xref or something.
	public enum ModelSource {
		KBASE("KBase"),
		SEED("ModelSEED"),
		GLAMM("GLAMM"),
		SESSION("Session");		// user uploaded
		
		String source;
		private ModelSource(String source) {
			this.source = source;
		}
	}
	
	/**
	 * Wraps a Reaction up with extra data specific for metabolic modeling.
	 * @author wjriehl
	 *
	 */
	public static class MMReaction implements HasMeasurements, Serializable {
		
		private Reaction reaction;
		private float upperBound;
		private float lowerBound;
		private Compartment compartment;
		private ReactionType type;
		private MeasurementSet measurementSet;
		
		@SuppressWarnings("unused")
		private MMReaction() { }
		
		public MMReaction(Reaction reaction) {
			this.reaction = reaction;
			upperBound = MAX_UPPER_BOUND;
			lowerBound = MAX_LOWER_BOUND;
			compartment = Compartment.CYTOSOL;
			type = ReactionType.NORMAL;
			measurementSet = new MeasurementSet();
		}
		
		public MMReaction(Reaction reaction, float upperBound, float lowerBound, Compartment compartment, ReactionType type) {
			this.reaction = reaction;
			this.upperBound = upperBound;
			this.lowerBound = lowerBound;
			this.compartment = compartment;
			this.type = type;
		}
		
		public Reaction getReaction() {
			return reaction;
		}
		
		public void setReaction(Reaction reaction) {
			this.reaction = reaction;
		}
		
		public float getUpperBound() { 
			return upperBound; 
		}
		
		public float getLowerBound() {
			return lowerBound;
		}
		
		public Compartment getCompartment() {
			return compartment;
		}
		
		public ReactionType getType() {
			return type;
		}
		
		public void setUpperBound(float ub) {
			upperBound = ub;
		}
		
		public void setLowerBound(float lb) {
			lowerBound = lb;
		}
		
		public void setCompartment(Compartment compartment) {
			this.compartment = compartment;
		}
		
		public void setType(ReactionType type) {
			this.type = type;
		}
		
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result
			+ (reaction == null ? 0 : reaction.hashCode());
			result = prime * result
			+ (compartment == null ? 0 : compartment.hashCode());
			result = prime * result
			+ (type == null ? 0 : type.hashCode());

			return result;
		}

		@Override
		public MeasurementSet getMeasurementSet() {
			return measurementSet;
		}
	}
	
	/**
	 * Wraps a Compound up with Metabolic Modeling-specific data (mainly compartment, so far).
	 * @author wjriehl
	 *
	 */
	public static class MMCompound implements HasMeasurements, Serializable {
		private Compound cpd;
		private MeasurementSet measurementSet;
		private Compartment compartment;

		@SuppressWarnings("unused")
		private MMCompound() { }
		
		public MMCompound(Compound cpd, Compartment compartment) {
			this.cpd = cpd;
			this.compartment = compartment;
			measurementSet = new MeasurementSet();
		}
		
		public Compound getCompound() {
			return cpd;
		}
		
		public void setCompound(Compound cpd) {
			this.cpd = cpd;
		}
		
		public Compartment getCompartment() {
			return compartment;
		}
		
		public void setCompartment(Compartment compartment) {
			this.compartment = compartment;
		}

		@Override
		public MeasurementSet getMeasurementSet() {
			return measurementSet;
		}
	}
	
	private static float MAX_UPPER_BOUND = Float.POSITIVE_INFINITY;
	private static float MAX_LOWER_BOUND = Float.NEGATIVE_INFINITY;

	private Map<Reaction, MMReaction> rxn2MMRxn;
	private Map<String, Reaction> id2Reaction;
	private Map<String, Compound> id2Compound;
	private Map<Compound, MMCompound> cpd2MMCpd;
	private Reaction biomass;
	private String modelId = "";
	private String name = "";

	// biomass is a common objective, but it could be a linear combination of several reactions
	// So add something to represent that. A Map of reaction-float?
	@SuppressWarnings("unused")
	private Map<Reaction, Float> objective;
	
	@SuppressWarnings("unused")
	private MetabolicModel() { }
	
	/**
	 * Constructor. Initializes an empty MetabolicModel (no reactions or compounds) with the given model ID
	 * @param modelId the model ID string to be used for this model.
	 */
	public MetabolicModel(String modelId, String name) {
		rxn2MMRxn = new HashMap<Reaction, MMReaction>();
		id2Reaction = new HashMap<String, Reaction>();
		cpd2MMCpd = new HashMap<Compound, MMCompound>();
		id2Compound = new HashMap<String, Compound>();
		biomass = null;
		this.modelId = modelId;
		this.name = name;
	}
	
	/**
	 * Adds a set of Reactions to this model. This also adds all of the Compounds involved in those Reactions.
	 * @param reactions the Set of Reactions to add to the MetabolicModel
	 */
	public void addReactions(Set<Reaction> reactions) {
		for (Reaction r : reactions) {
			addReaction(r);
		}
	}
	
	/**
	 * Adds a single Reaction to this MetabolicModel. Also adds all Compounds associated with the Reaction, if they are not already present.
	 * @param reaction the Reaction to add to the MetabolicModel.
	 */
	public void addReaction(Reaction reaction) {
		rxn2MMRxn.put(reaction, new MMReaction(reaction));
		addCompoundsForReaction(reaction);
	}
	
	/**
	 * Adds all compounds involved with the given Reaction to the model, but NOT the reaction itself.
	 * @param reaction the Reaction whose Compounds should be in the Metabolic model.
	 */
	public void addCompoundsForReaction(Reaction reaction) {
		for (Reaction.Participant p : reaction.getSubstrates()) {
			addCompound(p.getCompound());
		}
		for (Reaction.Participant p : reaction.getProducts()) {
			addCompound(p.getCompound());
		}		
	}
	
	/**
	 * Adds a single Compound to the model. Without a specified compartment, this defaults to placing the Compound in the cytosol.
	 * @param compound the Compound to add to the model.
	 */
	public void addCompound(Compound compound) {
		addCompound(compound, Compartment.CYTOSOL);
	}
	
	/**
	 * Adds a single Compound to the model in the given Compartment.
	 * @param compound the Compound to add to the model
	 * @param compartment the Compartment in which to place this Compound.
	 */
	public void addCompound(Compound compound, Compartment compartment) {
		cpd2MMCpd.put(compound, new MMCompound(compound, compartment));
		id2Compound.put(compound.getGuid(), compound);
	}
	
	/**
	 * Adds a user-made MMReaction to the model.
	 * @param mmreaction the MMReaction to add to the model.
	 */
	public void addReaction(MMReaction mmreaction) {
		Reaction r = mmreaction.getReaction();
		id2Reaction.put(r.getGuid(), r);
		rxn2MMRxn.put(r, mmreaction);
		addCompoundsForReaction(r);
	}

	/**
	 * Gets the MMReaction from the model for the given Reaction. If the Reaction is not in the model, it returns null.
	 * @param reaction the Reaction 
	 * @return a MMReaction or null
	 */
	public MMReaction getMMReaction(Reaction reaction) {
		return rxn2MMRxn.get(reaction);
	}
	
	/**
	 * Gets the Reaction with the given ID from the model, if it exists.
	 * @param rxnId the reaction ID string
	 * @return the Reaction with that id or null if it is not in the model.
	 */
	public Reaction getReaction(String rxnId) {
		return id2Reaction.get(rxnId);
	}
	
	/**
	 * Gets the MMReaction with the given ID from the model, if it exists.
	 * @param rxnId the reaction ID string
	 * @return the MMReaction with that id or null if it is not in the model.
	 */
	public MMReaction getMMReaction(String rxnId) {
		return rxn2MMRxn.get(id2Reaction.get(rxnId));
	}
	
	/**
	 * Gets the Compound with the given id from the model if it exists.
	 * @param cpdId the given Compound ID
	 * @return the Compound with the given Compound ID or null
	 */
	public Compound getCompound(String cpdId) {
		return id2Compound.get(cpdId);
	}
	
	/**
	 * Gets the set of Reactions associated with the model.
	 * @return the set of Reactions in this model.
	 */
	public Set<Reaction> getReactions() {
		return rxn2MMRxn.keySet();
	}
	
	/**
	 * Gets the set of Compounds associated with the model.
	 * @return the set of Compounds in the model.
	 */
	public Set<Compound> getCompounds() {
		return cpd2MMCpd.keySet();
	}
	
	/**
	 * Sets the biomass-generating (or overall optimization target for FBA) for the model.
	 * Also adds the biomass reaction if it is not already present.
	 * @param biomass
	 */
	public void setBiomass(Reaction biomass) {
		this.biomass = biomass;
		addReaction(biomass);
	}
	
	/**
	 * Sets the biomass-generating (or overall optimization target for FBA) for the model.
	 * Also adds the biomass reaction if it is not already present.
	 * @param biomass
	 */
	public Reaction getBiomass() {
		return biomass;
	}
	
	/**
	 * Generates a hashcode for the MetabolicModel.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ (modelId == null ? 0 : modelId.hashCode());
		result = prime * result
		+ (rxn2MMRxn == null ? 0 : rxn2MMRxn.hashCode());
		result = prime * result
		+ (cpd2MMCpd == null ? 0 : cpd2MMCpd.hashCode());
		result = prime * result
		+ (biomass == null ? 0 : biomass.hashCode());

		return result;
	}
	
	/**
	 * Tests equality of a MetabolicModel. Returns true if all Compounds and Reactions are shared between the compared model.
	 * @param obj the MetabolicModel to compare.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		MetabolicModel other = (MetabolicModel)obj;
		
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		if (biomass == null) {
			if (other.biomass != null)
				return false;
		} else if (!biomass.equals(other.biomass)) 
			return false;
		if (cpd2MMCpd == null) {
			if (other.cpd2MMCpd != null)
				return false;
		} else if (!cpd2MMCpd.equals(other.cpd2MMCpd))
			return false;
		if (rxn2MMRxn == null) {
			if (other.rxn2MMRxn != null)
				return false;
		} else if (!rxn2MMRxn.equals(other.rxn2MMRxn))
			return false;
		
		return true;
	}

	/**
	 * Returns the Type of this model
	 * @return the Type of this object.
	 */
	@Override
	public Type getType() {
		return TYPE;
	}

	/**
	 * Gets the ID string for this MetabolicModel
	 * @return the modelId String
	 */
	public String getModelId() {
		return modelId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
