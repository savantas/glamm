package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.ExperimentUploadEvent;
import gov.lbl.glamm.client.events.SamplePickedEvent;
import gov.lbl.glamm.client.events.ViewResizedEvent;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ExperimentPresenter {

	public interface View {
		
		public static final String STRING_ADD_BUTTON 		= "Add to view subset";
		public static final String STRING_DISCLOSURE_PANEL	= "Browse";
		public static final String STRING_DOWNLOAD_BUTTON 	= "Download experiment";
		public static final String STRING_EXP_LABEL 		= "Experiment: ";
		public static final String STRING_NEXT_BUTTON		= "<html>&rarr;</html>";
		public static final String STRING_NO_EXPERIMENTS	= "No experiments for organism";
		public static final String STRING_NO_EXPERIMENT_SELECTED = "No experiment selected";
		public static final String STRING_POPULATING		= "Populating...";
		public static final String STRING_PREV_BUTTON		= "<html>&larr;</html>";
		public static final String STRING_REMOVE_BUTTON 	= "Remove from view subset";
		public static final String STRING_RESET_BUTTON 		= "Reset view subset";
		public static final String STRING_SELECT_ORGANISM	= "Please select an organism.";
		public static final String STRING_UPLOAD_BUTTON 	= "Upload experiment";
		public static final String STRING_VIEW_EXPERIMENT	= "View experiment";
		public static final String STRING_VIEW_LABEL 		= "View subset:";

		public HasClickHandlers		getAddToSubsetButton();
		public DisclosurePanel		getDisclosurePanel();
		public HasClickHandlers 	getDownloadButton();
		public Panel				getExperimentPanel();
		public SuggestBox			getExperimentSuggestBox();
		public CellTable<Sample>	getExperimentTable();
		public HasClickHandlers		getNextExperimentButton();
		public HasClickHandlers		getPrevExperimentButton();
		public HasClickHandlers		getRemoveFromSubsetButton();
		public HasClickHandlers 	getResetSubsetButton();
		public Label				getStatusLabel();
		public HasClickHandlers 	getUploadButton();
		public HasClickHandlers		getViewExperimentButton();
		public Panel				getViewSubsetPanel();
		public CellTable<Sample>	getViewSubsetTable();
		public void					maximize();
		public void					minimize();
	}

	public static enum State {
		NO_ORGANISM_SELECTED,
		POPULATING,
		NO_EXPERIMENTS,
		HAS_EXPERIMENTS;
	}
	
	private static final String ACTION_DOWNLOAD_EXPERIMENT	= "downloadExperiment";


	private GlammServiceAsync rpc = null;
	private View view = null;
	private SimpleEventBus eventBus = null;

	private ListDataProvider<Sample> experimentDataProvider = null;
	private ListDataProvider<Sample> viewSubsetDataProvider = null;

	private MultiWordSuggestOracle suggestOracle = null;
	private HashMap<String, Sample> summary2Sample = null;

	private Sample experimentTableSelection = null;
	private Sample viewSubsetTableSelection = null;

	private Organism organism = null;

	public ExperimentPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		experimentDataProvider = new ListDataProvider<Sample>(Sample.KEY_PROVIDER);
		viewSubsetDataProvider = new ListDataProvider<Sample>(Sample.KEY_PROVIDER);

		suggestOracle = (MultiWordSuggestOracle) view.getExperimentSuggestBox().getSuggestOracle();
		summary2Sample = new HashMap<String, Sample>();

		initTable(view.getExperimentTable(), experimentDataProvider);
		initTable(view.getViewSubsetTable(), viewSubsetDataProvider);

		setViewState(State.NO_ORGANISM_SELECTED);

		bindView();
	}

	private void addSamples(final ArrayList<Sample> samples) {
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
				return sample.getTreatment();
			}
		};

		TextColumn<Sample> controlColumn = new TextColumn<Sample>() {
			@Override
			public String getValue(Sample sample) {
				return sample.getControl();
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
					urlBuilder.setParameter(RequestHandler.PARAM_EXPERIMENT, experimentTableSelection.getExperimentId());
					urlBuilder.setParameter(RequestHandler.PARAM_SAMPLE, experimentTableSelection.getSampleId());
					urlBuilder.setParameter(RequestHandler.PARAM_TAXONOMY_ID, experimentTableSelection.getTaxonomyId());
					urlBuilder.setParameter(RequestHandler.PARAM_EXP_SOURCE, experimentTableSelection.getSource());

					Window.open(urlBuilder.buildString(), "", "menubar=no,location=no,resizable=no,scrollbars=no,status=no,toolbar=false,width=0,height=0");
				}
			}
		});

		view.getUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ExperimentUploadEvent(organism, ExperimentUploadEvent.Action.REQUEST));
			}
		});

		view.getNextExperimentButton().addClickHandler(new ClickHandler() {
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

		view.getPrevExperimentButton().addClickHandler(new ClickHandler() {
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
					view.getExperimentSuggestBox().setText(View.STRING_NO_EXPERIMENT_SELECTED);
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
				view.getExperimentSuggestBox().setText(View.STRING_NO_EXPERIMENT_SELECTED);
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
		view.getExperimentSuggestBox().setText(View.STRING_NO_EXPERIMENT_SELECTED);
		experimentDataProvider.getList().clear();
		viewSubsetDataProvider.getList().clear();
		setViewState(State.NO_ORGANISM_SELECTED);
		setViewSubsetVisible(false);
	}
	
	public void clearSuggestBox() {
		view.getExperimentSuggestBox().setText("");
	}

	public void populate() {
		clear();

		if(organism == null || organism.isGlobalMap())
			return;

		setViewState(State.POPULATING);

		rpc.populateSamples(organism.getTaxonomyId(), new AsyncCallback<ArrayList<Sample>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateSamples");	
				setViewState(State.NO_ORGANISM_SELECTED);
			}

			@Override
			public void onSuccess(ArrayList<Sample> result) {
				if(result == null) {
					setViewState(State.NO_EXPERIMENTS);
					return;
				}
				addSamples(result);
				setViewState(State.HAS_EXPERIMENTS);
			}
		});
	}
	
	public void setOrganism(final Organism organism) {
		this.organism = organism;
	}

	private void setViewState(final State state) {
		switch(state) {
		default:
		case NO_ORGANISM_SELECTED:
			view.getExperimentPanel().setVisible(false);
			view.getStatusLabel().setText(View.STRING_SELECT_ORGANISM);
			view.getStatusLabel().setVisible(true);
			view.getExperimentSuggestBox().setText(View.STRING_NO_EXPERIMENT_SELECTED);
			break;
		case POPULATING:
			view.getExperimentPanel().setVisible(false);
			view.getStatusLabel().setText(View.STRING_POPULATING);
			view.getStatusLabel().setVisible(true);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		case NO_EXPERIMENTS:
			view.getExperimentPanel().setVisible(false);
			view.getStatusLabel().setText(View.STRING_NO_EXPERIMENTS);
			view.getStatusLabel().setVisible(true);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		case HAS_EXPERIMENTS:
			view.getExperimentPanel().setVisible(true);
			view.getStatusLabel().setVisible(false);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		}
	}

	public void setViewSubsetVisible(final boolean visible) {
		view.getViewSubsetPanel().setVisible(visible);
	}

}
