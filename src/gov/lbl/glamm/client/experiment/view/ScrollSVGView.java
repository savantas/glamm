package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.client.experiment.util.SVGData;

public interface ScrollSVGView {
	public void replaceSvg(SVGData svgData);

	public void setWidgetViewWidth(float widgetViewWidth);
	public void setWidgetViewHeight(float widgetViewHeight);

	public void setVerticalScrollRatio(float ratio);
	public void setHorizScrollRatio(float ratio);
	public void setZoomRatio(float ratio);

	public ObjectCount getObjCount();
}
