package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.MetabolicModelPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MetabolicModelView extends Composite implements MetabolicModelPresenter.View {

	private static final String TEXT_MODEL = "Model: ";
	private static final String TEXT_BROWSE = "Browse";
	private static final String TEXT_SHOW_RXNS = "Show all reactions?";
	private static final String TEXT_NO_MODEL = "No model selected";
	private static final String TEXT_SELECT_MODEL = "Select a model";
	private static final String TEXT_SELECT_FBA_RUN = "Select a result set";
	private static final String TEXT_MEDIA_BTN = "Show media";
	private static final String TEXT_BIOMASS_BTN = "Show biomass";
	private static final String TEXT_MODEL_BTN = "Show model details";
	private static final String TEXT_FBA_INFO_LABEL = "FBA objective: ";
	private static final String TEXT_CLEAR_MODEL = "Clear model view";
	private static final String TEXT_NO_FBA = "No FBA results found for this model";

	// Main enclosing panels - has a title element with some text about the current model
	// and a disclosure panel for loading options
	// The mainPanel is structural and is held by the decoratorPanel
	private DecoratorPanel decoratorPanel;
	// mainPanel holds the disclosurePanel and titlePanel
	private VerticalPanel mainPanel;

	private HorizontalPanel titlePanel;
	private Label titleLabel;
	private TextBox modelTextBox;
	
	// main disclosure panel - holds all the model lookup options as they appear.
	private DisclosurePanel disclosurePanel;
	private VerticalPanel disclosureContentPanel;

	// The model panel has options for loading up models from KBase using a dropdown.
	private VerticalPanel modelPanel;
	private ListBox	modelListBox;
	private CheckBox modelAllRxnsBox;
	
	// the FBA panel is initially hidden, and holds info for loading up FBA run results,
	// including media, objectives, and other parameters.
	private VerticalPanel fbaPanel;
	private HorizontalPanel modelButtonPanel;
	private Button fbaInfoModelButton;
	private TextBox fbaTextBox;
	private ListBox fbaRunBox;
	private Button clearModelButton;
	private Label noFbaLabel;
	
	// initially(?) will just hold a couple buttons for firing up views of all media components,
	// biomass (or other objective), tab-delimited flux results, etc.
	private VerticalPanel fbaInfoPanel;
	private Label fbaInfoLabel;
	private HorizontalPanel fbaInfoButtonPanel;
	private Button fbaInfoMediaButton;
	private Button fbaInfoBiomassButton;
	
	/**
	 * Constructor
	 */
	public MetabolicModelView() {
		decoratorPanel = new DecoratorPanel();
		mainPanel = new VerticalPanel();
		disclosurePanel = new DisclosurePanel(TEXT_BROWSE);
		disclosureContentPanel = new VerticalPanel();
		
		titlePanel = new HorizontalPanel();
		titleLabel = new Label(TEXT_MODEL);
		
		modelPanel = new VerticalPanel();
		modelButtonPanel = new HorizontalPanel();
		modelListBox = new ListBox();
		modelAllRxnsBox = new CheckBox(TEXT_SHOW_RXNS);
		modelTextBox = new TextBox();
		
		fbaPanel = new VerticalPanel();
		clearModelButton = new Button(TEXT_CLEAR_MODEL);
		fbaTextBox = new TextBox();
		fbaRunBox = new ListBox();
		noFbaLabel = new Label(TEXT_NO_FBA);
		
		fbaInfoPanel = new VerticalPanel();
		fbaInfoButtonPanel = new HorizontalPanel();
		fbaInfoLabel = new Label(TEXT_FBA_INFO_LABEL);
		fbaInfoMediaButton = new Button(TEXT_MEDIA_BTN);
		fbaInfoBiomassButton = new Button(TEXT_BIOMASS_BTN);
		fbaInfoModelButton = new Button(TEXT_MODEL_BTN);
		
		init();
	}
	
	private void init() {
		titlePanel.add(titleLabel);
		titlePanel.add(modelTextBox);

		modelPanel.add(modelAllRxnsBox);
		modelPanel.add(modelListBox);

		modelButtonPanel.add(fbaInfoModelButton);
		modelButtonPanel.add(clearModelButton);
		fbaPanel.add(modelButtonPanel);
		fbaPanel.add(noFbaLabel);
		fbaPanel.add(fbaRunBox);

		fbaInfoButtonPanel.add(fbaInfoMediaButton);
		fbaInfoButtonPanel.add(fbaInfoBiomassButton);
		fbaInfoPanel.add(fbaInfoLabel);
		fbaInfoPanel.add(fbaInfoLabel);
		fbaInfoPanel.add(fbaInfoButtonPanel);

		disclosureContentPanel.add(modelPanel);
		disclosureContentPanel.add(fbaPanel);
		disclosureContentPanel.add(fbaInfoPanel);
		
		disclosurePanel.add(disclosureContentPanel);
		
		decoratorPanel.add(mainPanel);
		mainPanel.add(titlePanel);
		mainPanel.add(disclosurePanel);
		
		mainPanel.setSpacing(1);
		mainPanel.setStylePrimaryName("glamm-picker");
		modelListBox.setWidth("20em");

		initWidget(decoratorPanel);
	}
	
	@Override
	public ListBox getModelListBox() {
		return modelListBox;
	}

	@Override
	public ListBox getFbaListBox() {
		return fbaRunBox;
	}

	@Override
	public Button getMediaButton() {
		return fbaInfoMediaButton;
	}

	@Override
	public Button getBiomassButton() {
		return fbaInfoBiomassButton;
	}

	@Override
	public Button getModelButton() {
		return fbaInfoModelButton;
	}

	@Override
	public Panel getFbaPanel() {
		return fbaPanel;
	}

	@Override
	public Panel getFbaInfoPanel() {
		return fbaInfoPanel;
	}

	@Override
	public TextBox getTitleTextBox() {
		return modelTextBox;
	}

	@Override
	public TextBox getFbaTextBox() {
		return fbaTextBox;
	}

	@Override
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	@Override
	public CheckBox getShowAllCheckBox() {
		return modelAllRxnsBox;
	}

	@Override
	public HasClickHandlers getClearModelButton() {
		return clearModelButton;
	}
	
	@Override
	public Label getNoFbaLabel() {
		return noFbaLabel;
	}
}
