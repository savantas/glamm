package gov.lbl.glamm.client.map.presenter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.lbl.glamm.client.map.events.GroupDataLoadedEvent;
import gov.lbl.glamm.client.map.events.GroupDataPickedEvent;
import gov.lbl.glamm.client.map.events.GroupDataServiceEvent;
import gov.lbl.glamm.client.map.events.GroupDataUploadEvent;
import gov.lbl.glamm.client.map.events.LoadingEvent;
import gov.lbl.glamm.client.map.events.ViewResizedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.OverlayDataGroup;

import com.google.gwt.cell.client.CheckboxCell;
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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
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
		public HasClickHandlers getUploadButton();

		public HasClickHandlers getServiceButton();
		
		/**
		 * Returns the clear button. When pressed, it should clear the loaded data.
		 * @return the Button
		 */
		public HasClickHandlers getClearButton();
		
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
		
		public SuggestBox getSuggestBox();
		
		public HasClickHandlers getPrevButton();
		
		public HasClickHandlers getNextButton();
		
		public HasClickHandlers getSelectAllButton();
		
		public HasClickHandlers getDeselectAllButton();
		
		public void maximize();
		
		public void minimize();
	}
	
	private enum State {
		NO_DATA_LOADED("No group data loaded"),
		DATA_LOADING("Loading..."),
		ALL_SELECTED("Showing all groups"),
		NONE_SELECTED("No groups selected"),
		MULTIPLE_SELECTED("Showing multiple groups");
		
		
		private String statusText;
		
		private State(String statusText) {
			this.statusText = statusText;
		}
		
		String getStatusText() {
			return statusText;
		}
	}
	
	@SuppressWarnings("unused")
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	private ListDataProvider<OverlayDataGroup> groupDataProvider = null;
	private MultiSelectionModel<OverlayDataGroup> selectionModel;
	private MultiWordSuggestOracle suggestOracle;
	private Map<String, OverlayDataGroup> name2DataGroup;
	
	public GroupDataPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		
		groupDataProvider = new ListDataProvider<OverlayDataGroup>();
		bindView();
		initTable(view.getGroupTable(), groupDataProvider);
		setViewState(State.NO_DATA_LOADED);
		suggestOracle = (MultiWordSuggestOracle) view.getSuggestBox().getSuggestOracle();
		name2DataGroup = new HashMap<String, OverlayDataGroup>();
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
		
		view.getPrevButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (groupDataProvider.getList().isEmpty())
					return;
				
				Set<OverlayDataGroup> selectedGroups = selectionModel.getSelectedSet();
				List<OverlayDataGroup> dpList = groupDataProvider.getList();
				if (selectedGroups.isEmpty()) {
					selectionModel.setSelected(dpList.get(0), true);					
				}
				else {
					// Figure out where the top-most (i.e. lowest index) group is.
					// Then select the previous one.
					int topIndex = dpList.size();
					for (OverlayDataGroup g : selectedGroups) {
						int curIndex = dpList.indexOf(g);
						if (curIndex != -1 && curIndex < topIndex)
							topIndex = curIndex;
					}
					if (topIndex != dpList.size()) {
						selectAllGroups(false);
						topIndex--;
						if (topIndex < 0)
							topIndex = dpList.size()-1;
						selectionModel.setSelected(dpList.get(topIndex), true);
					}
				}
			}
		});
		
		view.getNextButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (groupDataProvider.getList().isEmpty())
					return;
				
				Set<OverlayDataGroup> selectedGroups = selectionModel.getSelectedSet();
				List<OverlayDataGroup> dpList = groupDataProvider.getList();
				if (selectedGroups.isEmpty()) {
					selectionModel.setSelected(dpList.get(0), true);					
				}
				else {
					// Figure out where the top-most (i.e. lowest index) group is.
					// Then select the previous one.
					int topIndex = dpList.size();
					for (OverlayDataGroup g : selectedGroups) {
						int curIndex = dpList.indexOf(g);
						if (curIndex != -1 && curIndex < topIndex)
							topIndex = curIndex;
					}
					if (topIndex != dpList.size()) {
						selectAllGroups(false);
						topIndex++;
						if (topIndex >= dpList.size())
							topIndex = 0;
						selectionModel.setSelected(dpList.get(topIndex), true);
					}
				}

			}
		});
		
		view.getSelectAllButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectAllGroups(true);
			}
		});
		
		view.getDeselectAllButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectAllGroups(false);
			}
		});
		
		view.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String name = event.getSelectedItem().getReplacementString();
				
				if (name == null)
					return;
				
				OverlayDataGroup g = name2DataGroup.get(name);
				
				if (g == null)
					return;
				
				selectAllGroups(false);
				selectionModel.setSelected(g, true);
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

		table.setRowStyles(new RowStyles<OverlayDataGroup>() {

			@Override
			public String getStyleNames(OverlayDataGroup row, int rowIndex) {
				if (selectionModel.isSelected(row))
					return "glamm-ExtendedFlexTable-EvenRow";
				
				if (rowIndex % 2 == 0)
					return "glamm-ExtendedFlexTable-EvenRow";
				return "glamm-ExtendedFlexTable-OddRow";
			}
			
		});
		
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
        
        // Add a selection model so we can select cells.
        selectionModel = new MultiSelectionModel<OverlayDataGroup>(OverlayDataGroup.KEY_PROVIDER);
        table.setSelectionModel(selectionModel, DefaultSelectionEventManager.<OverlayDataGroup> createCheckboxManager());
        
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				setView();
			}
        });

    	Column<OverlayDataGroup, Boolean> checkColumn = new Column<OverlayDataGroup, Boolean>(new CheckboxCell(true, false)) {
    		@Override
    		public Boolean getValue(OverlayDataGroup object) {
    			return selectionModel.isSelected(object);
    		}
    	};

        TextColumn<OverlayDataGroup> countColumn = new TextColumn<OverlayDataGroup>() {
        	public String getValue(OverlayDataGroup g) {
        		return String.valueOf(g.getAllElements().size());
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
						if (g1.size() < g2.size())
							return -1;
					}
					return 1;
				}
				return 1;
			}
		});
		
		table.addColumnSortHandler(nameSortHandler);
		table.getColumnSortList().push(nameColumn);

		table.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

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
		name2DataGroup.clear();
		selectionModel.clear();
		view.getGroupTablePanel().setVisible(false);
		setViewState(State.NO_DATA_LOADED);
		view.minimize();
		
		eventBus.fireEvent(new GroupDataLoadedEvent(null));
		eventBus.fireEvent(new LoadingEvent(true));
	}

	/**
	 * Sets the currently displayed data groups. This populates the summary table with data and triggers a resize event.
	 * @param dataGroups the Set of OverlayDataGroups
	 */
	public void setDataGroups(final Set<OverlayDataGroup> dataGroups) {
		if (dataGroups == null || dataGroups.size() == 0)
			return;
		
		setViewState(State.DATA_LOADING);
		List<OverlayDataGroup> dpList = groupDataProvider.getList();
		dpList.clear();
		name2DataGroup.clear();
		
		for (OverlayDataGroup g : dataGroups) {
			dpList.add(g);
			name2DataGroup.put(g.getName(), g);
		}
		
		populateSuggestBox(dataGroups);
		
		view.getGroupTable().setVisibleRange(0, dpList.size());
		view.getGroupTablePanel().setVisible(dataGroups.size() != 0);
		selectAllGroups(true);
		eventBus.fireEvent(new ViewResizedEvent());
		
	}
	
	public void populateSuggestBox(final Set<OverlayDataGroup> dataGroups) {
		((MultiWordSuggestOracle) view.getSuggestBox().getSuggestOracle()).clear();
		
		for (OverlayDataGroup g : dataGroups) {
			suggestOracle.add(g.getName());
		}
	}
	
	/**
	 * Selects all (or zero) groups from the loaded table, depending on the boolean value of select. Note that this does NOT trigger any 
	 * visualization changes for anything, except for the table that shows the list of Data Groups.
	 * @param select If true, selects all groups to be shown. If false, deselects all.
	 */
	private void selectAllGroups(boolean select) {
		for (OverlayDataGroup dg : groupDataProvider.getList()) {
			selectionModel.setSelected(dg, select);
		}
		setViewState(State.ALL_SELECTED);
	}
	
	private void setView() {
		Set<OverlayDataGroup> displaySet = selectionModel.getSelectedSet();
		if (displaySet.size() == 0) {
			if (name2DataGroup.size() == 0)
				setViewState(State.NO_DATA_LOADED);
			else
				setViewState(State.NONE_SELECTED);
		} else if (displaySet.size() == 1) {
			for (OverlayDataGroup g : displaySet)
				view.getSuggestBox().setText(g.getName());
		} else if (displaySet.size() < groupDataProvider.getList().size()) {
			setViewState(State.MULTIPLE_SELECTED);
		} else
			setViewState(State.ALL_SELECTED);
		
		eventBus.fireEvent(new GroupDataPickedEvent(displaySet));
	}
	
	private void setViewState(final State state) {
		view.getSuggestBox().setText(state.getStatusText());
		
		boolean disabled = false;
		if (state == State.DATA_LOADING ||
			state == State.NO_DATA_LOADED)
			disabled = true;
		
		DOM.setElementPropertyBoolean(view.getSuggestBox().getElement(), "disabled", disabled);
	}

}
