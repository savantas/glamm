package gov.lbl.glamm.client.presenter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.lbl.glamm.client.events.LoadingEvent;
import gov.lbl.glamm.client.events.GroupDataLoadedEvent;
import gov.lbl.glamm.client.events.GroupDataServiceEvent;
import gov.lbl.glamm.client.events.GroupDataUploadEvent;
import gov.lbl.glamm.client.events.ViewResizedEvent;
import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;

/**
 * A presenter for overlaying arbitrary groups of data on the map.
 * For example, if a user wanted to view a group of reactions corresponding to genes regulated by a set of regulons,
 * this would help with the display.
 * <p>
 * It provides mechanisms for uploading group data that can be mapped onto reactions/compounds, and web services that
 * can fetch mappings from outside sources (e.g. RegPrecise).
 * @author wjriehl
 *
 */
public class GroupDataPresenter {
	
	/**
	 * View interface.
	 * @author wjriehl
	 *
	 */
	public interface View {
		/**
		 * Returns the load button. When pressed, it should load a new dataset.
		 * @return the Button
		 */
		public Button getUploadButton();

		public Button getServiceButton();
		
		/**
		 * Returns the clear button. When pressed, it should clear the loaded data.
		 * @return the Button
		 */
		public Button getClearButton();
		
		/**
		 * Returns an input box for loading. Essentially a placeholder for input.
		 * //TODO - make it not a placeholder...
		 * @return the TextBox
		 */
//		public TextBox getInputBox();
		
		/**
		 * Returns the DisclosurePanel containing the a summary view of loaded group data and widgets for interacting with it.
		 * @return the DisclosurePanel
		 */
		public DisclosurePanel getDisclosurePanel();
		
		/**
		 * Returns the CellTable that holds a summary of loaded group data. Note that this table lives inside a ScrollPanel
		 * @return the CellTable
		 */
		public CellTable<OverlayDataGroup> getGroupTable();
		
		/**
		 * Returns the Panel that houses the CellTable that displays group data.
		 * @return
		 */
		public Panel getGroupTablePanel();
		
		/**
		 * A short label that contains info about the source of the group data.
		 * @return the Label
		 */
		public Label getInfoLabel();
	}
	
	@SuppressWarnings("unused")
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	private ListDataProvider<OverlayDataGroup> groupDataProvider = null;
	
	public GroupDataPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		
		groupDataProvider = new ListDataProvider<OverlayDataGroup>();
		bindView();
		initTable(view.getGroupTable(), groupDataProvider);
	}
	
	public void bindView() {
		
		/*
		 * The load button should load data based on some kind of user input.
		 */
		view.getUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new GroupDataUploadEvent(GroupDataUploadEvent.Action.REQUEST));
			}
		});
		
		view.getServiceButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new GroupDataServiceEvent(GroupDataServiceEvent.Action.REQUEST));
			}
		});
		
		/*
		 * The clear button removes all loaded data and reset the view.
		 */
		view.getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearData();
			}
		});
		
		/*
		 * Opening the disclosure panel triggers a resize event, to reorient the larger panel.
		 */
		view.getDisclosurePanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});
		
		/*
		 * Closing the disclosure panel also triggers a resize event to reorient the smaller panel.
		 */
		view.getDisclosurePanel().addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				eventBus.fireEvent(new ViewResizedEvent());
			}
		});
	}
	
	/**
	 * The CellTable for viewing a data summary has three columns (currently. May be extended based on changing needs):
	 * group name (as text)
	 * color (as a little HTML <div> + css widget)
	 * link out (as SafeHtml)
	 * @param table the CellTable to act on.
	 * @param dataProvider the DataProvider for the OverlayDataGroup
	 */
	public void initTable(CellTable<OverlayDataGroup> table, ListDataProvider<OverlayDataGroup> dataProvider) {

		TextColumn<OverlayDataGroup> nameColumn = new TextColumn<OverlayDataGroup>() {
			public String getValue(OverlayDataGroup g) {
				return g.getName();
			}
		};
		nameColumn.setSortable(true);
		

        Column<OverlayDataGroup, SafeHtml> colorColumn= new Column<OverlayDataGroup, SafeHtml>(new SafeHtmlCell()) {
        	@Override
        	public SafeHtml getValue(OverlayDataGroup g) {
        		SafeHtmlBuilder builder = new SafeHtmlBuilder();
        		builder.appendHtmlConstant("<div style=\"background-color:"
        									+ g.getCssColor()
        									+ ";color:"
        									+ g.getCssColor()
        									+ ";\">x</div>");
        		return builder.toSafeHtml();
        	}
        };
          
        TextColumn<OverlayDataGroup> countColumn = new TextColumn<OverlayDataGroup>() {
        	public String getValue(OverlayDataGroup g) {
        		return String.valueOf(g.getElementSet().size());
        	}
        };
        countColumn.setSortable(true);
		
		Column<OverlayDataGroup, SafeHtml> linkColumn = new Column<OverlayDataGroup, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(OverlayDataGroup g) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				if (g.getUrl() == null)
					builder.appendHtmlConstant("No link available");
				else {
					builder.appendHtmlConstant("<a href=\"" + g.getUrl() + "\" target=\"_new\">");
					if (g.getSource().length() != 0)
						builder.appendHtmlConstant(g.getSource());
					builder.appendHtmlConstant("</a>");
				}
				
				return builder.toSafeHtml();
			}
		};
		
		ListHandler<OverlayDataGroup> nameSortHandler = new ListHandler<OverlayDataGroup>(dataProvider.getList());
		nameSortHandler.setComparator(nameColumn, new Comparator<OverlayDataGroup>() {
			@SuppressWarnings("unused")
			public int compare(OverlayDataGroup g1, OverlayDataGroup g2) {
				if (g1.equals(g2)) {
					return 0;
				}
				if (g1 != null) {
					return (g2 != null) ? g1.getName().compareTo(g2.getName()) : 1;
				}
				return -1;
			}
		});
		nameSortHandler.setComparator(countColumn, new Comparator<OverlayDataGroup>() {
			@SuppressWarnings("unused")
			public int compare(OverlayDataGroup g1, OverlayDataGroup g2) {
				if (g1.equals(g2)) {
					return 0;
				}
				if (g1 != null) {
					if (g2 != null) {
						if (g1.getElementSet().size() < g2.getElementSet().size())
							return -1;
					}
					return 1;
				}
				return 1;
			}
		});
		
		table.addColumnSortHandler(nameSortHandler);
		table.getColumnSortList().push(nameColumn);
		
		table.addColumn(nameColumn, "Group");
		table.addColumn(colorColumn, "Color");
		table.addColumn(countColumn, "Count");
		table.addColumn(linkColumn, "Link");
	
		dataProvider.addDataDisplay(table);
	}

	/**
	 * Removes all loaded group data, empties the summary table, clears the loading textbox, and resets the view.
	 */
	private void clearData() {
		eventBus.fireEvent(new LoadingEvent(false));

		groupDataProvider.getList().clear();
		view.getGroupTablePanel().setVisible(false);
		view.getInfoLabel().setText("none loaded");
		eventBus.fireEvent(new GroupDataLoadedEvent(new HashSet<OverlayDataGroup>()));

		eventBus.fireEvent(new LoadingEvent(true));
	}

	/**
	 * Sets the currently displayed data groups. This populates the summary table with data and triggers a resize event.
	 * @param dataGroups the Set of OverlayDataGroups
	 */
	public void setDataGroups(Set<OverlayDataGroup> dataGroups) {
		List<OverlayDataGroup> dpList = groupDataProvider.getList();
		dpList.clear();
		
		for (OverlayDataGroup g : dataGroups)
			dpList.add(g);
		
		view.getGroupTable().setVisibleRange(0, dpList.size());
		view.getGroupTablePanel().setVisible(dataGroups.size() != 0);
		eventBus.fireEvent(new ViewResizedEvent());
	}
	
//	/**
//	 * Loads the overlay group data from some user input. Pending change to something more robust than loading a simple string.
//	 * 
//	 * Once the data is loaded (via an rpc), it triggers an OverlayDataLoadedEvent with the outcome.
//	 * @param text
//	 */
//	private void loadData(String text, final String source) {
//		eventBus.fireEvent(new LoadingEvent(false));
//
//		rpc.getOverlayData(text, new AsyncCallback<Set<OverlayDataGroup>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				eventBus.fireEvent(new LoadingEvent(true));
//				Window.alert("Remote procedure call failed: getOverlayData");
//			}
//
//			@Override
//			public void onSuccess(Set<OverlayDataGroup> result) {
//				view.getInfoLabel().setText(source);
//				eventBus.fireEvent(new OverlayDataLoadedEvent(result));
//				eventBus.fireEvent(new LoadingEvent(true));
//			}
//		});
//	}

}
