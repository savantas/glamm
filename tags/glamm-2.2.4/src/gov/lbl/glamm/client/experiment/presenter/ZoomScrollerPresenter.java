package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.GenericScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.util.DrawUtil;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Base class for the presenters backing zoom scroll tool views.
 * Templates out the interface for storing ratios and lengths relevant for
 * making zoom/scrolling calculations and transferring the appropriate information
 * to the view. <br />
 * Abstract method bindViewEventHandlers is meant as placeholder for binding
 * event handlers to communicate with the view.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public abstract class ZoomScrollerPresenter {
	public interface View extends HasHandlers {
		public void setInitialToolLengthRatio( float initialToolLengthRatio );
		public void setTargetSegmentRatiosColors( float[] ratios, String[] cssColors );
		public void setControlToolColor(String color);
		public void reDraw();

		HandlerRegistration addToolScrollEventHandler(
				GenericScrollEvent.Handler handler);

		HandlerRegistration addToolZoomEventHandler(
				PathwayZoomEvent.Handler handler);

		void setControlToolZoomRatio(float ratio);
	}

	protected View view = null;
	protected HandlerManager eventBus = null;
	protected DrawUtil drawUtil = null;

	/* data and lengths of the actual widgets and views that this controller controls */
	protected float[] targetSegmentRatios = null;
	protected float targetViewLength = 0;
	protected float targetTotalViewableLength = 0;


	public ZoomScrollerPresenter( final View view, HandlerManager eventBus
			, final DrawUtil drawUtil, float targetDisplayLength ) {
		this.view = view;
		this.eventBus = eventBus;
		this.drawUtil = drawUtil;

		this.targetViewLength = targetDisplayLength;

		this.bindViewEventHandlers();
	}

	/**
	 * Bind event handlers for communication with the view.
	 */
	public abstract void bindViewEventHandlers();

	public void resetTargetViewLength( float targetViewLength ) {
		this.targetViewLength = targetViewLength;
		view.setInitialToolLengthRatio(
				this.targetViewLength/this.targetTotalViewableLength );
	}

	public void resetTargetTotalViewableLength( float targetTotalViewableLength ) {
		this.targetTotalViewableLength = targetTotalViewableLength;
		view.setInitialToolLengthRatio(
				this.targetViewLength/this.targetTotalViewableLength );
	}

	/**
	 * Override for non-externally-set colors.
	 * 
	 * @param ratios
	 * @param cssColors
	 */
	public void resetTargetSegmentRatiosColors(float[] ratios, String[] cssColors) {
		view.setTargetSegmentRatiosColors(ratios, cssColors);
	}
}
