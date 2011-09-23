package gov.lbl.glamm.server;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.retrosynthesis.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GlammSession {

	//********************************************************************************
	
	
	private static final String SESSION_EXPERIMENT		= "Session Experiment ";
	private static final String SESSION_ORGANISM 		= "Session Organism ";
	private static final String GLAMM_SESSION 			= "GLAMM_SESSION";

	//********************************************************************************

	private Map<String, Experiment> 		experimentId2Experiment;
	private Map<String, String>				experimentId2TaxonomyId;
	private Map<String, Map<String, Set<Measurement>>> 
											measurements;
	private List<Organism>					organismsWithUserData;
	private Map<String, List<Route>>		routes;
	private ServerConfig					serverConfig;
	private Map<String, List<Experiment>> 	taxonomyId2Experiments;
	private Map<String, List<Gene>>			taxonomyId2Genes;
	private Map<String, Organism> 			taxonomyId2Organism;
	private User						user;
	
	//********************************************************************************

	public static GlammSession getGlammSession(HttpServletRequest request) {
		GlammSession glammSession = null;
		HttpSession session = request.getSession(true); // create a session if one doesn't already exist
		if(session != null) {
			glammSession = (GlammSession) session.getAttribute(GLAMM_SESSION);
			if(glammSession == null) {
				glammSession = new GlammSession();
				glammSession.serverConfig = ConfigurationManager.getServerConfigForServerName(request.getServerName());
				session.setAttribute(GLAMM_SESSION, glammSession);
			}
		}
		return glammSession;
	}

	//********************************************************************************

	protected GlammSession() {
		experimentId2Experiment		= new HashMap<String, Experiment>();
		experimentId2TaxonomyId		= new HashMap<String, String>();
		measurements				= new HashMap<String, Map<String, Set<Measurement>>>();
		organismsWithUserData		= new ArrayList<Organism>();
		routes						= new HashMap<String, List<Route>>();
		taxonomyId2Experiments		= new HashMap<String, List<Experiment>>();
		taxonomyId2Genes 			= new HashMap<String, List<Gene>>();
		taxonomyId2Organism			= new HashMap<String, Organism>();
		user						= User.guestUser();
	}
	
	//********************************************************************************
	
	public void addExperiment(Experiment experiment, Organism organism) {
		if(experiment != null && organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			experimentId2Experiment.put(experiment.getExperimentId(), experiment);
			experimentId2TaxonomyId.put(experiment.getExperimentId(), taxonomyId);
			List<Experiment> experiments = taxonomyId2Experiments.get(taxonomyId);
			if(experiments == null) {
				experiments = new ArrayList<Experiment>();
				taxonomyId2Experiments.put(taxonomyId, experiments);
			}
			experiments.add(experiment);
			if(!organismsWithUserData.contains(organism))
				organismsWithUserData.add(organism);
		}
	}

	//********************************************************************************

	public void addGenesForOrganism(Organism organism, List<Gene> genes) {
		if(organism != null && genes != null) {
			String taxonomyId = organism.getTaxonomyId();
			addGenesForTaxonomyId(taxonomyId, genes);
		}
	}

	//********************************************************************************

	public void addGenesForTaxonomyId(String taxonomyId, List<Gene> genes) {
		if(taxonomyId != null && genes != null) 
			taxonomyId2Genes.put(taxonomyId, genes);
	}
	
	//********************************************************************************
	
	public void addMeasurements(String expId, String sampleId,
			Map<String, Set<Measurement>> id2Measurement) {
		String key = composeMeasurementsKey(expId, sampleId);
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
	
	public void addRoutes(List<Route> routes) {
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

	public List<Experiment> getExperimentsForOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			return getExperimentsForTaxonomyId(taxonomyId);
		}
		return null;
	}
	
	//********************************************************************************
	
	public List<Experiment> getExperimentsForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Experiments.get(taxonomyId);
		return null;
	}
	
	//********************************************************************************


	public List<Gene> getGenesForOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			return getGenesForTaxonomyId(taxonomyId);
		}
		return null;
	}

	//********************************************************************************

	public List<Gene> getGenesForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Genes.get(taxonomyId);
		return null;
	}
	
	//********************************************************************************
	
	public Map<String, Set<Measurement>> getMeasurements(String expId, String sampleId) {
		String key = composeMeasurementsKey(expId, sampleId);
		return measurements.get(key);
	}

	//********************************************************************************

	public List<Organism> getOrganisms() {
		return new ArrayList<Organism>(taxonomyId2Organism.values());
	}

	//********************************************************************************

	public Organism getOrganismForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Organism.get(taxonomyId);
		return null;
	}
	
	//********************************************************************************
	
	public List<Organism> getOrganismsWithUserData() {
		return organismsWithUserData;
	}
	
	//********************************************************************************

	public List<Route> getRoutes(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		String key = composeDirectionsKey(taxonomyId, cpdSrcId, cpdDstId, algorithm, mapTitle);
		return routes.get(key);
	}
	
	//********************************************************************************

	public ServerConfig getServerConfig() {
		return serverConfig;
	}
	
	//********************************************************************************

	public String getTaxonomyIdForExperimentId(String experimentId) {
		return experimentId2TaxonomyId.get(experimentId);
	}
	
	//********************************************************************************

	public User getUser() {
		return user;
	}
	
	//********************************************************************************

	public boolean hasExperiments() {
		return !(experimentId2Experiment == null || experimentId2Experiment.isEmpty());
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

	public void setUser(final User user) {
		this.user = user;
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
	
	private String composeMeasurementsKey(String expId, String sampleId) {
		return expId + "_" + sampleId;
	}

	//********************************************************************************

}
