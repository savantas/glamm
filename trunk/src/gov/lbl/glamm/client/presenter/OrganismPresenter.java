package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.OrganismPickedEvent;
import gov.lbl.glamm.client.events.OrganismUploadEvent;
import gov.lbl.glamm.client.events.ViewResizedEvent;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.Sample.DataType;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.RequestParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Presenter for viewing the list of organisms available to the user.  The list of organisms may be filtered by available experimental data types.
 * @author jtbates
 *
 */
public class OrganismPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		
		/**
		 * Adds a data type choice to the view.
		 * @param caption The caption for this data type choice.
		 * @param isDefault Flag indicating whether or not this is the default choice.
		 * @return The click event handlers associated with the data type choice.
		 */
		public HasClickHandlers		addDataTypeChoice(final String caption, final boolean isDefault);
		
		/**
		 * Clears the data type choices from the view.
		 */
		public void					clearDataTypeChoices();
		
		/**
		 * Gets the disclosure panel.
		 * @return The panel.
		 */
		public DisclosurePanel		getDisclosurePanel();
		
		/**
		 * Gets the download organism button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers 	getDownloadButton();
		
		/**
		 * Gets the organism list box.
		 * @return The list box.
		 */
		public ListBox 				getOrganismListBox();
		
		/**
		 * Gets the organism suggest box.
		 * @return The suggest box.
		 */
		public SuggestBox			getOrganismSuggestBox();
		
		/**
		 * Gets the reset button used to reset the view to a non-organism-specific global map.
		 * @return The interface for the button.
		 */
		public HasClickHandlers 	getResetButton();
		
		/**
		 * Gets the organism upload button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers 	getUploadButton();
		
		/**
		 * Maximizes the disclosure panel.
		 */
		public void					maximize();
		
		/**
		 * Minimizes the disclosure panel.
		 */
		public void					minimize();
	}

	private static final String ACTION_DOWNLOAD_ORGANISM	= "downloadOrganism";
	private static final Map<Sample.DataType, String> dataType2Caption = new HashMap<Sample.DataType, String>();
	static {
		dataType2Caption.put(Sample.DataType.NONE, "Show all organisms");
		dataType2Caption.put(Sample.DataType.FITNESS, "Show only organisms with fitness data");
		dataType2Caption.put(Sample.DataType.PROTEIN, "Show only organisms with proteomics data");
		dataType2Caption.put(Sample.DataType.RNA, "Show only organisms with mRNA data");
		dataType2Caption.put(Sample.DataType.RNASEQ, "Show only organisms with RNASeq data");
		dataType2Caption.put(Sample.DataType.SESSION, "Show only organisms with uploaded experiment data");
	}

	private GlammServiceAsync rpc 		= null;
	private View view = null;
	private SimpleEventBus eventBus = null;

	private Organism organism	= null;
	private Sample.DataType dataType		= Sample.DataType.NONE;

	private HashMap<String, Organism> name2Organism = null;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public OrganismPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {

		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		name2Organism	= new HashMap<String, Organism>();
		dataType		= Sample.DataType.NONE;

		// set up data type choices for guest users
		addDataTypeChoice(Sample.DataType.NONE, true);
		addDataTypeChoice(Sample.DataType.RNA, false);
		addDataTypeChoice(Sample.DataType.FITNESS, false);

		setOrganism(Organism.globalMap(), false);

		bindView();
	}

	/**
	 * Updates the data type filter options.
	 */
	public void updateDataTypeChoices() {

		view.clearDataTypeChoices();

		addDataTypeChoice(Sample.DataType.NONE, true);
		rpc.getAvailableSampleTypes(new AsyncCallback<List<Sample.DataType>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getAvailableExperimentTypes");
			}

			@Override
			public void onSuccess(List<DataType> result) {
				if(result == null)
					return;
				for(Sample.DataType dataType : result) {
					addDataTypeChoice(dataType, false);
				}
				populate();
			}
		});
	}

	private void addDataTypeChoice(final Sample.DataType dataType, final boolean isDefault) {
		final OrganismPresenter thePresenter = this;
		final String caption = dataType2Caption.get(dataType);

		HasClickHandlers dataTypeChoice = view.addDataTypeChoice(caption, isDefault);

		dataTypeChoice.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				thePresenter.dataType = dataType;
				populate();
			}
		});
	}

	private void bindView() {

		final OrganismPresenter thePresenter = this;

		view.getResetButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				thePresenter.setOrganism(name2Organism.get(Organism.GLOBAL_MAP_NAME), true);
				view.minimize();
			}
		});

		view.getOrganismListBox().addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent e) {

				ListBox listBox = view.getOrganismListBox();
				int index = listBox.getSelectedIndex();
				String organismName = listBox.getItemText(index);
				thePresenter.setOrganism(name2Organism.get(organismName), true);
				view.minimize();
			}
		});

		view.getOrganismSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Suggestion suggestion	= event.getSelectedItem();
				String organismName 	= suggestion.getReplacementString();
				thePresenter.setOrganism(name2Organism.get(organismName), true);
				if(organism != null) {
					view.minimize();
				}

			}
		});

		view.getUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new OrganismUploadEvent(OrganismUploadEvent.Action.REQUEST));
			}
		});

		view.getDisclosurePanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});

		view.getDisclosurePanel().addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});

		view.getDownloadButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(organism != null && !organism.isGlobalMap()) {

					UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
					urlBuilder.setParameter("action", ACTION_DOWNLOAD_ORGANISM);
					urlBuilder.setPath("glammServlet");
					urlBuilder.setParameter(RequestParameters.TAXONOMY_ID, organism.getTaxonomyId());

					view.minimize();
					Window.open(urlBuilder.buildString(), "", "menubar=no,location=no,resizable=no,scrollbars=no,status=no,toolbar=false,width=0,height=0");
				}
			}
		});
	}

	/**
	 * Populates the organism list and suggest box.
	 */
	public void populate() {

		final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) view.getOrganismSuggestBox().getSuggestOracle();

		updatePopulatingStatus(false);

		suggestOracle.clear();
		view.getOrganismListBox().clear();
		name2Organism.clear();

		suggestOracle.add(Organism.GLOBAL_MAP_NAME);
		view.getOrganismListBox().addItem("-");
		view.getOrganismListBox().addItem(Organism.GLOBAL_MAP_NAME);
		name2Organism.put(Organism.GLOBAL_MAP_NAME, Organism.globalMap());

		rpc.populateOrganisms(dataType,
				new AsyncCallback<List<Organism>>() {
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateOrganisms");
			}

			public void onSuccess(List<Organism> organisms) {

				if(organisms == null) {
					updatePopulatingStatus(true);
					return;
				}

				for(Organism organism : organisms) {
					String name = organism.getName();
					suggestOracle.add(name);
					view.getOrganismListBox().addItem(name);
					name2Organism.put(name, organism);
				}

				updatePopulatingStatus(true);
			}
		});
	}

	/**
	 * Sets the organism - fires an OrganismPickedEvent if necessary.
	 * @param organism The organism.
	 * @param shouldFireEvent Flag indicating if a OrganismPickedEvent should be fired.
	 */
	public void setOrganism(Organism organism, boolean shouldFireEvent) {

		this.organism = organism;

		if(this.organism != null) {
			view.getOrganismSuggestBox().setText(this.organism.getName());

			for(int i = 0; i < view.getOrganismListBox().getItemCount(); i++) {
				if(view.getOrganismListBox().getItemText(i).equals(this.organism.getName())) {
					view.getOrganismListBox().setItemSelected(i, true);
					break;
				}
			}

			view.minimize();
			if(shouldFireEvent)
				eventBus.fireEvent(new OrganismPickedEvent(organism));
		}
	}

	private void updatePopulatingStatus(final boolean donePopulating) {
		final String POPULATING_TEXT = "Populating...";
		if(donePopulating) {
			if(organism != null)
				view.getOrganismSuggestBox().setText(organism.getName());
			else
				view.getOrganismSuggestBox().setText("No organism selected");
			DOM.setElementPropertyBoolean(view.getOrganismSuggestBox().getElement(), "disabled", false);
		}
		else {
			view.getOrganismSuggestBox().setText(POPULATING_TEXT);
			DOM.setElementPropertyBoolean(view.getOrganismSuggestBox().getElement(), "disabled", true);
		}
	}
}
