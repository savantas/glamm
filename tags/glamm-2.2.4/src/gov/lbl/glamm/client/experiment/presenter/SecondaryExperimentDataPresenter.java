package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.ExpHorizScrollEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.events.SecondaryVertScrollEvent;
import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
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
public class SecondaryExperimentDataPresenter {
	public interface View extends ScrollSVGView {
		public SVGData createExperimentDataSvg(
				BinarySortedSet<ViewCompound> metabolites
				, List<Experiment> experiments
		);
	}

	@SuppressWarnings("unused")
	private View view = null;
	private HandlerManager eventBus = null;
	private ArrayList<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	public SecondaryExperimentDataPresenter( final View view, HandlerManager eventBus
	) {
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
						dcEvent.getSecondaryMetaboliteList(), dcEvent.getExperiments()
				);
				// replace svg in view
				view.replaceSvg(svgData);

				// send object count
				SecondaryExperimentDataPresenter.this.eventBus.fireEvent(
						new ObjectCountEvent( ObjectCountEvent.CountType
								.VIEW, view.getObjCount() ) );
			}
		});
		handlerRegistrations.add(handlerReg);
		handlerReg = eventBus.addHandler( ExpHorizScrollEvent.ASSOCIATED_TYPE
				, new ExpHorizScrollEvent.Handler() {
					@Override
					@SuppressWarnings("rawtypes")
					public void handleScroll(
							ScrollEvent sEvent) {
						view.setHorizScrollRatio(sEvent.getScrollRatio());
					}
		} );
		handlerRegistrations.add(handlerReg);
		handlerReg = this.eventBus.addHandler( SecondaryVertScrollEvent.ASSOCIATED_TYPE
				, new SecondaryVertScrollEvent.Handler() {
			@Override
			@SuppressWarnings("rawtypes")
			public void handleScroll(ScrollEvent se) {
				view.setVerticalScrollRatio(se.getScrollRatio());
			}
		});
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
