package gov.lbl.glamm.server;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.GlammPrimitive;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.rpc.GlammService;
import gov.lbl.glamm.server.actions.GenCpdPopup;
import gov.lbl.glamm.server.actions.GenPwyPopup;
import gov.lbl.glamm.server.actions.GenRxnPopup;
import gov.lbl.glamm.server.actions.GetExperiment;
import gov.lbl.glamm.server.actions.GetMapConnectivity;
import gov.lbl.glamm.server.actions.GetRxnsForOrganism;
import gov.lbl.glamm.server.actions.PopulateCompoundSearch;
import gov.lbl.glamm.server.actions.PopulateExperiments;
import gov.lbl.glamm.server.actions.PopulateLocusSearch;
import gov.lbl.glamm.server.actions.PopulateOrganisms;
import gov.lbl.glamm.server.actions.PopulateReactionSearch;
import gov.lbl.glamm.server.actions.PopulateSamples;
import gov.lbl.glamm.server.actions.requesthandlers.GetDirections;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * RPC service for glamm
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class GlammServiceImpl extends RemoteServiceServlet 
	implements GlammService {
	
	private static final String CONFIG_PATH						= "/config";
	private static final String DB_CONFIG_XML_FILE_NAME 		= CONFIG_PATH + "/db_config.xml";
	
	/**
	 * Gets the single instance of the SessionManager object
	 * @param createNew if true, creates a new SessionManager object if it doesn't already exist
	 * @return the SessionManager instance
	 */
	private SessionManager getSessionManager(boolean createNew) {
		HttpServletRequest request = this.getThreadLocalRequest();
		return SessionManager.getSessionManager(request, createNew);
	}
	
	@Override
	public String genCpdPopup(Compound compound, String taxonomyId) {
		return GenCpdPopup.genCpdPopup(getSessionManager(false), compound, taxonomyId);
	}
	
	@Override
	public String genCpdPopup(String extId, String extIdName, String taxonomyId) {
		return GenCpdPopup.genCpdPopup(getSessionManager(false), extId, extIdName, taxonomyId);
	}
	
	/**
	 * Generates the HTML for the compound popups.
	 * @param query The query string encoded in the map's svg representation
	 * @param taxonomyId
	 * @return The compound popup HTML.
	 */
	@Override
	public String genCpdPopup(String query, String taxonomyId) {
		return GenCpdPopup.genCpdPopupFromQueryString(getSessionManager(false), query, taxonomyId);
	}
	
	@Override
	public String genKeggPwyPopup(String query, String taxonomyId, String experimentId, String sampleId) {
		return GenPwyPopup.genPwyPopupFromQueryString(query, taxonomyId, experimentId, sampleId);
	}
	
	@Override
	public String genRxnPopup(String query, String taxonomyId) {
		return GenRxnPopup.genRxnPopupFromQueryString(getSessionManager(false), query, taxonomyId);
	}
	
	@Override
	public ArrayList<Pathway> getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm) {
		return GetDirections.getDirections(getSessionManager(true), taxonomyId, cpdSrc, cpdDst, mapTitle, algorithm);
	}
	
	@Override
	public MetabolicNetwork getMapConnectivity(String mapId) {
		return GetMapConnectivity.getMapConnectivity(mapId);
	}
	
	@Override
	public ArrayList<? extends GlammPrimitive> getMeasurementsForExperiment(String experimentId, String sampleId, String taxonomyId, String expSource) {
		return GetExperiment.getMeasurementsForExperiment(getSessionManager(false), experimentId, sampleId, taxonomyId, expSource);
	}
	
	@Override
	public ArrayList<Reaction> getRxnsForOrganism(String taxonomyId, HashSet<String> dbNames) {
		return GetRxnsForOrganism.getRxnsForOrganism(getSessionManager(false), taxonomyId, dbNames);
	}
	
	@Override
	public ArrayList<Compound> populateCompoundSearch(HashSet<String> dbNames) {
		return PopulateCompoundSearch.populateCompoundSearch(dbNames);
	}
	
	@Override
	public ArrayList<Experiment> populateExperiments(String taxonomyId) {
		return PopulateExperiments.populateExperiments(getSessionManager(false), taxonomyId);
	}
	
	@Override
	public ArrayList<Gene> populateLocusSearch(String taxonomyId) {
		return PopulateLocusSearch.populateLocusSearch(getSessionManager(false), taxonomyId);
	}

	@Override
	public ArrayList<Organism> populateOrganisms(String dataType) {
		return PopulateOrganisms.populateOrganisms(getSessionManager(false), dataType);
	}
	
	@Override
	public ArrayList<Reaction> populateReactionSearch(HashSet<String> dbNames) {
		return PopulateReactionSearch.populateReactionSearch(dbNames);
	}
	
	@Override
	public ArrayList<Sample> populateSamples(String taxonomyId) {
		return PopulateSamples.populateSamples(getSessionManager(false), taxonomyId);
	}
	
	/**
	 * Sets up database connection pool and request handler factory classes
	 */
	@Override
	public void init()
	throws ServletException  {
		try {
			ServletContext sc = this.getServletContext();
			GlammDbConnectionPool.init(sc.getResource(DB_CONFIG_XML_FILE_NAME).toString());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tears down database connection pool
	 */
	@Override
	public void destroy() {
		GlammDbConnectionPool.destroy();
	}

}
