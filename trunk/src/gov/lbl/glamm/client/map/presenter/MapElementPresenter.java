package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.AnnotatedMapData;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Sample;

import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presenter for the map element popups.  This will probably be deprecated soon.
 * @author jtbates
 *
 */
public class MapElementPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Hides the popup.
		 */
		public void hidePopup();
		
		/**
		 * Kills the popup.
		 */
		public void killPopup();
		
		/**
		 * Shows the popup at a specific position with content.
		 * @param content The content.
		 * @param left The position of the left edge of the popup.
		 * @param top The position of the top edge of the popup.
		 */
		public void showPopup(String content, int left, int top);
	}
	
	private GlammServiceAsync rpc = null;
	private View view = null;
	
	private Organism organism = null;
	@SuppressWarnings("unused")
	private Sample sample = null;
	
	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 */
	public MapElementPresenter(final GlammServiceAsync rpc, final View view) {
		this.rpc = rpc;
		this.view = view;
		this.organism = Organism.globalMap();
		this.sample = null;
	}
	
	/**
	 * Hides the popup.
	 */
	public void hidePopup() {
		view.hidePopup();
	}
	
	/**
	 * Kills the popup.
	 */
	public void killPopup() {
		view.killPopup();
	}
	
	/**
	 * Sets the organism for this popup.
	 * @param organism The organism.
	 */
	public void setOrganism(Organism organism) {
		this.organism = organism;
	}
	
	/**
	 * Sets the sample for this popup.
	 * @param sample The sample.
	 */
	public void setSample(Sample sample) {
		this.sample = sample;
	}
		
	/**
	 * Shows the popup.
	 * @param elementClass The AnnotatedMapData.ElementClass of the target of this popup.
	 * @param ids The set of ids from which the content of this popup is derived.
	 * @param clientX The client X position of this popup.
	 * @param clientY The client Y position of this popup.
	 */
	public void showPopup(final AnnotatedMapData.ElementClass elementClass, final Set<String> ids, final int clientX, final int clientY) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: generating map element popup.");
			}

			@Override
			public void onSuccess(String result) {
				view.showPopup(result, clientX, clientY);
			}
		};
		
		final String loadingMsg = "<html>Loading...</html>";
		
		if(elementClass.equals(AnnotatedMapData.ElementClass.CPD)) {
			view.showPopup(loadingMsg, clientX, clientY);
			rpc.genCpdPopup(ids, organism, callback);
		}
/* Now done through PwyPopupPresenter --Bill */
//		else if(elementClass.equals(AnnotatedMapData.ElementClass.MAP)) {
//			view.showPopup(loadingMsg, clientX, clientY);
//			rpc.genPwyPopup(ids, organism, sample, callback);
//		}
		
	}
}
