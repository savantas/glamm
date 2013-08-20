package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.presenter.SampleGuidePresenter;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.List;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class SampleGuideView extends BottomAlignZoomSVGViewImpl
		implements SampleGuidePresenter.View {
	public SampleGuideView(DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight
	) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
	}

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.presenter.SampleGuidePresenter.View#reDraw(java.util.List)
	 */
	@Override
	public void reDraw( List<Experiment> experiments ) {
		objCount.reset();
		SVGData newSVG = drawUtil.createExperimentGuideSvg( experiments
				, widgetViewWidth, widgetViewHeight, objCount);
		replaceSvg(newSVG);
	}

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
				, 1, 1);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_LIGHTGREY_VALUE);

		viewGroup.appendChild(rect);

		SVGData newSVGData = new SVGData( svg, viewGroup, widgetViewWidth, widgetViewHeight );
		return newSVGData;
	}

}
