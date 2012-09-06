package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.CpdDstDisambiguatedEvent;
import gov.lbl.glamm.client.map.events.CpdSrcDisambiguatedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.Compound;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presenter used to disambiguate between two compounds with the same synonym.
 * @author jtbates
 *
 */
public class CpdDisambiguationPresenter {

	/**
	 * The interface to which CpdDisambiguationPresenter views must conform.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Adds a compound choice to the view.
		 * @param html The HTML describing the compound choice (usually a name, mass, formula, image.)
		 * @return The interface to which click handlers may be attached.
		 */
		public HasClickHandlers addCpdChoice(final String html);
		
		/**
		 * Clears the compound choices from the view.
		 */
		public void clearCpdChoices();
		
		/**
		 * Hides the view.
		 */
		public void hideView();
		
		/**
		 * Shows the view.
		 */
		public void showView();
	}

	private GlammServiceAsync rpc = null;
	private View view = null;
	private SimpleEventBus eventBus = null;

	private enum CpdType {
		CPD_SRC,
		CPD_DST;
	}

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public CpdDisambiguationPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
	}
	
	private void addCompounds(final Collection<Compound> compounds, final CpdType cpdType) {
		for(final Compound compound: compounds) {

			rpc.genCpdPopup(compound, null, new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					Window.alert("Remote procedure call failure: genCpdPopup");
				}

				@Override
				public void onSuccess(String result) {
					if(result != null && !result.isEmpty()) {
						HasClickHandlers cpdChoice = view.addCpdChoice(result);
						cpdChoice.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								if(cpdType == CpdType.CPD_SRC)
									eventBus.fireEvent(new CpdSrcDisambiguatedEvent(compound));
								else
									eventBus.fireEvent(new CpdDstDisambiguatedEvent(compound));
								view.hideView();
							}
						});
					}
				}
			});
		}
	}

	/**
	 * Adds source compounds to the view.
	 * @param compounds The collection of source compounds.
	 */
	public void addSrcCompounds(final Collection<Compound> compounds) {
		addCompounds(compounds, CpdType.CPD_SRC);
	}
	
	/**
	 * Adds destination compounds to the view.
	 * @param compounds The collection of destination compounds.
	 */
	public void addDstCompounds(final Collection<Compound> compounds) {
		addCompounds(compounds, CpdType.CPD_DST);
	}

	/**
	 * Clears the compounds from the view.
	 */
	public void clearCpdChoices() {
		view.clearCpdChoices();
	}

}
