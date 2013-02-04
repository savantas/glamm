package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.MetabolicModelChooserEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceData;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class MetabolicModelChooserPresenter {

	public interface View {
		public Button getLoadSubsetButton();
		public Button getAddToSubsetButton();
		public Button getRemoveFromSubsetButton();
		public Button getClearSubsetButton();
		public Button getCancelButton();

		public ListBox getWorkspaceListBox(); // oracle?

		public DataGrid<KBWorkspaceObjectData> getWorkspaceTable();
		public DataGrid<KBWorkspaceObjectData> getSubsetTable();

		public PopupPanel getPopupPanel();
		
		public Label getEmptyWorkspaceTableLabel();
	}
	
	private static enum State {
		NO_WORKSPACES("No workspaces found"),
		NO_WORKSPACE_SELECTED("No workspace selected"),
		NO_MODELS("No models found in this workspace"),
		POPULATING_WORKSPACES("Populating..."),
		POPULATING_MODELS("Populating..."),
		HAS_MODELS("");
		
		private String statusText;
		private State(final String statusText) {
			this.statusText = statusText;
		}
		
		String getStatusText() {
			return statusText;
		}
	}
	
	private static final String TEXT_ID_COL = "Id";
	private static final String TEXT_OWNER_COL = "Owner";
	private static final String TEXT_COMMAND_COL = "Command";
	private static final String TEXT_DATE_COL = "Mod. Date";
	
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	private ListDataProvider<KBWorkspaceObjectData> workspaceDataProvider;
	private ListDataProvider<KBWorkspaceObjectData> subsetDataProvider;
	private List<KBWorkspaceData> workspaceList;
	
	private KBWorkspaceObjectData workspaceTableSelection = null;
	private KBWorkspaceObjectData subsetTableSelection = null;
	
	public MetabolicModelChooserPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		
		workspaceDataProvider = new ListDataProvider<KBWorkspaceObjectData>();
		subsetDataProvider = new ListDataProvider<KBWorkspaceObjectData>();
		workspaceList = new ArrayList<KBWorkspaceData>();
		
		initTable(view.getWorkspaceTable(), workspaceDataProvider);
		initTable(view.getSubsetTable(), subsetDataProvider);

		bindView();
	}
	
	private void bindView() {
		final SingleSelectionModel<? super KBWorkspaceObjectData> workspaceTableSelectionModel = (SingleSelectionModel<? super KBWorkspaceObjectData>) view.getWorkspaceTable().getSelectionModel();
		workspaceTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				workspaceTableSelection = (KBWorkspaceObjectData) workspaceTableSelectionModel.getSelectedObject();
			}
		});

		final SingleSelectionModel<? super KBWorkspaceObjectData> subsetTableSelectionModel = (SingleSelectionModel<? super KBWorkspaceObjectData>) view.getSubsetTable().getSelectionModel();
		subsetTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				subsetTableSelection = (KBWorkspaceObjectData) subsetTableSelectionModel.getSelectedObject();	
			}
		});

		// connect the plugging for view events and table rendering
		view.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.getPopupPanel().hide();
				clear();
			}
		});
		
		view.getLoadSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<KBWorkspaceObjectData> dataList = subsetDataProvider.getList();
				if (!dataList.isEmpty())
					eventBus.fireEvent(new MetabolicModelChooserEvent(MetabolicModelChooserEvent.Action.SELECT, dataList));
				clear();
				view.getPopupPanel().hide();
			}
		});
		
		view.getAddToSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// 1. Get the selected KBWorkspaceObjectData
				// 2. Append it to the other dataprovider's list.
				// 3. Done!
				List<KBWorkspaceObjectData> dataList = subsetDataProvider.getList();
				if (workspaceTableSelection != null && !dataList.contains(workspaceTableSelection)) {
					dataList.add(workspaceTableSelection);
					if (!view.getLoadSubsetButton().isEnabled())
						view.getLoadSubsetButton().setEnabled(true);
				}
			}
		});
		
		view.getRemoveFromSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<KBWorkspaceObjectData> dataList = subsetDataProvider.getList();
				if (subsetTableSelection != null && dataList.contains(subsetTableSelection)) {
					dataList.remove(subsetTableSelection);
					if (dataList.isEmpty())
						view.getLoadSubsetButton().setEnabled(false);
					clearSelections();
				}
			}
		});
		
		view.getClearSubsetButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				subsetDataProvider.getList().clear();
				view.getLoadSubsetButton().setEnabled(false);
				clearSelections();
			}
		});
		
		view.getWorkspaceListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				populateWorkspaceTable();
			}
		});
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
		
		TextColumn<KBWorkspaceObjectData> ownerColumn = new TextColumn<KBWorkspaceObjectData>() {
			@Override
			public String getValue(KBWorkspaceObjectData metadata) {
				return metadata.getOwner();
			}
		};
	
		TextColumn<KBWorkspaceObjectData> commandColumn = new TextColumn<KBWorkspaceObjectData>() {
			@Override
			public String getValue(KBWorkspaceObjectData metadata) {
				return metadata.getCommand();
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
		table.addColumn(ownerColumn, TEXT_OWNER_COL);
		table.addColumn(commandColumn, TEXT_COMMAND_COL);
		table.addColumn(modDateColumn, TEXT_DATE_COL);
		
		// add data provider
		dataProvider.addDataDisplay(table);

		// add a selection model
		final SingleSelectionModel<KBWorkspaceObjectData> selectionModel = new SingleSelectionModel<KBWorkspaceObjectData>(KBWorkspaceObjectData.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
	}
	
	public void showChooser() {
		clearSelections();
		
		populateWorkspaceList();
		view.getPopupPanel().center();
		view.getPopupPanel().show();
	}
	
	private void clear() {
		// clear all transient data from workspaces and the list of workspaces.
		clearSelections();
		
		workspaceList.clear();
		workspaceDataProvider.getList().clear();
		subsetDataProvider.getList().clear();
		
		view.getWorkspaceListBox().clear();
		view.getLoadSubsetButton().setEnabled(false);
	}
	
	private void clearSelections() {
		view.getWorkspaceTable().getSelectionModel().setSelected(workspaceTableSelection, false);
		view.getSubsetTable().getSelectionModel().setSelected(subsetTableSelection, false);
	}
	
	public void populateWorkspaceList() {
		clear();		
		setViewState(State.POPULATING_WORKSPACES);

		rpc.populateWorkspaces(new AsyncCallback<List<KBWorkspaceData>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: populateWorkspaces.\nUnable to load workspace information!");
				setViewState(State.NO_WORKSPACES);
			}

			@Override
			public void onSuccess(List<KBWorkspaceData> workspaceList) {
				if (workspaceList == null || workspaceList.size() == 0) {
					setViewState(State.NO_WORKSPACES);
					return;
				}
				view.getWorkspaceListBox().clear();
				addWorkspaces(workspaceList);
				setViewState(State.NO_WORKSPACE_SELECTED);
			}
		});
		
	}

	private void addWorkspaces(List<KBWorkspaceData> workspaceList) {
		this.workspaceList = workspaceList;
		
		ListBox listBox = view.getWorkspaceListBox();
		
		for (KBWorkspaceData workspace : workspaceList) {
			listBox.addItem(workspace.getId());
		}
		
		populateWorkspaceTable();
		
	}
	
	private void populateWorkspaceTable() {
		workspaceDataProvider.getList().clear();
		setViewState(State.POPULATING_MODELS);
		
		String workspace = view.getWorkspaceListBox().getItemText(view.getWorkspaceListBox().getSelectedIndex());
		rpc.populateWorkspaceModels(workspace, new AsyncCallback<List<KBWorkspaceObjectData>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: populateWorkspaceModels.\nUnable to load model information from workspace!");
				setViewState(State.NO_MODELS);
			}

			@Override
			public void onSuccess(List<KBWorkspaceObjectData> result) {
				if (result == null || result.isEmpty())
					setViewState(State.NO_MODELS);
				else {
					workspaceDataProvider.getList().addAll(result);
					setViewState(State.HAS_MODELS);					
				}
			}
			
		});
	}
	
	private void setViewState(State state) {
		switch (state) {
			case POPULATING_WORKSPACES :
				view.getWorkspaceListBox().clear();
				view.getWorkspaceListBox().addItem(state.getStatusText());
				break;
			
			case NO_WORKSPACES : 
				view.getWorkspaceListBox().clear();
				view.getWorkspaceListBox().addItem(state.getStatusText());
				break;
				
			case NO_WORKSPACE_SELECTED :
				break;
				
			case NO_MODELS :
				view.getEmptyWorkspaceTableLabel().setText(state.getStatusText());
				break;
				
			case POPULATING_MODELS : 
				view.getEmptyWorkspaceTableLabel().setText(state.getStatusText());
				break;
				
			case HAS_MODELS :
				break;
				
			default :
				break;
		}
	}
	
}
