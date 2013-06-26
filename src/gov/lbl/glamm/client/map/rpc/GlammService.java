package gov.lbl.glamm.client.map.rpc;

import gov.lbl.glamm.shared.DeploymentDomain;
import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.exceptions.GlammStateException;
import gov.lbl.glamm.shared.exceptions.UnauthorizedException;
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

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RPC interface for GLAMM client-server communication.
 * @author jtbates
 *
 */
@RemoteServiceRelativePath("rpc")
public interface GlammService extends RemoteService {
	
	public static String PATHWAY_SELECTION_COOKIE = "pathwaySelection";
	public static String EXPERIMENT_SELECTION_COOKIE = "experimentSelection";

	/**
	 * Authenticates the user based on the user id and the contents of the MicrobesOnline "auth" cookie.
	 * @param userId The user id.
	 * @param auth The contents of the MicrobesOnline "auth" cookie.
	 * @return The authenticated User - guest User if authentication fails.
	 */
	public User authenticateUser(final String userId, final String auth);
	
	/**
	 * Generates the HTML for a compound popup.
	 * @param compound The compound.
	 * @param organism The organism associated with this popup.
	 * @return The popup HTML.
	 */
	public String genCpdPopup(final Compound compound, final Organism organism);
	
	/**
	 * Generates the HTML for a compound popup.
	 * @param ids The set of ids for the compounds in this popup.
	 * @param organism The organism associated with this popup.
	 * @return The popup HTML.
	 */
	public String genCpdPopup(final Set<String> ids, final Organism organism);
	
	/**
	 * Generates the HTML for a pathway popup - the popup will contain a link back to the MicrobesOnline pathway page,
	 * which will include the microarray data overlay for the specified experiment and sample.
	 * @param ids The set of ids for the pathways in this popup.
	 * @param organism The organism associated with this popup.
	 * @param sample The sample associated with this popup.
	 * @return The popup HTML.
	 */
	public String genPwyPopup(final Set<String> ids, final Organism organism, final Sample sample);
	
	/**
	 * Gets the list of available annotated map descriptors.
	 * @return A list of annotated map descriptors.
	 */
	public List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors();
	
	/**
	 * Gets the list of available sample types.  The content of list is dependent on the access level of the user.
	 * @return The list.
	 */
	public List<Sample.DataType> getAvailableSampleTypes();
	
	/**
	 * Gets directions (i.e. retrosynthetic pathway routes) between the source and destination compounds.
	 * @param organism The organism associated with this popup.
	 * @param cpdSrc The source compound.
	 * @param cpdDst The destination compound.
	 * @param mapTitle The title of the map.
	 * @param algorithm The algorithm used to find these routes.
	 * @return A list of pathways.
	 */
	public List<Pathway> getDirections(final Organism organism, final Compound cpdSrc, final Compound cpdDst, final String mapTitle, final Algorithm algorithm);
	
	/**
	 * Gets the host name for isolate organism links.  (Typically MicrobesOnline.)
	 * @return The host name.
	 */
	public String getIsolateHost();
	
	/**
	 * Gets a set of object implementing the HasMeasurements interface and their associated measurements for a given sample.
	 * @param sample The sample
	 * @return The set of objects.
	 */
	public Set<? extends HasMeasurements> getSample(final Sample sample);
	
	/**
	 * Gets the host name for metagenome organism links.  (Typically MetaMicrobesOnline.)
	 * @return The host name.
	 */
	public String getMetagenomeHost();
	
	/**
	 * Gets a set of reactions for a particular organism.  If the sample is specified, measurements are associated with those reactions.
	 * @param ids The reaction ids.
	 * @param organism The organism.
	 * @param sample The sample.
	 * @return The set of reactions.
	 */
	public Set<Reaction> getReactions(final Set<String> ids, final Organism organism, final Sample sample);
	
	/**
	 * Gets a pathway for a particular organism. 
	 * //TODO what if there's a sample to associate with it? Modify the map images somehow?
	 *        Pass that data to MicrobesOnline onClick?
	 */
	public Set<Pathway> getPathways(final Set<String> ids, final Organism organism, final Sample sample);
	
	/**
	 * Gets the set of reactions possible for an organism.
	 * @param organism The organism.
	 * @return The set of reactions.
	 */
	public Set<Reaction> getRxnsForOrganism(final Organism organism);
	
	/**
	 * Gets the user logged in for the current session.
	 * @return The user.
	 */
	public User getLoggedInUser();
	
	/**
	 * Logs out theuser loggedin for the current session.
	 */
	public void logOutUser();
	
	/**
	 * Gets the set of compounds visible on a map with a given mapId, used to populate the compound search box.
	 * @param mapId The map id.
	 * @return The set of compounds.
	 */
	public Set<Compound> populateCompoundSearch(final String mapId);
		
	/**
	 * Gets the set of genes available for an organism.
	 * @param organism The organism.
	 * @return The set of genes.
	 */
	public Set<Gene> populateLocusSearch(final Organism organism);
	
	/**
	 * Gets the set of reactions visible on a map with a given map Id, used to populate the search box.
	 * @param mapId The map id.
	 * @return The set of reactions.
	 */
	public Set<Reaction> populateReactionSearch(final String mapId);
	
	/**
	 * Gets the list of available organisms with samples available for a given data type.  The content of list is dependent on the access level of the user.
	 * @param dataType The data type.
	 * @return The list of organisms, in alphabetical order by species name, with user-uploaded (session) organisms listed first.
	 */
	public List<Organism> populateOrganisms(final Sample.DataType dataType);
	
	/**
	 * Gets the list of available samples for a given organism.  The content of list is dependent on the access level of the user.
	 * @param organism The organism.
	 * @return The list of samples.
	 */
	public List<Sample> populateSamples(final Organism organism);
	
	/**
	 * Gets a Metabolic model with a given id.
	 * @param modelId The model id
	 * @return The metabolic model
	 */
	public MetabolicModel getMetabolicModel(final String modelId);
		
	/**
	 * //TODO
	 * This is kind of a dummy rpc call for now that will return an OverlayData based on the given String.
	 * In reality, this will (eventually) encompass some kind of web service call that will fetch data to be overlayed on the map and
	 * presented to the user. Whether or not this makes sense to store as an Experiment or Sample or whatever (instead of the
	 * haphazard OverlayData model) will be explored in more detail later.
	 * @param text
	 * @return The OverlayData
	 */
	public Set<OverlayDataGroup> getOverlayData(final String text);
	
	/**
	 * Retrieves a set of OverlayDataGroups from the named service, with the given parameters. If there is a failure somewhere, then it
	 * returns an empty set.
	 * @param serviceName the name of the service to invoke.
	 * @param parameters the Map of parameters (key = parameter name, value = parameter value)
	 * @return a Set of OverlayDataGroup, which might be empty if no data is found.
	 */
	public Set<OverlayDataGroup> getOverlayDataFromService(final ExternalDataService service);
	
	/**
	 * Gets a Map containing information about available data services.
	 * @return a Map of data service information. Keys are the service name, and values are a list of parameter names for that service.
	 */
	public List<ExternalDataService> populateDataServices();
	
	public Organism getOrganismForTaxId(final String taxId);
	
	public GlammState getStateFromHistoryToken(final String token) throws UnauthorizedException, GlammStateException;
	
	public List<KBWorkspaceData> populateWorkspaces();

	public List<KBWorkspaceObjectData> populateWorkspaceModels(final String workspace);
	
	public List<KBWorkspaceObjectData> populateWorkspaceFbas(final String workspace);
	
	public KBMetabolicModel getKBaseMetabolicModel(final String modelId, final String workspaceId) throws UnauthorizedException;

	public KBFBAResult getKBaseFBAResult(final String fbaId, final String workspaceId) throws UnauthorizedException;
	
	public DeploymentDomain getDeploymentDomain();
	
	/**
	 * A debug rpc call that contacts the server without invoking the database.
	 * @return a success or fail String.
	 */
	public String nonDBTest();
}
