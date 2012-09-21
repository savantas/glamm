package gov.lbl.glamm.client.experiment;

import gov.lbl.glamm.client.experiment.dao.DefaultExperimentDao;
import gov.lbl.glamm.client.experiment.dao.DefaultPathwayDao;
import gov.lbl.glamm.client.experiment.dao.ExperimentDao;
import gov.lbl.glamm.client.experiment.dao.PathwayDao;
import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.RawDataReceivedEvent;
import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.presenter.DataRetrievalPresenter;
import gov.lbl.glamm.client.experiment.presenter.DisplayControlPresenter;
import gov.lbl.glamm.client.experiment.presenter.ExpZoomScrollerPresenter;
import gov.lbl.glamm.client.experiment.presenter.ExperimentDataPresenter;
import gov.lbl.glamm.client.experiment.presenter.PathwayGuideColorPresenter;
import gov.lbl.glamm.client.experiment.presenter.PathwayLayoutPresenter;
import gov.lbl.glamm.client.experiment.presenter.PrimaryZoomScrollerPresenter;
import gov.lbl.glamm.client.experiment.presenter.SampleGuidePresenter;
import gov.lbl.glamm.client.experiment.presenter.SecondaryExperimentDataPresenter;
import gov.lbl.glamm.client.experiment.presenter.SecondaryMetabolitePresenter;
import gov.lbl.glamm.client.experiment.presenter.SecondaryZoomScrollerPresenter;
import gov.lbl.glamm.client.experiment.presenter.ZoomScrollerPresenter;
import gov.lbl.glamm.client.experiment.rpc.PathwayExperimentService;
import gov.lbl.glamm.client.experiment.rpc.PathwayExperimentServiceAsync;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.LengthsAndRatios;
import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.client.experiment.util.SVGUtil;
import gov.lbl.glamm.client.experiment.view.AboveRightPopupPanel;
import gov.lbl.glamm.client.experiment.view.BelowRightPopupPanel;
import gov.lbl.glamm.client.experiment.view.DataRetrievalView;
import gov.lbl.glamm.client.experiment.view.DisplayControlView;
import gov.lbl.glamm.client.experiment.view.ExperimentDataView;
import gov.lbl.glamm.client.experiment.view.PathwayGuideColorView;
import gov.lbl.glamm.client.experiment.view.PathwayLayoutView;
import gov.lbl.glamm.client.experiment.view.SampleGuideView;
import gov.lbl.glamm.client.experiment.view.SecondaryExperimentDataView;
import gov.lbl.glamm.client.experiment.view.SecondaryMetaboliteView;
import gov.lbl.glamm.client.experiment.view.ZoomScrollerView;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Sample.DataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Main application controller.
 * Processes raw pathway and experiment xml data into model and view objects
 * as a source for visual rendering.
 *
 * @author DHAP Digital, Inc - angie
 */
@SuppressWarnings("deprecation")
public class AppController implements RawDataReceivedEvent.Handler {
	private static AppController instance = null;

	public static final float DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET = 175;
	public static final float DEFAULT_PRIMARY_WIDGET_ROW_BOTTOM_OFFSET = 30;
	public static final float DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET = 60;
	public static final float DEFAULT_SAMPLE_GUIDE_WIDGET_TOP_OFFSET = 25;
	public static final float DEFAULT_PANEL_PADDING_HEIGHT = 2;

	public static AppController getInstance() {
		if ( instance == null ) {
			instance = new AppController();
		}
		return instance;
	}


	/*
	 * Instance
	 */
	// widgets
	private LayoutPanel layout = new LayoutPanel() {
		@Override
		public void onResize() {
			AppController.this.onResize();
			super.onResize();
		}

		@Override
		public void onBrowserEvent(Event event) {
			// TODO Auto-generated method stub
			super.onBrowserEvent(event);
		}
	};

	private final PathwayExperimentServiceAsync pathwayExperimentService
			= GWT.create( PathwayExperimentService.class );

	@SuppressWarnings("unused")
	private PathwayLayoutPresenter pathLayoutPresenter = null;
	private PathwayLayoutView pathLayoutView = null;
	@SuppressWarnings("unused")
	private PathwayGuideColorPresenter pathwayGuideColorPresenter = null;
	private PathwayGuideColorView pathwayGuideColorView = null;
	private DataRetrievalPresenter dataRetrievalPresenter = null;
	private DataRetrievalView dataRetrievalView = null;
	private ZoomScrollerPresenter primaryZoomScrollerPresenter = null;
	private ZoomScrollerView primaryZoomScrollerView = null;
	private ZoomScrollerPresenter expZoomScrollerPresenter = null;
	private ZoomScrollerView expZoomScrollerView = null;
	private ZoomScrollerPresenter secondZoomScrollerPresenter = null;
	private ZoomScrollerView secondZoomScrollerView = null;
	@SuppressWarnings("unused")
	private SecondaryMetabolitePresenter secondaryMetabPresenter = null;
	private SecondaryMetaboliteView secondaryMetabView = null;
	private SimplePanel secondMetabLabelPanel = null;

	private String[][] dataTypeElementTypeArray = null;
	/** panels grouping primary-secondary experiment views */
	private ArrayList<LayoutPanel> expViewsGroupPanelList = null;
	private ArrayList<SimplePanel> expDataTypeLabelPanelList = null;
	private ArrayList<Object[]> expSampleGuidePresenterViewList = null;
	private ArrayList<Object[]> primaryExpPresenterViewList = null;
	private ArrayList<Object[]> secondaryExpPresenterViewList = null;
	private ArrayList<String> dynamicDefBaseNameList = null;

	@SuppressWarnings("unused")
	private DisplayControlPresenter displayControlPresenter = null;
	private DisplayControlView displayControlView = null;

	private AboveRightPopupPanel topRightPopup = new AboveRightPopupPanel();
	private AboveRightPopupPanel topRightPopup2 = new AboveRightPopupPanel();
	private BelowRightPopupPanel sampleGuidePopup = new BelowRightPopupPanel();

	// events
	private HandlerManager eventBus = null;

	// performance and debug
	private Boolean isInDebugMode = null;
	public long requestResponseMillisecs = 0;
	public long daoProcessingMillisecs = 0;
	public Date startSVGDate = new Date();
	public Date endSVGDate = new Date();
	public long drawSVGMillisecs = 0;

	// data
	private List<Experiment> experiments = null;
	private List<ViewPathway> pathways = null;
	private String[] pathwayGuideColors = null;

	private Comparator<ViewCompound> viewCompoundIdComparator = new Comparator<ViewCompound>() {
		@Override
		public int compare( ViewCompound o1, ViewCompound o2 ) {
			return o1.getBaseObject().getGuid().compareTo(o2.getBaseObject().getGuid());
		}
	};
	private BinarySortedSet<ViewCompound> secondaryMetaboliteSet = null;
	private BinarySortedSet<ViewCompound> displaySecondaryMetaboliteList = null;

	private ObjectCount objCount = new ObjectCount();

	// drawing data
	private float mainSymbolSize = 12;
	private DrawUtil drawUtil = null;
	private HeatMapUtil heatMapUtil = null;
	private HeatMapUtil primZoomScrollerHeatMap = null;
	private float zoomScrollerThickness = 0;
	private float pathwayGuideColorThickness = 0;
	private float pathwayLayoutWidth = 0;
	private float sampleGuideHeight = 0;
	private float experimentLeftOffset = 0;
	private float experimentDataTotalWidth = 0;
	private float experimentDataWidth = 0;

	/* the (calculated) viewable (excluding borders, etc) size of the panels */
	private float primaryPanelHeight = 0;
	private float primaryPanelViewableHeight = 0;
	private float secondaryPanelHeight = 0;
	private float secondaryPanelViewableHeight = 0;

	private AppController() {
		this.calculateWidgetDimensions();

		this.topRightPopup.setStyleName("nodeInfo");
		this.topRightPopup2.setStyleName("nodeInfo");
		this.sampleGuidePopup.setStyleName("nodeInfo");

		this.eventBus = new HandlerManager(null);
		this.drawUtil = new DrawUtil( mainSymbolSize
				, topRightPopup, topRightPopup2, sampleGuidePopup );
		this.heatMapUtil = new HeatMapUtil( new float[]{ 0, 0.5f, 1.0f }
			, new int[]{ 0x0000ff, 0x999999, 0xFFFF00 } );
		this.primZoomScrollerHeatMap = new HeatMapUtil(
				new float[]{ 0, 0.33f, 0.66f, 1}
				, new int[]{ 0x990000, 0x883300, 0xeab011, 0xffcc88 } );

		this.pathLayoutView = new PathwayLayoutView(this.drawUtil, this.heatMapUtil
				, this.pathwayLayoutWidth, this.primaryPanelViewableHeight);
		this.pathLayoutView.setStyleName("primaryPanel");
		this.pathLayoutPresenter = new PathwayLayoutPresenter( pathLayoutView, eventBus );
		this.dataRetrievalView = new DataRetrievalView();
		this.dataRetrievalPresenter = new DataRetrievalPresenter( dataRetrievalView, eventBus );

		this.pathwayGuideColorView = new PathwayGuideColorView(
				drawUtil, null
				, this.zoomScrollerThickness, this.primaryPanelViewableHeight );
		this.pathwayGuideColorView.setStyleName("primaryPanel leftPanel");
		this.pathwayGuideColorPresenter = new PathwayGuideColorPresenter(
				pathwayGuideColorView, eventBus, drawUtil );

		this.primaryZoomScrollerView = new ZoomScrollerView(
				ZoomScrollerView.Orientation.VERTICAL, true, drawUtil
				, this.zoomScrollerThickness, this.primaryPanelViewableHeight );
		this.primaryZoomScrollerPresenter = new PrimaryZoomScrollerPresenter(
				primaryZoomScrollerView, eventBus, drawUtil, primZoomScrollerHeatMap
				, this.primaryPanelViewableHeight );

		this.expZoomScrollerView = new ZoomScrollerView(
				ZoomScrollerView.Orientation.HORIZONTAL, false, drawUtil
				, experimentDataTotalWidth, zoomScrollerThickness );
		this.expZoomScrollerPresenter = new ExpZoomScrollerPresenter(
				expZoomScrollerView, eventBus, drawUtil, experimentDataWidth );

		this.secondZoomScrollerView = new ZoomScrollerView(
				ZoomScrollerView.Orientation.VERTICAL, false, drawUtil
				, zoomScrollerThickness, secondaryPanelViewableHeight );
		this.secondZoomScrollerPresenter = new SecondaryZoomScrollerPresenter(
				secondZoomScrollerView, eventBus, drawUtil
				, secondaryPanelViewableHeight );

		this.secondMetabLabelPanel = new SimplePanel();
		this.secondMetabLabelPanel.addStyleName("dataTypeLabelPanel");
		this.secondMetabLabelPanel.addStyleName("labelControlWrapper");
		this.secondMetabLabelPanel.addStyleName("boundedPanel");
		Label label = new Label("Secondary Metabolites");
		label.addStyleName("dataTypeLabel");
		this.secondMetabLabelPanel.add(label);
		this.secondaryMetabView = new SecondaryMetaboliteView(this.drawUtil, this.heatMapUtil
				, this.pathwayLayoutWidth, this.secondaryPanelViewableHeight );
		this.secondaryMetabView.setStyleName("secondaryPanel leftPanel");
		this.secondaryMetabPresenter = new SecondaryMetabolitePresenter( secondaryMetabView, eventBus );

		this.displayControlView = new DisplayControlView();
		this.displayControlView.addStyleName("leftPanel");
		this.displayControlView.addStyleName("bottomPanel");
		this.displayControlView.addStyleName("labelControlWrapper");
		this.displayControlPresenter = new DisplayControlPresenter(
				displayControlView, eventBus );

		eventBus.addHandler(RawDataReceivedEvent.ASSOCIATED_TYPE, this);
		eventBus.addHandler(ObjectCountEvent.ASSOCIATED_TYPE
				, new ObjectCountEvent.Handler() {
			@Override
			public void handleObjectCountUpdated(ObjectCountEvent ocEvent) {
				if ( ObjectCountEvent.CountType.VIEW
						== ocEvent.getCountType() ) {
					AppController.this.objCount.add(ocEvent.getCount());
					AppController.this.eventBus.fireEvent(
							new ObjectCountEvent( ObjectCountEvent
									.CountType.TOTAL, objCount )
							);
				}
			}
		});
	}

	/**
	 * Entry point method.
	 *
	 * @param rp
	 * @param rlp
	 */
	public void start( RootPanel rp, RootLayoutPanel rlp ) {
		layout = new LayoutPanel();
		layout.setStyleName("ge");
		layout.setVisible(true);

		rlp.add(layout);
		rlp.setWidgetTopBottom(layout, 0, Unit.PX, 0, Unit.PX);

		loadDataRetrieval();
		loadPrimaryZoomScroller();
		loadPathwayGuideColors();
		loadExpZoomScroller();
		loadSecondaryZoomScroller();
		loadPathLayout();

		loadSecondaryMetaboliteLayout();
		createLoadPlayControlPlaceholder();
		loadSecondMetabLabelPanel();

		loadDisplayControlPanel();
		
		// retrieve data
		this.dataRetrievalPresenter.requestFromServer( pathwayExperimentService );
	}

	/**
	 * Calculate panel sizes, depending upon the window size and
	 * the number of experiment data types.
	 */
	protected void calculateWidgetDimensions() {
		this.zoomScrollerThickness = 25;
		this.pathwayGuideColorThickness = 8;
		this.pathwayLayoutWidth = Window.getClientWidth() * 0.27f;
		this.pathwayLayoutWidth = ( 340 > pathwayLayoutWidth ) ? 340 : pathwayLayoutWidth;
		this.sampleGuideHeight = DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET
				- DEFAULT_SAMPLE_GUIDE_WIDGET_TOP_OFFSET;
		this.experimentLeftOffset = zoomScrollerThickness
				+ pathwayGuideColorThickness
				+ pathwayLayoutWidth;
		this.experimentDataTotalWidth = Window.getClientWidth()
				- (experimentLeftOffset);
		if ( this.dataTypeElementTypeArray == null ) {
			this.experimentDataWidth = 0;
		} else {
			this.experimentDataWidth = experimentDataTotalWidth
				/ this.dataTypeElementTypeArray.length;
		}

		this.primaryPanelHeight = Window.getClientHeight()
		- (DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET+DEFAULT_PRIMARY_WIDGET_ROW_BOTTOM_OFFSET
			);
		this.primaryPanelViewableHeight = this.primaryPanelHeight
				- DEFAULT_PANEL_PADDING_HEIGHT;
		this.secondaryPanelHeight = DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET
				- DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET;
		this.secondaryPanelViewableHeight = this.secondaryPanelHeight
				- DEFAULT_PANEL_PADDING_HEIGHT;
	}

	/**
	 * Remove current set of grouped primary and secondary metabolite/gene/reaction
	 * experiment data panels and replace with new panels in accordance with data.
	 */
	protected void reInitExperimentPresenterViewGroupList() {
		// Clear the primary and secondary handlers and presenters
		if ( primaryExpPresenterViewList != null ) {
			for ( Object[] presenterView : primaryExpPresenterViewList ) {
				((ExperimentDataPresenter)presenterView[0]).removeHandlers();
			}
			primaryExpPresenterViewList.clear();
		}
		if ( secondaryExpPresenterViewList != null ) {
			for ( Object[] presenterView : secondaryExpPresenterViewList ) {
				((SecondaryExperimentDataPresenter)presenterView[0]).removeHandlers();
			}
			secondaryExpPresenterViewList.clear();
		}
		// Clear the labels and sample guide strips
		if ( expDataTypeLabelPanelList != null ) {
			expDataTypeLabelPanelList.clear();
		}
		if ( expSampleGuidePresenterViewList != null ) {
			for ( Object[] presenterView : expSampleGuidePresenterViewList ) {
				((SampleGuidePresenter)presenterView[0]).removeHandlers();
			}
		}
		// Clear the list of grouped experiment panels
		//	and remove all the experiment views from the main layout
		if ( expViewsGroupPanelList != null ) {
			for ( Panel expViewPanel : expViewsGroupPanelList ) {
				expViewPanel.clear();
				layout.remove(expViewPanel);
			}
			expViewsGroupPanelList.clear();
		}

		// Add new experiment and grouping panels as appropriate
		if ( dataTypeElementTypeArray != null ) {
			expViewsGroupPanelList = new ArrayList<LayoutPanel>();
			expDataTypeLabelPanelList = new ArrayList<SimplePanel>();
			expSampleGuidePresenterViewList = new ArrayList<Object[]>();
			primaryExpPresenterViewList = new ArrayList<Object[]>();
			secondaryExpPresenterViewList = new ArrayList<Object[]>();

			for ( int i=0; i<dataTypeElementTypeArray.length; i++ ) {
				String[] dataTypeElementType = dataTypeElementTypeArray[i];
				// Grouping panels
				LayoutPanel expViewsGroupPanel = new LayoutPanel();
				expViewsGroupPanel.addStyleName("experimentGroupPanel");
				expViewsGroupPanelList.add(expViewsGroupPanel);
				loadExpViewsGroup(expViewsGroupPanel, i);

				// Data type labels
				SimplePanel dataTypeLabelPanel = createLoadExpViewsLabel(
						expViewsGroupPanel, dataTypeElementType[0], i );
				expDataTypeLabelPanelList.add(dataTypeLabelPanel);

				// Sample guide strips
				SampleGuideView guideView = new SampleGuideView( drawUtil, heatMapUtil
						, experimentDataWidth - DEFAULT_PANEL_PADDING_HEIGHT
						, sampleGuideHeight - DEFAULT_PANEL_PADDING_HEIGHT
						);
				guideView.addStyleName("boundedPanel");
				SampleGuidePresenter guidePresenter = new SampleGuidePresenter(
						guideView, eventBus, drawUtil );
				expSampleGuidePresenterViewList.add(
						new Object[] { guidePresenter, guideView } );
				loadSampleGuideView( expViewsGroupPanel, guideView, i );

				// Primary experiment presenters and views
				ExperimentDataView primView = new ExperimentDataView( drawUtil, heatMapUtil
						, experimentDataWidth, primaryPanelViewableHeight
						, dataTypeElementType[0], dataTypeElementType[1]
						);
				primView.setRange(-2, 2);
				primView.addStyleName("primaryPanel");
				if ( i == dataTypeElementTypeArray.length-1 ) {
					primView.addStyleName("rightPanel");
				}
				ExperimentDataPresenter primPresenter = new ExperimentDataPresenter(
						primView, eventBus );
				primaryExpPresenterViewList.add(
						new Object[] { primPresenter, primView } );
				loadPrimaryExpDataView(expViewsGroupPanel, primView, i);

				// Primary experiment presenters and views
				SecondaryExperimentDataView secView = new SecondaryExperimentDataView(
						drawUtil, heatMapUtil
						, experimentDataWidth, primaryPanelViewableHeight
						, dataTypeElementType[0], dataTypeElementType[1]
						);
				secView.setRange(-2, 2);
				secView.addStyleName("secondaryPanel");
				if ( i == dataTypeElementTypeArray.length-1 ) {
					secView.addStyleName("rightPanel");
				}
				SecondaryExperimentDataPresenter secPresenter
						= new SecondaryExperimentDataPresenter( secView, eventBus );
				secondaryExpPresenterViewList.add(
						new Object[] { secPresenter, secView } );
				loadSecondaryExpDataView(expViewsGroupPanel, secView, i);
			}
		}
	}

	public void loadExpViewsGroup( LayoutPanel expViewsGroupPanel, int order ) {
		if ( layout.getWidgetIndex(expViewsGroupPanel) < 0 ) {
			layout.add(expViewsGroupPanel);
			this.resizeExpViewsGroup(expViewsGroupPanel, order);
		}
	}
	public void resizeExpViewsGroup( LayoutPanel expViewsGroupPanel, int order ) {
		layout.setWidgetTopHeight( expViewsGroupPanel
				, 0, Unit.PX
				, Window.getClientHeight() - DEFAULT_PRIMARY_WIDGET_ROW_BOTTOM_OFFSET
				, Unit.PX );
		layout.setWidgetLeftWidth( expViewsGroupPanel
				, experimentLeftOffset + order*this.experimentDataWidth
				, Unit.PX
				, this.experimentDataWidth, Unit.PX );
	}

	public SimplePanel createLoadExpViewsLabel( LayoutPanel expViewsGroupPanel
			, String labelString, int order ) {
		SimplePanel labelPanel = new SimplePanel();
		labelPanel.addStyleName("dataTypeLabelPanel");
		labelPanel.addStyleName("labelControlWrapper");
		if ( order == dataTypeElementTypeArray.length-1 ) {
			labelPanel.addStyleName("rightPanel");
		}
		labelPanel.setVisible(true);
		Label label = new Label(labelString);
		label.addStyleName("dataTypeLabel");
		labelPanel.add(label);

		expViewsGroupPanel.add(labelPanel);
		this.resizeExpViewsLabel(expViewsGroupPanel, labelPanel, order);

		return labelPanel;
	}
	public void resizeExpViewsLabel( LayoutPanel expViewsGroupPanel
			, SimplePanel labelPanel, int order ) {
		expViewsGroupPanel.setWidgetTopHeight( labelPanel
				, 0, Unit.PX
				, DEFAULT_SAMPLE_GUIDE_WIDGET_TOP_OFFSET, Unit.PX );
	}

	public void loadSampleGuideView( LayoutPanel expViewsGroupPanel
			, SampleGuideView view, int order ) {
		if ( expViewsGroupPanel.getWidgetIndex(view) < 0 ) {
			expViewsGroupPanel.add(view);
			this.resizeSampleGuideView(expViewsGroupPanel, view, order);
		}
	}
	public void resizeSampleGuideView( LayoutPanel expViewsGroupPanel
			, SampleGuideView view, int order ) {
		expViewsGroupPanel.setWidgetTopHeight( view
				, DEFAULT_SAMPLE_GUIDE_WIDGET_TOP_OFFSET, Unit.PX
				, sampleGuideHeight, Unit.PX );
	}

	public void loadPrimaryExpDataView( LayoutPanel expViewsGroupPanel
			, ExperimentDataView view, int order ) {
		if ( expViewsGroupPanel.getWidgetIndex(view) < 0 ) {
			expViewsGroupPanel.add(view);
			this.resizePrimaryExpDataLayout(expViewsGroupPanel, view, order);
		}
	}
	public void resizePrimaryExpDataLayout( LayoutPanel expViewsGroupPanel
			, ExperimentDataView view, int order ) {
		expViewsGroupPanel.setWidgetTopHeight( view
				, DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET, Unit.PX
				, this.primaryPanelHeight, Unit.PX );
	}

	public void loadSecondaryExpDataView( LayoutPanel expViewsGroupPanel
			, SecondaryExperimentDataView view, int order ) {
		if ( expViewsGroupPanel.getWidgetIndex(view) < 0 ) {
			expViewsGroupPanel.add(view);
			this.resizeSecondaryExpDataLayout(expViewsGroupPanel, view, order);
		}
	}
	public void resizeSecondaryExpDataLayout( LayoutPanel expViewsGroupPanel
			, SecondaryExperimentDataView view, int order ) {
		expViewsGroupPanel.setWidgetTopHeight( view
				, DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET, Unit.PX
				, this.secondaryPanelHeight
				, Unit.PX );
	}

	/**
	 * Resize child panels relative to the client window.
	 */
	public void onResize() {
		this.calculateWidgetDimensions();
		this.resizeDataRetrievalView();

		this.primaryZoomScrollerPresenter.resetTargetViewLength(this.primaryPanelViewableHeight);
		this.primaryZoomScrollerView.setWidgetViewWidth(this.zoomScrollerThickness);
		this.primaryZoomScrollerView.setWidgetViewHeight(this.primaryPanelViewableHeight);
		this.primaryZoomScrollerView.setAreaThicknessFromViewableThickness();
		this.resizePrimaryZoomScroller();

		this.expZoomScrollerPresenter.resetTargetViewLength(
				experimentDataWidth );
		this.expZoomScrollerView.setWidgetViewWidth( experimentDataTotalWidth );
		this.expZoomScrollerView.setWidgetViewHeight( zoomScrollerThickness );
		this.expZoomScrollerView.setAreaThicknessFromViewableThickness();
		this.resizeExpZoomScroller();

		this.secondZoomScrollerPresenter.resetTargetViewLength(
				secondaryPanelViewableHeight );
		this.secondZoomScrollerView.setWidgetViewWidth( zoomScrollerThickness );
		this.secondZoomScrollerView.setWidgetViewHeight( secondaryPanelViewableHeight );
		this.secondZoomScrollerView.setAreaThicknessFromViewableThickness();
		this.resizeSecondaryZoomScroller();

		this.pathwayGuideColorView.setWidgetViewWidth( pathwayGuideColorThickness );
		this.pathwayGuideColorView.setWidgetViewHeight( primaryPanelViewableHeight );
		this.pathwayGuideColorView.reDraw();
		this.resizePathwayGuideColors();

		this.pathLayoutView.setWidgetViewWidth(this.pathwayLayoutWidth);
		this.pathLayoutView.setWidgetViewHeight(this.primaryPanelViewableHeight);
		this.resizePathLayout();
		this.secondaryMetabView.setWidgetViewWidth(this.pathwayLayoutWidth);
		this.secondaryMetabView.setWidgetViewHeight(this.secondaryPanelViewableHeight);
		this.resizeSecondaryMetaboliteLayout();
		this.resizeSecondMetabLabelPanel();

		for ( int i=0; i<expViewsGroupPanelList.size(); i++ ) {
			LayoutPanel expViewsGroupPanel = expViewsGroupPanelList.get(i);
			this.resizeExpViewsGroup(expViewsGroupPanel, i);

			// Label and sample guide strip
			resizeExpViewsLabel( expViewsGroupPanel
					, expDataTypeLabelPanelList.get(i), i );
			resizeSampleGuideView( expViewsGroupPanel
					, ((SampleGuideView)expSampleGuidePresenterViewList.get(i)[1])
					, i );

			// Primary experiment views
			Object[] primPresenterView = primaryExpPresenterViewList.get(i);
			resizePrimaryExpDataLayout( expViewsGroupPanel
					, (ExperimentDataView)primPresenterView[1], i );
			((ExperimentDataView)primPresenterView[1]).setWidgetViewWidth(
					this.experimentDataWidth );
			((ExperimentDataView)primPresenterView[1]).setWidgetViewHeight(
					this.primaryPanelViewableHeight );

			// Secondary experiment views
			Object[] secPresenterView = secondaryExpPresenterViewList.get(i);
			resizeSecondaryExpDataLayout( expViewsGroupPanel
					, (SecondaryExperimentDataView)secPresenterView[1], i );
			((SecondaryExperimentDataView)secPresenterView[1]).setWidgetViewWidth(
					this.experimentDataWidth );
			((SecondaryExperimentDataView)secPresenterView[1]).setWidgetViewHeight(
					this.secondaryPanelViewableHeight );
		}
	}

	public void loadDataRetrieval() {
		if ( layout.getWidgetIndex(dataRetrievalView) < 0 ) {
			layout.add(dataRetrievalView);
			this.resizeDataRetrievalView();
		}
	}
	public void resizeDataRetrievalView() {
		layout.setWidgetTopHeight(dataRetrievalView, 0, Unit.PX, 100, Unit.PX);
	}

	public void loadPrimaryZoomScroller() {
		if ( layout.getWidgetIndex(primaryZoomScrollerView) < 0 ) {
			layout.add(primaryZoomScrollerView);
			this.resizePrimaryZoomScroller();
		}
	}
	public void resizePrimaryZoomScroller() {
		layout.setWidgetTopHeight( primaryZoomScrollerView
				, DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET, Unit.PX
				, this.primaryPanelHeight, Unit.PX );
		layout.setWidgetLeftWidth( primaryZoomScrollerView, 0, Unit.PX
				, this.zoomScrollerThickness, Unit.PX );
	}

	public void loadExpZoomScroller() {
		if ( layout.getWidgetIndex(expZoomScrollerView) < 0 ) {
			layout.add(expZoomScrollerView);
			resizeExpZoomScroller();
		}
	}
	public void resizeExpZoomScroller() {
		layout.setWidgetTopHeight( expZoomScrollerView
				, Window.getClientHeight() - DEFAULT_PRIMARY_WIDGET_ROW_BOTTOM_OFFSET
				, Unit.PX
				, zoomScrollerThickness, Unit.PX );
		layout.setWidgetLeftWidth( expZoomScrollerView
				, experimentLeftOffset, Unit.PX
				, experimentDataTotalWidth, Unit.PX );
	}

	public void loadSecondaryZoomScroller() {
		if ( layout.getWidgetIndex(secondZoomScrollerView) < 0 ) {
			layout.add(secondZoomScrollerView);
			resizeSecondaryZoomScroller();
		}
	}
	public void resizeSecondaryZoomScroller() {
		layout.setWidgetTopHeight( secondZoomScrollerView
				, DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET
				, Unit.PX
				, secondaryPanelHeight, Unit.PX );
		layout.setWidgetLeftWidth( secondZoomScrollerView
				, 0, Unit.PX
				, zoomScrollerThickness, Unit.PX );
	}

	public void loadPathwayGuideColors() {
		if ( layout.getWidgetIndex(pathwayGuideColorView) < 0 ) {
			layout.add(pathwayGuideColorView);
			this.resizePathwayGuideColors();
		}
	}
	public void resizePathwayGuideColors() {
		layout.setWidgetTopHeight( pathwayGuideColorView
				, DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET, Unit.PX
				, this.primaryPanelHeight, Unit.PX );
		layout.setWidgetLeftWidth( pathwayGuideColorView
				, this.zoomScrollerThickness, Unit.PX
				, this.pathwayGuideColorThickness, Unit.PX );
	}

	public void loadPathLayout() {
		if ( layout.getWidgetIndex(pathLayoutView) < 0 ) {
			layout.add(pathLayoutView);
			this.resizePathLayout();
		}
	}
	public void resizePathLayout() {
		layout.setWidgetTopHeight( pathLayoutView
				, DEFAULT_PRIMARY_WIDGET_ROW_TOP_OFFSET, Unit.PX
				, this.primaryPanelHeight, Unit.PX );
		layout.setWidgetLeftWidth( pathLayoutView
				, this.zoomScrollerThickness+this.pathwayGuideColorThickness, Unit.PX
				, this.pathwayLayoutWidth, Unit.PX );
	}

	public void loadSecondMetabLabelPanel() {
		if ( layout.getWidgetIndex(secondMetabLabelPanel) < 0 ) {
			layout.add(secondMetabLabelPanel);
			this.resizeSecondMetabLabelPanel();
		}
	}
	public void resizeSecondMetabLabelPanel() {
		layout.setWidgetTopHeight( secondMetabLabelPanel
				, DEFAULT_SAMPLE_GUIDE_WIDGET_TOP_OFFSET, Unit.PX
				, sampleGuideHeight
				, Unit.PX );
		layout.setWidgetLeftWidth( secondMetabLabelPanel
				, 0, Unit.PX
				, experimentLeftOffset, Unit.PX );
	}
	public void loadSecondaryMetaboliteLayout() {
		if ( layout.getWidgetIndex(secondaryMetabView) < 0 ) {
			layout.add(secondaryMetabView);
			this.resizeSecondaryMetaboliteLayout();
		}
	}
	public void resizeSecondaryMetaboliteLayout() {
		layout.setWidgetTopHeight( secondaryMetabView
				, DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET, Unit.PX
				, this.secondaryPanelHeight
				, Unit.PX );
		layout.setWidgetLeftWidth( secondaryMetabView
				, zoomScrollerThickness+pathwayGuideColorThickness, Unit.PX
				, this.pathwayLayoutWidth, Unit.PX );
	}

	public void createLoadPlayControlPlaceholder() {
		SimplePanel labelPanel = new SimplePanel();
		labelPanel.addStyleName("labelControlWrapper");
		labelPanel.addStyleName("leftPanel");
		labelPanel.addStyleName("boundedPanel");
		layout.add(labelPanel);
		layout.setWidgetTopHeight( labelPanel
				, 0, Unit.PX
				, DEFAULT_SECONDARY_WIDGET_ROW_TOP_OFFSET, Unit.PX );
		layout.setWidgetLeftWidth( labelPanel
				, 0, Unit.PX
				, experimentLeftOffset, Unit.PX );
	}

	public void loadDisplayControlPanel() {
		if ( layout.getWidgetIndex( displayControlView ) < 0 ) {
			layout.add(displayControlView);
			this.resizeDisplayControlPanel();
		}
	}
	public void resizeDisplayControlPanel() {
		layout.setWidgetTopHeight( displayControlView
				, Window.getClientHeight() - DEFAULT_PRIMARY_WIDGET_ROW_BOTTOM_OFFSET
				, Unit.PX
				, DEFAULT_PRIMARY_WIDGET_ROW_BOTTOM_OFFSET, Unit.PX );
		layout.setWidgetLeftWidth( displayControlView
				, 0, Unit.PX
				, experimentLeftOffset, Unit.PX );
	}

	/**
	 * Parse the raw data into model and view objects.
	 * Call methods to refresh panels and their sizes.
	 * Fire event to report that new data is available.
	 * Also sets dataTypeElementTypeArray that is used to associate each
	 * data type with an element type (i.e. gene, compound, reaction).
	 *
	 * @see gov.lbl.glamm.experiment.client.events.RawDataReceivedEvent.Handler#handleDataReceived(gov.lbl.glamm.experiment.client.events.RawDataReceivedEvent)
	 */
	@Override
	public void handleDataReceived(RawDataReceivedEvent drEvent) {
		objCount.reset();
		this.requestResponseMillisecs = drEvent.getRequestResponseMillisecs();
		Date daoProcessingStartDate = new Date();

//		this.parsePathwayXML(drEvent.getData(), objCount);

		// Pathway data
		// TODO: use a deferred instantiation
		PathwayDao pathwayDao = new DefaultPathwayDao();
		pathways = new ArrayList<ViewPathway>();
		objCount.listCount++;
		secondaryMetaboliteSet = new BinarySortedSet<ViewCompound>( viewCompoundIdComparator );
		objCount.listCount++;
		pathwayDao.addPathwayData( pathways, secondaryMetaboliteSet
				, drEvent.getPEData(), objCount );

		this.setPathwayGuideColors( this.pathways );
		// Calculate/set visual positioning info
		this.drawUtil.setPathwayComponentPositions(this.pathways);
		this.dynamicDefBaseNameList = new ArrayList<String>();
		this.displaySecondaryMetaboliteList = this.drawUtil
				.createDisplaySecondaryMetaboliteList( this.secondaryMetaboliteSet
						, dynamicDefBaseNameList, objCount );

		// Experiment data
		// TODO: use a deferred instantiation
//		this.parseExperimentXML(drEvent.getData(), objCount);
		ExperimentDao experimentDao = new DefaultExperimentDao();
		HashMap<DataType,Object[]> dataTypeElementAssocMap = new HashMap<DataType,Object[]>();
		this.experiments = new ArrayList<Experiment>();
		objCount.expListCount++;
		experimentDao.addExperimentData( experiments, dataTypeElementAssocMap
				, drEvent.getPEData(), objCount );
		dataTypeElementTypeArray =
				createOrderedArrayFromDataTypeMap( dataTypeElementAssocMap );
		// TODO: handle empty pathways
		// TODO: handle invalid formats

		Date daoProcessingEndDate = new Date();
		this.daoProcessingMillisecs = daoProcessingEndDate.getTime() - daoProcessingStartDate.getTime();

		this.startSVGDate = new Date();
		this.calculateWidgetDimensions();
		this.reInitExperimentPresenterViewGroupList();
		this.onResize();

		/*this.eventBus.fireEvent(
				new ObjectCountEvent( ObjectCountEvent.CountType.TOTAL, objCount )
		);*/

		DeferredCommand.addCommand(new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent( new DataChangedEvent(
						pathways, pathwayGuideColors, experiments
						, displaySecondaryMetaboliteList, dynamicDefBaseNameList
				) );
			}
		});
	}

	/**
	 * @param dataTypeElementAssocMap
	 * @return array of String[] elements.
	 *  Each String[] element is either null or { dataType.name(), elementType }
	 *  where elementType is, depending on the original data,
	 *  one of "rxm", "gene", "compound"
	 */
	public String[][] createOrderedArrayFromDataTypeMap(
			HashMap<DataType,Object[]> dataTypeElementAssocMap ) {
		String[][] list = new String[dataTypeElementAssocMap.size()][];
		for ( DataType dataType : dataTypeElementAssocMap.keySet() ) {
			Object[] elementAssoc = dataTypeElementAssocMap.get(dataType);
			int order = (Integer) elementAssoc[0];
			String element = (String) elementAssoc[1];
			String[] item = new String[] { dataType.name(), element };
			list[order] = item;
		}
		return list;
	}

	/**
	 * For use if/when pathway guide colors are not externally set.
	 * @param pathways TODO
	 */
	private void setPathwayGuideColors( List<ViewPathway> pathways ) {
		LengthsAndRatios lengthsRatios = drawUtil
				.getPathwayLengthsAndRatios(pathways);
		float[] pathwayLengthRatios = lengthsRatios.getRatios();
		int[] colors = primZoomScrollerHeatMap.generateColors(pathwayLengthRatios);
		pathwayGuideColors = new String[colors.length];
		for ( int i=0; i<colors.length; i++ ) {
			pathwayGuideColors[i] = SVGUtil.convertToCssColor(colors[i]);
		}
	}

	/**
	 * Debug
	 */
	public boolean isInDebugMode() {
		if ( isInDebugMode == null ) {
			String debugParam = Window.Location.getParameter( "debug" );
			if ( debugParam != null && debugParam.equalsIgnoreCase( "true" ) ) {
				isInDebugMode = Boolean.TRUE;
			}
			else
				isInDebugMode = Boolean.FALSE;
		}
		return isInDebugMode;
	}
}
