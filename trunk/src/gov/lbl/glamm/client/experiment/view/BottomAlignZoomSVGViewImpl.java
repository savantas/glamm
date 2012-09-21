package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;

import org.vectomatic.dom.svg.OMSVGTransform;

public abstract class BottomAlignZoomSVGViewImpl extends ScrollSVGViewImpl {

	public BottomAlignZoomSVGViewImpl( DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight ) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
	}

	@Override
	protected void scrollZoom() {
		if ( this.svgViewport != null ) {
			svgViewport.getTransform().getBaseVal().clear();

			OMSVGTransform scale = svgData.getSvg().createSVGTransform();
			scale.setScale( currZoomRatio, currZoomRatio);
			svgViewport.getTransform().getBaseVal().appendItem(scale);

			float ty = svgData.getAvailableSvgHeight()/currZoomRatio
					- svgData.getAvailableSvgHeight();
			OMSVGTransform translate = svgData.getSvg().createSVGTransform();
			translate.setTranslate(
					-svgData.getAvailableSvgWidth()*currHorizScrollRatio
					, ty );
			svgViewport.getTransform().getBaseVal().appendItem(translate);
		}
	}

}
