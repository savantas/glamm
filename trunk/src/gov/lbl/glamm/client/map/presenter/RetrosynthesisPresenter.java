package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.GlammClientBundle;
import gov.lbl.glamm.client.map.events.CpdDstPickedEvent;
import gov.lbl.glamm.client.map.events.CpdSrcPickedEvent;
import gov.lbl.glamm.client.map.events.RoutePickedEvent;
import gov.lbl.glamm.client.map.events.RouteStepPickedEvent;
import gov.lbl.glamm.client.map.events.SearchTargetEvent;
import gov.lbl.glamm.client.map.events.ViewResizedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.map.util.ReactionColor;
import gov.lbl.glamm.client.map.util.RowDependentSelectionCell;
import gov.lbl.glamm.shared.RequestParameters;
import gov.lbl.glamm.shared.model.Algorithm;
import gov.lbl.glamm.shared.model.AnnotatedMapData;
import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.interfaces.HasType;
import gov.lbl.glamm.shared.model.util.Synonym;
import gov.lbl.glamm.shared.model.util.Xref;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.SafeHtmlCell;
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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Presenter for compound, reaction, and gene search, as well as discovery of retrosynthetic pathways via a variety of algorithms.
 * @author jtbates
 *
 */
public class RetrosynthesisPresenter {
	
	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {

		/**
		 * Gets the algorithms list box.
		 * @return The list box.
		 */
		public ListBox 				getAlgorithmsListBox();
		
		/**
		 * Gets the destination compound suggest box.
		 * @return The suggest box.
		 */
		public SuggestBox 			getCpdDstSuggestBox();
		
		/**
		 * Gets the source compound suggest box.
		 * @return The suggest box.
		 */
		public SuggestBox 			getCpdSrcSuggestBox();
		
		/**
		 * Gets the disclosure panel.
		 * @return The disclosure panel.
		 */
		public DisclosurePanel		getDisclosurePanel();
		
		/**
		 * Gets the export routes button.
		 * @return The button.
		 */
		public HasClickHandlers 	getExportRoutes();

		/**
		 * Gets the clear routes button.
		 * @return The button.
		 */
		public HasClickHandlers		getClearRoutes();
		
		/**
		 * Gets the find routes button.
		 * @return The button.
		 */
		public HasClickHandlers 	getFindRoutes();
		
		/**
		 * Gets the next route button.
		 * @return The button.
		 */
		public HasClickHandlers 	getNextRouteButton();
		
		/**
		 * Gets the HTML for the link to the phylogenetic profile page on MicrobesOnline for the genes associated with the reactions in the active route.
		 * @return The HTML for the link.
		 */
		public HTML					getPhyloProfileLink();
		
		/**
		 * Gets the previous route button.
		 * @return The button.
		 */
		public HasClickHandlers 	getPrevRouteButton();
		
		/**
		 * Gets the route label.
		 * @return The label.
		 */
		public Label				getRoutesLabel();
		
		/**
		 * Gets the route panel.
		 * @return The panel.
		 */
		public Panel 				getRoutesPanel();
		
		/**
		 * Gets the routes table - the table of ordered reactions for a given route.
		 * @return The table.
		 */
		public CellTable<Reaction> 	getRoutesTable();
		
		/**
		 * Gets the search suggest box.
		 * @return The suggest box.
		 */
		public SuggestBox 			getSearchSuggestBox();
		
		/**
		 * Gets the status label.
		 * @return The label.
		 */
		public Label 				getStatusLabel();
	}
	
	private static enum State {
		INITIAL(""),
		CALCULATING("Calculating..."),
		HAS_ROUTES(""),
		NO_ROUTES("No routes found");
		
		private String statusText;
		
		private State(final String statusText) {
			this.statusText = statusText;
		}
		
		String getStatusText() {
			return statusText;
		}
	}
	
	private static final class LocusTrackPicker {

		private static final Map<ReactionColor, ImageResource> color2Resource;
		
		static {
			color2Resource = new HashMap<ReactionColor, ImageResource>();
			color2Resource.put(ReactionColor.BLUE, GlammClientBundle.INSTANCE.locusTrackBlue());
			color2Resource.put(ReactionColor.CYAN, GlammClientBundle.INSTANCE.locusTrackCyan());
			color2Resource.put(ReactionColor.GRAY, GlammClientBundle.INSTANCE.locusTrackGray());
			color2Resource.put(ReactionColor.GREEN, GlammClientBundle.INSTANCE.locusTrackGreen());
			color2Resource.put(ReactionColor.MAGENTA, GlammClientBundle.INSTANCE.locusTrackMagenta());
			color2Resource.put(ReactionColor.ORANGE, GlammClientBundle.INSTANCE.locusTrackOrange());
			color2Resource.put(ReactionColor.RED, GlammClientBundle.INSTANCE.locusTrackRed());
			color2Resource.put(ReactionColor.VIOLET, GlammClientBundle.INSTANCE.locusTrackViolet());
			color2Resource.put(ReactionColor.WHITE, GlammClientBundle.INSTANCE.locusTrackWhite());
			color2Resource.put(ReactionColor.YELLOW, GlammClientBundle.INSTANCE.locusTrackYellow());
			color2Resource.put(ReactionColor.NATIVE, GlammClientBundle.INSTANCE.locusTrackWhite());
		}
		
		public static ImageResource getLocusTrackForColor(ReactionColor color) {
			return color2Resource.get(color);
		}

	}

	

	private String isolateHost;
	private String metagenomeHost;


	private String genEcNumLinkForMolOrganism(String ecNum, Organism organism) {

		String taxonomyId = organism.getTaxonomyId();

		String link = "<a href=\"http://";
		if(metagenomeHost != null && Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			link += metagenomeHost;
		else
			link += isolateHost;
		link += "/cgi-bin/fetchEC2.cgi?ec=" + ecNum + 
		"&taxId=" + taxonomyId + "\" target=\"_new\">" + ecNum + "</a>";

		return link;
	}

	private String genEcNumLinkForSessionOrganism(String ecNum, Organism organism) {
		String link = "<b>" + ecNum + "</b>";

		// get set of all molTaxonomyIds
		Set<String> metaMolTaxonomyIds = organism.getMolTaxonomyIds();

		if(metagenomeHost == null || metaMolTaxonomyIds == null || metaMolTaxonomyIds.isEmpty())
			return link;

		link = "<a href=\"http://" + metagenomeHost + "/cgi-bin/fetchEC2.cgi?ec=" + ecNum;
		for(String taxonomyId : metaMolTaxonomyIds) 
			link += taxonomyId != null ? "&taxId=" + taxonomyId : "";
		link += "\" target=\"_new\">";
		link += "<b>" + ecNum + "</b></a>";

		return link;
	}

	private String genEcNumLink(String ecNum, Organism organism) {

		if(ecNum == null || ecNum.isEmpty())
			return "<b>No EC</b>";

		// annotate ecNum
		if(organism == null || organism.isGlobalMap())
			return ecNum;

		if(organism.isSessionOrganism())
			return genEcNumLinkForSessionOrganism(ecNum, organism);
		return genEcNumLinkForMolOrganism(ecNum, organism);
	}

	private GlammServiceAsync rpc = null;
	private View view = null;

	private SimpleEventBus eventBus = null;
	private AnnotatedMapData mapData = null;

	private Organism organism = null;

	// mapping between ids in the suggest boxes and their respective primitives
	private Map<String, Set<HasType>> cpdHash;
	private Map<String, Set<HasType>> rxnHash;
	private Map<String, Set<HasType>> locHash;

	private Compound cpdSrc = null;
	private Compound cpdDst = null;
	private List<Pathway> routes = null;
	private int routeIndex = -1;

	private ListDataProvider<Reaction> routeDataProvider = null;

	private State viewState;

	private static final String ACTION_GET_DIRECTIONS = "getDirections";

	private boolean compoundsPopulated = false;
	private boolean genesPopulated = false;
	private boolean reactionsPopulated = false;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public RetrosynthesisPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		cpdHash = new HashMap<String, Set<HasType>>();
		rxnHash = new HashMap<String, Set<HasType>>();
		locHash = new HashMap<String, Set<HasType>>();

		routeDataProvider = new ListDataProvider<Reaction>(Reaction.KEY_PROVIDER);

		loadIsolateHost();
		loadMetagenomeHost();

		initTable(view.getRoutesTable(), routeDataProvider);
		setViewState(State.INITIAL);
		
		bindView();

	}

	private void addToHash(String key, HasType primitive, Map<String, Set<HasType>> target) {
		Set<HasType> primitives = target.get(key);
		if(primitives == null) {
			primitives = new HashSet<HasType>();
			target.put(key, primitives);
		}
		primitives.add(primitive);
	}

	private void bindView() {

		view.getCpdDstSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Set<HasType> primitives = getPrimitivesForId(event.getSelectedItem().getReplacementString());
				if(primitives.size() > 1) {
					Set<Compound> compounds = new HashSet<Compound>();
					for(HasType primitive : primitives) 
						compounds.add((Compound) primitive);
					eventBus.fireEvent(new CpdDstPickedEvent(compounds));
				}
				else
					setCpdDst((Compound) primitives.toArray()[0]);
			}
		});

		view.getCpdSrcSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Set<HasType> primitives = getPrimitivesForId(event.getSelectedItem().getReplacementString());
				if(primitives.size() > 1) {
					Set<Compound> compounds = new HashSet<Compound>();
					for(HasType primitive : primitives) 
						compounds.add((Compound) primitive);
					eventBus.fireEvent(new CpdSrcPickedEvent(compounds));
				}
				else
					setCpdSrc((Compound) primitives.toArray()[0]);
			}
		});

		view.getExportRoutes().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if(cpdSrc == null || cpdDst == null || mapData == null)
					return;

				Xref cpdSrcXref = cpdSrc.getXrefSet().getXrefForDbNames(mapData.getCpdDbNames());
				Xref cpdDstXref = cpdDst.getXrefSet().getXrefForDbNames(mapData.getCpdDbNames());

				if(cpdSrcXref == null || cpdDstXref == null)
					return;

				int index = view.getAlgorithmsListBox().getSelectedIndex();
				String algorithm = view.getAlgorithmsListBox().getValue(index);

				UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
				urlBuilder.setParameter("action", ACTION_GET_DIRECTIONS);
				urlBuilder.setPath("glammServlet");
				urlBuilder.setParameter(RequestParameters.CPD_SRC, cpdSrcXref.getXrefId());
				urlBuilder.setParameter(RequestParameters.CPD_DST, cpdDstXref.getXrefId());
				for(String dbName : mapData.getCpdDbNames()) 
					urlBuilder.setParameter(RequestParameters.DBNAME, dbName);
				urlBuilder.setParameter(RequestParameters.MAPID, mapData.getDescriptor().getMapId());
				urlBuilder.setParameter(RequestParameters.ALGORITHM, algorithm);
				urlBuilder.setParameter(RequestParameters.AS_TEXT, "true");
				if(organism != null && !organism.isGlobalMap())
					urlBuilder.setParameter(RequestParameters.TAXONOMY_ID, organism.getTaxonomyId());
				Window.open(urlBuilder.buildString(), "", "menubar=no,location=no,resizable=no,scrollbars=no,status=no,toolbar=false,width=0,height=0");
			}
		});
		
		view.getClearRoutes().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				routes = null;
				setViewState(State.INITIAL);
				eventBus.fireEvent(new RoutePickedEvent(null, null, null));
			}
		});

		view.getFindRoutes().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(cpdSrc != null && cpdDst != null) {
					setViewState(State.CALCULATING);
					int index = view.getAlgorithmsListBox().getSelectedIndex();
					String algorithmShortName = view.getAlgorithmsListBox().getValue(index);
					rpc.getDirections(organism, 
							cpdSrc, cpdDst, 
							mapData.getDescriptor().getMapId(), 
							Algorithm.getAlgorithmForShortName(algorithmShortName),
							new AsyncCallback<List<Pathway>>() {
						@Override
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							Window.alert("Remote procedure call failure: getDirections");							
						}
						@Override
						public void onSuccess(List<Pathway> result) {
							routes = result;
							if(routes == null)
								setViewState(State.NO_ROUTES);
							else {
								routeIndex = 0;
								updateRouteLabel();
								setRoute(routes.get(routeIndex));
								setViewState(State.HAS_ROUTES);
							}
						}
					});
				}
			}
		});

		view.getNextRouteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(viewState == State.HAS_ROUTES && routes != null && routeIndex < routes.size() - 1) {
					setRoute(routes.get(++routeIndex));
					updateRouteLabel();
				}
			}
		});

		view.getPrevRouteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(viewState == State.HAS_ROUTES && routes != null && routeIndex > 0) {
					setRoute(routes.get(--routeIndex));
					updateRouteLabel();
				}
			}
		});

		view.getSearchSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Set<HasType> primitives = getPrimitivesForId(event.getSelectedItem().getReplacementString());
				eventBus.fireEvent(new SearchTargetEvent(primitives));
			}
		});

		final SingleSelectionModel<? super Reaction> routesTableSelectionModel = (SingleSelectionModel<? super Reaction>) view.getRoutesTable().getSelectionModel();
		routesTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Reaction selectedReaction = (Reaction) routesTableSelectionModel.getSelectedObject();
				eventBus.fireEvent(new RouteStepPickedEvent(selectedReaction));
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

	}

	private String genPhyloProfileLink(final Pathway route) {

		String html = "No phylogenetic profile";

		if(route != null) {

			UrlBuilder builder = new UrlBuilder();
			Set<String> argSet = new HashSet<String>();

			builder.setHost(isolateHost);
			builder.setPath("/cgi-bin/phyloprofile.cgi");
			builder.setParameter("download", "0");
			builder.setParameter("show", "astree");

			List<Reaction> reactions = route.getReactions();
			if(reactions != null) {
				for(Reaction reaction : reactions) {
					Set<String> ecNums = reaction.getEcNums();
					if(ecNums != null) {
						for(String ecNum : ecNums) 
							argSet.add("EC" + ecNum);
					}
				}
			}

			if(!argSet.isEmpty()) {
				String[] args = new String[argSet.size()];
				int i = 0;
				for(String arg : argSet)
					args[i++] = arg;
				builder.setParameter("group", args);
				html = "<a href=\"" + builder.buildString() + "\" target=\"new\">Phylogenetic profile</a>";
			}


		}

		return html;
	}

	private Set<HasType> getPrimitivesForId(String id) {

		Set<HasType> primitives = cpdHash.get(id);
		if(primitives != null)
			return primitives;

		primitives = rxnHash.get(id);
		if(primitives != null)
			return primitives;

		return locHash.get(id);
	}


	private void initTable(final CellTable<Reaction> table, ListDataProvider<Reaction> dataProvider) {

		if(table == null || dataProvider == null)
			return;

		// create EC, Genes, and Species columns
		// EC column contains a list of EC numbers, separated by new lines, linking to the appropriate gene page on MOL
		// Gene column contains a LocusTrack glyph, white if the reaction is native, another color if it isn't
		// Definition column contains reaction definition
		// Species column contains a listbox of transgenic candidates

		// initialize table columns		
		Column<Reaction, SafeHtml> ecColumn = new Column<Reaction, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Reaction reaction) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				Set<String> ecNums = reaction.getEcNums();
				if(ecNums == null || ecNums.isEmpty())
					builder.appendHtmlConstant("No EC");
				else {

					Organism candidate = reaction.getSelectedTransgenicCandidate();
					Set<String> ecNumsForCandidate = null;

					if(candidate != null)
						ecNumsForCandidate = reaction.getEcNumsForTransgenicCandidate(candidate);

					for(String ecNum : ecNums) {
						if(reaction.isNative())
							builder.appendHtmlConstant(genEcNumLink(ecNum, organism));
						else {
							if(ecNumsForCandidate != null && ecNumsForCandidate.contains(ecNum))
								builder.appendHtmlConstant(genEcNumLink(ecNum, candidate));
							else
								builder.appendHtmlConstant(ecNum);
						}
						builder.appendHtmlConstant("<br>");
					}
				}
				return builder.toSafeHtml();
			}
		};

		Column<Reaction, ImageResource> locusTrackColumn = new Column<Reaction, ImageResource>(new ImageResourceCell()) {
			@Override
			public ImageResource getValue(Reaction reaction) {
				return LocusTrackPicker.getLocusTrackForColor(reaction.getColor());
			}
		};

		Column<Reaction, String> speciesColumn = new Column<Reaction, String>(new RowDependentSelectionCell("15em")) {

			@Override
			public String getValue(Reaction reaction) {
				Organism selected = reaction.getSelectedTransgenicCandidate();
				if(selected != null)
					return selected.getName();
				return null;
			}

		};
		speciesColumn.setFieldUpdater(new FieldUpdater<Reaction, String>() {
			public void update(int index, Reaction reaction, String value) {
				reaction.setSelectedTransgenicCandidate(value);
				table.redraw();
			}
		});

		TextColumn<Reaction> definitionColumn = new TextColumn<Reaction>() {
			@Override
			public String getValue(Reaction reaction) {
				String html = "No definition";
				String definition = reaction.getDefinition();
				if(definition == null || definition.isEmpty())
					return html;
				return definition;
			}
		};

		table.addColumn(ecColumn, "EC");
		table.addColumn(locusTrackColumn, "Gene");
		table.addColumn(definitionColumn, "Definition");
		table.addColumn(speciesColumn, "Species");

		table.setColumnWidth(ecColumn, "5em");
		table.setColumnWidth(locusTrackColumn, "5em");
		table.setColumnWidth(definitionColumn, "25em");
		table.setColumnWidth(speciesColumn, "15em");

		// set the data provider
		dataProvider.addDataDisplay(table);

		// add a selection model
		final SingleSelectionModel<Reaction> selectionModel = new SingleSelectionModel<Reaction>(Reaction.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
	}

	private void loadIsolateHost() {
		rpc.getIsolateHost(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getIsolateHost");
			}

			@Override
			public void onSuccess(String result) {
				isolateHost = result;
			}
		});
	}

	private void loadMetagenomeHost() {
		rpc.getMetagenomeHost(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getMetagenomeHost");
			}

			@Override
			public void onSuccess(String result) {
				metagenomeHost = result;
			}
		});
	}

	/**
	 * Populates the search and compound suggest boxes with the genes, EC numbers, and compounds for the current organism.
	 */
	public void populate() {

		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdSrcOracle = (MultiWordSuggestOracle) view.getCpdSrcSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdDstOracle = (MultiWordSuggestOracle) view.getCpdDstSuggestBox().getSuggestOracle();

		searchOracle.clear();
		cpdSrcOracle.clear();
		cpdDstOracle.clear();

		cpdHash.clear();
		rxnHash.clear();
		locHash.clear();

		compoundsPopulated = false;
		genesPopulated = false;
		reactionsPopulated = false;

		updatePopulatingStatus();

		if(mapData == null)
			return;
		
		populateCompoundSearch();
		populateReactionSearch();

		if(organism != null && !organism.isGlobalMap())
			populateLocusSearch();
		else
			genesPopulated = true;
	}

	private void populateCompoundSearch() {
		rpc.populateCompoundSearch(mapData.getDescriptor().getMapId(), new AsyncCallback<Set<Compound>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateCompoundSearch");	
			}

			@Override
			public void onSuccess(Set<Compound> result) {
				if(result != null)
					populateWithCompounds(result);
			}
		});
	}

	private void populateLocusSearch() {
		rpc.populateLocusSearch(organism, new AsyncCallback<Set<Gene>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateLocusSearch");	
			}

			@Override
			public void onSuccess(Set<Gene> result) {
				if(result != null)
					populateWithGenes(result);
			}
		});
	}

	private void populateReactionSearch() {
		rpc.populateReactionSearch(mapData.getDescriptor().getMapId(), new AsyncCallback<Set<Reaction>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateReactionSearch");	
			}

			@Override
			public void onSuccess(Set<Reaction> result) {
				if(result != null)
					populateWithReactions(result);
			}
		});
	}

	private void populateWithCompounds(final Collection<Compound> compounds) {

		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdSrcOracle = (MultiWordSuggestOracle) view.getCpdSrcSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdDstOracle = (MultiWordSuggestOracle) view.getCpdDstSuggestBox().getSuggestOracle();

		for(Compound compound : compounds) {
			String name = compound.getName();
			if(name != null) {
				searchOracle.add(name);
				cpdSrcOracle.add(name);
				cpdDstOracle.add(name);
				addToHash(name, compound, cpdHash);
			}

			Set<Synonym> synonyms = compound.getSynonyms();
			if(synonyms != null) {
				for(Synonym synonym : synonyms) {
					searchOracle.add(synonym.getName());
					cpdSrcOracle.add(synonym.getName());
					cpdDstOracle.add(synonym.getName());
					addToHash(synonym.getName(), compound, cpdHash);
				}
			}
		}

		compoundsPopulated = true;
		updatePopulatingStatus();
	}

	private void populateWithGenes(final Collection<Gene> genes) {
		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();

		for(Gene gene : genes) {
			Set<Synonym> synonyms = gene.getSynonyms();
			if(synonyms != null) {
				for(Synonym synonym : synonyms) {
					searchOracle.add(synonym.getName());
					addToHash(synonym.getName(), gene, locHash);
				}
			}
		}

		genesPopulated = true;
		updatePopulatingStatus();
	}

	private void populateWithReactions(final Collection<Reaction> reactions) {
		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();

		for(Reaction reaction : reactions) {
			Set<String> ecNums = reaction.getEcNums();
			if(ecNums != null) {
				for(String ecNum : ecNums) {
					searchOracle.add(ecNum); 
					addToHash(ecNum, reaction, rxnHash);
				}
			}
		}

		reactionsPopulated = true;
		updatePopulatingStatus();
	}

	/**
	 * Resets all suggest box text and sets the view state to initial.
	 */
	public void reset() {
		view.getCpdDstSuggestBox().setText("");
		view.getCpdSrcSuggestBox().setText("");
		view.getSearchSuggestBox().setText("");

		cpdSrc = null;
		cpdDst = null;

		setViewState(State.INITIAL);
	}

	/**
	 * Sets the destination compound.
	 * @param cpdDst The destination compound.
	 */
	public void setCpdDst(final Compound cpdDst) {
		this.cpdDst = cpdDst;
	}

	/**
	 * Sets the source compound.
	 * @param cpdSrc The source compound.
	 */
	public void setCpdSrc(final Compound cpdSrc) {
		this.cpdSrc = cpdSrc;
	}

	/**
	 * Sets the map data.
	 * @param mapData The map data.
	 */
	public void setMapData(final AnnotatedMapData mapData) {
		this.mapData = mapData;
	}

	/**
	 * Sets the organism.
	 * @param organism The organism.
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;

		view.getAlgorithmsListBox().clear();
		
		for(Algorithm algorithm : Algorithm.values()) {
			// if the organism is null or the organism is the global map, only add algorithms that don't require an organism
			if(!algorithm.requiresOrganism() || (organism != null && !organism.isGlobalMap()))
				view.getAlgorithmsListBox().addItem(algorithm.getFullName(), algorithm.getShortName());
		}
	}

	private void setRoute(final Pathway route) {
		List<Reaction> dpList = routeDataProvider.getList();
		dpList.clear();
		ReactionColor.resetNonNativeColors();
		for(Reaction reaction : route.getReactions()) {
			if(reaction.isNative())
				reaction.setColor(ReactionColor.NATIVE);
			else
				reaction.setColor(ReactionColor.getNextNonNativeColor());
			dpList.add(reaction);
		}
		view.getPhyloProfileLink().setHTML(genPhyloProfileLink(route));
		view.getRoutesTable().setVisibleRange(0, route.getReactions().size());
		eventBus.fireEvent(new RoutePickedEvent(cpdSrc, cpdDst, route));
	}


	private void setViewState(final State state) {
		this.viewState = state;

		view.getStatusLabel().setText(state.getStatusText());
		switch(viewState) {
		default:
		case INITIAL:
			view.getRoutesPanel().setVisible(false);
			view.getStatusLabel().setVisible(false);
			break;
		case CALCULATING:
			view.getRoutesPanel().setVisible(false);
			view.getStatusLabel().setVisible(true);
			break;
		case HAS_ROUTES:
			view.getRoutesPanel().setVisible(true);
			view.getStatusLabel().setVisible(false);
			break;
		case NO_ROUTES:
			view.getRoutesPanel().setVisible(false);
			view.getStatusLabel().setVisible(true);
			break;
		}
		eventBus.fireEvent(new ViewResizedEvent());
	}

	private void updatePopulatingStatus() {
		final String POPULATING_TEXT = "Populating...";
		boolean donePopulating = compoundsPopulated && genesPopulated && reactionsPopulated;

		if(!donePopulating) {
			view.getSearchSuggestBox().setText(POPULATING_TEXT);
			DOM.setElementPropertyBoolean(view.getSearchSuggestBox().getElement(), "disabled", true);
			view.getCpdSrcSuggestBox().setText(POPULATING_TEXT);
			DOM.setElementPropertyBoolean(view.getCpdSrcSuggestBox().getElement(), "disabled", true);
			view.getCpdDstSuggestBox().setText(POPULATING_TEXT);
			DOM.setElementPropertyBoolean(view.getCpdDstSuggestBox().getElement(), "disabled", true);
		}
		else {
			view.getSearchSuggestBox().setText("");
			DOM.setElementPropertyBoolean(view.getSearchSuggestBox().getElement(), "disabled", false);
			view.getCpdSrcSuggestBox().setText("");
			DOM.setElementPropertyBoolean(view.getCpdSrcSuggestBox().getElement(), "disabled", false);
			view.getCpdDstSuggestBox().setText("");
			DOM.setElementPropertyBoolean(view.getCpdDstSuggestBox().getElement(), "disabled", false);
		}
	}

	private void updateRouteLabel() {
		String labelText = "Route: " + (1 + routeIndex) + " of " + routes.size();
		view.getRoutesLabel().setText(labelText);
	}

}