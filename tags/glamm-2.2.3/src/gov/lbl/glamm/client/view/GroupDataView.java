package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.client.presenter.GroupDataPresenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A view for the Group Data information. This includes a table for a list of groups, buttons to load groups, and a button to clear loaded data,
 * all wrapped up in a DisclosurePanel
 * @author wjriehl
 *
 */
public class GroupDataView extends Composite implements GroupDataPresenter.View {

	private static final String UPLOAD_BUTTON = "Upload File";
	private static final String LOAD_SERVICE_BUTTON = "Fetch from service";
	private static final String CLEAR_BUTTON = "Clear";
	private static final String DATA_LABEL = "Group Info: ";
	private static final String NONE = "none loaded";
	private static final String BROWSE = "Browse ";
	
	private DecoratorPanel decoratorPanel;
	private VerticalPanel  mainPanel;

	private Label groupLabel;
	private Label infoLabel;
	
	// Button panel
	private HorizontalPanel loaderPanel;
	private Button  		uploadButton;
	private Button			loadServiceButton;
	private Button  		clearButton;
	
	// DisclosurePanel
	private DisclosurePanel disclosurePanel;
	private VerticalPanel   disclosurePanelContent;

	// group table panel
	private CellTable<OverlayDataGroup> groupTable;
	private ScrollPanel 			    groupTableScrollPanel;
	
	/**
	 * Constructor
	 */
	public GroupDataView() {
		decoratorPanel = 		 new DecoratorPanel();
		mainPanel = 			 new VerticalPanel();

		groupLabel =			 new Label(DATA_LABEL);
		infoLabel = 			 new Label(NONE);
		
		disclosurePanel = 		 new DisclosurePanel(BROWSE);
		disclosurePanelContent = new VerticalPanel();

		groupTable = 			 new CellTable<OverlayDataGroup>();
		groupTableScrollPanel =  new ScrollPanel();

		loaderPanel = 			 new HorizontalPanel();
		uploadButton =			 new Button(UPLOAD_BUTTON);
		loadServiceButton = 	 new Button(LOAD_SERVICE_BUTTON);
		clearButton =			 new Button(CLEAR_BUTTON);

		init();
	}
	
	private void init() {
		loaderPanel.add(uploadButton);
		loaderPanel.add(loadServiceButton);
		loaderPanel.add(clearButton);

		groupTableScrollPanel.add(groupTable);
		
		disclosurePanelContent.add(groupTableScrollPanel);
		disclosurePanelContent.add(loaderPanel);
		disclosurePanel.add(disclosurePanelContent);
		
		// Build widget
		decoratorPanel.add(mainPanel);
		mainPanel.add(groupLabel);
		mainPanel.add(disclosurePanel);
		
		// Style the widget
		mainPanel.setStylePrimaryName("glamm-picker");
		groupTableScrollPanel.setSize("32em", "40em");
		groupTable.setWidth("30em");
		loaderPanel.setSpacing(5);
		mainPanel.setSpacing(1);
		groupTableScrollPanel.setVisible(false); //initially invisible, until some data is loaded
		
		initWidget(decoratorPanel);
	}
	
	public Button getUploadButton() {
		return uploadButton;
	}
	
	public Button getServiceButton() {
		return loadServiceButton;
	}
	
	public Button getClearButton() {
		return clearButton;
	}
	
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}
	
	public CellTable<OverlayDataGroup> getGroupTable() {
		return groupTable;
	}
	
	public Panel getGroupTablePanel() {
		return groupTableScrollPanel;
	}

	@Override
	public Label getInfoLabel() {
		return infoLabel;
	}
}
