package gov.lbl.glamm.server;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.client.experiment.rpc.PathwayExperimentService;
import gov.lbl.glamm.client.map.rpc.GlammService;
import gov.lbl.glamm.server.actions.AuthenticateUser;
import gov.lbl.glamm.server.actions.GenCpdPopup;
import gov.lbl.glamm.server.actions.GenPwyPopup;
import gov.lbl.glamm.server.actions.GetAnnotatedMapDescriptors;
import gov.lbl.glamm.server.actions.GetAvailableExperimentTypes;
import gov.lbl.glamm.server.actions.GetExperimentPathwayData;
import gov.lbl.glamm.server.actions.GetGlammState;
import gov.lbl.glamm.server.actions.GetGroupData;
import gov.lbl.glamm.server.actions.GetOrganism;
import gov.lbl.glamm.server.actions.GetPathways;
import gov.lbl.glamm.server.actions.GetReactions;
import gov.lbl.glamm.server.actions.GetRxnsForOrganism;
import gov.lbl.glamm.server.actions.GetSample;
import gov.lbl.glamm.server.actions.PopulateCompoundSearch;
import gov.lbl.glamm.server.actions.PopulateDataServices;
import gov.lbl.glamm.server.actions.PopulateLocusSearch;
import gov.lbl.glamm.server.actions.PopulateOrganisms;
import gov.lbl.glamm.server.actions.PopulateReactionSearch;
import gov.lbl.glamm.server.actions.PopulateSamples;
import gov.lbl.glamm.server.actions.requesthandlers.GetDirections;
import gov.lbl.glamm.server.externalservice.ExternalDataServiceManager;
import gov.lbl.glamm.server.kbase.actions.GetMetabolicModel;
import gov.lbl.glamm.server.kbase.actions.GetWorkspaceData;
import gov.lbl.glamm.shared.DeploymentDomain;
import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.model.Algorithm;
import gov.lbl.glamm.shared.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.GlammState;
import gov.lbl.glamm.shared.model.MetabolicModel;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.OverlayDataGroup;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.User;
import gov.lbl.glamm.shared.model.interfaces.HasMeasurements;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;
import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceData;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * RPC service for glamm
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class GlammServiceImpl extends RemoteServiceServlet 
	implements GlammService, PathwayExperimentService {
	
	private static final String SERVER_CONFIG_XML_FILE_NAME		= "/config/server_config.xml";
	private static final String EXTERNAL_SERVICES_XML_FILE_NAME = "/config/external_services.xml";
	private static final String LOCATION_CONFIG_XML_FILE_NAME	= "/config/location_config.xml";
	
	private static Set<GlammSession> sessions;
	
	static {
		sessions = new HashSet<GlammSession>();
	}
	
	/**
	 * Gets the single instance of the GlammSession object associated with this session
	 * @return the GlammSession instance
	 */
	private GlammSession getGlammSession() {
		HttpServletRequest request = this.getThreadLocalRequest();
		GlammSession glammSession = GlammSession.getGlammSession(request);
		
		sessions.add(glammSession);
//		System.out.println(glammSession);
		
		return glammSession;
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
	public String genCpdPopup(final Compound compound, final Organism organism) {
		return GenCpdPopup.genCpdPopup(getGlammSession(), compound, organism);
	}
		
	@Override
	public String genCpdPopup(Set<String> ids, final Organism organism) {
		return GenCpdPopup.genCpdPopupForIds(getGlammSession(), ids, organism);
	}
	
	@Override
	public String genPwyPopup(Set<String> ids, final Organism organism, final Sample sample) {
		return GenPwyPopup.genPwyPopup(getGlammSession(), ids, organism, sample);
	}
	
	@Override
	public List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors() {
		return GetAnnotatedMapDescriptors.getAnnotatedMapDescriptors(getGlammSession());
	}
	
	@Override
	public List<Sample.DataType> getAvailableSampleTypes() {
		return GetAvailableExperimentTypes.getAvailableExperimentTypes(getGlammSession());
	}
	
	@Override
	public List<Pathway> getDirections(final Organism organism, final Compound cpdSrc, final Compound cpdDst, final String mapTitle, final Algorithm algorithm) {
		return GetDirections.getDirections(getGlammSession(), organism, cpdSrc, cpdDst, mapTitle, algorithm);
	}
	
	@Override
	public String getIsolateHost() {
		GlammSession sm = getGlammSession();
		return sm.getServerConfig().getIsolateHost();
	}
	
	@Override
	public Set<? extends HasMeasurements> getSample(final Sample sample) {
		return GetSample.getMeasurementsForSample(getGlammSession(), sample);
	}
	
	@Override
	public String getMetagenomeHost() {
		return getGlammSession().getServerConfig().getMetagenomeHost();
	}
	
	@Override
	public Set<Pathway> getPathways(final Set<String> ids, final Organism organism, final Sample sample) {
		return GetPathways.getPathways(getGlammSession(), ids, organism, sample);
	}
	
	@Override
	public Set<Reaction> getReactions(final Set<String> ids, final Organism organism, final Sample sample) {
		return GetReactions.getReactions(getGlammSession(), ids, organism, sample);
	}
	
	@Override
	public Set<Reaction> getRxnsForOrganism(final Organism organism) {
		return GetRxnsForOrganism.getRxnsForOrganism(getGlammSession(), organism);
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
	public Set<Compound> populateCompoundSearch(final String mapId) {
		return PopulateCompoundSearch.populateCompoundSearch(getGlammSession(), mapId);
	}
	
	@Override
	public Set<Gene> populateLocusSearch(final Organism organism) {
		return PopulateLocusSearch.populateLocusSearch(getGlammSession(), organism);
	}

	@Override
	public List<Organism> populateOrganisms(final Sample.DataType dataType) {
		return PopulateOrganisms.populateOrganisms(getGlammSession(), dataType);
	}
	
	@Override
	public Set<Reaction> populateReactionSearch(final String mapId) {
		return PopulateReactionSearch.populateReactionSearch(getGlammSession(), mapId);
	}
	
	@Override
	public List<Sample> populateSamples(final Organism organism) {
		return PopulateSamples.populateSamples(getGlammSession(), organism);
	}
	
	public String nonDBTest() {
		return "RPC didn't touch the database!";
	}
	
	/**
	 * Sets up database connection pool and request handler factory classes
	 */
	@Override
	public void init()
	throws ServletException  {
		try {
			ServletContext sc = this.getServletContext();
			ExternalDataServiceManager.init(sc.getResource(EXTERNAL_SERVICES_XML_FILE_NAME).toString());
			ConfigurationManager.init(sc.getResource(SERVER_CONFIG_XML_FILE_NAME).toString(), sc.getResource(LOCATION_CONFIG_XML_FILE_NAME).toString());
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

	@Override
	public MetabolicModel getMetabolicModel(final String modelId) {
		return null;
//		return GetMetabolicModel.getMetabolicModel(getGlammSession(), modelId);
	}
	
//	@Override
//	public Set<Reaction> getReactionFluxes(final FluxExperiment exp) {
//		return GetFluxes.getFluxes(getGlammSession(), exp);
//	}

	@Override
	public Set<OverlayDataGroup> getOverlayData(final String text) {
		return GetGroupData.getOverlayData(getGlammSession(), text);
	}

	@Override
	public Set<OverlayDataGroup> getOverlayDataFromService(final ExternalDataService service) {
		return GetGroupData.getOverlayDataFromService(getGlammSession(), service);
	}

	@Override
	public List<ExternalDataService> populateDataServices() {
		return PopulateDataServices.populateDataServices(getGlammSession());
	}

	@Override
	public Organism getOrganismForTaxId(final String taxId) {
		return GetOrganism.getOrganismForTaxId(getGlammSession(), taxId);
	}
	
	@Override
	public GlammState getStateFromHistoryToken(final String token) {
		return GetGlammState.getStateFromHistoryToken(getGlammSession(), token);
	}

	@Override
	public PathwayExperimentData getPathwayData(final String pathwayIds, final String experimentIds) throws IllegalArgumentException,
																								RequestException {
		String fileName = this.getServletContext().getRealPath( "/data/pathway.xml" );
		return GetExperimentPathwayData.getPathwayData(pathwayIds, experimentIds, fileName);
	}
//
//	@Override
//	public List<MetabolicModel> populateMetabolicModels() {
//		return PopulateMetabolicModels.populateMetabolicModels(getGlammSession());
//	}
//
//	@Override
//	public List<String> populateFbaResults(final String modelId) {
//		return PopulateFbaResults.populateFbaResults(getGlammSession(), modelId);
//	}
//
//	@Override
//	public Media getMetabolicModelMedia(final String mediaId, final String biochemistryId) {
//		return GetMetabolicModelMedia.getMetabolicModelMedia(getGlammSession(), mediaId, biochemistryId);
//	}
//
//	@Override
//	public FluxExperiment getFluxExperiment(final String expId) {
//		return GetFluxes.getFluxExperiment(getGlammSession(), expId);
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public FluxExperiment getFbaResults(final String expId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public List<KBWorkspaceData> populateWorkspaces() {
		return GetWorkspaceData.getWorkspaceList(getGlammSession());
	}
	
	@Override
	public List<KBWorkspaceObjectData> populateWorkspaceModels(final String workspace) {
		return GetWorkspaceData.getWorkspaceModelList(getGlammSession(), workspace);
	}
	
	@Override
	public KBMetabolicModel getKBaseMetabolicModel(final String modelId, final String workspaceId) {
		return GetMetabolicModel.getMetabolicModel(getGlammSession(), modelId, workspaceId);
	}
	
	@Override
	public KBFBAResult getKBaseFBAResult(final String fbaId, final String workspaceId) {
		return GetMetabolicModel.getFBAResult(getGlammSession(), fbaId, workspaceId);
	}
	
	@Override
	public List<KBWorkspaceObjectData> populateWorkspaceFbas(final String workspace) {
		return GetWorkspaceData.getWorkspaceFbaList(getGlammSession(), workspace);
	}
	
	@Override
	public DeploymentDomain getDeploymentDomain() {
		return ConfigurationManager.getDeploymentDomain();
	}
}
