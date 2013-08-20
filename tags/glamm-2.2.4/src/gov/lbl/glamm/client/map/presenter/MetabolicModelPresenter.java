package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.FBAChooserEvent;
import gov.lbl.glamm.client.map.events.FBAResultLoadedEvent;
import gov.lbl.glamm.client.map.events.LoadingEvent;
import gov.lbl.glamm.client.map.events.MetabolicModelChooserEvent;
import gov.lbl.glamm.client.map.events.MetabolicModelLoadedEvent;
import gov.lbl.glamm.client.map.events.ViewResizedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;
import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * A presenter for dealing with metabolic models. This allows the user to either select a model from a list, or upload one.
 * @author wjriehl
 *
 */
public class MetabolicModelPresenter {

	public interface View {
		public DisclosurePanel getDisclosurePanel();
		
		// Disclosure preview widgets
		public TextBox getModelTextBox();
		public HasClickHandlers getNextModelButton();
		public HasClickHandlers getPrevModelButton();
		public Panel getFBATitlePanel();
		public TextBox getFBATextBox();
		public HasClickHandlers getNextFBAButton();
		public HasClickHandlers getPrevFBAButton();

		// Model panel widgets
		public CheckBox getShowAllCheckBox();
		public HasClickHandlers getModelChooserButton();
		public Panel getModelTablePanel();
		public DataGrid<KBWorkspaceObjectData> getModelTable();
		public HasClickHandlers getViewModelButton();
		public HasClickHandlers getViewModelDetailsButton();
		public HasClickHandlers getRemoveModelButton();
		public HasClickHandlers getClearModelsButton();
		
		// FBA panel widgets
		public Panel getFBAPanel();
		public HasClickHandlers getFBAChooserButton();
		public Panel getFBATablePanel();
		public DataGrid<KBWorkspaceObjectData> getFBATable();
		public HasClickHandlers getViewFBAButton();
		public HasClickHandlers getViewFBADetailsButton();
		public HasClickHandlers getRemoveFBAButton();
		public HasClickHandlers getClearFBAButton();
	}

	private static enum State {
		NO_MODELS_LOADED("No models loaded"),
		MODEL_SELECTED(""),
		MODELS_LOADED("No model selected"),

		
		NO_FBAS_LOADED("No results loaded"),
		FBA_SELECTED(""),
		FBA_LOADED("No results selected");
		
		private String statusText;
		
		private State(final String statusText) {
			this.statusText = statusText;
		}
		
		String getStatusText() {
			return statusText;
		}
	}
	
//	private static final String TEXT_NO_MODEL_LOADED = "No model loaded";
	
	private static final String TEXT_ID_COL = "Id";
	private static final String TEXT_WORKSPACE_COL = "Workspace";
	private static final String TEXT_OWNER_COL = "Owner";
	private static final String TEXT_DATE_COL = "Mod. Date";
	
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;

	private ListDataProvider<KBWorkspaceObjectData> modelDataProvider;
	private ListDataProvider<KBWorkspaceObjectData> fbaDataProvider;
	
	private KBWorkspaceObjectData modelTableSelection = null;
	private KBWorkspaceObjectData fbaTableSelection = null;
	
	public MetabolicModelPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		
		modelDataProvider = new ListDataProvider<KBWorkspaceObjectData>();
		fbaDataProvider = new ListDataProvider<KBWorkspaceObjectData>();
		
		initTable(view.getModelTable(), modelDataProvider);
		initTable(view.getFBATable(), fbaDataProvider);

		bindView();

		setState(State.NO_MODELS_LOADED);
		setState(State.NO_FBAS_LOADED);
	}
	
	private void initTable(DataGrid<KBWorkspaceObjectData> table, ListDataProvider<KBWorkspaceObjectData> dataProvider) {
		// populate the view with the current user's stuff.
		// columns: id, owner, command, mod date
		TextColumn<KBWorkspaceObjectData> idColumn = new TextColumn<KBWorkspaceObjectData>() {
			@Override
			public String getValue(KBWorkspaceObjectData metadata) {
				return metadata.getId();
			}
		};
		
		TextColumn<KBWorkspaceObjectData> workspaceColumn = new TextColumn<KBWorkspaceObjectData>() {
			@Override
			public String getValue(KBWorkspaceObjectData metadata) {
				return metadata.getWorkspace();
			}
		};

		TextColumn<KBWorkspaceObjectData> ownerColumn = new TextColumn<KBWorkspaceObjectData>() {
			@Override
			public String getValue(KBWorkspaceObjectData metadata) {
				return metadata.getOwner();
			}
		};
	
		TextColumn<KBWorkspaceObjectData> modDateColumn = new TextColumn<KBWorkspaceObjectData>() {
			@Override
			public String getValue(KBWorkspaceObjectData metadata) {
				return metadata.getModDate();
			}
		};
		
		// add columns to the table
		table.addColumn(idColumn, TEXT_ID_COL);
		table.addColumn(workspaceColumn, TEXT_WORKSPACE_COL);
		table.addColumn(ownerColumn, TEXT_OWNER_COL);
		table.addColumn(modDateColumn, TEXT_DATE_COL);
		
		// add data provider
		dataProvider.addDataDisplay(table);

		// add a selection model
		final SingleSelectionModel<KBWorkspaceObjectData> selectionModel = new SingleSelectionModel<KBWorkspaceObjectData>(KBWorkspaceObjectData.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
	
	}
	
	private void bindView() {
		
		/**
		 * GENERAL DISCLOSURE PANEL BEHAVIOR.
		 */

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
		

		/**
		 * MODEL SECTION BUTTONS AND ASSORTED WIDGETRY.
		 */
		final SingleSelectionModel<? super KBWorkspaceObjectData> modelTableSelectionModel = (SingleSelectionModel<? super KBWorkspaceObjectData>) view.getModelTable().getSelectionModel();
		modelTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				modelTableSelection = (KBWorkspaceObjectData) modelTableSelectionModel.getSelectedObject();
			}
		});

		view.getModelChooserButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new MetabolicModelChooserEvent(MetabolicModelChooserEvent.Action.REQUEST));
			}
		});

		view.getClearModelsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new MetabolicModelLoadedEvent(null, false));
				modelDataProvider.getList().clear();
				fbaDataProvider.getList().clear();
				setState(State.NO_MODELS_LOADED);
			}
		});
		
		view.getNextModelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<KBWorkspaceObjectData> modelList = modelDataProvider.getList();
				if (modelList != null && !modelList.isEmpty()) {
					KBWorkspaceObjectData modelData = null;
					if (modelTableSelection == null)
						modelData = modelList.get(0);
					else {
						int index = modelList.lastIndexOf(modelTableSelection) + 1;
						index = (index < modelList.size() ? index : index - modelList.size());
						modelData = modelList.get(index);
					}
					if (modelData != modelTableSelection) {
						view.getModelTable().getSelectionModel().setSelected(modelData, true);
						loadModel(modelData, view.getShowAllCheckBox().getValue());
					}
				}
			}
		});
		
		view.getPrevModelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<KBWorkspaceObjectData> modelList = modelDataProvider.getList();
				if (modelList != null && !modelList.isEmpty()) {
					KBWorkspaceObjectData modelData = null;
					if (modelTableSelection == null)
						modelData = modelList.get(0);
					else {
						int index = modelList.lastIndexOf(modelTableSelection) - 1;
						index = (index >= 0 ? index : index + modelList.size());
						modelData = modelList.get(index);
					}
					if (modelData != modelTableSelection) {
						view.getModelTable().getSelectionModel().setSelected(modelData, true);
						loadModel(modelData, view.getShowAllCheckBox().getValue());
					}
				}
			}
		});
		
		view.getViewModelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadModel(modelTableSelection, view.getShowAllCheckBox().getValue());
			}
		});
		
		view.getRemoveModelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (modelTableSelection != null) {
					modelDataProvider.getList().remove(modelTableSelection);
					modelTableSelection = null;
				}
			}
		});
		
		view.getViewModelDetailsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Model viewer link goes **here**");
			}
		});
		
		/**
		 * FBA SECTION BUTTONS AND ASSORTED WIDGETRY.
		 */
		
		final SingleSelectionModel<? super KBWorkspaceObjectData> fbaTableSelectionModel = (SingleSelectionModel<? super KBWorkspaceObjectData>) view.getFBATable().getSelectionModel();
		fbaTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				fbaTableSelection = (KBWorkspaceObjectData) fbaTableSelectionModel.getSelectedObject();
			}
		});

		view.getFBAChooserButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new FBAChooserEvent(FBAChooserEvent.Action.REQUEST));
			}
		});

		view.getViewFBAButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadFBAResults(fbaTableSelection, view.getShowAllCheckBox().getValue());
			}
		});
		
		view.getViewFBADetailsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Model & FBA view link goes **here**");
			}
		});
		
		view.getRemoveFBAButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (fbaTableSelection != null) {
					fbaDataProvider.getList().remove(fbaTableSelection);
					fbaTableSelection = null;
				}
			}
		});
		
		view.getClearFBAButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fbaDataProvider.getList().clear();
				setState(State.NO_FBAS_LOADED);
			}
		});
		
		view.getPrevFBAButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<KBWorkspaceObjectData> fbaList = fbaDataProvider.getList();
				if (fbaList != null && !fbaList.isEmpty()) {
					KBWorkspaceObjectData fbaData = null;
					if (fbaTableSelection == null)
						fbaData = fbaList.get(0);
					else {
						int index = fbaList.lastIndexOf(fbaTableSelection) - 1;
						index = (index >= 0 ? index : index + fbaList.size());
						fbaData = fbaList.get(index);
					}
					if (fbaData != fbaTableSelection) {
						view.getFBATable().getSelectionModel().setSelected(fbaData, true);
						loadFBAResults(fbaData, view.getShowAllCheckBox().getValue());
					}
				}
			}
		});
		
		view.getNextFBAButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<KBWorkspaceObjectData> fbaList = fbaDataProvider.getList();
				if (fbaList != null && !fbaList.isEmpty()) {
					KBWorkspaceObjectData fbaData = null;
					if (fbaTableSelection == null)
						fbaData = fbaList.get(0);
					else {
						int index = fbaList.lastIndexOf(fbaTableSelection) + 1;
						index = (index < fbaList.size() ? index : index - fbaList.size());
						fbaData = fbaList.get(index);
					}
					if (fbaData != fbaTableSelection) {
						view.getFBATable().getSelectionModel().setSelected(fbaData, true);
						loadFBAResults(fbaData, view.getShowAllCheckBox().getValue());
					}
				}
			}
		});

	}

	public void loadModel(KBWorkspaceObjectData modelData, boolean showAllRxns) {
		if (modelData == null || modelData.getId() == null || modelData.getWorkspace() == null)
			return;
		
		eventBus.fireEvent(new LoadingEvent(false));
		rpc.getKBaseMetabolicModel(modelData.getId(), modelData.getWorkspace(), new AsyncCallback<KBMetabolicModel>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getKBaseMetabolicModel");
				setState(State.NO_MODELS_LOADED);
				eventBus.fireEvent(new LoadingEvent(true));
			}

			@Override
			public void onSuccess(KBMetabolicModel model) {
				//fire it off to the rest of the app.
				eventBus.fireEvent(new MetabolicModelLoadedEvent(model, view.getShowAllCheckBox().getValue()));
				if (model == null || model.getId().length() == 0) {
					Window.alert("Sorry, unable to display that model. Please try again.\nIf this problem persists, please contact the site administrator.");
				}
				else
					setState(State.MODEL_SELECTED);
				eventBus.fireEvent(new LoadingEvent(true));
			}
		});
	}

	public void loadFBAResults(KBWorkspaceObjectData fbaData, boolean showAllRxns) {
		if (fbaData == null || fbaData.getId() == null || fbaData.getWorkspace() == null)
			return;
		
		eventBus.fireEvent(new LoadingEvent(false));
		rpc.getKBaseFBAResult(fbaData.getId(), fbaData.getWorkspace(), new AsyncCallback<KBFBAResult>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getKBaseFBAResult");
				setState(State.NO_FBAS_LOADED);
				eventBus.fireEvent(new LoadingEvent(true));
			}

			@Override
			public void onSuccess(KBFBAResult fba) {
				//fire it off to the rest of the app.
				if (fba == null || fba.getId().length() == 0) {
					Window.alert("Sorry, unable to display that FBA result. Please try again.\n If this problem persists, please contact the site administrator.");
				}
				else {
					setState(State.FBA_SELECTED);
					view.getFBATextBox().setText(fba.getId() + " - " + fba.getWorkspace() + " - Biomass: " + fba.getObjective());
					eventBus.fireEvent(new FBAResultLoadedEvent(fba, view.getShowAllCheckBox().getValue()));
				}
				eventBus.fireEvent(new LoadingEvent(true));
			}
			
		});
	}
	
	private void setState(State state) {
		switch (state) {
			default:
				break;

			case NO_MODELS_LOADED :
				// model panel shouldn't be there.
				// model text box should say something appropriate.
				view.getModelTextBox().setText(state.getStatusText());
				view.getModelTablePanel().setVisible(false);
				break;
			
			case MODELS_LOADED :
				// model panel should be there.
				// model text box should say none selected
				view.getModelTablePanel().setVisible(true);
				view.getModelTextBox().setText(state.getStatusText());
				break;

			case MODEL_SELECTED :
				// model text box should say model selected
				view.getModelTablePanel().setVisible(true);
				view.getModelTextBox().setText(modelTableSelection.getId() + " - " + modelTableSelection.getWorkspace());
				break;
			
			case NO_FBAS_LOADED :
				// fba panel shouldn't be there.
				// fba text box should say something approrpriate
				view.getFBATablePanel().setVisible(false);
				view.getFBATextBox().setText(state.getStatusText());
				break;
			
			case FBA_LOADED :
				// fba panel should be there.
				// fba text box should say none selected
				view.getFBATablePanel().setVisible(true);
				view.getFBATextBox().setText(state.getStatusText());
				break;
			
			case FBA_SELECTED :
				// fba text box should say what fba's selected.
				view.getFBATablePanel().setVisible(true);
				view.getFBATextBox().setText(fbaTableSelection.getId() + " - " + fbaTableSelection.getWorkspace());
				break;
				
		}
		eventBus.fireEvent(new ViewResizedEvent());
	}
	
	public void setModelData(KBWorkspaceObjectData modelData) {
		if (modelData == null)
			return;
		
		modelDataProvider.getList().clear();
		modelDataProvider.getList().add(modelData);
		
		modelTableSelection = modelDataProvider.getList().get(0);
		
		setState(State.MODEL_SELECTED);
	}
	
	public void setFBAResultData(KBWorkspaceObjectData fbaResultData) {
		if (fbaResultData == null)
			return;
		
		fbaDataProvider.getList().clear();
		fbaDataProvider.getList().add(fbaResultData);
		
		fbaTableSelection = fbaDataProvider.getList().get(0);
		
		setState(State.FBA_SELECTED);
	}

	public void populateModels(List<KBWorkspaceObjectData> modelInfo) {
		modelDataProvider.getList().clear();
		modelDataProvider.getList().addAll(modelInfo);

		setState(State.MODELS_LOADED);
	}
	
	public void populateFbaResults(List<KBWorkspaceObjectData> fbaInfo) {
		fbaDataProvider.getList().clear();
		fbaDataProvider.getList().addAll(fbaInfo);

		setState(State.FBA_LOADED);
	}
	
	/**
	 * Should be invoked when a user logs in or out.
	 * In either case, it should check whether the current user has access to currently loaded models or FBA results.
	 */
	public void processUserChange() {
		/* Do a lookup (getModel or get FBA result) on each item in order.
		 * Keep the keepable ones, remove, but tag the non-keepable ones.
		 * If any were removed from the list, alert the user.	
		 */
		if (!modelDataProvider.getList().isEmpty()) {
			List<KBWorkspaceObjectData> modelData = new ArrayList<KBWorkspaceObjectData>();
			modelData.addAll(modelDataProvider.getList());

			eventBus.fireEvent(new LoadingEvent(false));
			rpc.getObjectsWithValidPermissions(modelData, new AsyncCallback<List<KBWorkspaceObjectData>>() {
				@Override
				public void onFailure(Throwable caught) {
					eventBus.fireEvent(new LoadingEvent(true));
					Window.alert("Sorry, an error occurred while checking model\nviewing permissions");
				}
	
				@Override
				public void onSuccess(List<KBWorkspaceObjectData> validData) {
					if (validData.size() < modelDataProvider.getList().size()) {
						Window.alert("Changing login status has removed access to some models.\nResetting view...");
						modelDataProvider.getList().clear();
						if (validData.size() > 0) {
							modelDataProvider.getList().addAll(validData);
						}
						else
							setState(State.NO_MODELS_LOADED);
						eventBus.fireEvent(new MetabolicModelLoadedEvent(null));
					}
					eventBus.fireEvent(new LoadingEvent(true));					
				}
			});
		}
		
		if (!fbaDataProvider.getList().isEmpty()) {
			List<KBWorkspaceObjectData> fbaData = new ArrayList<KBWorkspaceObjectData>();
			fbaData.addAll(fbaDataProvider.getList());

			eventBus.fireEvent(new LoadingEvent(false));
			rpc.getObjectsWithValidPermissions(fbaDataProvider.getList(), new AsyncCallback<List<KBWorkspaceObjectData>>() {
				@Override
				public void onFailure(Throwable caught) {
					eventBus.fireEvent(new LoadingEvent(true));					
					Window.alert("Sorry, an error occurred while checking fba\nresult viewing permissions");
				}
	
				@Override
				public void onSuccess(List<KBWorkspaceObjectData> validData) {
					if (validData.size() < fbaDataProvider.getList().size()) {
						Window.alert("Changing login status has removed access to some FBA results.\nResetting view...");
						fbaDataProvider.getList().clear();
						if (validData.size() > 0) {
							fbaDataProvider.getList().addAll(validData);
							setState(State.FBA_LOADED);
						}
						eventBus.fireEvent(new FBAResultLoadedEvent(null));
					}
					eventBus.fireEvent(new LoadingEvent(true));					
				}
				
			});
		}
	}
}
