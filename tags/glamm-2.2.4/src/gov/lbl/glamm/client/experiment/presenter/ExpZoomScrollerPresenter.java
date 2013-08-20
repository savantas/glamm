package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.ExpHorizScrollEvent;
import gov.lbl.glamm.client.experiment.events.GenericScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.LengthsAndRatios;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Main experiment panels' horizontal zoom scroller widget's presenter.
 * Updates view when new data is available and on outside zoom events.
 * Sends zoom events intended for all diagrams.
 * (Zoom functionality available even if the view is instantiated
 * to not allow user to control zoom.)
 * Sends scroll events intended for horizontal scrolling of
 * the main experiment diagrams.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class ExpZoomScrollerPresenter extends ZoomScrollerPresenter
		implements DataChangedEvent.Handler {

	public ExpZoomScrollerPresenter(View view, HandlerManager eventBus,
			DrawUtil drawUtil, float targetDisplayLength) {
		super(view, eventBus, drawUtil, targetDisplayLength);

		this.eventBus.addHandler( DataChangedEvent.ASSOCIATED_TYPE
				, this );
		this.eventBus.addHandler( PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				ExpZoomScrollerPresenter.this.view.setControlToolZoomRatio(
						pzEvent.getZoomRatio() );
			}
		} );
	}

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.presenter.ZoomScrollerPresenter#bindViewEventHandlers()
	 */
	@Override
	public void bindViewEventHandlers() {
		view.addToolScrollEventHandler(new GenericScrollEvent.Handler() {
			@Override
			@SuppressWarnings("rawtypes")
			public void handleScroll( ScrollEvent sEvent ) {
				ExpZoomScrollerPresenter.this.eventBus.fireEvent(
						new ExpHorizScrollEvent(sEvent.getScrollRatio()) );
			}
		});
		view.addToolZoomEventHandler(new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				ExpZoomScrollerPresenter.this.eventBus.fireEvent(pzEvent);
			}
		});
	}

	@Override
	public void resetTargetSegmentRatiosColors(float[] ratios, String[] cssColors) {
		cssColors = new String[ratios.length];
		// even segments
		for ( int i=0; i< cssColors.length; i+=2 ) {
			cssColors[i] = DrawUtil.EXPERIMENT_EVEN_SEG_CSS_COLOR;
		}
		// odd segments
		for ( int i=1; i< cssColors.length; i+=2 ) {
			cssColors[i] = DrawUtil.EXPERIMENT_ODD_SEG_CSS_COLOR;
		}
		view.setTargetSegmentRatiosColors( ratios
				, cssColors );
	}

	@Override
	public void handleDataReceived(DataChangedEvent dcEvent) {
		LengthsAndRatios lengthsRatios = drawUtil
		.getExperimentLengthsAndRatios(dcEvent.getExperiments());
		targetSegmentRatios = lengthsRatios.getRatios();
		targetTotalViewableLength = lengthsRatios.getTotalLength();
		resetTargetViewLength( this.targetViewLength );
		resetTargetSegmentRatiosColors( targetSegmentRatios, null );
		view.reDraw();
	}
}
