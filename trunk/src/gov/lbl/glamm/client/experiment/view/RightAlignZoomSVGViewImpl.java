package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;

import org.vectomatic.dom.svg.OMSVGTransform;

public abstract class RightAlignZoomSVGViewImpl extends ScrollSVGViewImpl {

	public RightAlignZoomSVGViewImpl( DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight
	) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
	}

	@Override
	protected void scrollZoom() {
		if ( this.svgViewport != null ) {
			this.svgViewport.getTransform().getBaseVal().clear();

			OMSVGTransform scale = svgData.getSvg().createSVGTransform();
			scale.setScale( this.currZoomRatio, this.currZoomRatio);
			this.svgViewport.getTransform().getBaseVal().appendItem(scale);

			float tx = svgData.getAvailableSvgWidth()/currZoomRatio
					- svgData.getAvailableSvgWidth();
			OMSVGTransform translate = svgData.getSvg().createSVGTransform();
			translate.setTranslate( tx
					, -this.svgData.getAvailableSvgHeight()*this.currVerticalScrollRatio );
			this.svgViewport.getTransform().getBaseVal().appendItem(translate);
		}

	}
}
