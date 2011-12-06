package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.presenter.ExperimentPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for experiment data filtering and selection.
 * @author jtbates
 *
 */
public class ExperimentView extends Composite 
	implements ExperimentPresenter.View {
	
	private static final String TEXT_ADD_BUTTON 		= "Add to view subset";
	private static final String TEXT_DISCLOSURE_PANEL	= "Browse";
	private static final String TEXT_DOWNLOAD_BUTTON 	= "Download experiment";
	private static final String TEXT_EXP_LABEL 			= "Experiment: ";
	private static final String TEXT_NEXT_BUTTON		= "<html>&rarr;</html>";
	private static final String TEXT_PREV_BUTTON		= "<html>&larr;</html>";
	private static final String TEXT_REMOVE_BUTTON 		= "Remove from view subset";
	private static final String TEXT_RESET_BUTTON 		= "Reset view subset";
	private static final String TEXT_UPLOAD_BUTTON 		= "Upload experiment";
	private static final String TEXT_VIEW_EXPERIMENT	= "View experiment";
	private static final String TEXT_VIEW_LABEL 		= "View subset:";
	
	private final String RB_GROUP = "EXPERIMENT_RB_GROUP";
		
	// the widget
	private DecoratorPanel		decoratorPanel;
	private VerticalPanel		mainPanel;
	
	// header
	private HorizontalPanel		headerPanel;
	private Label				experimentLabel;
	private SuggestBox			experimentSuggestBox;
	private Button				prevButton;
	private Button				nextButton;
	
	// disclosure panel
	private DisclosurePanel		disclosurePanel;
	private VerticalPanel		disclosurePanelContent;
	
	// status panel
	private HorizontalPanel		statusPanel;
	private Label				statusLabel;
	private Button				statusUploadButton;
	
	// experiment panel
	private VerticalPanel		radioButtonPanel;
	private VerticalPanel		experimentPanel;
	private ScrollPanel			experimentTableScrollPanel;
	private CellTable<Sample> 	experimentTable;
	private HorizontalPanel		expButtonPanel;
	private Button				addButton;
	private Button				uploadButton;
	private Button				downloadButton;
	
	private VerticalPanel		viewSubsetPanel;
	private Label				viewSubsetLabel;
	private ScrollPanel			viewSubsetTableScrollPanel;
	private CellTable<Sample> 	viewSubsetTable;
	private HorizontalPanel		viewButtonPanel;
	private Button				viewExperimentButton;
	private Button				removeButton;
	private Button				resetButton;
	
	/**
	 * Constructor
	 */
	public ExperimentView() {
		
		// the widget
		decoratorPanel 				= new DecoratorPanel();
		mainPanel 					= new VerticalPanel();
		
		// header
		headerPanel					= new HorizontalPanel();
		experimentLabel				= new Label(TEXT_EXP_LABEL);
		experimentSuggestBox 		= new SuggestBox();
		prevButton 					= new Button(TEXT_PREV_BUTTON);
		nextButton					= new Button(TEXT_NEXT_BUTTON);
		
		// disclosure panel
		disclosurePanel				= new DisclosurePanel(TEXT_DISCLOSURE_PANEL);
		disclosurePanelContent		= new VerticalPanel();
		statusLabel					= new Label();
		statusPanel					= new HorizontalPanel();
		statusUploadButton			= new Button(TEXT_UPLOAD_BUTTON);
		experimentPanel				= new VerticalPanel();
		radioButtonPanel				= new VerticalPanel();
		experimentTableScrollPanel	= new ScrollPanel();
		experimentTable				= new CellTable<Sample>();
		expButtonPanel				= new HorizontalPanel();
		addButton					= new Button(TEXT_ADD_BUTTON);
		uploadButton				= new Button(TEXT_UPLOAD_BUTTON);
		downloadButton				= new Button(TEXT_DOWNLOAD_BUTTON);
		
		viewSubsetPanel				= new VerticalPanel();
		viewSubsetLabel				= new Label(TEXT_VIEW_LABEL);
		viewSubsetTableScrollPanel 	= new ScrollPanel();
		viewSubsetTable				= new CellTable<Sample>();
		viewButtonPanel				= new HorizontalPanel();
		viewExperimentButton		= new Button(TEXT_VIEW_EXPERIMENT);
		removeButton				= new Button(TEXT_REMOVE_BUTTON);
		resetButton					= new Button(TEXT_RESET_BUTTON);
		
		init();
		
	}
	
	private void init() {
		
		// build header
		headerPanel.add(experimentLabel);
		headerPanel.add(experimentSuggestBox);
		headerPanel.add(prevButton);
		headerPanel.add(nextButton);
		
		// build disclosure panel
		experimentTableScrollPanel.add(experimentTable);
		
		expButtonPanel.add(addButton);
		expButtonPanel.add(uploadButton);
		expButtonPanel.add(downloadButton);
		
		viewSubsetPanel.add(viewSubsetLabel);
		viewSubsetPanel.add(viewSubsetTableScrollPanel);
		viewSubsetTableScrollPanel.add(viewSubsetTable);
		viewSubsetPanel.add(viewButtonPanel);
		viewButtonPanel.add(viewExperimentButton);
		viewButtonPanel.add(removeButton);
		viewButtonPanel.add(resetButton);
		
		experimentPanel.add(radioButtonPanel);
		experimentPanel.add(experimentTableScrollPanel);
		experimentPanel.add(expButtonPanel);
		experimentPanel.add(viewSubsetPanel);
		
		statusPanel.setSpacing(5);
		statusPanel.add(statusLabel);
		statusPanel.add(statusUploadButton);
		disclosurePanelContent.add(statusPanel);
		disclosurePanelContent.add(experimentPanel);
		disclosurePanel.setContent(disclosurePanelContent);
		
		// build widget
		decoratorPanel.add(mainPanel);
		mainPanel.add(headerPanel);
		mainPanel.add(disclosurePanel);
		
		// style widget
		mainPanel.setStylePrimaryName("glamm-picker");
		experimentSuggestBox.setWidth("40em");
		experimentTableScrollPanel.setSize("72em", "15em");
		viewSubsetTableScrollPanel.setSize("72em", "15em");
		experimentTable.setWidth("70em");
		viewSubsetTable.setWidth("70em");
		expButtonPanel.setSpacing(5);
		viewButtonPanel.setSpacing(5);
		
		initWidget(decoratorPanel);
		
	}
	
	@Override
	public HasClickHandlers addDataTypeChoice(final String caption, final boolean isDefault) {
		RadioButton dataTypeChoice = new RadioButton(RB_GROUP, caption);
		dataTypeChoice.setValue(isDefault);
		radioButtonPanel.add(dataTypeChoice);
		return dataTypeChoice;
	}
	
	@Override
	public void clearDataTypeChoices() {
		while(radioButtonPanel.getWidgetCount() > 0)
			radioButtonPanel.remove(0);
	}

	@Override
	public HasClickHandlers getAddToSubsetButton() {
		return addButton;
	}

	@Override
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}
	
	@Override
	public HasClickHandlers getDownloadButton() {
		return downloadButton;
	}
	
	@Override
	public Panel getExperimentPanel() {
		return experimentPanel;
	}

	@Override
	public SuggestBox getExperimentSuggestBox() {
		return experimentSuggestBox;
	}

	@Override
	public CellTable<Sample> getExperimentTable() {
		return experimentTable;
	}

	@Override
	public HasClickHandlers getNextSampleButton() {
		return nextButton;
	}

	@Override
	public HasClickHandlers getPrevSampleButton() {
		return prevButton;
	}

	@Override
	public HasClickHandlers getRemoveFromSubsetButton() {
		return removeButton;
	}

	@Override
	public HasClickHandlers getResetSubsetButton() {
		return resetButton;
	}
	
	@Override
	public Label getStatusLabel() {
		return statusLabel;
	}
	
	@Override
	public Button getStatusUploadButton() {
		return statusUploadButton;
	}

	@Override
	public HasClickHandlers getUploadButton() {
		return uploadButton;
	}
	
	@Override
	public HasClickHandlers getViewExperimentButton() {
		return viewExperimentButton;
	}
	
	@Override
	public Panel getViewSubsetPanel() {
		return viewSubsetPanel;
	}

	@Override
	public CellTable<Sample> getViewSubsetTable() {
		return viewSubsetTable;
	}

	@Override
	public void maximize() {
		disclosurePanel.setOpen(true);
	}

	@Override
	public void minimize() {
		disclosurePanel.setOpen(false);
	}
}
