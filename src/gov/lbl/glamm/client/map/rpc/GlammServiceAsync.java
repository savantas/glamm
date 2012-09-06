package gov.lbl.glamm.client.map.rpc;

import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.model.Algorithm;
import gov.lbl.glamm.shared.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.FluxExperiment;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous interface for GlammService.
 * @author jtbates
 * @see GlammService
 *
 */
public interface GlammServiceAsync {
	public void authenticateUser(final String userId, final String auth, AsyncCallback<User> callback);
	public void genCpdPopup(final Compound compound, final Organism organism, AsyncCallback<String> callback);
	public void genCpdPopup(final Set<String> ids, final Organism organism, AsyncCallback<String> callback);
	public void genPwyPopup(final Set<String> ids, final Organism organism, final Sample sample, AsyncCallback<String> callback);
	public void getAnnotatedMapDescriptors(AsyncCallback<List<AnnotatedMapDescriptor>> callback);
	public void getAvailableSampleTypes(AsyncCallback<List<Sample.DataType>> callback);
	public void getDirections(final Organism organism, final Compound cpdSrc, final Compound cpdDst, final String mapTitle, final Algorithm algorithm, AsyncCallback<List<Pathway>> callback);
	public void getIsolateHost(AsyncCallback<String> callback);
	public void getSample(final Sample sample, AsyncCallback<Set<? extends HasMeasurements>> callback);
	public void getMetagenomeHost(AsyncCallback<String> callback);
	public void getLoggedInUser(AsyncCallback<User> user);
	public void getReactions(final Set<String> ids, final Organism organism, final Sample sample, AsyncCallback<Set<Reaction>> callback);
	public void getPathways(final Set<String> ids, final Organism organism, final Sample sample, AsyncCallback<Set<Pathway>> callback);
	public void getRxnsForOrganism(final Organism organism, AsyncCallback<Set<Reaction>> callback);
	public void logOutUser(AsyncCallback<Void> callback);
	public void populateCompoundSearch(String mapId, AsyncCallback<Set<Compound>> callback);
	public void populateLocusSearch(final Organism organism, AsyncCallback<Set<Gene>> callback);
	public void populateOrganisms(final Sample.DataType dataType, AsyncCallback<List<Organism>> callback);
	public void populateReactionSearch(final String mapId, AsyncCallback<Set<Reaction>> callback);
	public void populateSamples(final Organism organism, AsyncCallback<List<Sample>> callback);
	public void getMetabolicModel(String modelId, AsyncCallback<MetabolicModel> callback);
	public void getFluxes(FluxExperiment exp, AsyncCallback<Set<Reaction>> callback);
	public void getOverlayData(String text, AsyncCallback<Set<OverlayDataGroup>> callback);
	public void getOverlayDataFromService(ExternalDataService service, AsyncCallback<Set<OverlayDataGroup>> callback);
	public void populateDataServices(AsyncCallback<List<ExternalDataService>> asyncCallback);
	public void getOrganismForTaxId(final String taxId, AsyncCallback<Organism> callback);
	
	public void nonDBTest(AsyncCallback<String> callback);
	public void getStateFromHistoryToken(String token, AsyncCallback<GlammState> asyncCallback);
	
}