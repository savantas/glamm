package gov.lbl.glamm.client;

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
import gov.lbl.glamm.client.rpc.GlammService;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.util.Interpolator;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Top level application controller class.
 * 
 * @author jtbates
 * 
 */

public class AppController {

	private static AppController instance;

	private GlammServiceAsync rpc;
	private SimpleEventBus eventBus;
	private AbsolutePanel mainPanel = null;

	private AnnotatedMapData mapData;

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

		// start with global map
		//		mapData = AnnotatedMapData.createFromRawSvg(GlammClientBundle.INSTANCE.globalMapText().getText(), 
		//				"map01100", 
		//				new String[] { "LIGAND-CPD", "GLYCAN" }, 
		//				new String[] { "LIGAND-RXN" });

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
	 * @return AppController singleton instance
	 */
	public static AppController instance() {
		if (instance == null)
			instance = new AppController();
		return instance;
	}

	/**
	 * Called by the entryPoint class Glamm
	 * 
	 * @param rlp
	 *            The RootLayoutPanel
	 */
	public void start(final RootLayoutPanel rlp) {
		rlp.add(layout);
		rlp.setWidgetTopBottom(layout, 0, Unit.PX, 0, Unit.PX);

		mainPanel.setStylePrimaryName("glamm-global-map");
		layout.add(mainPanel);

		loadMapPanel();
		loadCpdDisambiguation();
		loadInterpolator();
		loadLoadingPanel();
		loadMapElementPopup();
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
	 * Computes widget positions on window resize
	 */
	public void onResize() {
		// perform resize computations at the end of the current event loop
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				mainPanel.setWidgetPosition(organismView,
						retrosynthesisView.getOffsetWidth() + 5, 0);
				mainPanel.setWidgetPosition(
						experimentView,
						retrosynthesisView.getOffsetWidth()
						+ organismView.getOffsetWidth() + 10, 0);
				mainPanel.setWidgetPosition(
						miniMapView,
						0,
						Window.getClientHeight()
						- miniMapView.getOffsetHeight());
				mainPanel.setWidgetPosition(
						panZoomView,
						miniMapView.getOffsetWidth() + 1,
						Window.getClientHeight()
						- panZoomView.getOffsetHeight());
				mainPanel.setWidgetPosition(
						citationsView,
						Window.getClientWidth()
						- citationsView.getOffsetWidth(),
						Window.getClientHeight()
						- citationsView.getOffsetHeight());
				mainPanel.setWidgetPosition(
						interpolatorView,
						Window.getClientWidth()
						- interpolatorView.getOffsetWidth(),
						Window.getClientHeight()
						- citationsView.getOffsetHeight()
						- interpolatorView.getOffsetHeight() - 5);
				mainPanel.setWidgetPosition(helpView, Window.getClientWidth()
						- helpView.getOffsetWidth(), 0);
				mainPanel.setWidgetPosition(loginView,
						Window.getClientWidth() - loginView.getOffsetWidth()
						- helpView.getOffsetWidth() - 5, 0);
			}
		});
	}

	/**
	 * Initializes compound disambiguation panel
	 */
	private void loadCpdDisambiguation() {

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

	/**
	 * Initializes popup displaying loading message
	 */
	private void loadLoadingPanel() {
		eventBus.addHandler(LoadingEvent.TYPE, new LoadingEvent.Handler() {
			@Override
			public void onLoading(LoadingEvent event) {
				loadingPresenter.update(event.isDoneLoading());
			}
		});
	}

	/**
	 * Initializes popup displaying information about compounds, reactions, and
	 * pathways on the map
	 */
	private void loadMapElementPopup() {
		eventBus.addHandler(MapElementClickEvent.TYPE,
				new MapElementClickEvent.Handler() {
			@Override
			public void onMapElementClick(
					final MapElementClickEvent event) {
				mapElementPresenter.showPopup(event.getElementClass(),
						event.getElementQuery(), event.getClientX(),
						event.getClientY());
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

	/**
	 * Initializes the map view and presenter
	 */
	private void loadMapPanel() {

		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("/svg/map01100.svg");
		mapData = AnnotatedMapData.createFromUrl(urlBuilder.buildString(), 
				"map01100", 
				new String[] { "LIGAND-CPD", "GLYCAN" }, 
				new String[] { "LIGAND-RXN" },
				new Command() {
			@Override
			public void execute() {
				mapPresenter.setMapData(mapData);
			}
		});


		mainPanel.add(mapView, 0, 0);
		eventBus.addHandler(PanZoomControlEvent.TYPE,
				new PanZoomControlEvent.Handler() {
			public void onPanZoom(PanZoomControlEvent event) {

				short action = event.getAction();
				if (action == PanZoomControlEvent.ACTION_PAN_UP)
					mapPresenter.translateNorm(0, -0.05f);
				else if (action == PanZoomControlEvent.ACTION_PAN_DOWN)
					mapPresenter.translateNorm(0, 0.05f);
				else if (action == PanZoomControlEvent.ACTION_PAN_LEFT)
					mapPresenter.translateNorm(0.05f, 0.0f);
				else if (action == PanZoomControlEvent.ACTION_PAN_RIGHT)
					mapPresenter.translateNorm(-0.05f, 0.0f);
				else if (action == PanZoomControlEvent.ACTION_ZOOM_TO_FIT)
					mapPresenter.fitMapToPanel();
				else
					mapPresenter.setZoomNormAboutPoint(
							event.getZoomNorm(),
							Window.getClientWidth() >> 1,
							Window.getClientHeight() >> 1);

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
				mapPresenter.updateMapForSearchTarget(event
						.getPrimitives());
			}
		});
	}

	/**
	 * Initializes inset mini map panel
	 */
	private void loadMiniMapPanel() {
		miniMapPresenter.setMiniMap(GlammClientBundle.INSTANCE.globalMiniMap());
		mainPanel.add(miniMapView, 0, 0);

		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			@Override
			public void onMapUpdate(MapUpdateEvent event) {
				miniMapPresenter.updateReticle(event.getViewRectNorm());
			}
		});

	}

	/**
	 * Initializes Organism picker widget
	 */
	private void loadOrganismPicker() {
		mainPanel.add(organismView, 0, 0);
		organismPresenter.populate();

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

	/**
	 * Initializes experiment upload panel
	 */
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

	/**
	 * Initializes interpolator widget
	 */
	private void loadInterpolator() {
		mainPanel.add(interpolatorView, 0, 0);

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

	/**
	 * Initializes organism upload panel
	 */
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

	/**
	 * Initializes pan/zoom control cluster
	 */
	private void loadPanZoomControl() {
		mainPanel.add(panZoomView, 0, 0);
		eventBus.addHandler(MapUpdateEvent.TYPE, new MapUpdateEvent.Handler() {
			public void onMapUpdate(MapUpdateEvent event) {
				panZoomPresenter.setSliderValue(event.getZoomNorm());
			}
		});
	}

	/**
	 * Initializes citations popup panel
	 */
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

	/**
	 * Initializes online help/tutorial panel
	 */
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

	/**
	 * Initializes retrosynthesis search/get directions widget
	 */
	private void loadRetrosynthesis() {
		mainPanel.add(retrosynthesisView, 0, 0);

		retrosynthesisPresenter.setMapData(mapData);
		retrosynthesisPresenter.setOrganism(Organism.globalMap());

		eventBus.addHandler(OrganismPickedEvent.TYPE,
				new OrganismPickedEvent.Handler() {
			@Override
			public void onOrganismPicked(OrganismPickedEvent event) {
				retrosynthesisPresenter.setOrganism(event.getOrganism());
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
