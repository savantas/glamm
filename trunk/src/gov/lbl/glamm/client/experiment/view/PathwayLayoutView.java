package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.presenter.PathwayLayoutPresenter;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.SVGData;

import java.util.List;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class PathwayLayoutView
		extends RightAlignZoomSVGViewImpl implements PathwayLayoutPresenter.View {

	public PathwayLayoutView( DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight ) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
	}

	@Override
	protected SVGData createNoDataSvgData() {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		OMSVGRectElement rect = doc.createSVGRectElement(10, 10, 200, 100, 1, 1);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_LIGHTBLUE_VALUE);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2px");

		OMSVGTextElement text = doc.createSVGTextElement(
				58, 65
				, OMSVGLength.SVG_LENGTHTYPE_PX
				, "No Data Loaded" );

		svg.appendChild(rect);
		svg.appendChild(text);
		return new SVGData( svg, null, 220, 120);
	}

	@Override
	public SVGData createPathwaySvg( List<ViewPathway> pathways ) {
		this.objCount.reset();
		SVGData svg = this.drawUtil.createPathwaySvg( pathways
				, this.widgetViewWidth, this.widgetViewHeight
				, objCount );
//		Window.alert("Finished creating  PathwayLayoutView SVG");
		return svg;
	}

	@Override
	public void showLongNames() {
		OMNodeList<OMElement> textNodes = this.svgData.getSvg().getElementsByTagName("text");
		for ( int i=0; i< textNodes.getLength(); i++ ) {
			OMElement textNode = textNodes.getItem(i);
			String classValue = textNode.getAttribute(SVGConstants.SVG_CLASS_ATTRIBUTE);
			if ( DrawUtil.LONG_NAME_CLASS_VALUE.equals(classValue) ) {
				OMSVGTextElement textElement = (OMSVGTextElement) textNode;
				textElement.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "");
			} else if ( DrawUtil.SHORT_NAME_CLASS_VALUE.equals(classValue) ) {
				OMSVGTextElement textElement = (OMSVGTextElement) textNode;
				textElement.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
			}
		}
	}

	@Override
	public void showShortNames() {
		OMNodeList<OMElement> textNodes = this.svgData.getSvg().getElementsByTagName("text");
		for ( int i=0; i< textNodes.getLength(); i++ ) {
			OMElement textNode = textNodes.getItem(i);
			String classValue = textNode.getAttribute(SVGConstants.SVG_CLASS_ATTRIBUTE);
			if ( DrawUtil.SHORT_NAME_CLASS_VALUE.equals(classValue) ) {
				OMSVGTextElement textElement = (OMSVGTextElement) textNode;
				textElement.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "");
			} else if ( DrawUtil.LONG_NAME_CLASS_VALUE.equals(classValue) ) {
				OMSVGTextElement textElement = (OMSVGTextElement) textNode;
				textElement.getStyle().setSVGProperty(SVGConstants.CSS_DISPLAY_PROPERTY, "none");
			}
		}
	}

	@Override
	public void onResize() {
		super.onResize();
		this.scrollToRight();
	}
}
