package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.client.experiment.util.SVGData;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGTransform;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * Base template implementation for views whose primary component is a SVG which
 * can be zoomed and scrolled.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public abstract class ScrollSVGViewImpl extends ScrollPanel implements ScrollSVGView {

	protected DrawUtil drawUtil = null;
	protected HeatMapUtil heatMapUtil = null;

	protected float widgetViewWidth = 0;
	protected float widgetViewHeight = 0;

	protected SVGData svgData = null;
	protected OMSVGGElement svgViewport = null;	// needs to be kept in sync with svg
	private Element svgElement = null;

	protected ObjectCount objCount = new ObjectCount();

	protected float currVerticalScrollRatio = 0;
	protected float currHorizScrollRatio = 0;
	protected float currZoomRatio = 1;

	protected SVGData noDataSvg = null;

	public ScrollSVGViewImpl( DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight ) {
		super();
		this.drawUtil = drawUtil;
		this.heatMapUtil = heatMapUtil;
		this.widgetViewWidth = widgetViewWidth;
		this.widgetViewHeight = widgetViewHeight;

		noDataSvg = this.createNoDataSvgData();
		this.replaceSvg(noDataSvg);

		DOM.setStyleAttribute(this.getElement(), "overflowX", "hidden");
		DOM.setStyleAttribute(this.getElement(), "overflowY", "hidden");
	}

	protected abstract SVGData createNoDataSvgData();

	@Override
	public ObjectCount getObjCount() {
		return this.objCount;
	}

	@Override
	public void replaceSvg( SVGData svgData ) {
		if ( svgElement != null ) {
			this.getElement().removeChild(svgElement);
		}
		this.svgData = svgData;

		this.svgViewport = this.svgData.getViewport();

		svgElement = this.getElement().appendChild(this.svgData.getSvg().getElement());
	}

	@Override
	public void setVerticalScrollRatio(float ratio) {
		currVerticalScrollRatio = ratio;
		if ( this.svgData != null ) {
			this.scrollZoom();
		}
	}

	@Override
	public void setHorizScrollRatio(float ratio) {
		currHorizScrollRatio = ratio;
		if ( svgData != null ) {
			scrollZoom();
		}
	}

	@Override
	public void setZoomRatio(float ratio) {
		this.currZoomRatio = ratio;
		if ( this.svgData != null ) {
			this.scrollZoom();
		}
	}

	@Override
	public void setWidgetViewHeight( float widgetViewHeight ) {
		this.widgetViewHeight = widgetViewHeight;
	}

	@Override
	public void setWidgetViewWidth( float widgetViewWidth ) {
		this.widgetViewWidth = widgetViewWidth;
	}

	protected void scrollZoom() {
		if ( this.svgViewport != null ) {
			this.svgViewport.getTransform().getBaseVal().clear();

			OMSVGTransform scale = svgData.getSvg().createSVGTransform();
			scale.setScale( this.currZoomRatio, this.currZoomRatio);
			this.svgViewport.getTransform().getBaseVal().appendItem(scale);

			OMSVGTransform translate = svgData.getSvg().createSVGTransform();
			translate.setTranslate(
					-svgData.getAvailableSvgWidth()*currHorizScrollRatio
					, -svgData.getAvailableSvgHeight()*currVerticalScrollRatio );
			this.svgViewport.getTransform().getBaseVal().appendItem(translate);
		}

	}
}
