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
	
	public static transient final Type TYPE = new Type();
	

	
	public Compound() {
		this.synonyms = new HashSet<Synonym>();
		this.measurementSet = new MeasurementSet();
		this.xrefSet = new XrefSet();
	}

	/**
	 * Accessor
	 * @return The compound's common name.  Considering treating the common name as a synonym and deprecating in future releases
	 */
	public String getName() {
		return name;
	}
	


	/**
	 * Mutator
	 * @param The compound's common name.
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * Accessor
	 * @return The compound's formula - KEGG formulas currently get precedence over formulas from other data sources
	 */
	public String getFormula() {
		return formula;
	}



	/**
	 * Mutator
	 * @param The compound's formula.
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}



	/**
	 * Accessor - TODO: Treat mass as a floating point value
	 * @return The compound's mass - KEGG masses currently get precedence over massess from other data sources
	 */
	public String getMass() {
		return mass;
	}



	/**
	 * Mutator - TODO: Treat mass as a floating point value
	 * @param mass The compound's mass
	 */
	public void setMass(String mass) {
		this.mass = mass;
	}



	/**
	 * Accessor
	 * @return The compound's InChi identifier
	 */
	public String getInchi() {
		return inchi;
	}
	


	/**
	 * Mutator
	 * @param The compound's InChi identifier
	 */
	public void setInchi(String inchi) {
		this.inchi = inchi;
	}
	
	


	/**
	 * Accessor
	 * @return The compound's SMILES string
	 */
	public String getSmiles() {
		return smiles;
	}
	


	/**
	 * Mutator
	 * @param The compound's SMILES string
	 */
	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}
	
	public String getGuid() {
		return guid;
	}

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