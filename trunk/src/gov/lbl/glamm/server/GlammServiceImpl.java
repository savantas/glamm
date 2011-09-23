package gov.lbl.glamm.server;

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
import gov.lbl.glamm.client.rpc.GlammService;
import gov.lbl.glamm.server.actions.AuthenticateUser;
import gov.lbl.glamm.server.actions.GenCpdPopup;
import gov.lbl.glamm.server.actions.GenPwyPopup;
import gov.lbl.glamm.server.actions.GenRxnPopup;
import gov.lbl.glamm.server.actions.GetAvailableExperimentTypes;
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

import java.util.List;
import java.util.Set;

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
	
	private static final String SERVER_CONFIG_XML_FILE_NAME		= "/config/server_config.xml";
	
	/**
	 * Gets the single instance of the GlammSession object associated with this session
	 * @return the GlammSession instance
	 */
	private GlammSession getGlammSession() {
		HttpServletRequest request = this.getThreadLocalRequest();
		return GlammSession.getGlammSession(request);
	}
	
	@Override
	public User authenticateUser(final String userId, final String auth) {
		HttpServletRequest request = this.getThreadLocalRequest();
		String remoteAddr = request.getRemoteAddr();
		GlammSession sm = getGlammSession();
		User user = AuthenticateUser.authenticateUser(sm, userId, auth, remoteAddr);
		sm.setUser(user);
		return user;
	}
	
	@Override
	public String genCpdPopup(Compound compound, String taxonomyId) {
		return GenCpdPopup.genCpdPopup(getGlammSession(), compound, taxonomyId);
	}
	
	@Override
	public String genCpdPopup(String extId, String extIdName, String taxonomyId) {
		return GenCpdPopup.genCpdPopup(getGlammSession(), extId, extIdName, taxonomyId);
	}
	
	/**
	 * Generates the HTML for the compound popups.
	 * @param query The query string encoded in the map's svg representation
	 * @param taxonomyId
	 * @return The compound popup HTML.
	 */
	@Override
	public String genCpdPopup(String query, String taxonomyId) {
		return GenCpdPopup.genCpdPopupFromQueryString(getGlammSession(), query, taxonomyId);
	}
	
	@Override
	public String genKeggPwyPopup(String query, String taxonomyId, String experimentId, String sampleId) {
		return GenPwyPopup.genPwyPopupFromQueryString(getGlammSession(), query, taxonomyId, experimentId, sampleId);
	}
	
	@Override
	public String genRxnPopup(String query, String taxonomyId) {
		return GenRxnPopup.genRxnPopupFromQueryString(getGlammSession(), query, taxonomyId);
	}
	
	@Override
	public List<Sample.DataType> getAvailableExperimentTypes() {
		return GetAvailableExperimentTypes.getAvailableExperimentTypes(getGlammSession());
	}
	
	@Override
	public List<Pathway> getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm) {
		return GetDirections.getDirections(getGlammSession(), taxonomyId, cpdSrc, cpdDst, mapTitle, algorithm);
	}
	
	@Override
	public String getIsolateHost() {
		GlammSession sm = getGlammSession();
		return sm.getServerConfig().getIsolateHost();
	}
	
	@Override
	public MetabolicNetwork getMapConnectivity(String mapId) {
		return GetMapConnectivity.getMapConnectivity(getGlammSession(), mapId);
	}
	
	@Override
	public List<? extends HasMeasurements> getMeasurementsForExperiment(String experimentId, String sampleId) {
		return GetExperiment.getMeasurementsForExperiment(getGlammSession(), experimentId, sampleId);
	}
	
	@Override
	public String getMetagenomeHost() {
		GlammSession sm = getGlammSession();
		return sm.getServerConfig().getMetagenomeHost();
	}
	
	@Override
	public List<Reaction> getRxnsForOrganism(String taxonomyId, Set<String> dbNames) {
		return GetRxnsForOrganism.getRxnsForOrganism(getGlammSession(), taxonomyId, dbNames);
	}
	
	@Override
	public User getLoggedInUser() {
		return getGlammSession().getUser();
	}
	
	@Override
	public void logOutUser() {
		getGlammSession().setUser(User.guestUser());
	}
	
	@Override
	public List<Compound> populateCompoundSearch(Set<String> dbNames) {
		return PopulateCompoundSearch.populateCompoundSearch(getGlammSession(), dbNames);
	}
	
	@Override
	public List<Experiment> populateExperiments(String taxonomyId) {
		return PopulateExperiments.populateExperiments(getGlammSession(), taxonomyId);
	}
	
	@Override
	public List<Gene> populateLocusSearch(String taxonomyId) {
		return PopulateLocusSearch.populateLocusSearch(getGlammSession(), taxonomyId);
	}

	@Override
	public List<Organism> populateOrganisms(Sample.DataType dataType) {
		return PopulateOrganisms.populateOrganisms(getGlammSession(), dataType);
	}
	
	@Override
	public List<Reaction> populateReactionSearch(Set<String> dbNames) {
		return PopulateReactionSearch.populateReactionSearch(getGlammSession(), dbNames);
	}
	
	@Override
	public List<Sample> populateSamples(String taxonomyId) {
		return PopulateSamples.populateSamples(getGlammSession(), taxonomyId);
	}
	
	/**
	 * Sets up database connection pool and request handler factory classes
	 */
	@Override
	public void init()
	throws ServletException  {
		try {
			ServletContext sc = this.getServletContext();
			ConfigurationManager.init(sc.getResource(SERVER_CONFIG_XML_FILE_NAME).toString());
		} catch(Exception e) {
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
