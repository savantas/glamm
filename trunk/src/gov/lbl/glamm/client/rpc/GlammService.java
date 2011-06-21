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
import java.util.HashSet;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface GlammService extends RemoteService {
	public String genCpdPopup(Compound compound, String taxonomyId);
	public String genCpdPopup(String extId, String extIdName, String taxonomyId);
	public String genCpdPopup(String query, String taxonomyId);
	public String genKeggPwyPopup(String query, String taxonomyId, String experimentId, String sampleId);
	public String genRxnPopup(String query, String taxonomyId);
	public ArrayList<Sample.DataType> getAvailableExperimentTypes();
	public ArrayList<Pathway> getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm);
	public MetabolicNetwork getMapConnectivity(String mapId);
	public ArrayList<? extends GlammPrimitive> getMeasurementsForExperiment(String experimentId, String sampleId, String taxonomyId, String expSource);
	public ArrayList<Reaction> getRxnsForOrganism(String taxonomyId, HashSet<String> rxnDbNames);
	public ArrayList<Compound> populateCompoundSearch(HashSet<String> cpdDbNames);
	public ArrayList<Experiment> populateExperiments(String taxonomyId);
	public ArrayList<Gene> populateLocusSearch(String taxonomyId);
	public ArrayList<Reaction> populateReactionSearch(HashSet<String> rxnDbNames);
	public ArrayList<Organism> populateOrganisms(Sample.DataType dataType);
	public ArrayList<Sample> populateSamples(String taxonomyId);
}
