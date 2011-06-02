package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.CpdDstPickedEvent;
import gov.lbl.glamm.client.events.CpdSrcPickedEvent;
import gov.lbl.glamm.client.events.RoutePickedEvent;
import gov.lbl.glamm.client.events.RouteStepPickedEvent;
import gov.lbl.glamm.client.events.SearchTargetEvent;
import gov.lbl.glamm.client.events.ViewResizedEvent;
import gov.lbl.glamm.client.model.AnnotatedMapData;
import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.GlammPrimitive;
import gov.lbl.glamm.client.model.GlammPrimitive.Synonym;
import gov.lbl.glamm.client.model.GlammPrimitive.Xref;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.util.ReactionColor;
import gov.lbl.glamm.client.util.RowDependentSelectionCell;
import gov.lbl.glamm.shared.RequestParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

public class RetrosynthesisPresenter {

	public enum State {
		INITIAL,
		CALCULATING,
		HAS_ROUTES,
		NO_ROUTES;
	}

	public interface View {
		
		public static final String ALGORITHM_DFS_TEXT				= "Depth-first Search";
		public static final String ALGORITHM_DFS_VALUE				= "dfs";
		public static final String ALGORITHM_TW_DFS_TEXT			= "Taxon-weighted Depth-first Search";
		public static final String ALGORITHM_TW_DFS_VALUE			= "twdfs";

		public static String STRING_STATUS_CALCULATING		= "Calculating...";
		public static String STRING_BUTTON_EXPORT_ROUTES	= "Export Routes";
		public static String STRING_BUTTON_FIND_ROUTES 		= "Find Routes";
		public static String STRING_BUTTON_NEXT 			= "<html>&rarr;</html>";
		public static String STRING_BUTTON_PREV 			= "<html>&larr;</html>";
		public static String STRING_DP_GET_DIRECTIONS		= "Get directions";
		public static String STRING_LABEL_ALGORITHM			= "Algorithm: ";
		public static String STRING_LABEL_CPD_DST			= "To: ";
		public static String STRING_LABEL_CPD_SRC 			= "From: ";
		public static String STRING_STATUS_NO_ROUTES		= "No routes found";
		public static String STRING_LABEL_ROUTES 			= "Route: ";
		public static String STRING_LABEL_SEARCH 			= "Search: ";

		public ListBox 				getAlgorithmsListBox();
		public SuggestBox 			getCpdDstSuggestBox();
		public SuggestBox 			getCpdSrcSuggestBox();
		public DisclosurePanel		getDisclosurePanel();
		public HasClickHandlers 	getExportRoutes();
		public HasClickHandlers 	getFindRoutes();
		public HasClickHandlers 	getNextRouteButton();
		public HTML					getPhyloProfileLink();
		public HasClickHandlers 	getPrevRouteButton();
		public Label				getRoutesLabel();
		public Panel 				getRoutesPanel();
		public CellTable<Reaction> 	getRoutesTable();
		public SuggestBox 			getSearchSuggestBox();
		public Label 				getStatusLabel();
	}



	private static String genEcNumLink(String ecNum, String taxonomyId) {

		String link = "<b>No EC</b>";

		// annotate ecNum
		if(taxonomyId == null || taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID))
			return ecNum;
		
		if(ecNum != null && !ecNum.equals("NULL")) {
			link = "<a href=\"http://";
			link += Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID ? "meta." : "";
			link += "microbesonline.org/cgi-bin/fetchEC2.cgi?ec=" + ecNum + 
			"&taxId=" + taxonomyId + "\" target=\"_new\">" + ecNum + "</a>";
		}

		return link;
	}
	private GlammServiceAsync rpc = null;
	private View view = null;

	private SimpleEventBus eventBus = null;
	private AnnotatedMapData mapData = null;

	private Organism organism = null;
	// these two collections are not organism-specific and hence should ideally be retrieved only once
	private ArrayList<Compound> compounds = null;

	private ArrayList<Reaction> reactions = null;
	// mapping between ids in the suggest boxes and their respective primitives
	private HashMap<String, HashSet<GlammPrimitive>> cpdHash = null;
	private HashMap<String, HashSet<GlammPrimitive>> rxnHash = null;

	private HashMap<String, HashSet<GlammPrimitive>> locHash = null;
	private Compound cpdSrc = null;

	private Compound cpdDst = null;
	private ArrayList<Pathway> routes = null;
	private int routeIndex = -1;

	private ListDataProvider<Reaction> routeDataProvider = null;

	private State viewState;

	private static final String ACTION_GET_DIRECTIONS = "getDirections";

	public RetrosynthesisPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		cpdHash = new HashMap<String, HashSet<GlammPrimitive>>();
		rxnHash = new HashMap<String, HashSet<GlammPrimitive>>();
		locHash = new HashMap<String, HashSet<GlammPrimitive>>();

		routeDataProvider = new ListDataProvider<Reaction>(Reaction.KEY_PROVIDER);

		initTable(view.getRoutesTable(), routeDataProvider);
		setViewState(State.INITIAL);

		bindView();

	}

	private void addToHash(String key, GlammPrimitive primitive, HashMap<String, HashSet<GlammPrimitive>> target) {
		HashSet<GlammPrimitive> primitives = target.get(key);
		if(primitives == null) {
			primitives = new HashSet<GlammPrimitive>();
			target.put(key, primitives);
		}
		primitives.add(primitive);
	}

	private void bindView() {

		view.getCpdDstSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				HashSet<GlammPrimitive> primitives = getPrimitivesForId(event.getSelectedItem().getReplacementString());
				if(primitives.size() > 1)
					eventBus.fireEvent(new CpdDstPickedEvent(primitives));
				else
					setCpdDst((Compound) primitives.toArray()[0]);
			}
		});

		view.getCpdSrcSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				HashSet<GlammPrimitive> primitives = getPrimitivesForId(event.getSelectedItem().getReplacementString());
				if(primitives.size() > 1)
					eventBus.fireEvent(new CpdSrcPickedEvent(primitives));
				else
					setCpdSrc((Compound) primitives.toArray()[0]);
			}
		});

		view.getExportRoutes().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if(cpdSrc == null || cpdDst == null || mapData == null)
					return;

				Xref cpdSrcXref = cpdSrc.getXrefForDbNames(mapData.getCpdDbNames());
				Xref cpdDstXref = cpdDst.getXrefForDbNames(mapData.getCpdDbNames());

				if(cpdSrcXref == null || cpdDstXref == null)
					return;

				int index = view.getAlgorithmsListBox().getSelectedIndex();
				String algorithm = view.getAlgorithmsListBox().getValue(index);

				UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
				urlBuilder.setParameter("action", ACTION_GET_DIRECTIONS);
				urlBuilder.setPath("glammServlet");
				urlBuilder.setParameter(RequestParameters.PARAM_CPD_SRC, cpdSrcXref.getXrefId());
				urlBuilder.setParameter(RequestParameters.PARAM_CPD_DST, cpdDstXref.getXrefId());
				for(String dbName : mapData.getCpdDbNames()) 
					urlBuilder.setParameter(RequestParameters.PARAM_DBNAME, dbName);
				urlBuilder.setParameter(RequestParameters.PARAM_MAP_TITLE, mapData.getMapId());
				urlBuilder.setParameter(RequestParameters.PARAM_ALGORITHM, algorithm);
				urlBuilder.setParameter(RequestParameters.PARAM_AS_TEXT, "true");
				if(organism != null && !organism.isGlobalMap())
					urlBuilder.setParameter(RequestParameters.PARAM_TAXONOMY_ID, organism.getTaxonomyId());
				Window.open(urlBuilder.buildString(), "", "menubar=no,location=no,resizable=no,scrollbars=no,status=no,toolbar=false,width=0,height=0");
			}
		});

		view.getFindRoutes().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(cpdSrc != null && cpdDst != null) {
					setViewState(State.CALCULATING);
					int index = view.getAlgorithmsListBox().getSelectedIndex();
					String algorithm = view.getAlgorithmsListBox().getValue(index);
					rpc.getDirections(organism.getTaxonomyId(), 
							cpdSrc, cpdDst, 
							mapData.getMapId(), 
							algorithm,
							new AsyncCallback<ArrayList<Pathway>>() {
						@Override
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							Window.alert("Remote procedure call failure: getDirections");							
						}
						@Override
						public void onSuccess(ArrayList<Pathway> result) {
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
				HashSet<GlammPrimitive> primitives = getPrimitivesForId(event.getSelectedItem().getReplacementString());
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
			HashSet<String> argSet = new HashSet<String>();

			builder.setHost("www.microbesonline.org");
			builder.setPath("/cgi-bin/phyloprofile.cgi");
			builder.setParameter("download", "0");
			builder.setParameter("show", "astree");

			ArrayList<Reaction> reactions = route.getReactions();
			if(reactions != null) {
				for(Reaction reaction : reactions) {
					HashSet<String> ecNums = reaction.getEcNums();
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

	private HashSet<GlammPrimitive> getPrimitivesForId(String id) {

		HashSet<GlammPrimitive> primitives = cpdHash.get(id);
		if(primitives != null)
			return primitives;

		primitives = rxnHash.get(id);
		if(primitives != null)
			return primitives;

		return locHash.get(id);
	}


	private void initTable(CellTable<Reaction> table, ListDataProvider<Reaction> dataProvider) {

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
				HashSet<String> ecNums = reaction.getEcNums();
				if(ecNums == null || ecNums.isEmpty())
					builder.appendHtmlConstant("No EC");
				else {

					Organism candidate = reaction.getSelectedTransgenicCandidate();
					HashSet<String> ecNumsForCandidate = null;

					if(candidate != null)
						ecNumsForCandidate = reaction.getEcNumsForTransgenicCandidate(candidate);

					for(String ecNum : ecNums) {
						if(reaction.isNative())
							builder.appendHtmlConstant(genEcNumLink(ecNum, organism.getTaxonomyId()));
						else {
							if(ecNumsForCandidate != null && ecNumsForCandidate.contains(ecNum))
								builder.appendHtmlConstant(genEcNumLink(ecNum, candidate.getTaxonomyId()));
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
				return reaction.getReactionColor().getImageResource();
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

	public void populate() {

		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdSrcOracle = (MultiWordSuggestOracle) view.getCpdSrcSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdDstOracle = (MultiWordSuggestOracle) view.getCpdDstSuggestBox().getSuggestOracle();

		searchOracle.clear();
		cpdSrcOracle.clear();
		cpdDstOracle.clear();

		if(mapData != null) {
			if(compounds == null)
				populateCompoundSearch();
			else
				populateWithCompounds(compounds, false);

			if(reactions == null)
				populateReactionSearch();
			else
				populateWithReactions(reactions, false);
		}

		if(organism != null && !organism.isGlobalMap())
			populateLocusSearch();
	}

	private void populateCompoundSearch() {
		rpc.populateCompoundSearch(mapData.getCpdDbNames(), new AsyncCallback<ArrayList<Compound>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateCompoundSearch");	
			}

			@Override
			public void onSuccess(ArrayList<Compound> result) {
				compounds = result;
				if(compounds != null)
					populateWithCompounds(compounds, true);
			}
		});
	}

	private void populateLocusSearch() {
		rpc.populateLocusSearch(organism.getTaxonomyId(), new AsyncCallback<ArrayList<Gene>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateLocusSearch");	
			}

			@Override
			public void onSuccess(ArrayList<Gene> result) {
				if(result != null)
					populateWithGenes(result);
			}
		});
	}

	private void populateReactionSearch() {
		rpc.populateReactionSearch(mapData.getRxnDbNames(), new AsyncCallback<ArrayList<Reaction>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: populateReactionSearch");	
			}

			@Override
			public void onSuccess(ArrayList<Reaction> result) {
				reactions = result;
				if(reactions != null)
					populateWithReactions(reactions, true);
			}
		});
	}

	private void populateWithCompounds(final Collection<Compound> compounds, final boolean populateHash) {
		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdSrcOracle = (MultiWordSuggestOracle) view.getCpdSrcSuggestBox().getSuggestOracle();
		MultiWordSuggestOracle cpdDstOracle = (MultiWordSuggestOracle) view.getCpdDstSuggestBox().getSuggestOracle();

		for(Compound compound : compounds) {
			String name = compound.getName();
			if(name != null) {
				searchOracle.add(name);
				cpdSrcOracle.add(name);
				cpdDstOracle.add(name);
				if(populateHash)
					addToHash(name, compound, cpdHash);
			}

			HashSet<Synonym> synonyms = compound.getSynonyms();
			if(synonyms != null) {
				for(Synonym synonym : synonyms) {
					searchOracle.add(synonym.getName());
					cpdSrcOracle.add(synonym.getName());
					cpdDstOracle.add(synonym.getName());
					if(populateHash)
						addToHash(synonym.getName(), compound, cpdHash);
				}
			}
		}
	}

	private void populateWithGenes(final Collection<Gene> genes) {
		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();

		for(Gene gene : genes) {
			HashSet<Synonym> synonyms = gene.getSynonyms();
			if(synonyms != null) {
				for(Synonym synonym : synonyms) {
					searchOracle.add(synonym.getName());
					addToHash(synonym.getName(), gene, locHash);
				}
			}
		}
	}
	
	private void populateWithReactions(final Collection<Reaction> reactions, final boolean populateHash) {
		MultiWordSuggestOracle searchOracle = (MultiWordSuggestOracle) view.getSearchSuggestBox().getSuggestOracle();

		for(Reaction reaction : reactions) {
			HashSet<String> ecNums = reaction.getEcNums();
			if(ecNums != null) {
				for(String ecNum : ecNums) {
					searchOracle.add(ecNum);
					if(populateHash) 
						addToHash(ecNum, reaction, rxnHash);
				}
			}
		}
	}

	public void reset() {
		view.getCpdDstSuggestBox().setText("");
		view.getCpdSrcSuggestBox().setText("");
		view.getSearchSuggestBox().setText("");
		
		cpdSrc = null;
		cpdDst = null;
		
		setViewState(State.INITIAL);
	}

	public void setCpdDst(final Compound cpdDst) {
		this.cpdDst = cpdDst;
	}

	public void setCpdSrc(final Compound cpdSrc) {
		this.cpdSrc = cpdSrc;
	}

	public void setMapData(final AnnotatedMapData mapData) {
		this.mapData = mapData;
	}

	public void setOrganism(final Organism organism) {
		this.organism = organism;

		view.getAlgorithmsListBox().clear();

		if(this.organism == null || this.organism.isGlobalMap()) {
			view.getAlgorithmsListBox().addItem(View.ALGORITHM_DFS_TEXT, View.ALGORITHM_DFS_VALUE);
		}
		else {
			view.getAlgorithmsListBox().addItem(View.ALGORITHM_TW_DFS_TEXT, View.ALGORITHM_TW_DFS_VALUE);
			view.getAlgorithmsListBox().addItem(View.ALGORITHM_DFS_TEXT, View.ALGORITHM_DFS_VALUE);
		}

		populate();
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

		switch(viewState) {
		default:
		case INITIAL:
			view.getRoutesPanel().setVisible(false);
			view.getStatusLabel().setVisible(false);
			break;
		case CALCULATING:
			view.getRoutesPanel().setVisible(false);
			view.getStatusLabel().setText(View.STRING_STATUS_CALCULATING);
			view.getStatusLabel().setVisible(true);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		case HAS_ROUTES:
			view.getRoutesPanel().setVisible(true);
			view.getStatusLabel().setVisible(false);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		case NO_ROUTES:
			view.getRoutesPanel().setVisible(false);
			view.getStatusLabel().setText(View.STRING_STATUS_NO_ROUTES);
			view.getStatusLabel().setVisible(true);
			eventBus.fireEvent(new ViewResizedEvent());
			break;
		}
	}

	private void updateRouteLabel() {
		String labelText = View.STRING_LABEL_ROUTES + " " + (1 + routeIndex) + " of " + routes.size();
		view.getRoutesLabel().setText(labelText);
	}

}
