package gov.lbl.glamm.client.rpc;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.GlammPrimitive;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GlammServiceAsync {
	public void genCpdPopup(Compound compound, String taxonomyId, AsyncCallback<String> callback);
	public void genCpdPopup(String extId, String extIdName, String taxonomyId, AsyncCallback<String> callback);
	public void genCpdPopup(String query, String taxonomyId, AsyncCallback<String> callback);
	public void genKeggPwyPopup(String query, String taxonomyId, String experimentId, String sampleId, AsyncCallback<String> callback);
	public void genRxnPopup(String query, String taxonomyId, AsyncCallback<String> callback);
	public void getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm, AsyncCallback<ArrayList<Pathway>> callback);
	public void getMapConnectivity(String mapId, AsyncCallback<MetabolicNetwork> callback);
	public void getMeasurementsForExperiment(String experimentId, String sampleId, String taxonomyId, String expSource, AsyncCallback<ArrayList<? extends GlammPrimitive>> callback);
	public void getRxnsForOrganism(String taxonomyId, String dbName, AsyncCallback<ArrayList<Reaction>> callback);
	public void populateCompoundSearch(String extIdName, AsyncCallback<ArrayList<Compound>> callback);
	public void populateExperiments(String taxonomyId, AsyncCallback<ArrayList<Experiment>> callback);
	public void populateLocusSearch(String taxonomyId, AsyncCallback<ArrayList<Gene>> callback);
	public void populateOrganisms(String dataType, AsyncCallback<ArrayList<Organism>> callback);
	public void populateReactionSearch(String extIdName, AsyncCallback<ArrayList<Reaction>> callback);
	public void populateSamples(String taxonomyId, AsyncCallback<ArrayList<Sample>> callback);
}
