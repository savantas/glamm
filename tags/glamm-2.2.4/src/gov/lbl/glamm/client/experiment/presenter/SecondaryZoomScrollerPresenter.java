package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.GenericScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.events.SecondaryVertScrollEvent;
import gov.lbl.glamm.client.experiment.util.DrawUtil;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Secondary experiment panels' vertical zoom scroller widget's presenter.
 * Updates view when new data is available and on outside zoom events.
 * Sends zoom events intended for all diagrams.
 * (Zoom functionality available even if the view is instantiated
 * to not allow user to control zoom.)
 * Sends scroll events intended for vertical scrolling of
 * the secondary metabolite key and secondary experiment diagrams.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class SecondaryZoomScrollerPresenter extends ZoomScrollerPresenter {

	public SecondaryZoomScrollerPresenter(final View view, HandlerManager eventBus,
			final DrawUtil drawUtil, float targetDisplayLength) {
		super(view, eventBus, drawUtil, targetDisplayLength);

		this.eventBus.addHandler( DataChangedEvent.ASSOCIATED_TYPE
				, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				targetSegmentRatios = new float[] { 1 };
				targetTotalViewableLength = drawUtil
						.getSecondaryMetabKeyTotalLength(
								dcEvent.getSecondaryMetaboliteList() );
				resetTargetViewLength( targetViewLength );
				resetTargetSegmentRatiosColors( targetSegmentRatios, null );
				view.reDraw();
			}
				} );
		this.eventBus.addHandler( PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				view.setControlToolZoomRatio(
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
				eventBus.fireEvent(
						new SecondaryVertScrollEvent(sEvent.getScrollRatio()) );
			}
		});
		view.addToolZoomEventHandler(new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				eventBus.fireEvent(pzEvent);
			}
		});
	}

	@Override
	public void resetTargetSegmentRatiosColors(float[] ratios, String[] cssColors) {
		cssColors = new String[ratios.length];
		// even segments
		for ( int i=0; i< cssColors.length; i+=2 ) {
			cssColors[i] = DrawUtil.SCND_METAB_EVEN_SEG_CSS_COLOR;
		}
		// odd segments
		for ( int i=1; i< cssColors.length; i+=2 ) {
			cssColors[i] = DrawUtil.SCND_METAB_ODD_SEG_CSS_COLOR;
		}
		view.setTargetSegmentRatiosColors( ratios
				, cssColors );
	}
}
