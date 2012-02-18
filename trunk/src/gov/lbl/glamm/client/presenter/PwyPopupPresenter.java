package gov.lbl.glamm.client.presenter;

import java.util.HashSet;
import java.util.Set;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/* TODO - This is a stub.  The goal is to generate pathway popups client-side, so we'll have access to the Pathway object,
 * so that we can manipulate it directly from the popup (e.g. add it to the Cart, etc.)  See the RxnPopupPresenter for an analogous approach.
 */

/**
 * Presenter for pathway popups.
 * @author jtbates
 *
 */
public class PwyPopupPresenter {
	
	private static enum State {
		LOADING,
		NO_PATHWAY,
		HAS_PATHWAY;
	}

	public interface View {
		
		/**
		 * Gets the add to cart button.
		 * @return The button.
		 */
		public Button getAddToCartButton();
	
		/**
		 * Gets the pathway image, if available.
		 * @return The image.
		 */
		public Image getImage();
		
		/**
		 * Gets the popup panel.
		 * @return The panel.
		 */
		public Panel getPanel();
		
		/**
		 * Gets the status label.
		 * @return The label.
		 */
		public Label getStatusLabel();
		
		/**
		 * Hides the popup.
		 */
		public void hidePopup();
		
		/**
		 * Kills the popup.
		 */
		public void killPopup();
		
		/**
		 * Shows the popup at a position in client space.
		 * @param left The client space x position.
		 * @param top The client space y position.
		 */
		public void showPopup(int left, int top);
		
		public HTML getPwyLinkHtml();
		
		public void updateImage(String imgUrl);
		
		public void resetPopupPosition();

	}
	
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	
	private Set<Pathway> pathways;
	private Organism organism;
	private Sample sample;
	private User user;
	
	private String host;
	
	/**
	 * Initializes the PwyPopupPresenter.
	 * 
	 * @param rpc
	 * @param view
	 * @param eventBus
	 */
	public PwyPopupPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
		
		this.pathways = new HashSet<Pathway>();
		this.organism = Organism.globalMap();
		this.sample = null;
		setUser(User.guestUser());
		
		loadHost();
	}
	
	/**
	 * Sets the current with our pathway(s) of interest.
	 * @param organism
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;
	}
	
	/**
	 * Sets the pathways to be displayed in the popup.
	 * @param pathways
	 */
	private void setPathways(final Set<Pathway> pathways) {
		if(!this.pathways.isEmpty())
			this.pathways.clear();
		this.pathways.addAll(pathways);
	}
	
	/**
	 * Sets the experimental data sample to be displayed.
	 * @param sample
	 */
	public void setSample(final Sample sample) {
		this.sample = sample;
	}
	
	/**
	 * Sets the current user to associate with this popup. If the user is logged in (not a guest), then an 
	 * "Add to Cart" button is displayed.
	 * @param user
	 */
	public void setUser(final User user) {
		this.user = user;
		if (this.user == null || this.user.isGuestUser())
			view.getAddToCartButton().setVisible(false);
		else
			view.getAddToCartButton().setVisible(true);
	}
	
	/**
	 * Sets the state of the popup viewer. Displays a "loading..." string, an 
	 * error if there's no pathway in the database, or populates the panel with
	 * pathway data if present.
	 * @param state
	 */
	private void setViewState(final State state) {
		switch(state) {

		case LOADING: {
			final String msg = "Loading...";
			view.getStatusLabel().setText(msg);
			view.getStatusLabel().setVisible(true);
			view.getPanel().setVisible(false);
			break;
		}

		case NO_PATHWAY: {
			final String msg = "Pathway not found.";
			view.getStatusLabel().setText(msg);
			view.getStatusLabel().setVisible(true);
			view.getPanel().setVisible(false);
			break;
		}

		case HAS_PATHWAY: {
			view.getStatusLabel().setVisible(false);
			view.getPanel().setVisible(true);
			view.resetPopupPosition();
			break;
		}
		}
	}
	
	/**
	 * Shows the popup. The presenter fetches the data from the database, formats it, and feeds it into the
	 * PwyPopupView.
	 * @param mapIds
	 * @param left
	 * @param top
	 */
	public void showPopup(final Set<String> mapIds, int left, int top) {
		setViewState(State.LOADING);
		view.showPopup(left, top);
		
		rpc.getPathways(mapIds, organism, sample, new AsyncCallback<Set<Pathway>>() {
			public void onFailure(Throwable caught) {
				// Show the RPC message to the user
				Window.alert("Remote procedure call failure: getPathways");
			}
			
			public void onSuccess(Set<Pathway> result) {
				setPathways(result);
				if(result == null)
					setViewState(State.NO_PATHWAY);
				else
				{
					clearPathwayPanel();
					addPathwaysToPanel();
					setViewState(State.HAS_PATHWAY);
				}
			}
		});
	}
	
	/**
	 * Clears the pathway panel.
	 */
	private void clearPathwayPanel() {
	}
	
	/**
	 * Creates a table of pathway sub-panels (for the case in which there are multiple pathways
	 * to show).
	 * 
	 * Each row will contain some data from the pathway, a link to the pathway in MicrobesOnline, an
	 * image of the pathway (constrained to some maximum size), and (if there are two or more rows)
	 * a checkbox to be used in conjunction with the Add to Cart button.
	 *
	 */
	private void addPathwaysToPanel() {
		if (!pathways.isEmpty()) {
			for (Pathway pwy : pathways) {
				String taxonomyId = organism == null ? null : organism.getTaxonomyId();
				String experimentId = sample == null ? null : sample.getExperimentId();
				String sampleId = sample == null ? null : sample.getSampleId();
				
				view.getPwyLinkHtml().setHTML(genKeggMapLink(pwy, taxonomyId, experimentId, sampleId));
				String imageUrl = genImgLink(pwy);
				view.updateImage(imageUrl);
			}
		}
	}

	/**
	 * 
	 * @param pwy
	 * @param taxonomyId
	 * @param experimentId
	 * @param sampleId
	 * @return
	 */
	private String genKeggMapLink(
			final Pathway pwy, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) { 
		String link = "";
		String mapTitle = pwy.getName();

		if(mapTitle != null && !mapTitle.isEmpty())
			link = "<a href=\"" + 
			genKeggMapUrl(pwy, taxonomyId, experimentId, sampleId) + 
			"\" target = \"_new\">" + 
			mapTitle + 
			"</a>";
		return link;
	}

	/**
	 * 
	 * @param pwy
	 * @param taxonomyId
	 * @param experimentId
	 * @param sampleId
	 * @return
	 */
	private String genKeggMapUrl(
			final Pathway pwy, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) { 

		String url = "";
		String mapId = pwy.getMapId();

		if(	experimentId != null && !experimentId.equals(Experiment.DEFAULT_EXPERIMENT_ID) &&
				sampleId != null && !sampleId.equals(Sample.DEFAULT_SAMPLE_ID) &&
				taxonomyId != null && !taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID)) {

			url = "http://" + host + "/cgi-bin/microarray/reportSet.cgi?disp=3&";
			url += "mapId=" + mapId;
			url += "&taxId=" + taxonomyId;
			url += "&expId=" + experimentId;
			url += "&setId=" + sampleId;
			url += "&z=0.5&n=-1";

		}
		else {

			url = "http://" +  host + "/cgi-bin/browseKegg?";
			url += "mapId=map" + mapId;
			url += taxonomyId != null && !taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID) ? "&taxId=" + taxonomyId : "";

		}
		return url;
	}

	/**
	 * 
	 * @param pwy
	 * @return
	 */
	private String genImgLink(final Pathway pwy) {
		String imgUrlString = "http://" + host + "/kegg/map" + pwy.getMapId() + ".png";
		return imgUrlString;
	}

	/**
	 * Loads the name of the host to be used in constructing URLs.
	 */
	private void loadHost() {
		rpc.getIsolateHost(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getIsolateHost");
			}

			@Override
			public void onSuccess(String result) {
				host = result;
			}
		});
	}

	/**
	 * Removes the popup from the screen.
	 */
	public void killPopup() {
		view.killPopup();
	}
	
}
