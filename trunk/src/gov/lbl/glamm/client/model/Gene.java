package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasSynonyms;
import gov.lbl.glamm.client.model.interfaces.Mappable;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.client.model.util.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Representation of a Gene
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Gene
implements Serializable, HasSynonyms, HasMeasurements, Mappable {

	public static transient final Type TYPE = new Type();
	public static transient final String SYNONYM_TYPE_NAME		= "0";
	public static transient final String SYNONYM_TYPE_NCBI		= "1";
	public static transient final String SYNONYM_TYPE_SESSION	= "S";
	public static transient final String SYNONYM_TYPE_VIMSS		= "VIMSS";

	private Set<String> ecNums;
	private Set<String> molTaxonomyIds;
	private Set<String> metaMolTaxonomyIds;
	private Set<Measurement> measurements;
	private Set<Synonym> synonyms;
	
	public static final transient ProvidesKey<Gene> KEY_PROVIDER = new ProvidesKey<Gene>() {
		public Object getKey(Gene item) {
			return item == null ? null : item.hashCode();
		}
	};

	public Gene() {
		ecNums = new HashSet<String>();
		molTaxonomyIds = new HashSet<String>();
		metaMolTaxonomyIds = new HashSet<String>();
		measurements = new HashSet<Measurement>();
		synonyms = new HashSet<Synonym>();
	}

	//********************************************************************************

	/**
	 * @return The set of Enzyme Commission (EC) numbers associated with this Gene
	 */
	public Set<String> getEcNums() {
		return ecNums;
	}

	//********************************************************************************

	/**
	 * Adds an EC number to the set of EC numbers associated with this Gene
	 * @param ecNum The EC number
	 */
	public void addEcNum(String ecNum) {
		ecNums.add(ecNum);
	}	

	//********************************************************************************
	
	public Set<String> getMolTaxonomyIds() {
		return molTaxonomyIds;
	}
	
	public void addMolTaxonomyId(String taxonomyId) {
		molTaxonomyIds.add(taxonomyId);
	}
	
	public Set<String> getMetaMolTaxonomyIds() {
		return metaMolTaxonomyIds;
	}
	
	public void addMetaMolTaxonomyId(String taxonomyId) {
		metaMolTaxonomyIds.add(taxonomyId);
	}
	
	@Override
	public void addSynonym(final Synonym synonym) {
		this.synonyms.add(synonym);
	}

	@Override
	public Set<Synonym> getSynonyms() {
		return synonyms;
	}
	
	public String getSynonymWithType(final String type) {
		for(Synonym synonym : getSynonyms()) {
			if(synonym.getType().equals(type))
				return synonym.getName();
		}
		return null;
	}
	
	public String getVimssId() {
		for(Synonym synonym : getSynonyms()) {
			if(synonym.getType().equals(SYNONYM_TYPE_VIMSS))
				return synonym.getName();
		}
		return null;
	}

	@Override
	public void setSynonyms(Set<Synonym> synonyms) {
		if(this.synonyms != synonyms)
			this.synonyms.clear();
		if(synonyms != null && !synonyms.isEmpty())
			this.synonyms.addAll(synonyms);
	}

	@Override
	public void addMeasurement(Measurement measurement) {
		measurements.add(measurement);
	}

	@Override
	public Set<Measurement> getMeasurements() {
		return measurements;
	}

	@Override
	public void setMeasurements(Set<Measurement> measurements) {
		if(this.measurements != measurements)
			measurements.clear();
		if(measurements != null && !measurements.isEmpty())
			this.measurements.addAll(measurements);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ecNums == null) ? 0 : ecNums.hashCode());
		result = prime * result
				+ ((measurements == null) ? 0 : measurements.hashCode());
		result = prime
				* result
				+ ((metaMolTaxonomyIds == null) ? 0 : metaMolTaxonomyIds
						.hashCode());
		result = prime * result
				+ ((molTaxonomyIds == null) ? 0 : molTaxonomyIds.hashCode());
		result = prime * result
				+ ((synonyms == null) ? 0 : synonyms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gene other = (Gene) obj;
		if (ecNums == null) {
			if (other.ecNums != null)
				return false;
		} else if (!ecNums.equals(other.ecNums))
			return false;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
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
		if (synonyms == null) {
			if (other.synonyms != null)
				return false;
		} else if (!synonyms.equals(other.synonyms))
			return false;
		return true;
	}

}