package gov.lbl.glamm.server;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.retrosynthesis.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {

	//********************************************************************************
	
	private static final String SESSION_EXPERIMENT = "Session Experiment ";
	private static final String SESSION_ORGANISM = "Session Organism ";
	private static final String GLAMM_SESSION_MANAGER = "GLAMM_SESSION_MANAGER";

	//********************************************************************************

	private HashMap<String, Experiment> 			experimentId2Experiment	= null;
	private HashMap<String, HashMap<String, HashSet<Measurement>>> 
													measurements 			= null;
	private HashMap<String, ArrayList<Route>>		routes					= null;
	private HashMap<String, ArrayList<Experiment>> 	taxonomyId2Experiments 	= null;
	private HashMap<String, ArrayList<Gene>>		taxonomyId2Genes 		= null;
	private HashMap<String, Organism> 				taxonomyId2Organism		= null;
	
	//********************************************************************************

	public static SessionManager getSessionManager(HttpServletRequest request, boolean createNew) {
		SessionManager sessionManager = null;
		HttpSession session = request.getSession(createNew);
		if(session != null) {
			sessionManager = (SessionManager) session.getAttribute(GLAMM_SESSION_MANAGER);
			if(sessionManager == null) {
				sessionManager = new SessionManager();
				session.setAttribute(GLAMM_SESSION_MANAGER, sessionManager);
			}
		}
		return sessionManager;
	}

	//********************************************************************************

	protected SessionManager() {
		experimentId2Experiment		= new HashMap<String, Experiment>();
		measurements				= new HashMap<String, HashMap<String, HashSet<Measurement>>>();
		routes						= new HashMap<String, ArrayList<Route>>();
		taxonomyId2Experiments		= new HashMap<String, ArrayList<Experiment>>();
		taxonomyId2Genes 			= new HashMap<String, ArrayList<Gene>>();
		taxonomyId2Organism			= new HashMap<String, Organism>();
	}
	
	//********************************************************************************
	
	public void addExperiment(Experiment experiment, String taxonomyId) {
		if(experiment != null && taxonomyId != null) {
			experimentId2Experiment.put(experiment.getExperimentId(), experiment);
			ArrayList<Experiment> experiments = taxonomyId2Experiments.get(taxonomyId);
			if(experiments == null) {
				experiments = new ArrayList<Experiment>();
				taxonomyId2Experiments.put(taxonomyId, experiments);
			}
			experiments.add(experiment);
		}
	}

	//********************************************************************************

	public void addGenesForOrganism(Organism organism, ArrayList<Gene> genes) {
		if(organism != null && genes != null) {
			String taxonomyId = organism.getTaxonomyId();
			addGenesForTaxonomyId(taxonomyId, genes);
		}
	}

	//********************************************************************************

	public void addGenesForTaxonomyId(String taxonomyId, ArrayList<Gene> genes) {
		if(taxonomyId != null && genes != null) 
			taxonomyId2Genes.put(taxonomyId, genes);
	}
	
	//********************************************************************************
	
	public void addMeasurements(String expId, String sampleId, String taxonomyId, 
			HashMap<String, HashSet<Measurement>> id2Measurement) {
		String key = composeMeasurementsKey(expId, sampleId, taxonomyId);
		measurements.put(key, id2Measurement);
	}
	
	//********************************************************************************

	public void addOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			if(taxonomyId != null)
				taxonomyId2Organism.put(taxonomyId, organism);
		}
	}
	
	//********************************************************************************
	
	public void addRoutes(ArrayList<Route> routes) {
		if(routes != null && !routes.isEmpty()) {
			Route route = routes.get(0);
			String key = composeDirectionsKey(route);
			this.routes.put(key, routes);
		}
	}
	
	//********************************************************************************
	
	public Experiment getExperimentForId(String experimentId) {
		if(experimentId != null)
			return experimentId2Experiment.get(experimentId);
		return null;
	}

	//********************************************************************************

	public ArrayList<Experiment> getExperimentsForOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			return getExperimentsForTaxonomyId(taxonomyId);
		}
		return null;
	}
	
	//********************************************************************************
	
	public ArrayList<Experiment> getExperimentsForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Experiments.get(taxonomyId);
		return null;
	}
	
	//********************************************************************************


	public ArrayList<Gene> getGenesForOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			return getGenesForTaxonomyId(taxonomyId);
		}
		return null;
	}

	//********************************************************************************

	public ArrayList<Gene> getGenesForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Genes.get(taxonomyId);
		return null;
	}
	
	//********************************************************************************
	
	public HashMap<String, HashSet<Measurement>> getMeasurements(String expId, String sampleId, String taxonomyId) {
		String key = composeMeasurementsKey(expId, sampleId, taxonomyId);
		return measurements.get(key);
	}

	//********************************************************************************

	public ArrayList<Organism> getOrganisms() {
		return new ArrayList<Organism>(taxonomyId2Organism.values());
	}

	//********************************************************************************

	public Organism getOrganismForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Organism.get(taxonomyId);
		return null;
	}
	
	//********************************************************************************

	public ArrayList<Route> getRoutes(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		String key = composeDirectionsKey(taxonomyId, cpdSrcId, cpdDstId, algorithm, mapTitle);
		return routes.get(key);
	}
	
	//********************************************************************************
	
	public boolean isSessionExperiment(String experimentId) {
		return (getExperimentForId(experimentId) != null);
	}
	
	//********************************************************************************
	
	public boolean isSessionOrganism(String taxonomyId) {
		return (getOrganismForTaxonomyId(taxonomyId) != null);
	}
	
	//********************************************************************************

	public String nextAvailableExperimentId() {
		return SESSION_EXPERIMENT + " " + experimentId2Experiment.size();
	}
	
	//********************************************************************************
	
	public String nextAvailableTaxonomyId() {
		return SESSION_ORGANISM + " " + getOrganisms().size();
	}
	
	//********************************************************************************

	private String composeDirectionsKey(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		return taxonomyId + "_" + cpdSrcId + "_" + cpdDstId + "_" + algorithm + "_" + mapTitle;
	}
	
	//********************************************************************************

	private String composeDirectionsKey(Route route) {
		return composeDirectionsKey(route.getTaxonomyId(),
									route.getCpdSrcId(),
									route.getCpdDstId(),
									route.getAlgorithm(),
									route.getMapTitle());
	}
	
	//********************************************************************************
	
	private String composeMeasurementsKey(String expId, String sampleId, String taxonomyId) {
		return expId + "_" + sampleId + "_" + taxonomyId;
	}

	//********************************************************************************

}
