package gov.lbl.glamm.client.map;

import gov.lbl.glamm.client.map.events.AMDPickedEvent;
import gov.lbl.glamm.client.map.events.AnnotatedMapDataLoadedEvent;
import gov.lbl.glamm.client.map.events.CpdDstDisambiguatedEvent;
import gov.lbl.glamm.client.map.events.CpdDstPickedEvent;
import gov.lbl.glamm.client.map.events.CpdSrcDisambiguatedEvent;
import gov.lbl.glamm.client.map.events.CpdSrcPickedEvent;
import gov.lbl.glamm.client.map.events.ExperimentUploadEvent;
import gov.lbl.glamm.client.map.events.GroupDataLoadedEvent;
import gov.lbl.glamm.client.map.events.GroupDataPickedEvent;
import gov.lbl.glamm.client.map.events.GroupDataServiceEvent;
import gov.lbl.glamm.client.map.events.GroupDataUploadEvent;
import gov.lbl.glamm.client.map.events.LoadingEvent;
import gov.lbl.glamm.client.map.events.LogInEvent;
import gov.lbl.glamm.client.map.events.LogOutEvent;
import gov.lbl.glamm.client.map.events.MapElementClickEvent;
import gov.lbl.glamm.client.map.events.MapUpdateEvent;
import gov.lbl.glamm.client.map.events.MetabolicModelLoadedEvent;
import gov.lbl.glamm.client.map.events.OrganismPickedEvent;
import gov.lbl.glamm.client.map.events.OrganismUploadEvent;
import gov.lbl.glamm.client.map.events.PanZoomControlEvent;
import gov.lbl.glamm.client.map.events.RoutePickedEvent;
import gov.lbl.glamm.client.map.events.RouteStepPickedEvent;
import gov.lbl.glamm.client.map.events.SamplePickedEvent;
import gov.lbl.glamm.client.map.events.SearchTargetEvent;
import gov.lbl.glamm.client.map.events.ViewResizedEvent;
import gov.lbl.glamm.client.map.presenter.AMDPresenter;
import gov.lbl.glamm.client.map.presenter.AnnotatedMapPresenter;
import gov.lbl.glamm.client.map.presenter.CpdDisambiguationPresenter;
import gov.lbl.glamm.client.map.presenter.ExperimentPresenter;
import gov.lbl.glamm.client.map.presenter.ExperimentUploadPresenter;
import gov.lbl.glamm.client.map.presenter.GroupDataPresenter;
import gov.lbl.glamm.client.map.presenter.GroupDataServicePresenter;
import gov.lbl.glamm.client.map.presenter.GroupDataUploadPresenter;
import gov.lbl.glamm.client.map.presenter.ImagePopupPresenter;
import gov.lbl.glamm.client.map.presenter.InterpolatorPresenter;
import gov.lbl.glamm.client.map.presenter.LoadingPresenter;
import gov.lbl.glamm.client.map.presenter.LoginPresenter;
import gov.lbl.glamm.client.map.presenter.MapElementPresenter;
import gov.lbl.glamm.client.map.presenter.MetabolicModelPresenter;
import gov.lbl.glamm.client.map.presenter.MiniMapPresenter;
import gov.lbl.glamm.client.map.presenter.OrganismPresenter;
import gov.lbl.glamm.client.map.presenter.OrganismUploadPresenter;
import gov.lbl.glamm.client.map.presenter.PanZoomControlPresenter;
import gov.lbl.glamm.client.map.presenter.PwyPopupPresenter;
import gov.lbl.glamm.client.map.presenter.RetrosynthesisPresenter;
import gov.lbl.glamm.client.map.presenter.RxnPopupPresenter;
import gov.lbl.glamm.client.map.rpc.GlammService;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.map.util.Interpolator;
import gov.lbl.glamm.client.map.view.AMDView;
import gov.lbl.glamm.client.map.view.AnnotatedMapView;
import gov.lbl.glamm.client.map.view.CpdDisambiguationView;
import gov.lbl.glamm.client.map.view.ExperimentUploadView;
import gov.lbl.glamm.client.map.view.ExperimentView;
import gov.lbl.glamm.client.map.view.GroupDataServiceView;
import gov.lbl.glamm.client.map.view.GroupDataUploadView;
import gov.lbl.glamm.client.map.view.GroupDataView;
import gov.lbl.glamm.client.map.view.ImagePopupView;
import gov.lbl.glamm.client.map.view.InterpolatorView;
import gov.lbl.glamm.client.map.view.LoadingView;
import gov.lbl.glamm.client.map.view.LoginView;
import gov.lbl.glamm.client.map.view.MapElementView;
import gov.lbl.glamm.client.map.view.MetabolicModelView;
import gov.lbl.glamm.client.map.view.MiniMapView;
import gov.lbl.glamm.client.map.view.OrganismUploadView;
import gov.lbl.glamm.client.map.view.OrganismView;
import gov.lbl.glamm.client.map.view.PanZoomControlView;
import gov.lbl.glamm.client.map.view.PwyPopupView;
import gov.lbl.glamm.client.map.view.RetrosynthesisView;
import gov.lbl.glamm.client.map.view.RxnPopupView;
import gov.lbl.glamm.shared.model.AnnotatedMapData;
import gov.lbl.glamm.shared.model.GlammState;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Top level application controller class, instantiated by Glamm entry point
 * @author jtbates
 * @see Glamm
 */
public class AppController {

	private static AppController instance;
	
	@SuppressWarnings("unused")
	private boolean showUI = true;
	
	private GlammServiceAsync rpc;
	private SimpleEventBus eventBus;
	private AbsolutePanel mainPanel = null;

	private AMDPresenter amdPresenter;
	private AMDView amdView;

	private ImagePopupPresenter citationsPresenter;
	private ImagePopupView citationsView;

	private CpdDisambiguationPresenter cpdDisambiguationPresenter;
	private CpdDisambiguationView cpdDisambiguationView;

	private ExperimentPresenter experimentPresenter;
	private ExperimentView experimentView;

	private ExperimentUploadPresenter experimentUploadPresenter;
	private ExperimentUploadView experimentUploadView;

	private ImagePopupPresenter helpPresenter;
	private ImagePopupView helpView;

	private InterpolatorPresenter interpolatorPresenter;
	private InterpolatorView interpolatorView;

	private LoadingPresenter loadingPresenter;
	private LoadingView loadingView;

	@SuppressWarnings("unused")
	private LoginPresenter loginPresenter;
	private LoginView loginView;

	private RxnPopupPresenter rxnElementPresenter;
	private RxnPopupView rxnElementView;

	private PwyPopupPresenter pwyPresenter;
	private PwyPopupView pwyView;
	
	private MapElementPresenter mapElementPresenter;
	private MapElementView mapElementView;

	private AnnotatedMapPresenter mapPresenter;
	private AnnotatedMapView mapView;

	private MiniMapPresenter miniMapPresenter;
	private MiniMapView miniMapView;

	private OrganismPresenter organismPresenter;
	private OrganismView organismView;

	@SuppressWarnings("unused")
	private OrganismUploadPresenter organismUploadPresenter;
	private OrganismUploadView organismUploadView;

	private PanZoomControlPresenter panZoomPresenter;
	private PanZoomControlView panZoomView;

	private RetrosynthesisPresenter retrosynthesisPresenter;
	private RetrosynthesisView retrosynthesisView;
	
	private MetabolicModelPresenter metabolicModelPresenter;
	private MetabolicModelView metabolicModelView;
	
	private GroupDataPresenter dataOverlayPresenter;
	private GroupDataView dataOverlayView;
	
	@SuppressWarnings("unused")
	private GroupDataUploadPresenter dataOverlayUploadPresenter;
	private GroupDataUploadView dataOverlayUploadView;
	
	@SuppressWarnings("unused")
	private GroupDataServicePresenter dataOverlayServicePresenter;
	private GroupDataServiceView dataOverlayServiceView;

	private CheckBox uiCheckBox;
	
	private LayoutPanel layout = new LayoutPanel() {

		@Override
		public void onResize() {
			AppController.this.onResize();
			super.onResize();
		}

		@Override
		public void onBrowserEvent(Event event) {
			super.onBrowserEvent(event);
		}
	};

	private AppController() {

		rpc = GWT.create(GlammService.class);
		eventBus = new SimpleEventBus();
		mainPanel = new AbsolutePanel();

		amdView = new AMDView();
		amdPresenter = new AMDPresenter(rpc, amdView, eventBus);

		cpdDisambiguationView = new CpdDisambiguationView();
		cpdDisambiguationPresenter = new CpdDisambiguationPresenter(rpc,
				cpdDisambiguationView, eventBus);

		interpolatorView = new InterpolatorView();
		interpolatorPresenter = new InterpolatorPresenter(interpolatorView);

		loadingView = new LoadingView();
		loadingPresenter = new LoadingPresenter(loadingView);

		loginView = new LoginView();
		loginPresenter = new LoginPresenter(rpc, loginView, eventBus);

		mapElementView = new MapElementView();
		mapElementPresenter = new MapElementPresenter(rpc, mapElementView);

		rxnElementView = new RxnPopupView();
		rxnElementPresenter = new RxnPopupPresenter(rpc, rxnElementView, eventBus);
		
		pwyView = new PwyPopupView();
		pwyPresenter = new PwyPopupPresenter(rpc, pwyView, eventBus);

		mapView = new AnnotatedMapView();
		mapPresenter = new AnnotatedMapPresenter(rpc, mapView, eventBus);

		miniMapView = new MiniMapView();
		miniMapPresenter = new MiniMapPresenter(miniMapView, eventBus);

		panZoomView = new PanZoomControlView();
		panZoomPresenter = new PanZoomControlPresenter(rpc, panZoomView,
				eventBus);

		organismView = new OrganismView();
		organismPresenter = new OrganismPresenter(rpc, organismView, eventBus);

		organismUploadView = new OrganismUploadView();
		organismUploadPresenter = new OrganismUploadPresenter(
				organismUploadView, eventBus);

		experimentView = new ExperimentView();
		experimentPresenter = new ExperimentPresenter(rpc, experimentView,
				eventBus);

		experimentUploadView = new ExperimentUploadView();
		experimentUploadPresenter = new ExperimentUploadPresenter(
				experimentUploadView, eventBus);

		citationsView = new ImagePopupView();
		citationsPresenter = new ImagePopupPresenter(citationsView);

		helpView = new ImagePopupView();
		helpPresenter = new ImagePopupPresenter(helpView);

		retrosynthesisView = new RetrosynthesisView();
		retrosynthesisPresenter = new RetrosynthesisPresenter(rpc,
				retrosynthesisView, eventBus);
		
		metabolicModelView = new MetabolicModelView();
		metabolicModelPresenter = new MetabolicModelPresenter(rpc, metabolicModelView, eventBus);
		
		dataOverlayView = new GroupDataView();
		dataOverlayPresenter = new GroupDataPresenter(rpc, dataOverlayView, eventBus);
		
		dataOverlayUploadView = new GroupDataUploadView();
		dataOverlayUploadPresenter = new GroupDataUploadPresenter(dataOverlayUploadView, eventBus);
		
		dataOverlayServiceView = new GroupDataServiceView();
		dataOverlayServicePresenter = new GroupDataServicePresenter(rpc, eventBus, dataOverlayServiceView);
		
		uiCheckBox = new CheckBox();
	}

	/**
	 * Generates or returns the single instance of AppController.
	 * @return The AppController instance.
	 */
	public static AppController instance() {
		if (instance == null)
			instance = new AppController();
		return instance;
	}

	/**
	 * Initialization code called by the Glamm entry point.
	 * @param rlp The RootLayoutPanel
	 * @see Glamm
	 */
	public void start(final RootLayoutPanel rlp) {
		rlp.add(layout);
		rlp.setWidgetTopBottom(layout, 0, Unit.PX, 0, Unit.PX);

		mainPanel.setStylePrimaryName("glamm-global-map");
		layout.add(mainPanel);

		rpc.getStateFromHistoryToken(History.getToken(), new AsyncCallback<GlammState>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Unable to resolve optional state.\nStarting with default options...");
				initialize(GlammState.defaultState());
			}
			
			@Override
			public void onSuccess(GlammState state) {
				initialize(state);
			}
		});
	}
	
	private void initialize(GlammState state) {
		
		/**
		 * This kinda breaks the MVC paradigm... but I think it's the best way to initialize without
		 * potentially throwing a cascade of conflicting RPCs before the map itself is loaded.
		 * 
		 * That is, once the map gets loaded, everything should know what the state is supposed to look like.
		 */
		retrosynthesisPresenter.setOrganism(state.getOrganism());
		rxnElementPresenter.setOrganism(state.getOrganism());
		pwyPresenter.setOrganism(state.getOrganism());
		experimentPresenter.setOrganism(state.getOrganism());
		organismPresenter.setOrganism(state.getOrganism(), false);
		mapElementPresenter.setOrganism(state.getOrganism());
		experimentUploadPresenter.setOrganism(state.getOrganism());
		mapPresenter.setOrganism(state.getOrganism());
		
		loadMapPanel(state); // always load first
		
		loadAnnotatedMapPicker(state);
		loadCpdDisambiguation();
		loadInterpolator();
		loadLoadingPanel();
		loadMapElementPopup();
		loadRxnElementPopup(state);
		loadPwyElementPopup();
		loadMiniMapPanel();
		loadPanZoomControl();
/**/	loadOrganismPicker();
		loadOrganismUpload();
/**/	loadExperimentPicker();
		loadExperimentUpload();
		loadCitations();
		loadHelp();
		loadRetrosynthesis();

/**/	loadMetabolicModelPicker(state);
/**/	loadDataOverlayControl(state);
		loadDataOverlayUpload();
/**/	loadDataOverlayService();

		loadLogin(); // always load last

		onResize();

		eventBus.addHandler(ViewResizedEvent.TYPE,
				new ViewResizedEvent.Handler() {
			@Override
			public void onViewResized(ViewResizedEvent event) {
				onResize();
			}
		});
		
		uiCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setUIVisible(event.getValue());
			}
		});
		uiCheckBox.setValue(state.getUIState());
		setUIVisible(state.getUIState());
		
		mainPanel.add(uiCheckBox, 0, 0);

		initHistory();
	}
	
	private void initHistory() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				final String token = event.getValue();
				
				rpc.getStateFromHistoryToken(token, new AsyncCallback<GlammState>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("RPC failed: getStateFromHistoryToken\nNot changing state.");
						
					}

					@Override
					public void onSuccess(GlammState state) {
						/**
						 * Only set if they're different from the state we're loading!
						 * Don't need to refire loading events if we're just loading up the same species again.
						 * 
						 * Then again, this should be handled by the presenters... right?
						 */
						organismPresenter.setOrganism(state.getOrganism(), true);
						amdPresenter.selectMap(state.getAMDId());
						
						if (state.getGroupData() != null && state.getGroupData().size() != 0)
							eventBus.fireEvent(new GroupDataLoadedEvent(state.getGroupData()));
					}
				});
			}
		});
	}
	
	/**
	 * Computes widget position when the window is resized.
	 * All resize computations are performed at the end of the current event loop.
	 */
	private void onResize() {
		
		// perform resize computations at the end of the current event loop
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				int minWidth = retrosynthesisView.getOffsetWidth() + 
				organismView.getOffsetWidth() + 
				experimentView.getOffsetWidth() + 
				loginView.getOffsetWidth() + 
				helpView.getOffsetWidth() + 
				20;

				mainPanel.setWidgetPosition(organismView, retrosynthesisView.getOffsetWidth() + 5, 0);
				if(Window.getClientWidth() > minWidth) 
					mainPanel.setWidgetPosition(experimentView, retrosynthesisView.getOffsetWidth() + organismView.getOffsetWidth() + 10, 0);
				else
					mainPanel.setWidgetPosition(experimentView, 0, organismView.getOffsetHeight() + 5);
				
				mainPanel.setWidgetPosition(miniMapView, 0, Window.getClientHeight() - miniMapView.getOffsetHeight());
				mainPanel.setWidgetPosition(panZoomView, miniMapView.getOffsetWidth() + 1, Window.getClientHeight() - panZoomView.getOffsetHeight());
				mainPanel.setWidgetPosition(amdView, 0, Window.getClientHeight() - amdView.getOffsetHeight() - miniMapView.getOffsetHeight() - 5);
				mainPanel.setWidgetPosition(citationsView, Window.getClientWidth() - citationsView.getOffsetWidth(), Window.getClientHeight() - citationsView.getOffsetHeight());
				mainPanel.setWidgetPosition(interpolatorView, Window.getClientWidth() - interpolatorView.getOffsetWidth(), Window.getClientHeight() - citationsView.getOffsetHeight() - interpolatorView.getOffsetHeight() - 5);
				mainPanel.setWidgetPosition(helpView, Window.getClientWidth() - helpView.getOffsetWidth(), 0);
				mainPanel.setWidgetPosition(loginView, Window.getClientWidth() - loginView.getOffsetWidth() - helpView.getOffsetWidth() - 5, 0);
				mainPanel.setWidgetPosition(metabolicModelView, 0, Window.getClientHeight() - miniMapView.getOffsetHeight() - amdView.getOffsetHeight() - metabolicModelView.getOffsetHeight() - 10);
				mainPanel.setWidgetPosition(dataOverlayView, panZoomView.getOffsetWidth() + miniMapView.getOffsetWidth() + 10, Window.getClientHeight() - dataOverlayView.getOffsetHeight());

				mainPanel.setWidgetPosition(uiCheckBox, Window.getClientWidth() - uiCheckBox.getOffsetWidth(), helpView.getOffsetHeight());
			}
		});
	}

	private void setUIVisible(boolean show) {
		showUI = show;
		organismView.setVisible(show);
		experimentView.setVisible(show);
		loginView.setVisible(show);
		amdView.setVisible(show);
		metabolicModelView.setVisible(show);
		dataOverlayView.setVisible(show);
		retrosynthesisView.setVisible(show);
		miniMapView.setVisible(show);
		panZoomView.setVisible(show);
		
		onResize();
	}
	
	private void loadDataOverlayControl(GlammState state) {
		mainPanel.add(dataOverlayView, 0, 0);
		
		dataOverlayPresenter.setDataGroups(state.getGroupData());
		
		eventBus.addHandler(GroupDataLoadedEvent.TYPE, new GroupDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(GroupDataLoadedEvent event) {
				dataOverlayPresenter.setDataGroups(event.getData());
			}
		});
		
		//TODO add events that should trigger the controller to clear itself.
	}
	
	private void loadDataOverlayService() {
		eventBus.addHandler(GroupDataServiceEvent.TYPE, new GroupDataServiceEvent.Handler() {
			@Override
			public void onRequest(GroupDataServiceEvent event) {
				dataOverlayServiceView.showView();
			}

			@Override
			public void onSuccess(GroupDataServiceEvent event) {
				return;
			}
		});
	}
	
	private void loadDataOverlayUpload() {
		eventBus.addHandler(GroupDataUploadEvent.TYPE, new GroupDataUploadEvent.Handler() {
			@Override
			public void onRequest(GroupDataUploadEvent event) {
				dataOverlayUploadView.showView();				
			}

			@Override
			public void onSuccess(GroupDataUploadEvent event) {
				return;
			}
		});
	}
	
	private void loadMetabolicModelPicker(GlammState state) {
		mainPanel.add(metabolicModelView, 0, 0);
//		metabolicModelPresenter.populate("0");
	}
	
	private void loadAnnotatedMapPicker(GlammState state) {
		mainPanel.add(amdView, 0, 0);
		amdPresenter.populate(state.getAMDId());
	}

	private void loadCpdDisambiguation() {
		
		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				cpdDisambiguationPresenter.clearCpdChoices();
				cpdDisambiguationView.hideView();
			}
		});

		eventBus.addHandler(CpdDstPickedEvent.TYPE,
				new CpdDstPickedEvent.Handler() {
			@Override
			public void onPicked(CpdDstPickedEvent event) {
				cpdDisambiguationPresenter.clearCpdChoices();
				cpdDisambiguationPresenter.addDstCompounds(event
						.getCompounds());
				cpdDisambiguationView.showView();
			}
		});

		eventBus.addHandler(CpdSrcPickedEvent.TYPE,
				new CpdSrcPickedEvent.Handler() {
			@Override
			public void onPicked(CpdSrcPickedEvent event) {
				cpdDisambiguationPresenter.clearCpdChoices();
				cpdDisambiguationPresenter.addSrcCompounds(event
						.getCompounds());
				cpdDisambiguationView.showView();
			}
		});
	}

	private void loadLoadingPanel() {
		eventBus.addHandler(LoadingEvent.TYPE, new LoadingEvent.Handler() {
			@Override
			public void onLoading(LoadingEvent event) {
				loadingPresenter.update(event.isDoneLoading());
			}
		});
	}

	private void loadMapElementPopup() {

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				mapElementPresenter.setSample(null);
			}
		});

		eventBus.addHandler(MapElementClickEvent.TYPE,
				new MapElementClickEvent.Handler() {
			@Override
			public void onMapElementClick(
					final MapElementClickEvent event) {
				mapElementPresenter.killPopup();
				if(!event.getElementClass().equals(AnnotatedMapData.ElementClass.RXN) &&
				   !event.getElementClass().equals(AnnotatedMapData.ElementClass.MAP))
					mapElementPresenter.showPopup(event.getElementClass(), event.getIds(), 
							event.getClientX(), event.getClientY());
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(final OrganismPickedEvent event) {
				mapElementPresenter.setOrganism(event.getOrganism());
				mapElementPresenter.setSample(null);
			}
		});

		eventBus.addHandler(SamplePickedEvent.TYPE,
				new SamplePickedEvent.Handler() {
			@Override
			public void onSamplePicked(final SamplePickedEvent event) {
				mapElementPresenter.setSample(event.getSample());
			}
		});

		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			@Override
			public void onMapUpdate(MapUpdateEvent event) {
				mapElementPresenter.killPopup();
			}
		});
	}

	private void loadRxnElementPopup(GlammState state) {

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				rxnElementPresenter.setSample(null);
			}
		});
		
		eventBus.addHandler(LogInEvent.TYPE, new LogInEvent.Handler() {
			@Override
			public void onLogIn(LogInEvent event) {
				rxnElementPresenter.setUser(event.getUser());
			}
		});
		
		eventBus.addHandler(LogOutEvent.TYPE, new LogOutEvent.Handler() {
			@Override
			public void onLogOut(LogOutEvent event) {
				rxnElementPresenter.setUser(User.guestUser());
			}
		});

		eventBus.addHandler(MapElementClickEvent.TYPE,
				new MapElementClickEvent.Handler() {
			@Override
			public void onMapElementClick(
					final MapElementClickEvent event) {
				rxnElementPresenter.killPopup();
				if(event.getElementClass().equals(AnnotatedMapData.ElementClass.RXN))
					rxnElementPresenter.showPopup(event.getIds(), 
							event.getClientX(), event.getClientY());
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(final OrganismPickedEvent event) {
				rxnElementPresenter.setOrganism(event.getOrganism());
				rxnElementPresenter.setSample(null);
			}
		});

		eventBus.addHandler(SamplePickedEvent.TYPE,
				new SamplePickedEvent.Handler() {
			@Override
			public void onSamplePicked(final SamplePickedEvent event) {
				rxnElementPresenter.setSample(event.getSample());
			}
		});

		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			@Override
			public void onMapUpdate(MapUpdateEvent event) {
				rxnElementPresenter.killPopup();
			}
		});
		
		eventBus.addHandler(RoutePickedEvent.TYPE,
				new RoutePickedEvent.Handler() {
			@Override
			public void onPicked(RoutePickedEvent event) {
				rxnElementPresenter.setSample(null);
			}
		});
		
		eventBus.addHandler(GroupDataLoadedEvent.TYPE, new GroupDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(GroupDataLoadedEvent event) {
				rxnElementPresenter.setDataGroups(event.getData());
			}
		});
		
		if (state.getGroupData() != null && state.getGroupData().size() > 0)
			rxnElementPresenter.setDataGroups(state.getGroupData());

	}

	private void loadPwyElementPopup() {

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				pwyPresenter.setSample(null);
			}
		});
		
		eventBus.addHandler(LogInEvent.TYPE, new LogInEvent.Handler() {
			@Override
			public void onLogIn(LogInEvent event) {
				pwyPresenter.setUser(event.getUser());
			}
		});
		
		eventBus.addHandler(LogOutEvent.TYPE, new LogOutEvent.Handler() {
			@Override
			public void onLogOut(LogOutEvent event) {
				pwyPresenter.setUser(User.guestUser());
			}
		});

		eventBus.addHandler(MapElementClickEvent.TYPE,
				new MapElementClickEvent.Handler() {
			@Override
			public void onMapElementClick(
					final MapElementClickEvent event) {
				pwyPresenter.killPopup();
				if(event.getElementClass().equals(AnnotatedMapData.ElementClass.MAP) &&
				   !event.isControlKeyDown())
					pwyPresenter.showPopup(event.getIds(), event.getClientX(), event.getClientY());
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(final OrganismPickedEvent event) {
				pwyPresenter.setOrganism(event.getOrganism());
				pwyPresenter.setSample(null);
			}
		});

		eventBus.addHandler(SamplePickedEvent.TYPE,
				new SamplePickedEvent.Handler() {
			@Override
			public void onSamplePicked(final SamplePickedEvent event) {
				pwyPresenter.setSample(event.getSample());
			}
		});

		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			@Override
			public void onMapUpdate(MapUpdateEvent event) {
				pwyPresenter.killPopup();
			}
		});
		
		eventBus.addHandler(RoutePickedEvent.TYPE,
				new RoutePickedEvent.Handler() {
			@Override
			public void onPicked(RoutePickedEvent event) {
				pwyPresenter.setSample(null);
			}
		});
	}
	
	private void loadMapPanel(GlammState state) {

		mainPanel.add(mapView, 0, 0);

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				mapPresenter.setMapData(event.getMapData());
			}
		});

		eventBus.addHandler(AMDPickedEvent.TYPE, new AMDPickedEvent.Handler() {
			@Override
			public void onPicked(AMDPickedEvent event) {
				mapPresenter.loadMapDataFromDescriptor(event.getDescriptor());
			}
		});

		eventBus.addHandler(PanZoomControlEvent.TYPE,
				new PanZoomControlEvent.Handler() {
			public void onPanZoom(PanZoomControlEvent event) {

				switch(event.getAction()) {
				case PAN_UP:
					mapPresenter.translateNorm(0, -0.05f);
					break;
				case PAN_DOWN:
					mapPresenter.translateNorm(0, 0.05f);
					break;
				case PAN_LEFT:
					mapPresenter.translateNorm(0.05f, 0.0f);
					break;
				case PAN_RIGHT:
					mapPresenter.translateNorm(-0.05f, 0.0f);
					break;
				case ZOOM_TO_FIT:
					mapPresenter.fitMapToWindow();
					break;
				case ZOOM_IN:
				case ZOOM_OUT:
				case ZOOM_SLIDER: 
					mapPresenter.setZoomNormAboutPoint(
							event.getZoomNorm(), 
							Window.getClientWidth() >> 1, 
							Window.getClientHeight() >> 1);
					break;
				case NONE:
				default:
					break;
				}
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(OrganismPickedEvent event) {
				mapPresenter.updateMapForOrganism(event.getOrganism());
			}
		});

		eventBus.addHandler(SamplePickedEvent.TYPE,
				new SamplePickedEvent.Handler() {
			@Override
			public void onSamplePicked(SamplePickedEvent event) {
				mapPresenter.updateMapForSample(event.getSample());
			}
		});

		eventBus.addHandler(RoutePickedEvent.TYPE,
				new RoutePickedEvent.Handler() {
			@Override
			public void onPicked(RoutePickedEvent event) {
				mapPresenter.updateMapForRoute(event.getCpdSrc(),
						event.getCpdDst(), event.getRoute());
			}
		});

		eventBus.addHandler(RouteStepPickedEvent.TYPE,
				new RouteStepPickedEvent.Handler() {
			@Override
			public void onPicked(RouteStepPickedEvent event) {
				mapPresenter.updateMapForRouteStep(event.getReaction());
			}
		});

		eventBus.addHandler(SearchTargetEvent.TYPE,
				new SearchTargetEvent.Handler() {
			@Override
			public void onPicked(SearchTargetEvent event) {
				mapPresenter.updateMapForSearchTarget(event.getTargets());
			}
		});

		eventBus.addHandler(MapElementClickEvent.TYPE, new MapElementClickEvent.Handler() {
			public void onMapElementClick(final MapElementClickEvent event) {
				if (event.isControlKeyDown()) {
					if (event.getElementClass().equals(AnnotatedMapData.ElementClass.MAP))
						mapPresenter.updateMapForPathway(event.getIds());
					else {
						mapPresenter.updateMapForPathway(null);
					}
				}
			}
		});
		
		eventBus.addHandler(MetabolicModelLoadedEvent.TYPE, new MetabolicModelLoadedEvent.Handler() {
			@Override
			public void onLoaded(MetabolicModelLoadedEvent event) {
				mapPresenter.updateMapForMetabolicModel(event.getModel(), event.showAllReactions());
			}			
		});
		
		eventBus.addHandler(GroupDataPickedEvent.TYPE, new GroupDataPickedEvent.Handler() {
			@Override
			public void onDataPicked(GroupDataPickedEvent event) {
				mapPresenter.updateMapForGroupData(event.getData());
			}
		});
		
		if (state.getGroupData() != null && state.getGroupData().size() > 0)
			mapPresenter.updateMapForGroupData(state.getGroupData());
		
	}

	private void loadMiniMapPanel() {
		//		miniMapPresenter.setMiniMap(GlammClientBundle.INSTANCE.globalMiniMap().getURL());
		mainPanel.add(miniMapView, 0, 0);

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				miniMapPresenter.setMiniMapUrl(event.getMapData().getIconUrl());
			}
		});

		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			@Override
			public void onMapUpdate(MapUpdateEvent event) {
				miniMapPresenter.updateReticle(event.getViewRectNorm());
			}
		});
	}

	private void loadOrganismPicker() {
		mainPanel.add(organismView, 0, 0);
		organismPresenter.populate();
		
		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				organismPresenter.resetOrganism();
			}
		});

		eventBus.addHandler(OrganismUploadEvent.TYPE,
				new OrganismUploadEvent.Handler() {
			@Override
			public void onRequest(OrganismUploadEvent event) {
				return; // do nothing
			}

			@Override
			public void onSuccess(OrganismUploadEvent event) {
				organismPresenter.populate();
			}
		});

		eventBus.addHandler(ExperimentUploadEvent.TYPE,
				new ExperimentUploadEvent.Handler() {
			@Override
			public void onRequest(ExperimentUploadEvent event) {
				return;
			}

			@Override
			public void onSuccess(ExperimentUploadEvent event) {
				organismPresenter.updateDataTypeChoices();
			}
		});

		eventBus.addHandler(LogInEvent.TYPE, new LogInEvent.Handler() {
			@Override
			public void onLogIn(LogInEvent event) {
				organismPresenter.updateDataTypeChoices();
			}
		});

		eventBus.addHandler(LogOutEvent.TYPE, new LogOutEvent.Handler() {
			@Override
			public void onLogOut(LogOutEvent event) {
				organismPresenter.updateDataTypeChoices();
			}
		});

	}

	/**
	 * Initializes experiment picker widget
	 */
	private void loadExperimentPicker() {
		mainPanel.add(experimentView, 0, 0);

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
//				experimentPresenter.setOrganism(Organism.globalMap());
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(OrganismPickedEvent event) {
				experimentPresenter.setOrganism(event.getOrganism());
			}
		});

		eventBus.addHandler(ExperimentUploadEvent.TYPE,
				new ExperimentUploadEvent.Handler() {
			@Override
			public void onSuccess(ExperimentUploadEvent event) {
				experimentPresenter.setOrganism(event.getOrganism());
			}

			@Override
			public void onRequest(ExperimentUploadEvent event) {
				return; // do nothing
			}
		});

		eventBus.addHandler(RoutePickedEvent.TYPE,
				new RoutePickedEvent.Handler() {

			@Override
			public void onPicked(RoutePickedEvent event) {
				experimentPresenter.clearSuggestBox();
			}
		});

		eventBus.addHandler(LogInEvent.TYPE, new LogInEvent.Handler() {
			@Override
			public void onLogIn(LogInEvent event) {
				experimentPresenter.setUser(event.getUser());
				experimentPresenter.populate();
			}
		});

		eventBus.addHandler(LogOutEvent.TYPE, new LogOutEvent.Handler() {
			@Override
			public void onLogOut(LogOutEvent event) {
				experimentPresenter.setUser(User.guestUser());
				experimentPresenter.populate();
			}
		});

	}

	private void loadExperimentUpload() {
		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(OrganismPickedEvent event) {
				experimentUploadPresenter.setOrganism(event
						.getOrganism());
			}
		});

		eventBus.addHandler(ExperimentUploadEvent.TYPE,
				new ExperimentUploadEvent.Handler() {
			@Override
			public void onRequest(ExperimentUploadEvent event) {
				experimentUploadView.showView();
			}

			@Override
			public void onSuccess(ExperimentUploadEvent event) {
				return; // do nothing
			}
		});
	}

	private void loadInterpolator() {
		mainPanel.add(interpolatorView, 0, 0);

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				interpolatorView.hide();
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(OrganismPickedEvent event) {
				interpolatorView.hide();
			}
		});

		eventBus.addHandler(RoutePickedEvent.TYPE,
				new RoutePickedEvent.Handler() {
			@Override
			public void onPicked(RoutePickedEvent event) {
				interpolatorView.hide();
			}
		});

		eventBus.addHandler(SamplePickedEvent.TYPE,
				new SamplePickedEvent.Handler() {
			@Override
			public void onSamplePicked(SamplePickedEvent event) {
				Sample sample = event.getSample();
				if (sample == null)
					interpolatorView.hide();
				else {
					Interpolator interpolator = Interpolator
					.getInterpolatorForSample(sample);
					interpolatorPresenter.setInterpolator(interpolator);
					interpolatorView.show();
					onResize();
				}
			}
		});
	}

	private void loadLogin() {
		mainPanel.add(loginView, 0, 0);
	}

	private void loadOrganismUpload() {
		eventBus.addHandler(OrganismUploadEvent.TYPE,
				new OrganismUploadEvent.Handler() {
			@Override
			public void onSuccess(OrganismUploadEvent event) {
				return; // do nothing
			}

			@Override
			public void onRequest(OrganismUploadEvent event) {
				organismUploadView.showView();
			}
		});
	}

	private void loadPanZoomControl() {
		mainPanel.add(panZoomView, 0, 0);
		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			public void onMapUpdate(MapUpdateEvent event) {
				panZoomPresenter.setSliderValue(event.getZoomNorm());
			}
		});
	}

	private void loadCitations() {

		final String ACTION_GEN_CITATIONS = "genCitationsPopup";
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("glammServlet");
		urlBuilder.setParameter("action", ACTION_GEN_CITATIONS);

		citationsPresenter.setDefaultImageUrl(GlammClientBundle.INSTANCE
				.glammLogoDefault().getSafeUri().asString());
		citationsPresenter.setMouseOverImageUrl(GlammClientBundle.INSTANCE
				.glammLogoMouseOver().getSafeUri().asString());
		citationsPresenter.setPopupContentUrl(urlBuilder.buildString());

		citationsView.setImageSize("208px", "40px");
		citationsView.setPopupSize("55em", "30em");

		mainPanel.add(citationsView, 0, 0);
	}

	private void loadHelp() {

		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("tutorial/index.html");

		helpPresenter.setDefaultImageUrl(GlammClientBundle.INSTANCE
				.helpIconDefault().getSafeUri().asString());
		helpPresenter.setPopupContentUrl(urlBuilder.buildString());

		helpView.setImageSize("32px", "32px");
		helpView.setPopupSize("90em", "50em");

		mainPanel.add(helpView, 0, 0);
	}

	private void loadRetrosynthesis() {
		mainPanel.add(retrosynthesisView, 0, 0);

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				retrosynthesisPresenter.setMapData(event.getMapData());
//				retrosynthesisPresenter.setOrganism(Organism.globalMap());
				retrosynthesisPresenter.populate();
				retrosynthesisPresenter.reset();
			}
		});

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(OrganismPickedEvent event) {
				retrosynthesisPresenter.setOrganism(event.getOrganism());
				retrosynthesisPresenter.populate();
				retrosynthesisPresenter.reset();
			}
		});

		eventBus.addHandler(SamplePickedEvent.TYPE,
				new SamplePickedEvent.Handler() {
			@Override
			public void onSamplePicked(SamplePickedEvent event) {
				retrosynthesisPresenter.reset();
			}
		});

		eventBus.addHandler(CpdDstDisambiguatedEvent.TYPE,
				new CpdDstDisambiguatedEvent.Handler() {
			@Override
			public void onDisambiguated(CpdDstDisambiguatedEvent event) {
				retrosynthesisPresenter.setCpdDst(event.getCompound());
			}
		});

		eventBus.addHandler(CpdSrcDisambiguatedEvent.TYPE,
				new CpdSrcDisambiguatedEvent.Handler() {
			@Override
			public void onDisambiguated(CpdSrcDisambiguatedEvent event) {
				retrosynthesisPresenter.setCpdSrc(event.getCompound());
			}
		});
	}
}
