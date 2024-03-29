package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.RetrosynthesisPresenter;
import gov.lbl.glamm.shared.model.Reaction;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for displaying the retrosynthesis panel.
 * @author jtbates
 *
 */
public class RetrosynthesisView extends Composite implements
RetrosynthesisPresenter.View {
	
	private static final String TEXT_BUTTON_CLEAR_ROUTES	= "Clear Routes";
	private static final String TEXT_BUTTON_EXPORT_ROUTES	= "Export Routes";
	private static final String TEXT_BUTTON_FIND_ROUTES 	= "Find Routes";
	private static final String TEXT_BUTTON_NEXT 			= "<html>&rarr;</html>";
	private static final String TEXT_BUTTON_PREV 			= "<html>&larr;</html>";
	private static final String TEXT_DP_GET_DIRECTIONS		= "Get directions";
	private static final String TEXT_LABEL_ALGORITHM		= "Algorithm: ";
	private static final String TEXT_LABEL_CPD_DST			= "To: ";
	private static final String TEXT_LABEL_CPD_SRC 			= "From: ";
	private static final String TEXT_LABEL_ROUTES 			= "Route: ";
	private static final String TEXT_LABEL_SEARCH 			= "Search: ";
	private static final String DELIMITERS					= "/\\-+[]()";

	// main panel
	private DecoratorPanel 	decoratorPanel	= null;
	private VerticalPanel	mainPanel		= null;

	// header
	private HorizontalPanel	headerPanel	= null;
	private SuggestBox		searchSuggestBox 	= null;

	// disclosure panel
	private DisclosurePanel 	disclosurePanel		= null;
	private VerticalPanel		browsePanel			= null;
	private Grid				grid				= null;
	private SuggestBox			cpdSrcSuggestBox 	= null;
	private SuggestBox			cpdDstSuggestBox 	= null;
	private ListBox				algorithmListBox	= null;
	private Button				findRoutes			= null;

	// routes
	private VerticalPanel		routesPanel				= null;
	private HTML				phyloProfileLink		= null;
	private HorizontalPanel		routesNavigationPanel	= null;
	private Button				prevRouteButton			= null;
	private Button				nextRouteButton			= null;
	private Label				routesLabel				= null;
	private ScrollPanel			routesScrollPanel		= null;
	private CellTable<Reaction>	routesTable				= null;
	
	// export and clear buttons
	private HorizontalPanel		exportButtonPanel		= null;
	private Button				exportRoutes			= null;
	private Button				clearRoutes				= null;

	// status label
	private Label				statusLabel		= null;

	/**
	 * Constructor
	 */
	public RetrosynthesisView() {

		decoratorPanel		= new DecoratorPanel();
		mainPanel			= new VerticalPanel();
		headerPanel			= new HorizontalPanel();
		searchSuggestBox 	= new SuggestBox(new MultiWordSuggestOracle(DELIMITERS));
		disclosurePanel		= new DisclosurePanel(TEXT_DP_GET_DIRECTIONS);
		browsePanel			= new VerticalPanel();
		grid				= new Grid(4, 2);
		cpdSrcSuggestBox 	= new SuggestBox(new MultiWordSuggestOracle(DELIMITERS));
		cpdDstSuggestBox	= new SuggestBox(new MultiWordSuggestOracle(DELIMITERS));
		algorithmListBox	= new ListBox();
		findRoutes			= new Button(TEXT_BUTTON_FIND_ROUTES);
		routesPanel			= new VerticalPanel();
		phyloProfileLink	= new HTML();
		routesNavigationPanel	= new HorizontalPanel();
		prevRouteButton			= new Button(TEXT_BUTTON_PREV);
		nextRouteButton			= new Button(TEXT_BUTTON_NEXT);
		routesLabel				= new Label(TEXT_LABEL_ROUTES);
		routesScrollPanel		= new ScrollPanel();    
		routesTable				= new CellTable<Reaction>();
		exportButtonPanel		= new HorizontalPanel();
		exportRoutes			= new Button(TEXT_BUTTON_EXPORT_ROUTES);
		clearRoutes				= new Button(TEXT_BUTTON_CLEAR_ROUTES);
		statusLabel			= new Label();

		init();

	}

	private void init() {

		// set up header panel
		headerPanel.add(new Label(TEXT_LABEL_SEARCH));
		headerPanel.add(searchSuggestBox);

		cpdSrcSuggestBox.setWidth("15em");
		cpdDstSuggestBox.setWidth("15em");
		searchSuggestBox.setWidth("15em");
		
		// set up the grid
		grid.setWidget(0, 0, new Label(TEXT_LABEL_CPD_SRC));
		grid.setWidget(0, 1, cpdSrcSuggestBox);
		grid.setWidget(1, 0, new Label(TEXT_LABEL_CPD_DST));
		grid.setWidget(1, 1, cpdDstSuggestBox);
		grid.setWidget(2, 0, new Label(TEXT_LABEL_ALGORITHM));
		grid.setWidget(2, 1, algorithmListBox);
		grid.setWidget(3, 1, findRoutes);

		algorithmListBox.setWidth("15em");
		findRoutes.setWidth("100%");

		// set up routes navigation panel
		routesNavigationPanel.setSpacing(5);
		routesNavigationPanel.add(prevRouteButton);
		routesNavigationPanel.add(nextRouteButton);
		routesNavigationPanel.add(routesLabel);

		// set up routesPanel
		routesPanel.add(phyloProfileLink);
		routesPanel.add(routesNavigationPanel);
		routesScrollPanel.add(routesTable);
		routesPanel.add(routesScrollPanel);
		
		// set up bottom-most export button panel
		exportButtonPanel.setSpacing(5);
		exportButtonPanel.add(exportRoutes);
		exportButtonPanel.add(clearRoutes);
		routesPanel.add(exportButtonPanel);

		// set up disclosure panel and the browse panel within it
		browsePanel.add(grid);
		browsePanel.add(routesPanel);
		browsePanel.add(statusLabel);
		disclosurePanel.add(browsePanel);

		// set up main panel
		mainPanel.add(headerPanel);
		mainPanel.add(disclosurePanel);
		mainPanel.setStylePrimaryName("glamm-picker");

		// decorate main panel
		decoratorPanel.add(mainPanel);

		// initialize widget
		initWidget(decoratorPanel);

	}

	@Override
	public ListBox getAlgorithmsListBox() {
		return algorithmListBox;
	}

	@Override
	public SuggestBox getCpdDstSuggestBox() {
		return cpdDstSuggestBox;
	}

	@Override
	public SuggestBox getCpdSrcSuggestBox() {
		return cpdSrcSuggestBox;
	}

	@Override
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}
	
	@Override
	public HasClickHandlers getClearRoutes() {
		return clearRoutes;
	}
	
	@Override
	public HasClickHandlers getExportRoutes() {
		return exportRoutes;
	}

	@Override
	public HasClickHandlers getFindRoutes() {
		return findRoutes;
	}

	@Override
	public HasClickHandlers getNextRouteButton() {
		return nextRouteButton;
	}

	@Override
	public Label getStatusLabel() {
		return statusLabel;
	}
	
	@Override
	public HTML getPhyloProfileLink() {
		return phyloProfileLink;
	}

	@Override
	public HasClickHandlers getPrevRouteButton() {
		return prevRouteButton;
	}

	@Override
	public Label getRoutesLabel() {
		return routesLabel;
	}
	
	@Override
	public Panel getRoutesPanel() {
		return routesPanel;
	}

	@Override
	public CellTable<Reaction> getRoutesTable() {
		return routesTable;
	}

	@Override
	public SuggestBox getSearchSuggestBox() {
		return searchSuggestBox;
	}
}
