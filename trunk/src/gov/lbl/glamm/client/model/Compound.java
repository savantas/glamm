package gov.lbl.glamm.client.model;


import java.io.Serializable;

/**
 * Representation of a Compound
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Compound extends GlammPrimitive implements Serializable {
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	private String name		= null;
	private String formula	= null;
	private String mass		= null;
	private String inchi	= null;
	private String smiles	= null;
	
	//********************************************************************************
	
	public Compound() {}

	/**
	 * Accessor
	 * @return The compound's common name.  Considering treating the common name as a synonym and deprecating in future releases
	 */
	public String getName() {
		return name;
	}
	
	//********************************************************************************

	/**
	 * Mutator
	 * @param The compound's common name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	//********************************************************************************

	/**
	 * Accessor
	 * @return The compound's formula - KEGG formulas currently get precedence over formulas from other data sources
	 */
	public String getFormula() {
		return formula;
	}

	//********************************************************************************

	/**
	 * Mutator
	 * @param The compound's formula.
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	//********************************************************************************

	/**
	 * Accessor - TODO: Treat mass as a floating point value
	 * @return The compound's mass - KEGG masses currently get precedence over massess from other data sources
	 */
	public String getMass() {
		return mass;
	}

	//********************************************************************************

	/**
	 * Mutator - TODO: Treat mass as a floating point value
	 * @param mass The compound's mass
	 */
	public void setMass(String mass) {
		this.mass = mass;
	}

	//********************************************************************************

	/**
	 * Accessor
	 * @return The compound's InChi identifier
	 */
	public String getInchi() {
		return inchi;
	}
	
	/**
	 * @return The GlammPrimitive Type
	 */
	@Override
	public Type getType() {
		return TYPE;
	}
	
	//********************************************************************************

	/**
	 * Mutator
	 * @param The compound's InChi identifier
	 */
	public void setInchi(String inchi) {
		this.inchi = inchi;
	}
	
	
	//********************************************************************************

	/**
	 * Accessor
	 * @return The compound's SMILES string
	 */
	public String getSmiles() {
		return smiles;
	}
	
	//********************************************************************************

	/**
	 * Mutator
	 * @param The compound's SMILES string
	 */
	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}
	
	//********************************************************************************
	
}