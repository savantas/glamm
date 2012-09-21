package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.NameTypeChangeEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.PathwayVerticalScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.client.experiment.view.ScrollSVGView;

import java.util.List;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for main pathway diagram view.
 * Updates view when notified of new data and of relevant zoom and scroll events.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class PathwayLayoutPresenter {
	public interface View extends ScrollSVGView {
		public SVGData createPathwaySvg( List<ViewPathway> pathways );

		void showLongNames();
		void showShortNames();
	}

	@SuppressWarnings("unused")
	private View view = null;
	private HandlerManager eventBus = null;

	public PathwayLayoutPresenter( final View view, HandlerManager eventBus ) {
		this.view = view;
		this.eventBus = eventBus;
		this.eventBus.addHandler(DataChangedEvent.ASSOCIATED_TYPE, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				SVGData svgData = null;
				// construct new svg
				svgData = view.createPathwaySvg( dcEvent.getPathways() );
				// replace svg in view
				view.replaceSvg(svgData);
				view.showShortNames();

				// send object count
				PathwayLayoutPresenter.this.eventBus.fireEvent(
						new ObjectCountEvent( ObjectCountEvent.CountType
								.VIEW, view.getObjCount() ) );
			}
		});
		this.eventBus.addHandler( PathwayVerticalScrollEvent.ASSOCIATED_TYPE
				, new PathwayVerticalScrollEvent.Handler() {
					@Override
					@SuppressWarnings("rawtypes")
					public void handleScroll(
							ScrollEvent pvsEvent) {
						view.setVerticalScrollRatio(pvsEvent.getScrollRatio());
					}
		} );
		this.eventBus.addHandler( PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				view.setZoomRatio(pzEvent.getZoomRatio());
			}
		});
		this.eventBus.addHandler( NameTypeChangeEvent.ASSOCIATED_TYPE
				, new NameTypeChangeEvent.Handler() {
			@Override
			public void handleNameTypeChange(NameTypeChangeEvent ntcEvent) {
				if ( ntcEvent == NameTypeChangeEvent.LONG_NAME_EVENT ) {
					view.showLongNames();
				} else {
					view.showShortNames();
				}
			}
		});
	}
}
