package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasSynonyms;
import gov.lbl.glamm.client.model.interfaces.Mappable;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.client.model.util.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

	private Set<String> ecNums 			= null;
	private Set<String> molTaxonomyIds 	= null;
	private Set<String> metaMolTaxonomyIds = null;
	private Set<Measurement> measurements;
	private Set<Synonym> synonyms;

	public Gene() {
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
		if(ecNums == null)
			ecNums = new HashSet<String>();
		ecNums.add(ecNum);
	}	

	//********************************************************************************
	
	public Set<String> getMolTaxonomyIds() {
		return molTaxonomyIds;
	}
	
	public void addMolTaxonomyId(String taxonomyId) {
		if(molTaxonomyIds == null)
			molTaxonomyIds = new HashSet<String>();
		molTaxonomyIds.add(taxonomyId);
	}
	
	public Set<String> getMetaMolTaxonomyIds() {
		return metaMolTaxonomyIds;
	}
	
	public void addMetaMolTaxonomyId(String taxonomyId) {
		if(metaMolTaxonomyIds == null)
			metaMolTaxonomyIds = new HashSet<String>();
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

}