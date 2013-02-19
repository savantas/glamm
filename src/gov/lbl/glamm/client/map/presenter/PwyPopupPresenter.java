package gov.lbl.glamm.client.map.presenter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.DeploymentDomain;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.User;
import gov.lbl.glamm.shared.model.util.Synonym;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Presenter for pathway popups.
 * @author jtbates
 *
 */
public class PwyPopupPresenter {
	
	private static enum State {
		LOADING,
		NO_PATHWAY,
		HAS_PATHWAY;
	}

	public interface View {
		
		/**
		 * Gets the table that contains pathway information.
		 */
		public DataGrid<Reaction> getPwyTable();
		
		/**
		 * Gets the add all to cart button.
		 * @return The button.
		 */
		public Button getAddAllToCartButton();
	
		/**
		 * Gets the add native species reactions/compounds in the pathway to cart button.
		 * @return The button.
		 */
		public Button getAddNativeToCartButton();
		
		/**
		 * Gets the view style button.
		 * @return
		 */
		public Button getViewStyleButton();
		
		/**
		 * Toggles the view style between the KEGG canonical map and the Reaction table.
		 */
		public void toggleViewStyle();
		
		/**
		 * Gets the button panel for this view. The button panel should contain the add to cart buttons.
		 * @return
		 */
		public Panel getButtonPanel();
		
		/**
		 * Gets the pathway image, if available.
		 * @return The image.
		 */
		public Image getImage();
		
		/**
		 * Gets the popup panel.
		 * @return The panel.
		 */
		public Panel getPanel();
		
		/**
		 * Gets the status label.
		 * @return The label.
		 */
		public Label getStatusLabel();
		
		/**
		 * Hides the popup.
		 */
		public void hidePopup();
		
		/**
		 * Kills the popup.
		 */
		public void killPopup();
		
		/**
		 * Shows the popup at a position in client space.
		 * @param left The client space x position.
		 * @param top The client space y position.
		 */
		public void showPopup(int left, int top);
		
		/**
		 * Gets the bit of HTML code that defines a link to the Pathway in MicrobesOnline
		 * @return
		 */
		public HTML getPwyLinkHtml();
		
		/**
		 * Updates the pathway map image in the viewer with the given URL.
		 * @param imgUrl
		 */
		public void updateImage(String imgUrl);
		
		/**
		 * Resets the position of the popup window.
		 */
		public void resetPopupPosition();

	}
	
	private GlammServiceAsync rpc;
	private View view;
	@SuppressWarnings("unused")
	private SimpleEventBus eventBus;
	
	private Set<Pathway> pathways;
	private Organism organism;
	private Sample sample;
	private User user;
	private ListDataProvider<Reaction> pwyDataProvider = null;
	private String host;
	private DeploymentDomain domain;
	private Column<Reaction, SafeHtml> geneColumn;
	
	/**
	 * Initializes the PwyPopupPresenter.
	 * 
	 * @param rpc
	 * @param view
	 * @param eventBus
	 */
	public PwyPopupPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
		
		this.pathways = new HashSet<Pathway>();
		this.organism = Organism.globalMap();
		this.sample = null;
		this.pwyDataProvider = new ListDataProvider<Reaction>(); 
		setUser(User.guestUser());
		
		loadDeploymentDomain();
		loadHost();
		initTable(view.getPwyTable(), pwyDataProvider);
		bindView();
	}
	
	private void loadDeploymentDomain() {
		rpc.getDeploymentDomain(new AsyncCallback<DeploymentDomain>() {

			@Override
			public void onFailure(Throwable caught) {
				domain = DeploymentDomain.KBASE;
			}

			@Override
			public void onSuccess(DeploymentDomain domain) {
				PwyPopupPresenter.this.domain = domain;
			}
			
		});
	}
	
	/**
	 * Initializes button behavior in the view.
	 */
	private void bindView() {
		view.getAddAllToCartButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// add all reactions in this path to the cart.
			}
		});
		
		view.getAddNativeToCartButton().setVisible(false);
		
		view.getAddNativeToCartButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// add only species native reactions to the cart
			}
		});
		
		view.getViewStyleButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.toggleViewStyle();
			}
		});
	}
	
	/**
	 * Generates an HTML link to the right EC number page on MicrobesOnline.
	 * @param ecNum the EC number to link to
	 * @return an HTML String
	 */
	private String genEcNumLink(final String ecNum) {

		if (domain == DeploymentDomain.LBL) { 
			StringBuilder builder = new StringBuilder();
	
			builder.append("<a href=\"");
			builder.append(genEcNumUrl(ecNum));
			builder.append("\" target=\"_new\">");
			builder.append("<b>" + ecNum + "</b></a>");
	
			return builder.toString();
		}
		else {
			return ecNum;
		}
	}

	/**
	 * Constructs a URL to information about the given EC number.
	 * @param ecNum the EC number
	 * @return a URL with info about the EC number.
	 */
	private String genEcNumUrl(final String ecNum) {

		if (domain == DeploymentDomain.LBL){ 
			UrlBuilder urlBuilder = new UrlBuilder();
	
			urlBuilder.setProtocol("http");
			urlBuilder.setHost(host);
			urlBuilder.setPath("/cgi-bin/fetchEC2.cgi");
			urlBuilder.setParameter("ec", ecNum);
			
			if (organism != null && !organism.isGlobalMap())
				urlBuilder.setParameter("taxId", organism.getTaxonomyId());
	
			return urlBuilder.buildString();
		}
		else {
			return ecNum;
		}
	}

	/**
	 * Generates a link to information about the given gene by wrapping a locus URL with an HTML <a> tag.
	 * @param gene the Gene of interest
	 * @return a String containing an HTML link.
	 */
	private String genGeneLink(final Gene gene) {
		if (gene == null)
			return "";

		String name = gene.getSynonymWithType(Gene.SYNONYM_TYPE_NAME);
		String ncbi = gene.getSynonymWithType(Gene.SYNONYM_TYPE_NCBI);
		String session = gene.getSynonymWithType(Gene.SYNONYM_TYPE_SESSION);

		StringBuilder textLinkBuilder = new StringBuilder();
		
		textLinkBuilder.append(name == null ? "" : name + " ");
		textLinkBuilder.append(ncbi == null ? "" : ncbi + " ");
		textLinkBuilder.append(session == null ? "" : session + " ");

		String textLink = textLinkBuilder.toString();
		if (gene.getVimssId().isEmpty() || host == null || domain != DeploymentDomain.LBL)
			return textLink;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<a href=\"");
		builder.append(genLocusUrl(gene.getVimssId()));
		builder.append("\" target=\"_new\">");
		builder.append("<b>" + textLink + "</b></a>");
		
		return builder.toString();
	}
	
	/**
	 * Generates a locus URL that points to the MicrobesOnline on whichever host it's currently situtated to run on.
	 * @param vimssId
	 * @return
	 */
	private String genLocusUrl(final String vimssId) {

		if (domain == DeploymentDomain.LBL) {
			UrlBuilder urlBuilder = new UrlBuilder();
	
			urlBuilder.setProtocol("http");
			urlBuilder.setHost(host);
			urlBuilder.setPath("/cgi-bin/fetchLocus.cgi");
			urlBuilder.setParameter("locus", vimssId);
			urlBuilder.setParameter("disp", "0");
	
			return urlBuilder.buildString();
		}
		else if (domain == DeploymentDomain.KBASE) {
			// look up the KBase fid!
			return vimssId;
		}
		else {
			return vimssId;
		}
	}

	/**
	 * Initializes the pathway reaction table, and sets it to display data properly.
	 * @param table
	 * @param dataProvider
	 */
	private void initTable(final DataGrid<Reaction> table, ListDataProvider<Reaction> dataProvider) {
		/* Columns:
		 * 1. EC number
		 * 2. Reaction name(s)
		 * 3. Reaction definition (e.g. A + B <=> C + D)
		 * 4. Is native? (checkbox? color block?)
		 */
		
		Column<Reaction, SafeHtml> ecColumn = new Column<Reaction, SafeHtml>(new SafeHtmlCell()) {
			public SafeHtml getValue(Reaction r) {
				
				String[] ecSet = r.getEcNums().toArray(new String[0]);
				
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				if (ecSet == null || ecSet.length == 0) {
					builder.appendHtmlConstant("No EC");
				}
				else {
					Arrays.sort(ecSet);
					for (String ecNum : ecSet) {
						if (ecNum != null && ecNum.length() > 0) {
							builder.appendHtmlConstant(genEcNumLink(ecNum));
						}
						builder.appendHtmlConstant("<br>");
					}
				}
				return builder.toSafeHtml();
			}
		};
		
		TextColumn<Reaction> nameColumn = new TextColumn<Reaction>() {
			public String getValue(Reaction r) {
				StringBuilder nameBuilder = new StringBuilder();
				
				for (Synonym s : r.getSynonyms()) {
					if (s.getName() != null)
						nameBuilder.append(s.getName() + "\n");
				}
				return nameBuilder.toString();
			}
		};
		
		TextColumn<Reaction> defColumn = new TextColumn<Reaction>() {
			public String getValue(Reaction r) {
				return r.getDefinition();
			}
		};
		
		geneColumn = new Column<Reaction, SafeHtml>(new SafeHtmlCell()) {
			public SafeHtml getValue(Reaction r) {
				if (organism == null || organism == Organism.globalMap())
					return null;

				/* Cases:
				 * 1. organism = global or null
				 *   - hide column
				 * 2. organism selected, no gene present.
				 *   - "No genes"
				 */

				SafeHtmlBuilder geneBuilder = new SafeHtmlBuilder();
				
				Set<Gene> genes = r.getGenes();
				if (genes.size() == 0)
					geneBuilder.appendHtmlConstant("No genes available");

				else {
					for (Gene gene : genes) {
						geneBuilder.appendHtmlConstant(genGeneLink(gene) + "<br>");
					}
				}

				return geneBuilder.toSafeHtml();
			}
		};
		
		table.addColumn(ecColumn, "EC");
		table.addColumn(nameColumn, "Reaction Name(s)");
		table.addColumn(defColumn, "Definition");
		table.addColumn(geneColumn, "Gene Name(s)");
	
		table.setColumnWidth(ecColumn, "8em");
		table.setColumnWidth(nameColumn, "25em");
		table.setColumnWidth(defColumn, "25em");
		table.setColumnWidth(geneColumn, "10em");
		
		table.setMinimumTableWidth(68, Unit.EM);
		dataProvider.addDataDisplay(table);
	}
	
	/**
	 * Sets the current with our pathway(s) of interest.
	 * @param organism
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;
		if (organism != null && !organism.isGlobalMap()) {
			view.getPwyTable().setColumnWidth(geneColumn, "10em");
			view.getPwyTable().setMinimumTableWidth(75, Unit.EM);
			view.getAddNativeToCartButton().setVisible(true);
		}
		else {
			view.getPwyTable().setColumnWidth(geneColumn, "0em");
			view.getPwyTable().setMinimumTableWidth(65, Unit.EM);
		}
	}
	
	/**
	 * Sets the pathways to be displayed in the popup.
	 * @param pathways
	 */
	private void setPathways(final Set<Pathway> pathways) {
		if(!this.pathways.isEmpty())
			this.pathways.clear();
		this.pathways.addAll(pathways);
		List<Reaction> dpList = pwyDataProvider.getList();
		dpList.clear();
		
		//TODO: This should be sorted by some metric (eventually...) to allow interaction with the linear pathway presenter
		for (Pathway p : pathways) {
			for (Reaction r : p.getReactions()) {
				dpList.add(r);
			}
		}
		view.getPwyTable().setVisibleRange(0, dpList.size());
	}
	
	/**
	 * Sets the experimental data sample to be displayed.
	 * @param sample
	 */
	public void setSample(final Sample sample) {
		this.sample = sample;
	}
	
	/**
	 * Sets the current user to associate with this popup. If the user is logged in (not a guest), then an 
	 * "Add to Cart" button is displayed.
	 * @param user
	 */
	public void setUser(final User user) {
		this.user = user;
		if (this.user == null || this.user.isGuestUser())
			view.getButtonPanel().setVisible(false);
		else
			view.getButtonPanel().setVisible(true);
	}
	
	/**
	 * Sets the state of the popup viewer. Displays a "loading..." string, an 
	 * error if there's no pathway in the database, or populates the panel with
	 * pathway data if present.
	 * @param state
	 */
	private void setViewState(final State state) {
		switch(state) {

		case LOADING: {
			final String msg = "Loading...";
			view.getStatusLabel().setText(msg);
			view.getStatusLabel().setVisible(true);
			view.getPanel().setVisible(false);
			break;
		}

		case NO_PATHWAY: {
			final String msg = "Pathway not found.";
			view.getStatusLabel().setText(msg);
			view.getStatusLabel().setVisible(true);
			view.getPanel().setVisible(false);
			break;
		}

		case HAS_PATHWAY: {
			view.getStatusLabel().setVisible(false);
			view.getPanel().setVisible(true);
			view.resetPopupPosition();
			break;
		}
		}
	}
	
	/**
	 * Shows the popup. The presenter fetches the data from the database, formats it, and feeds it into the
	 * PwyPopupView.
	 * @param mapIds
	 * @param left
	 * @param top
	 */
	public void showPopup(final Set<String> mapIds, int left, int top) {
		setViewState(State.LOADING);
		view.showPopup(left, top);
		
		rpc.getPathways(mapIds, organism, sample, new AsyncCallback<Set<Pathway>>() {
			public void onFailure(Throwable caught) {
				// Show the RPC message to the user
				Window.alert("Remote procedure call failure: getPathways");
			}
			
			public void onSuccess(Set<Pathway> result) {
				setPathways(result);
				if(result == null || result.size() == 0)
					setViewState(State.NO_PATHWAY);
				else
				{
					clearPathwayPanel();
					addPathwaysToPanel();
					setViewState(State.HAS_PATHWAY);
				}
			}
		});
	}
	
	/**
	 * Clears the pathway panel.
	 * //TODO stub
	 */
	private void clearPathwayPanel() {
	}
	
	/**
	 * Creates a table of pathway sub-panels (for the case in which there are multiple pathways
	 * to show).
	 * 
	 * Each row will contain some data from the pathway, a link to the pathway in MicrobesOnline, an
	 * image of the pathway (constrained to some maximum size), and (if there are two or more rows)
	 * a checkbox to be used in conjunction with the Add to Cart button.
	 *
	 */
	private void addPathwaysToPanel() {
		if (!pathways.isEmpty()) {
			for (Pathway pwy : pathways) {
				String taxonomyId = organism == null ? null : organism.getTaxonomyId();
				String experimentId = sample == null ? null : sample.getExperimentId();
				String sampleId = sample == null ? null : sample.getSampleId();
				
				view.getPwyLinkHtml().setHTML(genKeggMapLink(pwy, taxonomyId, experimentId, sampleId));
				String imageUrl = genImgUrl(pwy);
				view.updateImage(imageUrl);
			}
		}
	}

	/**
	 * Generates a link to the KEGG map that shows an image of the given pathway.
	 * @param pwy
	 * @param taxonomyId
	 * @param experimentId
	 * @param sampleId
	 * @return
	 */
	private String genKeggMapLink(final Pathway pwy, 
								  final String taxonomyId, 
								  final String experimentId, 
								  final String sampleId) { 
		String link = "";
		String mapTitle = pwy.getName();

		if(mapTitle != null && !mapTitle.isEmpty() && domain == DeploymentDomain.LBL) {
			link = "<a href=\"" + 
			genKeggMapUrl(pwy, taxonomyId, experimentId, sampleId) + 
			"\" target = \"_new\">" + 
			mapTitle + 
			"</a>";
		}
		else {
			link = mapTitle;
		}
		return link;
	}

	/**
	 * 
	 * @param pwy
	 * @param taxonomyId
	 * @param experimentId
	 * @param sampleId
	 * @return
	 */
	private String genKeggMapUrl(final Pathway pwy, 
								 final String taxonomyId, 
								 final String experimentId, 
								 final String sampleId) { 

		String url = "";
		String mapId = pwy.getMapId();
		
		// Hack to peel off the 'map' part of the id, if it exists.
		// If left in, this can cause issues with MicrobesOnline.
		if (mapId.startsWith("map"))
			mapId = mapId.substring(3);

		if(	experimentId != null && !experimentId.equals(Experiment.DEFAULT_EXPERIMENT_ID) &&
				sampleId != null && !sampleId.equals(Sample.DEFAULT_SAMPLE_ID) &&
				taxonomyId != null && !taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID)) {

			url = "http://" + host + "/cgi-bin/microarray/reportSet.cgi?disp=3&";
			url += "mapId=" + mapId;
			url += "&taxId=" + taxonomyId;
			url += "&expId=" + experimentId;
			url += "&setId=" + sampleId;
			url += "&z=0.5&n=-1";

		}
		else {

			url = "http://" +  host + "/cgi-bin/browseKegg?";
			url += "mapId=" + mapId;
			url += taxonomyId != null && !taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID) ? "&taxId=" + taxonomyId : "";

		}
		return url;
	}

	/**
	 * Generates a URL that points to the KEGG image for the given pathway.
	 * @param pwy
	 * @return
	 */
	private String genImgUrl(final Pathway pwy) {
		String imgUrlString = "http://" + host + "/kegg/" + pwy.getMapId() + ".png";
		return imgUrlString;
	}

	/**
	 * Loads the name of the host to be used in constructing URLs.
	 */
	private void loadHost() {
		rpc.getIsolateHost(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getIsolateHost");
			}

			@Override
			public void onSuccess(String result) {
				host = result;
			}
		});
	}

	/**
	 * Removes the popup from the screen.
	 */
	public void killPopup() {
		view.killPopup();
	}
	
}
