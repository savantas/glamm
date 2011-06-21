package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Representation of a Gene
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Gene extends GlammPrimitive implements Serializable {

	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	public static transient final String SYNONYM_TYPE_NAME		= "0";
	public static transient final String SYNONYM_TYPE_NCBI		= "1";
	public static transient final String SYNONYM_TYPE_SESSION	= "S";
	public static transient final String SYNONYM_TYPE_VIMSS		= "VIMSS";

	private String			vimmsId			= null;	
	private HashSet<String> ecNums 			= null;
	private HashSet<String> molTaxonomyIds 	= null;
	private HashSet<String> metaMolTaxonomyIds = null;
	
	private ArrayList<GlammPrimitive.Reference> regulators = null;

	public Gene() {}

	//********************************************************************************

	/**
	 * @return The set of Enzyme Commission (EC) numbers associated with this Gene
	 */
	public HashSet<String> getEcNums() {
		return ecNums;
	}

	//********************************************************************************

	/**
	 * Adds an EC number to the set of EC numbers associated with this Gene
	 * @param ecNum The EC number
	 */
	public void addEcNum(String ecNum) {
		if(ecNums == null)
			ecNums = new HashSet<String>();
		ecNums.add(ecNum);
	}	

	//********************************************************************************
	
	public HashSet<String> getMolTaxonomyIds() {
		return molTaxonomyIds;
	}
	
	public void addMolTaxonomyId(String taxonomyId) {
		if(molTaxonomyIds == null)
			molTaxonomyIds = new HashSet<String>();
		molTaxonomyIds.add(taxonomyId);
	}
	
	public HashSet<String> getMetaMolTaxonomyIds() {
		return metaMolTaxonomyIds;
	}
	
	public void addMetaMolTaxonomyId(String taxonomyId) {
		if(metaMolTaxonomyIds == null)
			metaMolTaxonomyIds = new HashSet<String>();
		metaMolTaxonomyIds.add(taxonomyId);
	}

	/**
	 * @return The GlammPrimitive Type
	 */
	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ecNums == null) ? 0 : ecNums.hashCode());
		result = prime
				* result
				+ ((metaMolTaxonomyIds == null) ? 0 : metaMolTaxonomyIds
						.hashCode());
		result = prime * result
				+ ((molTaxonomyIds == null) ? 0 : molTaxonomyIds.hashCode());
		result = prime * result
				+ ((regulators == null) ? 0 : regulators.hashCode());
		result = prime * result + ((vimmsId == null) ? 0 : vimmsId.hashCode());
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
		Gene other = (Gene) obj;
		if (ecNums == null) {
			if (other.ecNums != null)
				return false;
		} else if (!ecNums.equals(other.ecNums))
			return false;
		if (metaMolTaxonomyIds == null) {
			if (other.metaMolTaxonomyIds != null)
				return false;
		} else if (!metaMolTaxonomyIds.equals(other.metaMolTaxonomyIds))
			return false;
		if (molTaxonomyIds == null) {
			if (other.molTaxonomyIds != null)
				return false;
		} else if (!molTaxonomyIds.equals(other.molTaxonomyIds))
			return false;
		if (regulators == null) {
			if (other.regulators != null)
				return false;
		} else if (!regulators.equals(other.regulators))
			return false;
		if (vimmsId == null) {
			if (other.vimmsId != null)
				return false;
		} else if (!vimmsId.equals(other.vimmsId))
			return false;
		return true;
	}

}