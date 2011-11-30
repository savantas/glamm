package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasSynonyms;
import gov.lbl.glamm.client.model.interfaces.HasType;
import gov.lbl.glamm.client.model.interfaces.HasXrefs;
import gov.lbl.glamm.client.model.util.MeasurementSet;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.client.model.util.Type;
import gov.lbl.glamm.client.model.util.XrefSet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of a Compound
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Compound
implements HasMeasurements, HasSynonyms, Serializable, HasXrefs, HasType {
	
	private String guid;
	private String name;
	private String formula;
	private String mass;
	private String inchi;
	private String smiles;
	private Set<Synonym> synonyms;
	private MeasurementSet measurementSet;
	private XrefSet xrefSet;
	
	/**
	 * The Type associated with this object.
	 */
	public static transient final Type TYPE = new Type();
	
	
	/**
	 * Constructor
	 */
	public Compound() {
		this.synonyms = new HashSet<Synonym>();
		this.measurementSet = new MeasurementSet();
		this.xrefSet = new XrefSet();
	}

	/**
	 * Gets the common name.
	 * @return The compound's common name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the compound's common name
	 * @param name The common name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the compound's formula.  KEGG formulas currently take precedence over formulas from other data sources.
	 * @return The formula.
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * Sets the compound's formula
	 * @param formula The formula.
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * Gets the mass of the compound.  KEGG masses take precedence over masses from other data sources.
	 * @return The mass - should probably be float.
	 */
	public String getMass() {
		return mass;
	}

	/**
	 * Sets the mass of the compound.
	 * @param The mass - should probably be float.
	 */
	public void setMass(String mass) {
		this.mass = mass;
	}

	/**
	 * Gets the InChi identifier for the compound.
	 * @return The InChi id.
	 */
	public String getInchi() {
		return inchi;
	}
	
	/**
	 * Sets the InChi identifier for the compound.
	 * @param inchi The InChi id.
	 */
	public void setInchi(String inchi) {
		this.inchi = inchi;
	}
	
	/**
	 * Gets the SMILES string.
	 * @return The SMILES string.
	 */
	public String getSmiles() {
		return smiles;
	}
	
	/**
	 * Sets the SMILES string.
	 * @param The SMILES string.
	 */
	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}
	
	/**
	 * Gets the GLAMM GUID (A unique identifier for the GLAMM database - soon to be deprecated.)
	 * @return The GUID.
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Sets the GLAMM GUID.
	 * @param guid The GUID.
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public void addSynonym(final Synonym synonym) {
		synonyms.add(synonym);
	}

	@Override
	public Set<Synonym> getSynonyms() {
		return synonyms;
	}

	@Override
	public void setSynonyms(Set<Synonym> synonyms) {
		if(this.synonyms != synonyms)
			this.synonyms.clear();
		if(synonyms != null && !synonyms.isEmpty())
			this.synonyms.addAll(synonyms);
	}

	@Override
	public MeasurementSet getMeasurementSet() {
		return measurementSet;
	}
	
	@Override
	public Type getType() {
		return TYPE;
	}
	
	@Override
	public XrefSet getXrefSet() {
		return xrefSet;
	}
	

	
}