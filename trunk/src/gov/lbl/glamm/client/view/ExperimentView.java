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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExperimentView extends Composite 
	implements ExperimentPresenter.View {
		
	// the widget
	private DecoratorPanel		decoratorPanel 				= null;
	private VerticalPanel		mainPanel 					= null;
	
	// header
	private HorizontalPanel		headerPanel					= null;
	private Label				experimentLabel				= null;
	private SuggestBox			experimentSuggestBox 		= null;
	private Button				prevButton 					= null;
	private Button				nextButton					= null;
	
	// disclosure panel
	private DisclosurePanel		disclosurePanel				= null;
	private VerticalPanel		browsePanel					= null;
	private HorizontalPanel		statusPanel					= null;
	private Label				statusLabel					= null;
	private Button				statusUploadButton			= null;
	private VerticalPanel		experimentPanel				= null;
	private ScrollPanel			experimentTableScrollPanel	= null;
	private CellTable<Sample> 	experimentTable				= null;
	private HorizontalPanel		expButtonPanel				= null;
	private Button				addButton					= null;
	private Button				uploadButton				= null;
	private Button				downloadButton				= null;
	
	private VerticalPanel		viewSubsetPanel				= null;
	private Label				viewSubsetLabel				= null;
	private ScrollPanel			viewSubsetTableScrollPanel 	= null;
	private CellTable<Sample> 	viewSubsetTable				= null;
	private HorizontalPanel		viewButtonPanel				= null;
	private Button				viewExperimentButton		= null;
	private Button				removeButton				= null;
	private Button				resetButton					= null;
	
	public ExperimentView() {
		
		// the widget
		decoratorPanel 				= new DecoratorPanel();
		mainPanel 					= new VerticalPanel();
		
		// header
		headerPanel					= new HorizontalPanel();
		experimentLabel				= new Label(STRING_EXP_LABEL);
		experimentSuggestBox 		= new SuggestBox();
		prevButton 					= new Button(STRING_PREV_BUTTON);
		nextButton					= new Button(STRING_NEXT_BUTTON);
		
		// disclosure panel
		disclosurePanel				= new DisclosurePanel(STRING_DISCLOSURE_PANEL);
		browsePanel					= new VerticalPanel();
		statusLabel					= new Label();
		statusPanel					= new HorizontalPanel();
		statusUploadButton			= new Button(STRING_UPLOAD_BUTTON);
		experimentPanel				= new VerticalPanel();
		experimentTableScrollPanel	= new ScrollPanel();
		experimentTable				= new CellTable<Sample>();
		expButtonPanel				= new HorizontalPanel();
		addButton					= new Button(STRING_ADD_BUTTON);
		uploadButton				= new Button(STRING_UPLOAD_BUTTON);
		downloadButton				= new Button(STRING_DOWNLOAD_BUTTON);
		
		viewSubsetPanel				= new VerticalPanel();
		viewSubsetLabel				= new Label(STRING_VIEW_LABEL);
		viewSubsetTableScrollPanel 	= new ScrollPanel();
		viewSubsetTable				= new CellTable<Sample>();
		viewButtonPanel				= new HorizontalPanel();
		viewExperimentButton		= new Button(STRING_VIEW_EXPERIMENT);
		removeButton				= new Button(STRING_REMOVE_BUTTON);
		resetButton					= new Button(STRING_RESET_BUTTON);
		
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
		
		experimentPanel.add(experimentTableScrollPanel);
		experimentPanel.add(expButtonPanel);
		experimentPanel.add(viewSubsetPanel);
		
		statusPanel.setSpacing(5);
		statusPanel.add(statusLabel);
		statusPanel.add(statusUploadButton);
		browsePanel.add(statusPanel);
		browsePanel.add(experimentPanel);
		disclosurePanel.setContent(browsePanel);
		
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
	public HasClickHandlers getNextExperimentButton() {
		return nextButton;
	}

	@Override
	public HasClickHandlers getPrevExperimentButton() {
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
