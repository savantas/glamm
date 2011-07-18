package gov.lbl.glamm.client.rpc;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.GlammPrimitive;
import gov.lbl.glamm.client.model.GlammUser;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface GlammService extends RemoteService {
	public GlammUser authenticateUser(final String userId, final String auth);
	public String genCpdPopup(Compound compound, String taxonomyId);
	public String genCpdPopup(String extId, String extIdName, String taxonomyId);
	public String genCpdPopup(String query, String taxonomyId);
	public String genKeggPwyPopup(String query, String taxonomyId, String experimentId, String sampleId);
	public String genRxnPopup(String query, String taxonomyId);
	public List<Sample.DataType> getAvailableExperimentTypes();
	public List<Pathway> getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm);
	public String getIsolateHost();
	public MetabolicNetwork getMapConnectivity(String mapId);
	public List<? extends GlammPrimitive> getMeasurementsForExperiment(String experimentId, String sampleId, String taxonomyId, String expSource);
	public String getMetagenomeHost();
	public List<Reaction> getRxnsForOrganism(String taxonomyId, Set<String> rxnDbNames);
	public GlammUser getLoggedInUser();
	public void logOutUser();
	public List<Compound> populateCompoundSearch(Set<String> cpdDbNames);
	public List<Experiment> populateExperiments(String taxonomyId);
	public List<Gene> populateLocusSearch(String taxonomyId);
	public List<Reaction> populateReactionSearch(Set<String> rxnDbNames);
	public List<Organism> populateOrganisms(Sample.DataType dataType);
	public List<Sample> populateSamples(String taxonomyId);
	
//	public GlammUser updateGlammUser(final String userId, final String auth);
}
