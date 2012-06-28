package gov.lbl.glamm.client.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.lbl.glamm.client.events.LoadingEvent;
import gov.lbl.glamm.client.events.GroupDataLoadedEvent;
import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A presenter for the user to choose a data service and provide parameters. The data service should then be invoked, and the
 * response used to build a group data set, or to provide an error.
 * @author wjriehl
 *
 */
public class GroupDataServicePresenter {

	/**
	 * The view interface for this presenter.
	 * @author wjriehl
	 *
	 */
	public interface View {
		/**
		 * Gets the submit button for invoking a data service with given parameters.
		 * @return the submit Button
		 */
		public HasClickHandlers getSubmitButton();
		
		/**
		 * Gets the cancel button that should close the viewer.
		 * @return the cancel Button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets an input text box for parameters
		 * //TODO : this should return a List of text boxes (or better, a Map from 
		 * parameter name -> text box) for each service.
		 * @return a text box
		 */
		public TextBox getInputTextBox();
		
		/**
		 * Returns the list box that contains the available services.
		 * @return a ListBox with all services.
		 */
		public ListBox getServiceListBox();
		
		/**
		 * Tells the view to show itself.
		 */
		public void showView();
		
		/**
		 * Tells the view to hide itself, and reset fields if necessary.
		 */
		public void hideView();
	}
	
	private Map<String, List<String>> dataServices;
	private View view;
	private SimpleEventBus eventBus;
	private GlammServiceAsync rpc;
	
	public GroupDataServicePresenter (final GlammServiceAsync rpc, final SimpleEventBus eventBus, final View view) {
		this.eventBus = eventBus;
		this.view = view;
		this.rpc = rpc;
		
		bindView();
		populateServices(view.getServiceListBox());
	}

	/**
	 * Initializes the submit and cancel buttons and binds them to events.
	 */
	private void bindView() {
		view.getSubmitButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doServiceCall();
				view.hideView();
			}
		});
		
		view.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.hideView();
			}
		});
	}

	/**
	 * Invokes a data service call when the user hits the submit button.
	 * 
	 * If overlay data is returned, it is used sent to the rest of the program through a GroupDataLoadedEvent,
	 * otherwise, an alert window is displayed with an error or warning.
	 */
	protected void doServiceCall() {
		String serviceName = view.getServiceListBox().getItemText(view.getServiceListBox().getSelectedIndex());
		String paramValue = view.getInputTextBox().getText();
		
		final Map<String, String> parameters = new HashMap<String, String>();
		for (String paramName : dataServices.get(serviceName)) {
			parameters.put(paramName, paramValue);
		}
		
				
		eventBus.fireEvent(new LoadingEvent(false));
		rpc.getOverlayDataFromService(serviceName, parameters, new AsyncCallback<Set<OverlayDataGroup>>() {

			@Override
			public void onFailure(Throwable caught) {
				eventBus.fireEvent(new LoadingEvent(true));
				Window.alert("Remote procedure call failed: getOverlayData");
			}

			@Override
			public void onSuccess(Set<OverlayDataGroup> result) {
				if (result.size() == 0) {
					String errStr = "No Overlay Data found for '" + view.getServiceListBox().getItemText(view.getServiceListBox().getSelectedIndex()) + "'\nwith parameters:\n";
					for (String name : parameters.keySet())
						errStr += name + " = " + parameters.get(name);
				
					Window.alert(errStr);
				}
				else
					eventBus.fireEvent(new GroupDataLoadedEvent(result));
				
				eventBus.fireEvent(new LoadingEvent(true));
			}
			
		});
	}

	/**
	 * Populates the view's ListBox with information about the available services.
	 * @param serviceListBox
	 */
	private void populateServices(final ListBox serviceListBox) {
		eventBus.fireEvent(new LoadingEvent(false));
		rpc.populateDataServices(new AsyncCallback<Map<String, List<String>>>() {
			@Override
			public void onFailure(Throwable caught) {
				eventBus.fireEvent(new LoadingEvent(true));
				Window.alert("Remote procedure call failed: populateDataServices");
			}
			
			@Override
			public void onSuccess(Map<String, List<String>> result) {
				if (result.size() == 0) {
					System.out.println("No data services found.");
				}
				else {
					serviceListBox.clear();
					for (String name : result.keySet()) {
						serviceListBox.addItem(name);
					}
				}
				dataServices = result;
				eventBus.fireEvent(new LoadingEvent(true));
			}
		});
	}
}
