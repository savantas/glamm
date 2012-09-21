package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.PathwayVerticalScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.LengthsAndRatios;
import gov.lbl.glamm.client.experiment.view.ScrollSVGView;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for the coloring that identifies pathways.
 * Updates view when notified of new data and appropriate zoom and scroll events.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class PathwayGuideColorPresenter {
	public interface View extends ScrollSVGView {
		public void setTargetSegmentLengthsColors(float[] lengths, String[] cssColors);

		public void reDraw();
	}

	@SuppressWarnings("unused")
	private View view = null;
	private HandlerManager eventBus = null;
	private DrawUtil drawUtil = null;

	public PathwayGuideColorPresenter( final View view, HandlerManager eventBus
			, DrawUtil drawUtil ) {
		this.view = view;
		this.eventBus = eventBus;
		this.drawUtil = drawUtil;
		this.eventBus.addHandler(DataChangedEvent.ASSOCIATED_TYPE
				, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				LengthsAndRatios lengthsRatios = PathwayGuideColorPresenter.this.drawUtil
						.getPathwayLengthsAndRatios( dcEvent.getPathways() );
				view.setTargetSegmentLengthsColors( lengthsRatios.getLengths()
						, dcEvent.getPathwayGuideColors() );
				view.reDraw();

				// send object count
				PathwayGuideColorPresenter.this.eventBus.fireEvent(
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
	}
}
