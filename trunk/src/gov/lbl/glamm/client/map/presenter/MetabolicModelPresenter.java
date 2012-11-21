package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.LoadingEvent;
import gov.lbl.glamm.client.map.events.MetabolicModelLoadedEvent;
import gov.lbl.glamm.client.map.events.ViewResizedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.FluxExperiment;
import gov.lbl.glamm.shared.model.MetabolicModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A presenter for dealing with metabolic models. This allows the user to either select a model from a list, or upload one.
 * @author wjriehl
 *
 */
public class MetabolicModelPresenter {

	public interface View {
		public ListBox getModelListBox();
		public ListBox getFbaListBox();
		public Label getNoFbaLabel();
		
		public DisclosurePanel getDisclosurePanel();
		
		public HasClickHandlers getMediaButton();
		public HasClickHandlers getBiomassButton();
		public HasClickHandlers getModelButton();
		public HasClickHandlers getClearModelButton();
		
		public CheckBox getShowAllCheckBox();
		
		public TextBox getTitleTextBox();
		public TextBox getFbaTextBox();
		
		public Panel getFbaPanel();
		public Panel getFbaInfoPanel();
	}

	private static enum State {
		NO_MODEL_SELECTED("No model selected"),
		POPULATING_MODELS("Populating..."),
		POPULATING_FBA("Populating..."),
		NO_FBA("There are no experiments for the selected organism."),
		HAS_FBA(""),
		NO_MODELS("No models found"),
		MODEL_SELECTED(""),
		FBA_SELECTED("");
		
		private String statusText;
		
		private State(final String statusText) {
			this.statusText = statusText;
		}
		
		String getStatusText() {
			return statusText;
		}
	}
	
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	private Map<String, String> title2ModelId;
	private Map<String, String> title2FbaId;

	public MetabolicModelPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		
		title2ModelId = new HashMap<String, String>();
		
		title2FbaId = new HashMap<String, String>();
		bindView();
		init();
	}
	
	private void bindView() {
		view.getModelListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = view.getModelListBox().getSelectedIndex();
				String title = view.getModelListBox().getItemText(index);
				loadModelFromId(title2ModelId.get(title));
			}
		});
		
		view.getFbaListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = view.getFbaListBox().getSelectedIndex();
				String title = view.getFbaListBox().getItemText(index);
				loadFbaFromId(title2FbaId.get(title));
			}
		});
		
		view.getMediaButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				Window.alert("This will show a dialog with all medium components for this FBA run.");
				
			}
		});
		
		view.getBiomassButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				Window.alert("This will show a dialog with all biomass components for the model.");
			}
		});

		view.getModelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				Window.alert("This will show a dialog with a list of all components of the model,\nand a link to an external Model Viewer.");
			}
		});
		
		view.getClearModelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				eventBus.fireEvent(new MetabolicModelLoadedEvent(null, false));
				setState(State.NO_MODEL_SELECTED);
			}
		});
		
		/*
		 * Opening the disclosure panel triggers a resize event, to reorient the larger panel.
		 */
		view.getDisclosurePanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});
		
		/*
		 * Closing the disclosure panel also triggers a resize event to reorient the smaller panel.
		 */
		view.getDisclosurePanel().addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});

		view.getFbaPanel().setVisible(false);
	}

	/**
	 * Initializes presenter with models from the KBase CDM and user's document store (if logged in).
	 */
	private void init() {
		// Do stuff in order, async stuff at the end.
		populateModels();
	}
	
	/**
	 * Loads a model with the given id. If it is present and returned through the rpc call, then it is 
	 * used to fire a MetabolicModelLoadedEvent. Otherwise, an error is shown.
	 * @param id the ID for the MetabolicModel to load
	 */
	public void loadModelFromId(String id) {
		eventBus.fireEvent(new LoadingEvent(false));
		
		rpc.getMetabolicModel(id, new AsyncCallback<MetabolicModel>() {
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getMetabolicModel");
				setState(State.NO_MODEL_SELECTED);
			}
			
			public void onSuccess(MetabolicModel model) {
				eventBus.fireEvent(new MetabolicModelLoadedEvent(model, view.getShowAllCheckBox().getValue()));
				if (model.getModelId().length() == 0)
					setState(State.NO_MODEL_SELECTED);
				else
					setState(State.MODEL_SELECTED);
			}
		});
		
		eventBus.fireEvent(new LoadingEvent(true));
	}
	
	public void loadModel(MetabolicModel model, boolean showRxns) {
		if (model.getModelId().length() == 0) {
			// reset things.
			setState(State.NO_MODEL_SELECTED);
			view.getModelListBox().setSelectedIndex(0);
		} else {
			populateFbaResults(model.getModelId());
		}
	}
	
	public void loadFbaFromId(String id) {
		eventBus.fireEvent(new LoadingEvent(false));

		rpc.getFbaResults(id, new AsyncCallback<FluxExperiment>() {
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getFbaResults");
			}
			
			public void onSuccess(FluxExperiment exp) {
				
			}
		});
		
		eventBus.fireEvent(new LoadingEvent(true));
	}
	
	public void populateFbaResults(String modelId) {
		setState(State.POPULATING_FBA);
		rpc.populateFbaResults(modelId, new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				setState(State.NO_FBA);
				Window.alert("Remote procedure call failure: populateFbaResults");
			}

			@Override
			public void onSuccess(List<String> fbas) {
				if (fbas == null || fbas.isEmpty()) {
					setState(State.NO_FBA);
				} else {
					view.getFbaListBox().clear();
					for (String id : fbas) {
						view.getFbaListBox().addItem(id);
					}
					setState(State.HAS_FBA);
				}
			}
		});
	}
	
	/**
	 * Populates the ListBox for Metabolic Model selection.
	 * @param initialTitle
	 */
	public void populateModels() {
		setState(State.POPULATING_MODELS);

		rpc.populateMetabolicModels(new AsyncCallback<List<MetabolicModel>>() {
			@Override
			public void onFailure(Throwable caught) {
				setState(State.NO_MODELS);
				Window.alert("Remote procedure call failure: populateMetabolicModels");
			}

			@Override
			public void onSuccess(List<MetabolicModel> models) {
				title2ModelId.put("No model loaded", "");
				view.getModelListBox().addItem("No model loaded");
				
				title2ModelId.put("Test model", "1");
				view.getModelListBox().addItem("Test model");
				for (MetabolicModel model : models) {
					title2ModelId.put(model.getName(), model.getModelId());
					view.getModelListBox().addItem(model.getName());
				}
				setState(State.NO_MODEL_SELECTED);
			}
		});
	}
	
	private void setState(State state) {
		switch (state) {
			default:
				break;
			case NO_MODEL_SELECTED:
				view.getTitleTextBox().setText(state.getStatusText());
				view.getFbaInfoPanel().setVisible(false);
				view.getFbaPanel().setVisible(false);
				view.getModelListBox().setSelectedIndex(0);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
			case MODEL_SELECTED:
				view.getFbaInfoPanel().setVisible(false);
				view.getFbaPanel().setVisible(true);
				view.getTitleTextBox().setText(
					view.getModelListBox().getItemText(
							view.getModelListBox().getSelectedIndex()
					)
				);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
			case FBA_SELECTED:
				view.getFbaInfoPanel().setVisible(true);
				view.getFbaPanel().setVisible(true);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
			case POPULATING_MODELS:
				view.getTitleTextBox().setText(state.getStatusText());
				view.getFbaInfoPanel().setVisible(false);
				view.getFbaPanel().setVisible(false);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
			case POPULATING_FBA:
				view.getFbaTextBox().setText(state.getStatusText());
				view.getFbaInfoPanel().setVisible(false);
				view.getFbaPanel().setVisible(true);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
			case HAS_FBA:
				view.getNoFbaLabel().setVisible(false);
				view.getFbaListBox().setVisible(true);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
			case NO_FBA:
//				view.getFbaTextBox().setText(state.getStatusText());
				view.getNoFbaLabel().setVisible(true);
				view.getFbaListBox().setVisible(false);
				view.getFbaInfoPanel().setVisible(false);
				view.getFbaPanel().setVisible(true);
				eventBus.fireEvent(new ViewResizedEvent());
				break;
		}
	}
}
