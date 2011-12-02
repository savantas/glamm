package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.ExperimentUploadEvent;
import gov.lbl.glamm.client.events.SamplePickedEvent;
import gov.lbl.glamm.client.events.ViewResizedEvent;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.Sample.DataType;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.RequestParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Presenter for viewing the set of experiments available to an organism.  Provides a mechanism for designating
 * a subset of experiments, allowing the user to rapidly cycle through experiments by clicking arrow buttons.
 * This is ideal for exploring series of experiments.
 * @author jtbates
 *
 */
public class ExperimentPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Adds a data type choice to the view.
		 * @param caption The caption for this data type choice.
		 * @param isDefault Flag indicating whether or not this is the default choice.
		 * @return
		 */
		public HasClickHandlers		addDataTypeChoice(final String caption, final boolean isDefault);
		
		/**
		 * Clears all data type choices from the view
		 */
		public void					clearDataTypeChoices();
		
		/**
		 * Gets the button that, when clicked, adds the selected sample to the view subset.
		 * @return The interface for the button.
		 */
		public HasClickHandlers		getAddToSubsetButton();
		
		/**
		 * Gets the disclosure panel.
		 * @return The disclosure panel.
		 */
		public DisclosurePanel		getDisclosurePanel();
		
		/**
		 * Gets the button that, when clicked, downloads the selected sample.
		 * @return The interface for the button.
		 */
		public HasClickHandlers 	getDownloadButton();
		
		/**
		 * Gets the panel that holds the experiment table and button panel.
		 * @return The panel.
		 */
		public Panel				getExperimentPanel();
		
		/**
		 * Gets the experiment suggest box.
		 * @return The suggest box.
		 */
		public SuggestBox			getExperimentSuggestBox();
		
		/**
		 * Gets the experiment table.
		 * @return The table.
		 */
		public CellTable<Sample>	getExperimentTable();
		
		/**
		 * Gets the button that, when clicked, displays the next sample in the view subset.
		 * @return The interface for the button.
		 */
		public HasClickHandlers		getNextSampleButton();
		
		/**
		 * Gets the button that, when clicked, displays the previous sample in the view subset.
		 * @return The interface for the button.
		 */
		public HasClickHandlers		getPrevSampleButton();
		
		/**
		 * Gets the button that, when clicked, removes the selected sample from the view subset.
		 * @return The interface for that button.
		 */
		public HasClickHandlers		getRemoveFromSubsetButton();
		
		/**
		 * Gets the button that, when clicked, removes all of the samples from the view subset.
		 * @return The interface for that button.
		 */
		public HasClickHandlers 	getResetSubsetButton();
		
		/**
		 * Gets the status label.
		 * @return THe label.
		 */
		public Label				getStatusLabel();
		
		/**
		 * Gets the button that, when clicked, allows the user to upload an experiment sample when no other
		 * experiments are available for the selected organism.
		 * @return The interface for that button.
		 */
		public Button				getStatusUploadButton();
		
		/**
		 * Gets the button that, when clicked, allows the user to upload an experiment sample for the 
		 * selected organism.
		 * @return The interface for that button.
		 */
		public HasClickHandlers 	getUploadButton();
		
		/**
		 * Gets the button that, when clicked, displays the data for the experiment selected in the view subset.
		 * @return The interface for that button.
		 */
		public HasClickHandlers		getViewExperimentButton();
		
		/**
		 * Gets the panel that holds the view subset table and button panel.
		 * @return The panel.
		 */
		public Panel				getViewSubsetPanel();
		
		/**
		 * Gets the view subset table.
		 * @return The table.
		 */
		public CellTable<Sample>	getViewSubsetTable();
		
		/**
		 * Maximizes the view.
		 */
		public void					maximize();
		
		/**
		 * Minimizes the view.
		 */
		public void					minimize();
	}

	private static enum State {
		
		NO_ORGANISM_SELECTED("Please select an organism."),
		POPULATING("Populating..."),
		NO_EXPERIMENTS("There are no experiments for the selected organism."),
		HAS_EXPERIMENTS("");
		
		private String statusText;
		
		private State(final String statusText) {
			this.statusText = statusText;
		}
		
		String getStatusText() {
			return statusText;
		}
	}

	// DataType.NONE should be displayed first
	private static final DataType[] dataTypeDisplayOrder = new DataType[]{ 
		DataType.RNA, 
		DataType.FITNESS, 
		DataType.PROTEIN, 
		DataType.RNASEQ, 
		DataType.SESSION
	};
	
	private static final String NO_EXPERIMENT_SELECTED = "No experiment selected";
	private static final String ACTION_DOWNLOAD_EXPERIMENT	= "downloadExperiment";
	private static final Map<Sample.DataType, String> dataType2Caption = new HashMap<Sample.DataType, String>();
	static {
		dataType2Caption.put(Sample.DataType.NONE, "Show all experiments");
		dataType2Caption.put(Sample.DataType.FITNESS, "Show only fitness experiments");
		dataType2Caption.put(Sample.DataType.PROTEIN, "Show only proteomics experiments");
		dataType2Caption.put(Sample.DataType.RNA, "Show only mRNA experiments");
		dataType2Caption.put(Sample.DataType.RNASEQ, "Show only RNASeq experiments");
		dataType2Caption.put(Sample.DataType.SESSION, "Show only uploaded experiments");
	}

	private GlammServiceAsync rpc = null;
	private View view = null;
	private SimpleEventBus eventBus = null;

	private ListDataProvider<Sample> experimentDataProvider = null;
	private ListDataProvider<Sample> viewSubsetDataProvider = null;

	private MultiWordSuggestOracle suggestOracle = null;
	private Map<String, Sample> summary2Sample = null;

	private Sample experimentTableSelection = null;
	private Sample viewSubsetTableSelection = null;

	private Organism organism = null;

	private Map<DataType, List<Sample>> dataType2Samples;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public ExperimentPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		experimentDataProvider = new ListDataProvider<Sample>(Sample.KEY_PROVIDER);
		viewSubsetDataProvider = new ListDataProvider<Sample>(Sample.KEY_PROVIDER);

		suggestOracle = (MultiWordSuggestOracle) view.getExperimentSuggestBox().getSuggestOracle();
		summary2Sample = new HashMap<String, Sample>();
		
		dataType2Samples = new HashMap<DataType, List<Sample>>();

		initTable(view.getExperimentTable(), experimentDataProvider);
		initTable(view.getViewSubsetTable(), viewSubsetDataProvider);

		setViewState(State.NO_ORGANISM_SELECTED);

		bindView();
	}

	private void addDataTypeChoice(final Sample.DataType dataType, final boolean isDefault) {
		final String caption = dataType2Caption.get(dataType);

		HasClickHandlers dataTypeChoice = view.addDataTypeChoice(caption, isDefault);

		dataTypeChoice.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				filterSamples(dataType);
			}
		});
	}

	private void addSamples(final List<Sample> allSamples) {
		
		// hash samples by data type
		dataType2Samples.clear();
		dataType2Samples.put(DataType.NONE, allSamples);
		
		for(Sample sample : allSamples) {
			List<Sample> samples = dataType2Samples.get(sample.getDataType());
			if(samples == null) {
				samples = new ArrayList<Sample>();
				dataType2Samples.put(sample.getDataType(), samples);
			}
			samples.add(sample);
		}
		
		// add data type choices to view
		view.clearDataTypeChoices();
		addDataTypeChoice(DataType.NONE, true);
		for(DataType dataType : dataTypeDisplayOrder) {
			if(dataType2Samples.get(dataType) != null)
				addDataTypeChoice(dataType, false);
		}
		
		filterSamples(DataType.NONE);
	}

	private void filterSamples(final DataType dataType) {
		
		((MultiWordSuggestOracle) view.getExperimentSuggestBox().getSuggestOracle()).clear();
		experimentDataProvider.getList().clear();
		viewSubsetDataProvider.getList().clear();
		
		List<Sample> samples = dataType2Samples.get(dataType);
		List<Sample> dpList = experimentDataProvider.getList();
		for(Sample sample : samples) {
			String summary = sample.getSummary();
			dpList.add(sample);
			suggestOracle.add(summary);
			summary2Sample.put(summary, sample);
		}
		view.getExperimentTable().setVisibleRange(0, samples.size());
	}
	
	private void initTable(CellTable<Sample> table, ListDataProvider<Sample> dataProvider) {

		if(table == null || dataProvider == null)
			return;

		// initialize table columns
		TextColumn<Sample> expIdColumn = new TextColumn<Sample>() {
			@Override
			public String getValue(Sample sample) {
				return sample.getExperimentId();
			}
		};

		TextColumn<Sample> sampleIdColumn = new TextColumn<Sample>() {
			@Override
			public String getValue(Sample sample) {
				return sample.getSampleId();
			}
		};

		TextColumn<Sample> stressColumn = new TextColumn<Sample>() {
			@Override
			public String getValue(Sample sample) {
				return sample.getStress();
			}
		};

		TextColumn<Sample> treatmentColumn = new TextColumn<Sample>() {
			@Override
			public String getValue(Sample sample) {
				return sample.getTreatment() + sample.getUnitString();
			}
		};

		TextColumn<Sample> controlColumn = new TextColumn<Sample>() {
			@Override
			public String getValue(Sample sample) {
				return sample.getControl() + sample.getUnitString();
			}
		};

		// add columns to table
		table.addColumn(expIdColumn, "Experiment");
		table.addColumn(sampleIdColumn, "Sample");
		table.addColumn(stressColumn, "Stress");
		table.addColumn(treatmentColumn, "Treatment");
		table.addColumn(controlColumn, "Control");

		// set the data provider
		dataProvider.addDataDisplay(table);

		// add a selection model
		final SingleSelectionModel<Sample> selectionModel = new SingleSelectionModel<Sample>(Sample.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
	}

	private void bindView() {

		final SingleSelectionModel<? super Sample> experimentTableSelectionModel = (SingleSelectionModel<? super Sample>) view.getExperimentTable().getSelectionModel();
		experimentTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				experimentTableSelection = (Sample) experimentTableSelectionModel.getSelectedObject();
			}
		});

		final SingleSelectionModel<? super Sample> viewSubsetTableSelectionModel = (SingleSelectionModel<? super Sample>) view.getViewSubsetTable().getSelectionModel();
		viewSubsetTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				viewSubsetTableSelection = (Sample) viewSubsetTableSelectionModel.getSelectedObject();	
			}
		});

		view.getAddToSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Sample> dpList = viewSubsetDataProvider.getList();
				if(experimentTableSelection != null && !dpList.contains(experimentTableSelection)) {
					viewSubsetDataProvider.getList().add(experimentTableSelection);
					setViewSubsetVisible(true);
				}
			}
		});

		view.getDownloadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(experimentTableSelection != null) {

					UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
					urlBuilder.setParameter("action", ACTION_DOWNLOAD_EXPERIMENT);
					urlBuilder.setPath("glammServlet");
					urlBuilder.setParameter(RequestParameters.EXPERIMENT.toString(), experimentTableSelection.getExperimentId());
					urlBuilder.setParameter(RequestParameters.SAMPLE.toString(), experimentTableSelection.getSampleId());
					Window.open(urlBuilder.buildString(), "", "menubar=no,location=no,resizable=no,scrollbars=no,status=no,toolbar=false,width=0,height=0");
				}
			}
		});

		view.getStatusUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ExperimentUploadEvent(organism, ExperimentUploadEvent.Action.REQUEST));
			}
		});

		view.getUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ExperimentUploadEvent(organism, ExperimentUploadEvent.Action.REQUEST));
			}
		});

		view.getNextSampleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Sample> dpList = viewSubsetDataProvider.getList();
				if(dpList != null && !dpList.isEmpty()) {
					Sample sample = null;
					if(viewSubsetTableSelection == null)
						sample = dpList.get(0);
					else {
						int index = dpList.lastIndexOf(viewSubsetTableSelection) + 1;
						index = (index < dpList.size() ? index : index - dpList.size());
						sample = dpList.get(index);
					}
					view.getViewSubsetTable().getSelectionModel().setSelected(sample, true);
					view.getExperimentSuggestBox().setText(sample.getSummary());
					view.minimize();
					eventBus.fireEvent(new SamplePickedEvent(sample));
				}
			}
		});

		view.getPrevSampleButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Sample> dpList = viewSubsetDataProvider.getList();
				if(dpList != null && !dpList.isEmpty()) {
					Sample sample = null;
					if(viewSubsetTableSelection == null)
						sample = dpList.get(0);
					else {
						int index = dpList.lastIndexOf(viewSubsetTableSelection) - 1;
						index = (index >= 0 ? index : index + dpList.size());
						sample = dpList.get(index);
					}
					view.getViewSubsetTable().getSelectionModel().setSelected(sample, true);
					view.getExperimentSuggestBox().setText(sample.getSummary());
					view.minimize();
					eventBus.fireEvent(new SamplePickedEvent(sample));
				}
			}
		});

		view.getRemoveFromSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Sample> dpList = viewSubsetDataProvider.getList();
				if(viewSubsetTableSelection != null) {
					dpList.remove(viewSubsetTableSelection);
					view.getExperimentSuggestBox().setText(NO_EXPERIMENT_SELECTED);
					setViewSubsetVisible(dpList.size() > 0);

					// reset map if there are no experiments in the view subset
					if(dpList.size() <= 0)
						eventBus.fireEvent(new SamplePickedEvent(null));

				}
			}
		});

		view.getResetSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viewSubsetDataProvider.getList().clear();
				setViewSubsetVisible(false);
				view.getExperimentSuggestBox().setText(NO_EXPERIMENT_SELECTED);
				view.minimize();
				eventBus.fireEvent(new SamplePickedEvent(null));
			}
		});

		view.getViewExperimentButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(viewSubsetTableSelection != null) {
					view.getExperimentSuggestBox().setText(viewSubsetTableSelection.getSummary());
					view.minimize();
					eventBus.fireEvent(new SamplePickedEvent(viewSubsetTableSelection));
				}
			}
		});

		view.getExperimentSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Suggestion suggestion = event.getSelectedItem();
				String summary = suggestion.getReplacementString();

				if(summary == null)
					return;

				Sample sample = summary2Sample.get(summary);

				if(sample == null)
					return;

				List<Sample> dpList = viewSubsetDataProvider.getList();
				if(!dpList.contains(sample)) {
					viewSubsetDataProvider.getList().add(sample);
					setViewSubsetVisible(true);
				}

				view.getViewSubsetTable().getSelectionModel().setSelected(sample, true);
				view.getExperimentSuggestBox().setText(sample.getSummary());
				view.minimize();
				eventBus.fireEvent(new SamplePickedEvent(sample));

			}
		});

		view.getDisclosurePanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});

		view.getDisclosurePanel().addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});
	}

	private void clear() {
		((MultiWordSuggestOracle) view.getExperimentSuggestBox().getSuggestOracle()).clear();
		view.getExperimentSuggestBox().setText(NO_EXPERIMENT_SELECTED);
		experimentDataProvider.getList().clear();
		viewSubsetDataProvider.getList().clear();
		setViewState(State.NO_ORGANISM_SELECTED);
		setViewSubsetVisible(false);
	}

	/**
	 * Clears the suggest box.
	 */
	public void clearSuggestBox() {
		view.getExperimentSuggestBox().setText("");
	}

	/**
	 * Populates the view with experiments for the selected organism.
	 */
	public void populate() {
		clear();

		if(organism == null || organism.isGlobalMap())
			return;

		setViewState(State.POPULATING);

		rpc.populateSamples(organism.getTaxonomyId(), new AsyncCallback<List<Sample>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateSamples");	
				setViewState(State.NO_ORGANISM_SELECTED);
			}

			@Override
			public void onSuccess(List<Sample> result) {
				if(result == null) {
					setViewState(State.NO_EXPERIMENTS);
					return;
				}
				addSamples(result);
				setViewState(State.HAS_EXPERIMENTS);
			}
		});
	}

	/**
	 * Sets the selected organism.
	 * @param organism The organism.
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;
		populate();
	}

	private void setViewState(final State state) {
		
		view.getStatusLabel().setText(state.getStatusText());
		
		switch(state) {
		default:
		case NO_ORGANISM_SELECTED:
			view.getExperimentPanel().setVisible(false);
			view.getStatusLabel().setVisible(true);
			view.getStatusUploadButton().setVisible(false);
			view.getExperimentSuggestBox().setText(NO_EXPERIMENT_SELECTED);
			break;
		case POPULATING:
			view.getExperimentPanel().setVisible(false);
			view.getStatusLabel().setVisible(true);
			view.getStatusUploadButton().setVisible(false);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		case NO_EXPERIMENTS:
			view.getExperimentPanel().setVisible(false);
			view.getStatusLabel().setVisible(true);
			view.getStatusUploadButton().setVisible(true);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		case HAS_EXPERIMENTS:
			view.getExperimentPanel().setVisible(true);
			view.getStatusLabel().setVisible(false);
			view.getStatusUploadButton().setVisible(false);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		}
	}

	private void setViewSubsetVisible(final boolean visible) {
		view.getViewSubsetPanel().setVisible(visible);
	}

}
