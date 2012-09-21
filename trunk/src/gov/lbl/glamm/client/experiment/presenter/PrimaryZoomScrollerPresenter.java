package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.GenericScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayVerticalScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.LengthsAndRatios;

import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Main zoom scroller widget's presenter.
 * Updates view when new data is available and on outside zoom events.
 * Sends zoom events intended for all diagrams.
 * Sends scroll events intended for vertical scrolling of
 * the main pathway and main experiment diagrams.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class PrimaryZoomScrollerPresenter extends ZoomScrollerPresenter
		implements DataChangedEvent.Handler {

	protected HeatMapUtil heatMapUtil = null;

	public PrimaryZoomScrollerPresenter(View view, HandlerManager eventBus,
			DrawUtil drawUtil, HeatMapUtil heatMapUtil,
			float targetDisplayLength) {
		super(view, eventBus, drawUtil, targetDisplayLength);
		this.heatMapUtil = heatMapUtil;

		this.eventBus.addHandler( DataChangedEvent.ASSOCIATED_TYPE, this );
		this.eventBus.addHandler( PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				PrimaryZoomScrollerPresenter.this.view.setControlToolZoomRatio(
						pzEvent.getZoomRatio() );
			}
		} );
	}

	@Override
	public void bindViewEventHandlers() {
		view.addToolScrollEventHandler(new GenericScrollEvent.Handler() {
			@Override
			@SuppressWarnings("rawtypes")
			public void handleScroll( ScrollEvent sEvent ) {
				PrimaryZoomScrollerPresenter.this.eventBus.fireEvent(
						new PathwayVerticalScrollEvent(sEvent.getScrollRatio()) );
			}
		});
		view.addToolZoomEventHandler(new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				PrimaryZoomScrollerPresenter.this.eventBus.fireEvent(pzEvent);
			}
		});
	}

	@Override
	public void resetTargetSegmentRatiosColors(float[] ratios, String[] cssColors) {
		// use heatmap to generate colors
		view.setControlToolColor(SVGConstants.CSS_SALMON_VALUE);
		view.setTargetSegmentRatiosColors( ratios
				, cssColors );
	}

	@Override
	public void handleDataReceived(DataChangedEvent dcEvent) {
		LengthsAndRatios lengthsRatios = drawUtil
				.getPathwayLengthsAndRatios(dcEvent.getPathways());
		targetSegmentRatios = lengthsRatios.getRatios();
		targetTotalViewableLength = lengthsRatios.getTotalLength();
		resetTargetViewLength( this.targetViewLength );
		resetTargetSegmentRatiosColors( targetSegmentRatios
				, dcEvent.getPathwayGuideColors() );
		view.reDraw();
	}
}
