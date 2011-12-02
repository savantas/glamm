package gov.lbl.glamm.client;

import gov.lbl.glamm.client.events.AMDPickedEvent;
import gov.lbl.glamm.client.events.AnnotatedMapDataLoadedEvent;
import gov.lbl.glamm.client.events.CpdDstDisambiguatedEvent;
import gov.lbl.glamm.client.events.CpdDstPickedEvent;
import gov.lbl.glamm.client.events.CpdSrcDisambiguatedEvent;
import gov.lbl.glamm.client.events.CpdSrcPickedEvent;
import gov.lbl.glamm.client.events.ExperimentUploadEvent;
import gov.lbl.glamm.client.events.LoadingEvent;
import gov.lbl.glamm.client.events.LogInEvent;
import gov.lbl.glamm.client.events.LogOutEvent;
import gov.lbl.glamm.client.events.MapElementClickEvent;
import gov.lbl.glamm.client.events.MapUpdateEvent;
import gov.lbl.glamm.client.events.OrganismPickedEvent;
import gov.lbl.glamm.client.events.OrganismUploadEvent;
import gov.lbl.glamm.client.events.PanZoomControlEvent;
import gov.lbl.glamm.client.events.RoutePickedEvent;
import gov.lbl.glamm.client.events.RouteStepPickedEvent;
import gov.lbl.glamm.client.events.SamplePickedEvent;
import gov.lbl.glamm.client.events.SearchTargetEvent;
import gov.lbl.glamm.client.events.ViewResizedEvent;
import gov.lbl.glamm.client.model.AnnotatedMapData;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.presenter.AMDPresenter;
import gov.lbl.glamm.client.presenter.AnnotatedMapPresenter;
import gov.lbl.glamm.client.presenter.CpdDisambiguationPresenter;
import gov.lbl.glamm.client.presenter.ExperimentPresenter;
import gov.lbl.glamm.client.presenter.ExperimentUploadPresenter;
import gov.lbl.glamm.client.presenter.ImagePopupPresenter;
import gov.lbl.glamm.client.presenter.InterpolatorPresenter;
import gov.lbl.glamm.client.presenter.LoadingPresenter;
import gov.lbl.glamm.client.presenter.LoginPresenter;
import gov.lbl.glamm.client.presenter.MapElementPresenter;
import gov.lbl.glamm.client.presenter.MiniMapPresenter;
import gov.lbl.glamm.client.presenter.OrganismPresenter;
import gov.lbl.glamm.client.presenter.OrganismUploadPresenter;
import gov.lbl.glamm.client.presenter.PanZoomControlPresenter;
import gov.lbl.glamm.client.presenter.RetrosynthesisPresenter;
import gov.lbl.glamm.client.presenter.RxnPopupPresenter;
import gov.lbl.glamm.client.rpc.GlammService;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.util.Interpolator;
import gov.lbl.glamm.client.view.AMDView;
import gov.lbl.glamm.client.view.AnnotatedMapView;
import gov.lbl.glamm.client.view.CpdDisambiguationView;
import gov.lbl.glamm.client.view.ExperimentUploadView;
import gov.lbl.glamm.client.view.ExperimentView;
import gov.lbl.glamm.client.view.ImagePopupView;
import gov.lbl.glamm.client.view.InterpolatorView;
import gov.lbl.glamm.client.view.LoadingView;
import gov.lbl.glamm.client.view.LoginView;
import gov.lbl.glamm.client.view.MapElementView;
import gov.lbl.glamm.client.view.MiniMapView;
import gov.lbl.glamm.client.view.OrganismUploadView;
import gov.lbl.glamm.client.view.OrganismView;
import gov.lbl.glamm.client.view.PanZoomControlView;
import gov.lbl.glamm.client.view.RetrosynthesisView;
import gov.lbl.glamm.client.view.RxnPopupView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Top level application controller class, instantiated by Glamm entry point
 * @author jtbates
 * @see Glamm
 */
public class AppController {

	private static AppController instance;

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

		loadMapPanel(); // always load first
		loadAnnotatedMapPicker();
		loadCpdDisambiguation();
		loadInterpolator();
		loadLoadingPanel();
		loadMapElementPopup();
		loadRxnElementPopup();
		loadMiniMapPanel();
		loadPanZoomControl();
		loadOrganismPicker();
		loadOrganismUpload();
		loadExperimentPicker();
		loadExperimentUpload();
		loadCitations();
		loadHelp();
		loadRetrosynthesis();
		loadLogin(); // always load last

		onResize();

		eventBus.addHandler(ViewResizedEvent.TYPE,
				new ViewResizedEvent.Handler() {
			@Override
			public void onViewResized(ViewResizedEvent event) {
				onResize();
			}
		});
	}

	/**
	 * Computes widget position when the window is resized.
	 * All resize computations are peformed at the end of the current event loop.
	 */
	public void onResize() {
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
			}
		});
	}

	private void loadAnnotatedMapPicker() {
		mainPanel.add(amdView, 0, 0);
		amdPresenter.populate("map01100");
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
				mapElementPresenter.setOrganism(Organism.globalMap());
				mapElementPresenter.setSample(null);
			}
		});

		eventBus.addHandler(MapElementClickEvent.TYPE,
				new MapElementClickEvent.Handler() {
			@Override
			public void onMapElementClick(
					final MapElementClickEvent event) {
				mapElementPresenter.killPopup();
				if(!event.getElementClass().equals(AnnotatedMapData.ElementClass.RXN))
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

	private void loadRxnElementPopup() {

		eventBus.addHandler(AnnotatedMapDataLoadedEvent.TYPE, new AnnotatedMapDataLoadedEvent.Handler() {
			@Override
			public void onLoaded(AnnotatedMapDataLoadedEvent event) {
				rxnElementPresenter.setOrganism(Organism.globalMap());
				rxnElementPresenter.setSample(null);
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
	}

	private void loadMapPanel() {

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
				organismPresenter.setOrganism(Organism.globalMap(), false);
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
				experimentPresenter.setOrganism(Organism.globalMap());
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
				experimentPresenter.populate();
			}
		});

		eventBus.addHandler(LogOutEvent.TYPE, new LogOutEvent.Handler() {
			@Override
			public void onLogOut(LogOutEvent event) {
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
				.glammLogoDefault().getURL());
		citationsPresenter.setMouseOverImageUrl(GlammClientBundle.INSTANCE
				.glammLogoMouseOver().getURL());
		citationsPresenter.setPopupContentUrl(urlBuilder.buildString());

		citationsView.setImageSize("208px", "40px");
		citationsView.setPopupSize("55em", "30em");

		mainPanel.add(citationsView, 0, 0);
	}

	private void loadHelp() {

		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("tutorial/index.html");

		helpPresenter.setDefaultImageUrl(GlammClientBundle.INSTANCE
				.helpIconDefault().getURL());
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
				retrosynthesisPresenter.setOrganism(Organism.globalMap());
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
