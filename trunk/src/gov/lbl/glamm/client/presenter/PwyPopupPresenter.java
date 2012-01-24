package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Button;
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
		public Button getAddToCart();
	
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
	}
	
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	
	private Pathway pathway;
	private Organism organism;
	private Sample sample;
	private User user;
	
	public PwyPopupPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
		
		this.pathway = null;
		this.organism = Organism.globalMap();
		this.sample = null;
		this.user = User.guestUser();
	}
	
	public void setOrganism(final Organism organism) {
		this.organism = organism;
	}
	
	public void setPathway(final Pathway pathway) {
		this.pathway = pathway;
	}
	
	public void setSample(final Sample sample) {
		this.sample = sample;
	}
	
	public void setUser(final User user) {
		this.user = user;
	}
	
	private void setViewState(final State state) {
		switch(state) {
		case LOADING:
			break;
		default:
		case NO_PATHWAY:
			break;
		case HAS_PATHWAY:
			break;
		}
	}
	
}
