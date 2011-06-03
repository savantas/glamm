package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.CpdDstDisambiguatedEvent;
import gov.lbl.glamm.client.events.CpdSrcDisambiguatedEvent;
import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CpdDisambiguationPresenter {

	public interface View {
		public HasClickHandlers addCpdChoice(final String html);
		public void clearCpdChoices();
		public void hideView();
		public void showView();
	}

	private GlammServiceAsync rpc = null;
	private View view = null;
	private SimpleEventBus eventBus = null;

	private enum CpdType {
		CPD_SRC,
		CPD_DST;
	}


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

	public void addSrcCompounds(final Collection<Compound> compounds) {
		addCompounds(compounds, CpdType.CPD_SRC);
	}
	
	public void addDstCompounds(final Collection<Compound> compounds) {
		addCompounds(compounds, CpdType.CPD_DST);
	}

	public void clearCpdChoices() {
		view.clearCpdChoices();
	}

}
