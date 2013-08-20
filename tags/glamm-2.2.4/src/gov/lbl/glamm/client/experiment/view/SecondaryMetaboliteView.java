package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.presenter.SecondaryMetabolitePresenter;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.SVGData;

import java.util.ArrayList;

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
public class SecondaryMetaboliteView
		extends RightAlignZoomSVGViewImpl implements SecondaryMetabolitePresenter.View {

	public SecondaryMetaboliteView(DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight
	) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
	}

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.view.ScrollSVGViewImpl#createNoDataSvg()
	 */
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
	public SVGData createSecondaryMetaboliteSvg(
			BinarySortedSet<ViewCompound> metabolites
			, ArrayList<String> dynamicDefBaseNameList ) {
		this.objCount.reset();
		SVGData newSVGData = drawUtil.createSecondaryMetaboliteLayout(
				metabolites, dynamicDefBaseNameList, heatMapUtil
				, this.widgetViewWidth, this.widgetViewHeight
				, objCount );
		// TODO: use widgetViewHeight in conjunction with custom scroll
//		Window.alert("Finished creating SecondaryMetaboliteView SVG");
		return newSVGData;
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
}
