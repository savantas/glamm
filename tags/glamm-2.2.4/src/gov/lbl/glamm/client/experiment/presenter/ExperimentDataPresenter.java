package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.ExpHorizScrollEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.PathwayVerticalScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.client.experiment.view.ScrollSVGView;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Refreshes the view when notified of refreshed data and of zoom and scroll events.
 * Notifies the rest of the application of the view's object count.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class ExperimentDataPresenter {
	public interface View extends ScrollSVGView {
		public SVGData createExperimentDataSvg(
				List<ViewPathway> pathways, List<Experiment> experiments
		);
	}

	@SuppressWarnings("unused")
	private View view = null;
	private HandlerManager eventBus = null;
	private ArrayList<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	public ExperimentDataPresenter( final View view, HandlerManager eventBus ) {
		this.view = view;
		this.eventBus = eventBus;
		HandlerRegistration handlerReg = eventBus.addHandler(
				DataChangedEvent.ASSOCIATED_TYPE
				, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				SVGData svgData = null;
				// construct new svg
				svgData = view.createExperimentDataSvg(
						dcEvent.getPathways(), dcEvent.getExperiments()
				);
				// replace svg in view
				view.replaceSvg(svgData);

				// send object count
				ExperimentDataPresenter.this.eventBus.fireEvent(
						new ObjectCountEvent( ObjectCountEvent.CountType
								.VIEW, view.getObjCount() ) );
			}
		});
		handlerRegistrations.add(handlerReg);
		handlerReg = eventBus.addHandler( PathwayVerticalScrollEvent.ASSOCIATED_TYPE
				, new PathwayVerticalScrollEvent.Handler() {
					@SuppressWarnings("rawtypes")
					@Override
					public void handleScroll(
							ScrollEvent pvsEvent) {
						view.setVerticalScrollRatio(pvsEvent.getScrollRatio());
					}
		} );
		handlerRegistrations.add(handlerReg);
		handlerReg = eventBus.addHandler( ExpHorizScrollEvent.ASSOCIATED_TYPE
				, new ExpHorizScrollEvent.Handler() {
					@SuppressWarnings("rawtypes")
					@Override
					public void handleScroll(
							ScrollEvent sEvent) {
						view.setHorizScrollRatio(sEvent.getScrollRatio());
					}
		} );
		handlerRegistrations.add(handlerReg);
		handlerReg = eventBus.addHandler( PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				view.setZoomRatio(pzEvent.getZoomRatio());
			}
		});
		handlerRegistrations.add(handlerReg);
	}

	public void removeHandlers() {
		for ( HandlerRegistration reg : handlerRegistrations ) {
			reg.removeHandler();
		}
		handlerRegistrations.clear();
	}
}
