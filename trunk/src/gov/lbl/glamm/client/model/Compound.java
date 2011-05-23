package gov.lbl.glamm.client.model;


import java.io.Serializable;

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

	public String getName() {
		return name;
	}
	
	//********************************************************************************

	public void setName(String name) {
		this.name = name;
	}

	//********************************************************************************

	public String getFormula() {
		return formula;
	}

	//********************************************************************************

	public void setFormula(String formula) {
		this.formula = formula;
	}

	//********************************************************************************

	public String getMass() {
		return mass;
	}

	//********************************************************************************

	public void setMass(String mass) {
		this.mass = mass;
	}

	//********************************************************************************

	public String getInchi() {
		return inchi;
	}
	
	@Override
	public Type getType() {
		return TYPE;
	}
	
	//********************************************************************************

	public void setInchi(String inchi) {
		this.inchi = inchi;
	}
	
	
	//********************************************************************************

	public String getSmiles() {
		return smiles;
	}
	
	//********************************************************************************

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}
	
	//********************************************************************************
	
}