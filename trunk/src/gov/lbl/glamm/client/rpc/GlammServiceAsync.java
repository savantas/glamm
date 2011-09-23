package gov.lbl.glamm.client.rpc;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GlammServiceAsync {
	public void authenticateUser(final String userId, final String auth, AsyncCallback<User> callback);
	public void genCpdPopup(Compound compound, String taxonomyId, AsyncCallback<String> callback);
	public void genCpdPopup(String extId, String extIdName, String taxonomyId, AsyncCallback<String> callback);
	public void genCpdPopup(String query, String taxonomyId, AsyncCallback<String> callback);
	public void genKeggPwyPopup(String query, String taxonomyId, String experimentId, String sampleId, AsyncCallback<String> callback);
	public void genRxnPopup(String query, String taxonomyId, AsyncCallback<String> callback);
	public void getAvailableExperimentTypes(AsyncCallback<List<Sample.DataType>> callback);
	public void getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm, AsyncCallback<List<Pathway>> callback);
	public void getIsolateHost(AsyncCallback<String> callback);
	public void getMapConnectivity(String mapId, AsyncCallback<MetabolicNetwork> callback);
	public void getMeasurementsForExperiment(String experimentId, String sampleId, AsyncCallback<List<? extends HasMeasurements>> callback);
	public void getMetagenomeHost(AsyncCallback<String> callback);
	public void getLoggedInUser(AsyncCallback<User> user);
	public void getRxnsForOrganism(String taxonomyId, Set<String> rxnDbNames, AsyncCallback<List<Reaction>> callback);
	public void logOutUser(AsyncCallback<Void> callback);
	public void populateCompoundSearch(Set<String> cpdDbNames, AsyncCallback<List<Compound>> callback);
	public void populateExperiments(String taxonomyId, AsyncCallback<List<Experiment>> callback);
	public void populateLocusSearch(String taxonomyId, AsyncCallback<List<Gene>> callback);
	public void populateOrganisms(Sample.DataType dataType, AsyncCallback<List<Organism>> callback);
	public void populateReactionSearch(Set<String> rxnDbNames, AsyncCallback<List<Reaction>> callback);
	public void populateSamples(String taxonomyId, AsyncCallback<List<Sample>> callback);
}
