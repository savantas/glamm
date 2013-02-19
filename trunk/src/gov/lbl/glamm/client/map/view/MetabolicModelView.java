package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.MetabolicModelPresenter;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MetabolicModelView extends Composite implements MetabolicModelPresenter.View {

	private static final String TEXT_BROWSE		 	= "Browse";
	private static final String TEXT_NEXT_BUTTON 	= "<html>&rarr;</html>";
	private static final String TEXT_PREV_BUTTON 	= "<html>&larr;</html>";

	private static final String TEXT_MODEL 			= "Model: ";
	private static final String TEXT_MODELS 		= "Models:";
	private static final String TEXT_MODEL_CHOOSER 	= "Load models from workspaces";
	private static final String TEXT_MODEL_VIEW 	= "View selected model";
	private static final String TEXT_MODEL_DETAIL 	= "View details";
	private static final String TEXT_MODEL_REMOVE 	= "Remove model from view";
	private static final String TEXT_MODEL_CLEAR 	= "Clear models";
	private static final String TEXT_SHOW_ALL_RXNS 	= "Show all reactions?";

	private static final String TEXT_FBA 			= "FBA: ";
	private static final String TEXT_FBAS 			= "FBA Results:";
	private static final String TEXT_FBA_CHOOSER 	= "Load FBA results from workspaces";
	private static final String TEXT_FBA_VIEW 		= "view FBA";
	private static final String TEXT_FBA_DETAIL 	= "View details";
	private static final String TEXT_FBA_REMOVE 	= "Remove FBA from view";
	private static final String TEXT_FBA_CLEAR 		= "Clear FBA";
	
	// Main enclosing panels - has a title element with some text about the current model
	// and a disclosure panel for loading options
	// The mainPanel is structural and is held by the decoratorPanel
	private DecoratorPanel decoratorPanel;

	// mainPanel holds the disclosurePanel and titlePanel
	private VerticalPanel mainPanel;

	// The Title panel is separated into two rows - one for the Model and one for FBA results.
	private VerticalPanel titlePanel;
	// model
	private HorizontalPanel modelTitlePanel;
	private Label modelTitleLabel;
	private TextBox modelTextBox;
	private Button prevModelButton;
	private Button nextModelButton;
	// fba
	private HorizontalPanel fbaTitlePanel;
	private Label fbaTitleLabel;
	private TextBox fbaTextBox;
	private Button prevFbaButton;
	private Button nextFbaButton;
	

	// main disclosure panel - holds all the model lookup options as they appear.
	private DisclosurePanel disclosurePanel;
	private VerticalPanel disclosureContentPanel;

	// The bulk of the widget is separated into two parts - one for managing models and one for managing FBA data

	// Model panel data
	private VerticalPanel modelPanel;
	//    header
	private HorizontalPanel modelLabelPanel;
	private Label modelLabel;
	private Button modelChooserButton;
	//    panel
	private VerticalPanel modelTablePanel;
	private DataGrid<KBWorkspaceObjectData> modelTable;
	private HorizontalPanel modelButtonPanel;
	private Button viewModelButton;
	private Button viewModelDetailsButton;
	private Button removeModelButton;
	private Button clearModelsButton;
	
	// FBA panel data
	private VerticalPanel fbaPanel;
	//    header
	private HorizontalPanel fbaLabelPanel;
	private Label fbaLabel;
	private Button fbaChooserButton;
	//    panel
	private VerticalPanel fbaTablePanel;
	private DataGrid<KBWorkspaceObjectData> fbaTable;
	private HorizontalPanel fbaButtonPanel;
	private Button viewFbaButton;
	private Button viewFbaDetailsButton;
	private Button removeFbaButton;
	private Button clearFbasButton;
	
	private Grid titleGrid;
	
//	// The model panel has options for loading up models from KBase using a dropdown.
//	private ListBox	modelListBox;
	private CheckBox showAllRxnsBox;
	
	/**
	 * Constructor
	 */
	public MetabolicModelView() {
		// overall stuff
		decoratorPanel = new DecoratorPanel();
		mainPanel = new VerticalPanel();
		disclosurePanel = new DisclosurePanel(TEXT_BROWSE);
		disclosureContentPanel = new VerticalPanel();
		
		// title area
		// ----------
		titlePanel = new VerticalPanel();
		// model row
		modelTitlePanel = new HorizontalPanel();
		modelTitleLabel = new Label(TEXT_MODEL);
		modelTextBox = new TextBox();
		prevModelButton = new Button(TEXT_PREV_BUTTON);
		nextModelButton = new Button(TEXT_NEXT_BUTTON);
		// fba row
		fbaTitlePanel = new HorizontalPanel();
		fbaTitleLabel = new Label(TEXT_FBA);
		fbaTextBox = new TextBox();
		prevFbaButton = new Button(TEXT_PREV_BUTTON);
		nextFbaButton = new Button(TEXT_NEXT_BUTTON);
		
		// Main model panel
		modelPanel = new VerticalPanel();
		//    header
		modelLabelPanel = new HorizontalPanel();
		modelLabel = new Label(TEXT_MODELS);
		modelChooserButton = new Button(TEXT_MODEL_CHOOSER);
		//    panel
		modelTablePanel = new VerticalPanel();
		modelTable = new DataGrid<KBWorkspaceObjectData>();
		modelButtonPanel = new HorizontalPanel();
		viewModelButton = new Button(TEXT_MODEL_VIEW);
		viewModelDetailsButton = new Button(TEXT_MODEL_DETAIL);
		removeModelButton = new Button(TEXT_MODEL_REMOVE);
		clearModelsButton = new Button(TEXT_MODEL_CLEAR);
		
		// Main FBA panel
		fbaPanel = new VerticalPanel();
		//    header
		fbaLabelPanel = new HorizontalPanel();
		fbaLabel = new Label(TEXT_FBAS);
		fbaChooserButton = new Button(TEXT_FBA_CHOOSER);
		//    panel
		fbaTablePanel = new VerticalPanel();
		fbaTable = new DataGrid<KBWorkspaceObjectData>();
		fbaButtonPanel = new HorizontalPanel();
		viewFbaButton = new Button(TEXT_FBA_VIEW);
		viewFbaDetailsButton = new Button(TEXT_FBA_DETAIL);
		removeFbaButton = new Button(TEXT_FBA_REMOVE);
		clearFbasButton = new Button(TEXT_FBA_CLEAR);
		
		titleGrid = new Grid(2,4);
		
		showAllRxnsBox = new CheckBox(TEXT_SHOW_ALL_RXNS);
		init();
	}
	
	private void init() {
		titleGrid.setWidget(0, 0, modelTitleLabel);
		titleGrid.setWidget(0, 1, modelTextBox);
		titleGrid.setWidget(0, 2, prevModelButton);
		titleGrid.setWidget(0, 3, nextModelButton);
		
		titleGrid.setWidget(1, 0, fbaTitleLabel);
		titleGrid.setWidget(1, 1, fbaTextBox);
		titleGrid.setWidget(1, 2, prevFbaButton);
		titleGrid.setWidget(1, 3, nextFbaButton);
		
		// assemble the title panel
//		modelTitlePanel.add(modelTitleLabel);
//		modelTitlePanel.add(modelTextBox);
//		modelTitlePanel.add(prevModelButton);
//		modelTitlePanel.add(nextModelButton);
//		
//		fbaTitlePanel.add(fbaTitleLabel);
//		fbaTitlePanel.add(fbaTextBox);
//		fbaTitlePanel.add(prevFbaButton);
//		fbaTitlePanel.add(nextFbaButton);

//		titlePanel.add(modelTitlePanel);
//		titlePanel.add(fbaTitlePanel);

		titlePanel.add(titleGrid);
		
		modelLabelPanel.add(modelChooserButton);
		
		modelButtonPanel.add(viewModelButton);
		modelButtonPanel.add(viewModelDetailsButton);
		modelButtonPanel.add(removeModelButton);
		modelButtonPanel.add(clearModelsButton);
		
		modelTablePanel.add(modelLabel);
		modelTablePanel.add(modelTable);
		modelTablePanel.add(showAllRxnsBox);
		modelTablePanel.add(modelButtonPanel);

		modelPanel.add(modelLabelPanel);
		modelPanel.add(modelTablePanel);
		
		fbaLabelPanel.add(fbaChooserButton);
		
		fbaButtonPanel.add(viewFbaButton);
		fbaButtonPanel.add(viewFbaDetailsButton);
		fbaButtonPanel.add(removeFbaButton);
		fbaButtonPanel.add(clearFbasButton);

		fbaTablePanel.add(fbaLabel);
		fbaTablePanel.add(fbaTable);
		fbaTablePanel.add(fbaButtonPanel);
		
		fbaPanel.add(fbaLabelPanel);
		fbaPanel.add(fbaTablePanel);
		
		disclosureContentPanel.add(modelPanel);
		disclosureContentPanel.add(new HTML("<hr style=\"width: 100%\" />"));
		disclosureContentPanel.add(fbaPanel);
		disclosurePanel.add(disclosureContentPanel);

		mainPanel.add(titlePanel);
		mainPanel.add(disclosurePanel);
		decoratorPanel.add(mainPanel);

		modelTable.setSize("45em", "7em");
		fbaTable.setSize("45em", "7em");
		
		modelTextBox.setWidth("25em");
		fbaTextBox.setWidth("25em");
		
		mainPanel.setSpacing(1);
		mainPanel.setStylePrimaryName("glamm-picker");

		initWidget(decoratorPanel);
	}
	

	@Override
	public Panel getFBAPanel() {
		return fbaPanel;
	}

	@Override
	public TextBox getModelTextBox() {
		return modelTextBox;
	}

	@Override
	public TextBox getFBATextBox() {
		return fbaTextBox;
	}

	@Override
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	@Override
	public CheckBox getShowAllCheckBox() {
		return showAllRxnsBox;
	}

	@Override
	public HasClickHandlers getModelChooserButton() {
		return modelChooserButton;
	}

	@Override
	public HasClickHandlers getNextModelButton() {
		return nextModelButton;
	}

	@Override
	public HasClickHandlers getPrevModelButton() {
		return prevModelButton;
	}

	@Override
	public Panel getFBATitlePanel() {
		return fbaTitlePanel;
	}

	@Override
	public HasClickHandlers getNextFBAButton() {
		return nextFbaButton;
	}

	@Override
	public HasClickHandlers getPrevFBAButton() {
		return prevFbaButton;
	}

	@Override
	public Panel getModelTablePanel() {
		return modelTablePanel;
	}

	@Override
	public DataGrid<KBWorkspaceObjectData> getModelTable() {
		return modelTable;
	}

	@Override
	public HasClickHandlers getViewModelButton() {
		return viewModelButton;
	}

	@Override
	public HasClickHandlers getViewModelDetailsButton() {
		return viewModelDetailsButton;
	}

	@Override
	public HasClickHandlers getRemoveModelButton() {
		return removeModelButton;
	}

	@Override
	public HasClickHandlers getFBAChooserButton() {
		return fbaChooserButton;
	}

	@Override
	public Panel getFBATablePanel() {
		return fbaTablePanel;
	}

	@Override
	public DataGrid<KBWorkspaceObjectData> getFBATable() {
		return fbaTable;
	}

	@Override
	public HasClickHandlers getViewFBAButton() {
		return viewFbaButton;
	}

	@Override
	public HasClickHandlers getViewFBADetailsButton() {
		return viewFbaDetailsButton;
	}

	@Override
	public HasClickHandlers getRemoveFBAButton() {
		return removeFbaButton;
	}

	@Override
	public HasClickHandlers getClearFBAButton() {
		return clearFbasButton;
	}

	@Override
	public HasClickHandlers getClearModelsButton() {
		return clearModelsButton;
	}
}
