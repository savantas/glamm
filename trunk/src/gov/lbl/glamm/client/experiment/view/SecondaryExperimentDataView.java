package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.presenter.SecondaryExperimentDataPresenter;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.HeatMapUtil;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.List;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class SecondaryExperimentDataView extends ExperimentDataView
		implements SecondaryExperimentDataPresenter.View {

	public SecondaryExperimentDataView(DrawUtil drawUtil, HeatMapUtil heatMapUtil,
			float widgetViewWidth, float widgetViewHeight
			, String dataType, String elementType
	) {
		super(drawUtil, heatMapUtil, widgetViewWidth, widgetViewHeight, dataType, elementType);
	}

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.presenter.SecondaryExperimentDataPresenter.View#createExperimentDataSvg(gov.lbl.glamm.client.util.BinarySortedSet, java.util.ArrayList)
	 */
	@Override
	public SVGData createExperimentDataSvg(
			BinarySortedSet<ViewCompound> metabolites,
			List<Experiment> experiments) {
		this.objCount.reset();
		SVGData newSVGData = drawUtil.createSecondaryExperimentDataSvg(
				metabolites
				, experiments
				, this.dataType, this.elementType
				, this.widgetViewWidth, this.widgetViewHeight
				, heatMapUtil, rangeMin, rangeMax
				, objCount);
//		Window.alert("Finished creating " + dataType + "ExperimentDataView SVG");
		return newSVGData;
	}

}
