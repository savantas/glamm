package gov.lbl.glamm.client.rpc;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface GlammService extends RemoteService {
	public User authenticateUser(final String userId, final String auth);
	public String genCpdPopup(Compound compound, String taxonomyId);
	public String genCpdPopup(String id, String taxonomyId);
	public String genCpdPopup(Set<String> ids, String taxonomyId);
	public String genPwyPopup(Set<String> ids, String taxonomyId, String experimentId, String sampleId);
	public String genRxnPopup(Set<String> ids, String taxonomyId);
	public List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors();
	public List<Sample.DataType> getAvailableExperimentTypes();
	public List<Pathway> getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm);
	public String getIsolateHost();
//	public MetabolicNetwork getMapConnectivity(String mapId);
	public List<? extends HasMeasurements> getMeasurementsForExperiment(String experimentId, String sampleId);
	public String getMetagenomeHost();
	public Set<Reaction> getRxnsForOrganism(String taxonomyId);
	public User getLoggedInUser();
	public void logOutUser();
	public Set<Compound> populateCompoundSearch(String mapId);
	public List<Experiment> populateExperiments(String taxonomyId);
	public List<Gene> populateLocusSearch(String taxonomyId);
	public Set<Reaction> populateReactionSearch(String mapId);
	public List<Organism> populateOrganisms(Sample.DataType dataType);
	public List<Sample> populateSamples(String taxonomyId);
}
