package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.NameTypeChangeEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for the control panel that allows the user to 
 * switch between displaying a long or short name. <br />
 * 
 * Communicates with the view via a local event bus.
 * Notifies the rest of the application of state change via application event bus.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class DisplayControlPresenter {
	public interface View {
		public void enableNameSelection();
		public void disableNameSelection();
		HasClickHandlers getLongNameSelector();
		HasClickHandlers getShortNameSelector();
	}

	private View view = null;
	private HandlerManager eventBus = null;

	public DisplayControlPresenter( final View view, HandlerManager eventBus ) {
		this.view = view;
		initView();

		this.eventBus = eventBus;
		this.eventBus.addHandler(DataChangedEvent.ASSOCIATED_TYPE, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				view.enableNameSelection();
			}
		});
	}

	protected void initView() {
		this.view.getLongNameSelector().addClickHandler( new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DisplayControlPresenter.this.eventBus
						.fireEvent( NameTypeChangeEvent.LONG_NAME_EVENT );
			}
		});
		this.view.getShortNameSelector().addClickHandler( new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DisplayControlPresenter.this.eventBus
						.fireEvent( NameTypeChangeEvent.SHORT_NAME_EVENT );
			}
		});
	}
}
