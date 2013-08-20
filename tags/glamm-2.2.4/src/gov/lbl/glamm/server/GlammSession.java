package gov.lbl.glamm.server;

import gov.lbl.glamm.server.retrosynthesis.Route;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Manages GLAMM session data.
 * @author jtbates
 *
 */
public class GlammSession {

	private static final String SESSION_EXPERIMENT		= "Session Experiment ";
	private static final String SESSION_ORGANISM 		= "Session Organism ";
	private static final String GLAMM_SESSION 			= "GLAMM_SESSION";

	private Map<String, Experiment> 		experimentId2Experiment;
	private Map<String, String>				experimentId2TaxonomyId;
	private Map<String, Map<String, Set<Measurement>>> 
											measurements;
	private List<Organism>					organismsWithUserData;
	private Map<String, List<Route>>		routes;
	private ServerConfig					serverConfig;
	private Map<String, List<Experiment>> 	taxonomyId2Experiments;
	private Map<String, Set<Gene>>			taxonomyId2Genes;
	private Map<String, Organism> 			taxonomyId2Organism;
	private User						user;
	
	/**
	 * Gets the GLAMM session for the specified request.
	 * @param request The request.
	 * @return The session.
	 */
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

	protected GlammSession() {
		experimentId2Experiment		= new HashMap<String, Experiment>();
		experimentId2TaxonomyId		= new HashMap<String, String>();
		measurements				= new HashMap<String, Map<String, Set<Measurement>>>();
		organismsWithUserData		= new ArrayList<Organism>();
		routes						= new HashMap<String, List<Route>>();
		taxonomyId2Experiments		= new HashMap<String, List<Experiment>>();
		taxonomyId2Genes 			= new HashMap<String, Set<Gene>>();
		taxonomyId2Organism			= new HashMap<String, Organism>();
		user						= User.guestUser();
	}
	
	/**
	 * Adds an experiment for a given organism to the session.	
	 * @param experiment The experiment.
	 * @param organism The organism.
	 */
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

	/**
	 * Adds a set of genes for the specified organism.
	 * @param organism The organism.
	 * @param genes The set of genes.
	 */
	public void addGenesForOrganism(Organism organism, Set<Gene> genes) {
		if(organism != null && genes != null) {
			String taxonomyId = organism.getTaxonomyId();
			addGenesForTaxonomyId(taxonomyId, genes);
		}
	}

	/**
	 * Adds a set of genes for the specified taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @param genes The set of genes.
	 */
	public void addGenesForTaxonomyId(String taxonomyId, Set<Gene> genes) {
		if(taxonomyId != null && genes != null) 
			taxonomyId2Genes.put(taxonomyId, genes);
	}
	
	/**
	 * Adds a mapping of ids to measurements for an experiment and sample id.
	 * @param expId The experiment id.
	 * @param sampleId The sample id.
	 * @param id2Measurement The id to measurement mapping.
	 */
	public void addMeasurements(String expId, String sampleId,
			Map<String, Set<Measurement>> id2Measurement) {
		String key = composeMeasurementsKey(expId, sampleId);
		measurements.put(key, id2Measurement);
	}
	
	/**
	 * Adds and organism to the session.
	 * @param organism The organism.
	 */
	public void addOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			if(taxonomyId != null)
				taxonomyId2Organism.put(taxonomyId, organism);
		}
	}
	
	/**
	 * Adds a list of routes to the session.
	 * @param routes The list of routes.
	 */
	public void addRoutes(List<Route> routes) {
		if(routes != null && !routes.isEmpty()) {
			Route route = routes.get(0);
			String key = composeDirectionsKey(route);
			this.routes.put(key, routes);
		}
	}
	
	/**
	 * Gets an experiment for a given id.
	 * @param experimentId The experiment id.
	 * @return The experiment.
	 */
	public Experiment getExperimentForId(String experimentId) {
		if(experimentId != null)
			return experimentId2Experiment.get(experimentId);
		return null;
	}

	/**
	 * Gets a list of experiments for a given organism.
	 * @param organism The organism.
	 * @return The list of experiments.
	 */
	public List<Experiment> getExperimentsForOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			return getExperimentsForTaxonomyId(taxonomyId);
		}
		return null;
	}
	
	/**
	 * Gets a list of experiments for a given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The list of experiments.
	 */
	public List<Experiment> getExperimentsForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Experiments.get(taxonomyId);
		return null;
	}
	
	/**
	 * Gets a set of genes for a given organism.
	 * @param organism The organism.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForOrganism(Organism organism) {
		if(organism != null) {
			String taxonomyId = organism.getTaxonomyId();
			return getGenesForTaxonomyId(taxonomyId);
		}
		return new HashSet<Gene>();
	}

	/**
	 * Gets the set of genes for a given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The set of genes.
	 */
	public Set<Gene> getGenesForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Genes.get(taxonomyId);
		return new HashSet<Gene>();
	}
	
	/**
	 * Gets the id to measurement mapping for a given experiment id and sample id.
	 * @param expId The experiment id.
	 * @param sampleId The sample id.
	 * @return The id to measurment mapping.
	 */
	public Map<String, Set<Measurement>> getMeasurements(String expId, String sampleId) {
		String key = composeMeasurementsKey(expId, sampleId);
		return measurements.get(key);
	}

	/**
	 * Gets a list of all organisms in this session.
	 * @return The list of organisms.
	 */
	public List<Organism> getOrganisms() {
		return new ArrayList<Organism>(taxonomyId2Organism.values());
	}

	/**
	 * Gets an organism with the given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The organism.
	 */
	public Organism getOrganismForTaxonomyId(String taxonomyId) {
		if(taxonomyId != null)
			return taxonomyId2Organism.get(taxonomyId);
		return null;
	}
	
	/**
	 * Gets a list of organisms with user uploaded data.
	 * @return The list of organisms.
	 */
	public List<Organism> getOrganismsWithUserData() {
		return organismsWithUserData;
	}
	
	
	/**
	 * Gets a list of routes stored in this session.
	 * @param taxonomyId The taxonomy id.
	 * @param cpdSrcId The source compound id.
	 * @param cpdDstId The destination compound id.
	 * @param algorithm The algorithm used to compute these routes.
	 * @param mapTitle The title of the map.
	 * @return The list of routes.
	 */
	public List<Route> getRoutes(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		String key = composeDirectionsKey(taxonomyId, cpdSrcId, cpdDstId, algorithm, mapTitle);
		return routes.get(key);
	}
	
	/**
	 * Gets the server configuration for this session.
	 * @return The server configuration.
	 */
	public ServerConfig getServerConfig() {
		return serverConfig;
	}
	
	/**
	 * Gets the taxonomy id associated with an experiment specified by experiment id.
	 * @param experimentId The experiment id.
	 * @return The taxonomy id.
	 */
	public String getTaxonomyIdForExperimentId(String experimentId) {
		return experimentId2TaxonomyId.get(experimentId);
	}
	
	/**
	 * Gets the user for this session.
	 * @return The user.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Indicates if this session has experiments.
	 * @return Flag indicating if this session has experiments.
	 */
	public boolean hasExperiments() {
		return !(experimentId2Experiment == null || experimentId2Experiment.isEmpty());
	}
	
	/**
	 * Flag indicating whether or not an experiment id maps to a session experiment.
	 * @param experimentId The experiment id.
	 * @return Flag indicating if the experiment is a session experiment.
	 */
	public boolean isSessionExperiment(String experimentId) {
		return (getExperimentForId(experimentId) != null);
	}
	
	/**
	 * Flag indicating whether or not a taxonomy id maps to a session organism.
	 * @param taxonomyId The taxonomy id.
	 * @return Flag indicating if the taxonomy id maps ot a session organism.
	 */
	public boolean isSessionOrganism(String taxonomyId) {
		return (getOrganismForTaxonomyId(taxonomyId) != null);
	}
	
	/**
	 * Gets the next available experiment id for this session.
	 * @return The next available experiment id.
	 */
	public String nextAvailableExperimentId() {
		return SESSION_EXPERIMENT + " " + experimentId2Experiment.size();
	}
	
	/**
	 * Gets the next available taxonomy id for this session.
	 * @return The next available taxonomy id.
	 */
	public String nextAvailableTaxonomyId() {
		return SESSION_ORGANISM + " " + getOrganisms().size();
	}
	
	/**
	 * Sets the user for this session.
	 * @param user The user.
	 */
	public void setUser(final User user) {
		this.user = user;
	}
	
	private String composeDirectionsKey(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		return taxonomyId + "_" + cpdSrcId + "_" + cpdDstId + "_" + algorithm + "_" + mapTitle;
	}
	
	private String composeDirectionsKey(Route route) {
		return composeDirectionsKey(route.getTaxonomyId(),
									route.getCpdSrcId(),
									route.getCpdDstId(),
									route.getAlgorithm(),
									route.getMapTitle());
	}
	
	private String composeMeasurementsKey(String expId, String sampleId) {
		return expId + "_" + sampleId;
	}

}
