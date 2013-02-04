package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.MetabolicModelChooserPresenter;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MetabolicModelChooserView extends Composite implements MetabolicModelChooserPresenter.View {

	private static final String TEXT_LOAD_SUBSET_BTN = "Load Models";
	private static final String TEXT_ADD_SUBSET_BTN = "Add models to subset";
	private static final String TEXT_REM_SUBSET_BTN = "Remove selected from subset";
	private static final String TEXT_CLEAR_SUBSET_BTN = "Clear models from subset";
	private static final String TEXT_CANCEL_BTN = "Cancel";
	
	private static final String TEXT_WORKSPACE_LBL = "Choose a workspace: ";
	private static final String TEXT_EMPTY_WORKSPACE_LBL = "No models found in workspace!";
	private static final String TEXT_EMPTY_SUBSET_LBL = "No models selected!";
	
	private DecoratedPopupPanel popupPanel;
	
	private Label emptyWorkspaceTableLabel;
	private Label emptySubsetTableLabel;
	
	private Button loadSubsetButton;
	private Button addSubsetButton;
	private Button removeSubsetButton;
	private Button clearSubsetButton;
	private Button cancelButton;
	private Label workspaceLabel;
	private ListBox workspaceListBox;
	private DataGrid<KBWorkspaceObjectData> workspaceTable;
	private DataGrid<KBWorkspaceObjectData> subsetTable;
	
	private HorizontalPanel workspaceChooserPanel;
	private HorizontalPanel subsetButtonPanel;
	private HorizontalPanel loadCancelPanel;
	private VerticalPanel mainPanel;
	
	public MetabolicModelChooserView() {
		loadSubsetButton = new Button(TEXT_LOAD_SUBSET_BTN);
		addSubsetButton = new Button(TEXT_ADD_SUBSET_BTN);
		removeSubsetButton = new Button(TEXT_REM_SUBSET_BTN);
		clearSubsetButton = new Button(TEXT_CLEAR_SUBSET_BTN);
		cancelButton = new Button(TEXT_CANCEL_BTN);
		
		workspaceLabel = new Label(TEXT_WORKSPACE_LBL);
		emptyWorkspaceTableLabel = new Label(TEXT_EMPTY_WORKSPACE_LBL);
		emptySubsetTableLabel = new Label(TEXT_EMPTY_SUBSET_LBL);
		
		workspaceChooserPanel = new HorizontalPanel();
		subsetButtonPanel = new HorizontalPanel();
		loadCancelPanel = new HorizontalPanel();
		
		workspaceListBox = new ListBox();
		workspaceTable = new DataGrid<KBWorkspaceObjectData>();
		subsetTable = new DataGrid<KBWorkspaceObjectData>();
		
		popupPanel = new DecoratedPopupPanel(false, true);
		
		mainPanel = new VerticalPanel();
		
		init();
	}
	
	private void init() {
		workspaceTable.setSize("45em", "10em");
		workspaceTable.setEmptyTableWidget(emptyWorkspaceTableLabel);
		
		subsetTable.setSize("45em", "10em");
		subsetTable.setEmptyTableWidget(emptySubsetTableLabel);
		
		workspaceChooserPanel.add(workspaceLabel);
		workspaceChooserPanel.add(workspaceListBox);
		
		subsetButtonPanel.add(addSubsetButton);
		subsetButtonPanel.add(clearSubsetButton);
		
		loadCancelPanel.add(loadSubsetButton);
		loadCancelPanel.add(removeSubsetButton);
		loadCancelPanel.add(cancelButton);
		
		mainPanel.add(workspaceChooserPanel);
		mainPanel.add(workspaceTable);
		mainPanel.add(subsetButtonPanel);
		mainPanel.add(subsetTable);
		mainPanel.add(loadCancelPanel);

		mainPanel.setStylePrimaryName("glamm-picker");
		popupPanel.add(mainPanel);
	}
	
	@Override
	public Button getLoadSubsetButton() {
		return loadSubsetButton;
	}

	@Override
	public Button getAddToSubsetButton() {
		return addSubsetButton;
	}

	@Override
	public Button getRemoveFromSubsetButton() {
		return removeSubsetButton;
	}

	@Override
	public Button getClearSubsetButton() {
		return clearSubsetButton;
	}

	@Override
	public Button getCancelButton() {
		return cancelButton;
	}

	@Override
	public ListBox getWorkspaceListBox() {
		return workspaceListBox;
	}

	@Override
	public DataGrid<KBWorkspaceObjectData> getWorkspaceTable() {
		return workspaceTable;
	}

	@Override
	public DataGrid<KBWorkspaceObjectData> getSubsetTable() {
		return subsetTable;
	}

	@Override
	public PopupPanel getPopupPanel() {
		return popupPanel;
	}

	@Override
	public Label getEmptyWorkspaceTableLabel() {
		return emptyWorkspaceTableLabel;
	}
}