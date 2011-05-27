package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.OrganismPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OrganismView extends Composite implements OrganismPresenter.View {
	
	private final String RB_GROUP = "RB_GROUP";
	
	// main panel
	private DecoratorPanel 	decoratorPanel	= null;
	private VerticalPanel	mainPanel		= null;

	// header
	private HorizontalPanel	headerPanel	= null;
	private SuggestBox		suggestBox 	= null;

	// disclosure panel
	private DisclosurePanel	disclosurePanel		= null;
	private VerticalPanel	browsePanel			= null;
	private ListBox			listBox				= null;
	private VerticalPanel	radioButtonPanel	= null;
	private HorizontalPanel	buttonPanel			= null;
	private Button			resetButton			= null;
	private Button			uploadButton		= null;
	private Button			downloadButton		= null;
	
	public OrganismView() {
		
		// main panel
		decoratorPanel	= new DecoratorPanel();
		mainPanel		= new VerticalPanel();

		// header
		headerPanel	= new HorizontalPanel();
		suggestBox 	= new SuggestBox();

		// disclosure panel
		disclosurePanel		= new DisclosurePanel("Browse");
		browsePanel			= new VerticalPanel();
		listBox				= new ListBox();
		radioButtonPanel	= new VerticalPanel();
		buttonPanel			= new HorizontalPanel();
		resetButton			= new Button("Global Map");
		uploadButton		= new Button("Upload Organism");
		downloadButton		= new Button("Download Organism");
	
		init();
	}
	
	private void init() {
		
		// set up disclosure panel
		listBox.setWidth("30em");
	
		buttonPanel.setSpacing(5);
		buttonPanel.add(resetButton);
		buttonPanel.add(uploadButton);
		buttonPanel.add(downloadButton);
		
		resetButton.setTitle("Reset to global map.");
		uploadButton.setTitle("Upload tab-delimited organism data file of locus to EC number pairs.");
		downloadButton.setTitle("Download selected organism as a tab-delimited file of locus to EC number pairs.");

		browsePanel.add(listBox);
		browsePanel.add(radioButtonPanel);
		browsePanel.add(buttonPanel);

		disclosurePanel.setContent(browsePanel);

		// set up header panel
		headerPanel.add(new Label("Organism: "));
		headerPanel.add(suggestBox);
		suggestBox.setWidth("15em");

		// set up main panel
		mainPanel.add(headerPanel);
		mainPanel.add(disclosurePanel);
		mainPanel.setStylePrimaryName("glamm-picker");

		// wrap main panel
		decoratorPanel.add(mainPanel);
		
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
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	@Override
	public HasClickHandlers getDownloadButton() {
		return downloadButton;
	}

	@Override
	public ListBox getOrganismListBox() {
		return listBox;
	}

	@Override
	public SuggestBox getOrganismSuggestBox() {
		return suggestBox;
	}

	@Override
	public HasClickHandlers getResetButton() {
		return resetButton;
	}

	@Override
	public HasClickHandlers getUploadButton() {
		return uploadButton;
	}

	@Override
	public void maximize() {
		disclosurePanel.setOpen(true);
	}

	@Override
	public void minimize() {
		disclosurePanel.setOpen(false);
	}

//	@Override
//	public HandlerRegistration addResizeHandler(ResizeHandler handler) {
//		return addHandler(handler, ResizeEvent.getType());
//	}

	
}
