package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrganismDAO {
	public List<Organism> getAllOrganisms();
	public List<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType);
	public Organism getOrganismForTaxonomyId(final String taxonomyId);
	public Map<String, Set<Organism>> getTransgenicCandidatesForEcNums(Set<String> ecNums);
}
