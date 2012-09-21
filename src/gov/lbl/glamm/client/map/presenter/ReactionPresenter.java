package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.OverlayDataGroup;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Presenter for displaying reaction data for a map element.  These data typically include: the definition, the Enzyme Commission (EC) numbers,
 * and a table summarizing the genes and measurements for those genes, if applicable.
 * 
 * @author jtbates
 *
 */
public class ReactionPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		
		/**
		 * Gets the add to cart button.
		 * @return The button.
		 */
		public Button getAddToCartButton();
		
		/**
		 * Gets the reaction definition html interface.
		 * @return The html interface.
		 */
		public HasHTML getDefinitionHtml();
		
		/**
		 * Gets the ec number html interface.
		 * @return The html interface.
		 */
		public HasHTML getEcNumHtml();
		
		/**
		 * Gets the gene table.
		 * @return The gene table.
		 */
		public CellTable<Gene> getGeneTable();
		
		public CellTable<OverlayDataGroup> getGroupTable();
		
		/**
		 * Hides the gene table.
		 */
		public void hideGeneTable();
		
		/**
		 * Shows the gene table.
		 */
		public void showGeneTable();
		
		/**
		 * Hides the grouping information table.
		 */
		public void hideGroupTable();

		/**
		 * Shows the grouping information table.
		 */
		public void showGroupTable();
		

	}

	private String host;
	private Organism organism;
	private Set<OverlayDataGroup> dataGroups = null;
	private Reaction reaction;

	private ListDataProvider<Gene> geneDataProvider;
	private ListDataProvider<OverlayDataGroup> groupDataProvider;

	@SuppressWarnings("unused")
	private GlammServiceAsync rpc;
	private View view;
	@SuppressWarnings("unused")
	private SimpleEventBus eventBus;
	
	private User user;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public ReactionPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		geneDataProvider = new ListDataProvider<Gene>(Gene.KEY_PROVIDER);
		groupDataProvider = new ListDataProvider<OverlayDataGroup>(OverlayDataGroup.KEY_PROVIDER);
	}

	private String genEcNumLink(final String ecNum, final int numGenes) {

		if(numGenes == 0 || host == null || organism == null || organism.isGlobalMap())
			return "<b>" + ecNum + "</b>";

		StringBuilder builder = new StringBuilder();

		builder.append("<a href=\"");
		builder.append(genEcNumUrl(ecNum));
		builder.append("\" target=\"_new\">");
		builder.append("<b>" + ecNum + "</b></a>");

		return builder.toString();
	}

	private String genEcNumUrl(final String ecNum) {

		UrlBuilder urlBuilder = new UrlBuilder();

		urlBuilder.setProtocol("http");
		urlBuilder.setHost(host);
		urlBuilder.setPath("/cgi-bin/fetchEC2.cgi");
		urlBuilder.setParameter("ec", ecNum);
		urlBuilder.setParameter("taxId", organism.getTaxonomyId());

		return urlBuilder.buildString();
	}

	private String genLocusLink(final String vimssId) {

		if(vimssId == null || vimssId.isEmpty() || host == null)
			return vimssId;

		StringBuilder builder = new StringBuilder();

		builder.append("<a href=\"");
		builder.append(genLocusUrl(vimssId));
		builder.append("\" target=\"_new\">");
		builder.append("<b>" + vimssId + "</b></a>");

		return builder.toString();
	}

	private String genLocusUrl(final String vimssId) {

		UrlBuilder urlBuilder = new UrlBuilder();

		urlBuilder.setProtocol("http");
		urlBuilder.setHost(host);
		urlBuilder.setPath("/cgi-bin/fetchLocus.cgi");
		urlBuilder.setParameter("locus", vimssId);
		urlBuilder.setParameter("disp", "0");

		return urlBuilder.buildString();
	}

	private void initGroupTable(CellTable<OverlayDataGroup> table, ListDataProvider<OverlayDataGroup> dataProvider) {
		if (table == null || dataProvider == null)
			return;
		
		
		// init columns
		Column<OverlayDataGroup, SafeHtml> groupNameColumn = new Column<OverlayDataGroup, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(OverlayDataGroup group) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendHtmlConstant(group.getName());
				return builder.toSafeHtml();
			}
		};
		
		Column<OverlayDataGroup, SafeHtml> genomeNameColumn = new Column<OverlayDataGroup, SafeHtml>(new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(OverlayDataGroup group) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendHtmlConstant(group.getGenomeName());
				return builder.toSafeHtml();
			}
		};
		
		Column<OverlayDataGroup, SafeHtml> valueColumn = new Column<OverlayDataGroup, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(OverlayDataGroup group) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendHtmlConstant(String.valueOf(group.getMaxStrengthForReactionGenes(reaction)));
				return builder.toSafeHtml();
			}
		};
		
		Column<OverlayDataGroup, SafeHtml> groupUrlColumn = new Column<OverlayDataGroup, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(OverlayDataGroup group) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendHtmlConstant("<a href=\"" + group.getUrl() + "\" target=\"_blank\">" + group.getSource() + "</a>");
				return builder.toSafeHtml();
			}
		};
		
		// add columns to table
		table.addColumn(groupNameColumn, "Group Name");
		table.addColumn(genomeNameColumn, "Source Genome");
		table.addColumn(valueColumn, "Value");
		table.addColumn(groupUrlColumn, "Link");

		// set the data provider
		groupDataProvider.addDataDisplay(table);
		table.setVisibleRange(0, groupDataProvider.getList().size());

		// add a selection model
		final SingleSelectionModel<OverlayDataGroup> selectionModel = new SingleSelectionModel<OverlayDataGroup>(OverlayDataGroup.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);

	}
	
	private void initGeneTable(CellTable<Gene> table, ListDataProvider<Gene> dataProvider) {

		if(table == null || dataProvider == null)
			return;

		// determine whether or not we have measurement data
		boolean hasMeasurementData = false;
		for(Gene gene : dataProvider.getList()){
			if(gene.getMeasurementSet().getMeasurements().size() > 0) {
				hasMeasurementData = true;
				break;
			}
		}

		// initialize table columns
		Column<Gene, SafeHtml> vimssIdColumn = new Column<Gene, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Gene gene) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				String vimssId = gene.getVimssId();
				if(vimssId != null)
					builder.appendHtmlConstant(genLocusLink(vimssId));
				else
					builder.appendHtmlConstant("N/A");
				return builder.toSafeHtml();
			}
		};

		Column<Gene, SafeHtml> ecNumsColumn = new Column<Gene, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Gene gene) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				for(String ecNum : gene.getEcNums()) {
					builder.appendHtmlConstant(genEcNumLink(ecNum, 1));
					builder.appendHtmlConstant("<br>");
				}
				return builder.toSafeHtml();
			}
		};

		TextColumn<Gene> synonymsColumn = new TextColumn<Gene>() {
			@Override
			public String getValue(Gene gene) {
				StringBuilder builder = new StringBuilder();

				String name = gene.getSynonymWithType(Gene.SYNONYM_TYPE_NAME);
				String ncbi = gene.getSynonymWithType(Gene.SYNONYM_TYPE_NCBI);
				String session = gene.getSynonymWithType(Gene.SYNONYM_TYPE_SESSION);

				builder.append(name == null ? "" : name + "\n");
				builder.append(ncbi == null ? "" : ncbi + "\n");
				builder.append(session == null ? "" : session + "\n");

				return builder.toString();
			}
		};

		Column<Gene, SafeHtml> measurementColumn = null;
		if(hasMeasurementData) {
			measurementColumn = new Column<Gene, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(Gene gene) {
					SafeHtmlBuilder builder = new SafeHtmlBuilder();
					for(Measurement measurement : gene.getMeasurementSet().getMeasurements()) {
						builder.appendHtmlConstant(NumberFormat.getFormat("#.###").format(measurement.getValue()) + "<br>");
					}
					return builder.toSafeHtml();
				}
			};
		}

		// add columns to table
		table.addColumn(vimssIdColumn, "VIMSS Id");
		table.addColumn(ecNumsColumn, "EC");
		table.addColumn(synonymsColumn, "Synonyms");
		if(measurementColumn != null)
			table.addColumn(measurementColumn, "Measurement");

		// set the data provider
		dataProvider.addDataDisplay(table);
		table.setVisibleRange(0, dataProvider.getList().size());

		// add a selection model
		final SingleSelectionModel<Gene> selectionModel = new SingleSelectionModel<Gene>(Gene.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
	}

	public void setDataGroups(final Set<OverlayDataGroup> dataGroups) {
		this.dataGroups = dataGroups;
	}
	
	/**
	 * Sets the host to which genes and ec links will be linked.
	 * @param host The host name.
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * Sets the organism.
	 * @param organism The organism.
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;
	}

	/**
	 * Sets the reaction.
	 * @param reaction The reaction.
	 */
	public void setReaction(final Reaction reaction) {

		this.reaction = reaction;

		// set the definition
		view.getDefinitionHtml().setHTML(reaction.getDefinition());

		// set the ec number links
		Set<String> ecNums = reaction.getEcNums();
		Set<Gene> genes = reaction.getGenes();


		view.hideGeneTable();
		view.hideGroupTable();

		if(ecNums == null || ecNums.size() == 0)
			view.getEcNumHtml().setHTML("<b>No EC</b>");
		else {
			Map<String, Set<Gene>> ecNum2Genes = new HashMap<String, Set<Gene>>();
			if(genes != null && !genes.isEmpty()) {
				view.showGeneTable();
				geneDataProvider.getList().addAll(genes);
				initGeneTable(view.getGeneTable(), geneDataProvider);
				for(Gene gene : genes) {
					for(String ecNum : gene.getEcNums()) {
						Set<Gene> genesForEcNum = ecNum2Genes.get(ecNum);
						if(genesForEcNum == null) {
							genesForEcNum = new HashSet<Gene>();
							ecNum2Genes.put(ecNum, genesForEcNum);
						}
						genesForEcNum.add(gene);
					}
				}
			}
			
			if (dataGroups != null && !dataGroups.isEmpty()) {
				view.showGroupTable();
				groupDataProvider.getList().addAll(dataGroups);
				initGroupTable(view.getGroupTable(), groupDataProvider);
				
				for (OverlayDataGroup g : dataGroups) {
					System.out.print(g.getName() + ":");
					Set<Gene> groupGenes = g.getGenesForReaction(reaction);
					for (Gene gene : groupGenes) {
						System.out.print(" " + gene.getVimssId());
					}
					System.out.println();
				}
			}

			StringBuilder builder = new StringBuilder();
			for(String ecNum : ecNums) {
				int numGenes = ecNum2Genes.get(ecNum) != null ? ecNum2Genes.get(ecNum).size() : 0;
				builder.append(genEcNumLink(ecNum, numGenes));
				if(organism != null && !organism.isGlobalMap()) {
					builder.append(": ");
					builder.append(numGenes);
					builder.append(numGenes != 1 ? " genes" : " gene");
				}
				builder.append("<br>");
			}
			view.getEcNumHtml().setHTML(builder.toString());
		}
	}
	
	public void setUser(final User user) {
		this.user = user;
		if(this.user == null || this.user.isGuestUser())
			view.getAddToCartButton().setVisible(false);
		else
			view.getAddToCartButton().setVisible(true);
	}
}
