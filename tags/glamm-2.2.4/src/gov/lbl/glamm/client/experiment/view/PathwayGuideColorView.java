package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.presenter.PathwayGuideColorPresenter;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.SVGData;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTransform;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

/**
 * Vertical strip of color that identifies pathways. Should be controlled
 * so that zooming (no horizontal scaling) and scrolling are synched with the main pathway diagram.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class PathwayGuideColorView extends ScrollSVGViewImpl
		implements PathwayGuideColorPresenter.View {
	private float[] lengths = null;
	private String[] cssColors = null;

	public PathwayGuideColorView(DrawUtil drawUtil, HeatMapUtil heatMapUtil,
			float widgetViewWidth, float widgetViewHeight) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
	}

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.view.ScrollSVGViewImpl#createNoDataSvgData()
	 */
	@Override
	protected SVGData createNoDataSvgData() {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		OMSVGGElement viewGroup = doc.createSVGGElement();
		svg.appendChild(viewGroup);

		OMSVGRectElement rect = doc.createSVGRectElement(
				0, 0
				, widgetViewWidth, widgetViewHeight
				, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_LIGHTGREY_VALUE);

		viewGroup.appendChild(rect);

		SVGData newSVGData = new SVGData( svg, viewGroup, 0, 0 );
		return newSVGData;
	}

	@Override
	public void reDraw() {
		if ( this.lengths == null ) {
			if ( noDataSvg == null ) {
				noDataSvg = createNoDataSvgData();
			}
			svgData = noDataSvg;
		} else {
			// TODO: distinguish between cases when svg needs to be recreated entirely
			//	and when only length adjustments need to be made
			//	- investigate performance impacts of both cases
			this.objCount.reset();
			svgData = this.drawUtil.createColorSegmentsSvg( lengths, cssColors
					, this.widgetViewWidth, this.widgetViewHeight, objCount );
//			Window.alert("Finished creating PathwayGuideColor SVG");
		}
		replaceSvg(svgData);
	}

	@Override
	protected void scrollZoom() {
		if ( this.svgViewport != null ) {
			this.svgViewport.getTransform().getBaseVal().clear();

			OMSVGTransform scale = svgData.getSvg().createSVGTransform();
			scale.setScale( 1, this.currZoomRatio);
			this.svgViewport.getTransform().getBaseVal().appendItem(scale);

			OMSVGTransform translate = svgData.getSvg().createSVGTransform();
			translate.setTranslate(
					-svgData.getAvailableSvgWidth()*currHorizScrollRatio
					, -svgData.getAvailableSvgHeight()*currVerticalScrollRatio );
			this.svgViewport.getTransform().getBaseVal().appendItem(translate);
		}

	}

	@Override
	public void setTargetSegmentLengthsColors(float[] lengths, String[] cssColors) {
		this.lengths = lengths;
		this.cssColors = cssColors;
	}
}
