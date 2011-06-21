package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface OrganismDAO {
	public ArrayList<Organism> getAllOrganisms();
	public ArrayList<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType);
	public Organism getOrganismForTaxonomyId(final String taxonomyId);
	public HashMap<String, HashSet<Organism>> getTransgenicCandidatesForEcNums(HashSet<String> ecNums);
}
