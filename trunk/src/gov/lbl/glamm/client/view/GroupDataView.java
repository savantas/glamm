package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.client.presenter.GroupDataPresenter;

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

/**
 * A view for the Group Data information. This includes a table for a list of groups, buttons to load groups, and a button to clear loaded data,
 * all wrapped up in a DisclosurePanel
 * @author wjriehl
 *
 */
public class GroupDataView extends Composite implements GroupDataPresenter.View {

	private static final String TEXT_UPLOAD_BUTTON 			= "Upload File";
	private static final String TEXT_LOAD_SERVICE_BUTTON	= "Fetch from service";
	private static final String TEXT_CLEAR_BUTTON			= "Clear";
	private static final String TEXT_DATA_LABEL				= "Group Info:";
	private static final String TEXT_NONE 					= "none loaded";
	private static final String TEXT_BROWSE 				= "Browse";
	private static final String TEXT_NEXT_BUTTON			= "<html>&rarr;</html>";
	private static final String TEXT_PREV_BUTTON			= "<html>&larr;</html>";
	private static final String TEXT_SELECT_ALL_BUTTON		= "Select all";
	private static final String TEXT_DESELECT_ALL_BUTTON	= "Select none";

	private DecoratorPanel 				decoratorPanel;
	private VerticalPanel  				mainPanel;

	// Header panel
	private HorizontalPanel				headerPanel;
	private Label 						groupLabel;
	private Label 						infoLabel;
	private SuggestBox					groupSuggestBox;
	private Button						prevButton;
	private Button						nextButton;
	
	// Button panel
	private HorizontalPanel 			loaderPanel;
	private Button  					uploadButton;
	private Button						loadServiceButton;
	private Button  					clearButton;
	
	// DisclosurePanel
	private DisclosurePanel				disclosurePanel;
	private VerticalPanel				disclosurePanelContent;

	// group table panel
	private VerticalPanel				tablePanel;
	private HorizontalPanel				tableButtonPanel;
	private Button						selectAllButton;
	private Button						deselectAllButton;	
	private CellTable<OverlayDataGroup> groupTable;
	private ScrollPanel 			    groupTableScrollPanel;
	
	/**
	 * Constructor
	 */
	public GroupDataView() {
		decoratorPanel 			= new DecoratorPanel();
		mainPanel 				= new VerticalPanel();

		headerPanel				= new HorizontalPanel();
		groupLabel 				= new Label(TEXT_DATA_LABEL);
		infoLabel				= new Label(TEXT_NONE);
		groupSuggestBox			= new SuggestBox();
		prevButton				= new Button(TEXT_PREV_BUTTON);
		nextButton				= new Button(TEXT_NEXT_BUTTON);
		
		
		disclosurePanel			= new DisclosurePanel(TEXT_BROWSE);
		disclosurePanelContent 	= new VerticalPanel();

		tablePanel				= new VerticalPanel();
		tableButtonPanel		= new HorizontalPanel();
		selectAllButton			= new Button(TEXT_SELECT_ALL_BUTTON);
		deselectAllButton		= new Button(TEXT_DESELECT_ALL_BUTTON);
		groupTable 				= new CellTable<OverlayDataGroup>();
		groupTableScrollPanel	= new ScrollPanel();

		loaderPanel				= new HorizontalPanel();
		uploadButton			= new Button(TEXT_UPLOAD_BUTTON);
		loadServiceButton		= new Button(TEXT_LOAD_SERVICE_BUTTON);
		clearButton				= new Button(TEXT_CLEAR_BUTTON);

		init();
	}
	
	private void init() {
		loaderPanel.add(uploadButton);
		loaderPanel.add(loadServiceButton);
		loaderPanel.add(clearButton);

		tableButtonPanel.add(selectAllButton);
		tableButtonPanel.add(deselectAllButton);
		tablePanel.add(tableButtonPanel);
		tablePanel.add(groupTableScrollPanel);
		groupTableScrollPanel.add(groupTable);
		
		disclosurePanelContent.add(tablePanel);
		disclosurePanelContent.add(loaderPanel);
		disclosurePanel.add(disclosurePanelContent);
		
		headerPanel.add(groupLabel);
		headerPanel.add(groupSuggestBox);
		headerPanel.add(prevButton);
		headerPanel.add(nextButton);
		groupSuggestBox.setWidth("15em");
		
		// Build widget
		decoratorPanel.add(mainPanel);
		mainPanel.add(headerPanel);
		mainPanel.add(disclosurePanel);
		
		// Style the widget
		mainPanel.setStylePrimaryName("glamm-picker");
		groupTableScrollPanel.setSize("36em", "27em");
		groupTable.setWidth("30em");
		loaderPanel.setSpacing(5);
		mainPanel.setSpacing(1);
		tablePanel.setVisible(false); //initially invisible until some data is loaded
		
		initWidget(decoratorPanel);
	}
	
	@Override
	public HasClickHandlers getUploadButton() {
		return uploadButton;
	}
	
	@Override
	public HasClickHandlers getServiceButton() {
		return loadServiceButton;
	}
	
	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}
	
	@Override
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}
	
	@Override
	public CellTable<OverlayDataGroup> getGroupTable() {
		return groupTable;
	}
	
	@Override
	public Panel getGroupTablePanel() {
		return tablePanel;
	}

	@Override
	public Label getInfoLabel() {
		return infoLabel;
	}

	@Override
	public SuggestBox getSuggestBox() {
		return groupSuggestBox;
	}

	@Override
	public HasClickHandlers getPrevButton() {
		return prevButton;
	}

	@Override
	public HasClickHandlers getNextButton() {
		return nextButton;
	}
	
	@Override
	public HasClickHandlers getSelectAllButton() {
		return selectAllButton;
	}
	
	@Override
	public HasClickHandlers getDeselectAllButton() {
		return deselectAllButton;
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
