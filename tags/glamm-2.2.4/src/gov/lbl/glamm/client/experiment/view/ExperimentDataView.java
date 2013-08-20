package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.presenter.ExperimentDataPresenter;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.List;

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
public class ExperimentDataView
		extends ScrollSVGViewImpl implements ExperimentDataPresenter.View {

	protected float rangeMin = -1;
	protected float rangeMax = 1;

	protected String dataType = null;
	protected String elementType = null;

	public ExperimentDataView(DrawUtil drawUtil, HeatMapUtil heatMapUtil
			, float widgetViewWidth, float widgetViewHeight
			, String dataType, String elementType
	) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight);
		this.dataType = dataType;
		this.elementType = elementType;
		this.setStyleName(dataType);
	}

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.presenter.ExperimentDataPresenter.View#createExperimentDataSvg(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public SVGData createExperimentDataSvg(List<ViewPathway> pathways,
			List<Experiment> experiments) {
		this.objCount.reset();
		SVGData svgData = drawUtil.createExperimentDataSvg( pathways
				, experiments, this.dataType, this.elementType
				, this.widgetViewWidth
				, this.widgetViewHeight, heatMapUtil, rangeMin, rangeMax
				, objCount );
//		Window.alert("Finished creating " + dataType + "ExperimentDataView SVG");
		return svgData;
	}

	public void setRange( float rangeMin, float rangeMax ) {
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
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
		return new SVGData( svg, null, 220, 120 );
	}

}
