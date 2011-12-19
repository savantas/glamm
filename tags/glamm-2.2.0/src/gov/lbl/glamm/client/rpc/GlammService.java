package gov.lbl.glamm.client.rpc;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;

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
	 * @param taxonomyId The taxonomy id for the organism associated with this popup.
	 * @return The popup HTML.
	 */
	public String genCpdPopup(Compound compound, String taxonomyId);
	
	/**
	 * Generates the HTML for a compound popup.
	 * @param ids The set of ids for the compounds in this popup.
	 * @param taxonomyId The taxonomy id for the organism associated with this popup.
	 * @return The popup HTML.
	 */
	public String genCpdPopup(Set<String> ids, String taxonomyId);
	
	/**
	 * Generates the HTML for a pathway popup - the popup will contain a link back to the MicrobesOnline pathway page,
	 * which will include the microarray data overlay for the specified experiment and sample.
	 * @param ids The set of ids for the pathways in this popup.
	 * @param taxonomyId The taxonomy id for the organism associated with this popup.
	 * @param experimentId The experiment id.
	 * @param sampleId The sample id.
	 * @return The popup HTML.
	 */
	public String genPwyPopup(Set<String> ids, String taxonomyId, String experimentId, String sampleId);
	
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
	 * @param taxonomyId The taxonomy id for the organism.
	 * @param cpdSrc The source compound.
	 * @param cpdDst The destination compound.
	 * @param mapTitle The title of the map.
	 * @param algorithm The algorithm used to find these routes.
	 * @return A list of pathways.
	 */
	public List<Pathway> getDirections(String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm);
	
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
	 * Gets the set of reactions possible for an organism with the given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The set of reactions.
	 */
	public Set<Reaction> getRxnsForOrganism(String taxonomyId);
	
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
	public Set<Compound> populateCompoundSearch(String mapId);
		
	/**
	 * Gets the set of genes available for an organism with a given taxonomy id.
	 * @param taxonomyId The taxonomy id.
	 * @return The set of genes.
	 */
	public Set<Gene> populateLocusSearch(String taxonomyId);
	
	/**
	 * Gets the set of reactions visible on a map with a given map Id, used to populate the search box.
	 * @param mapId The map id.
	 * @return The set of reactions.
	 */
	public Set<Reaction> populateReactionSearch(String mapId);
	
	/**
	 * Gets the list of available organisms with samples available for a given data type.  The content of list is dependent on the access level of the user.
	 * @param dataType The data type.
	 * @return The list of organisms, in alphabetical order by species name, with user-uploaded (session) organisms listed first.
	 */
	public List<Organism> populateOrganisms(Sample.DataType dataType);
	
	/**
	 * Gets the list of available samples for a given organism.  The content of list is dependent on the access level of the user.
	 * @param taxonomyId The taxonomy id of the organism.
	 * @return The list of samples.
	 */
	public List<Sample> populateSamples(String taxonomyId);
}