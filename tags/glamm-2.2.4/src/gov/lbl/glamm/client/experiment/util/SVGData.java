package gov.lbl.glamm.client.experiment.util;

import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

/**
 * Wrapper class for a SVG and its elements/attributes
 * where the visible area differs from the
 * actual area that contains viewable elements.
 * By convention, the visible area is defined by a "g" element
 * within the main svg and termed the "viewport" in this wrapper.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class SVGData {
	private OMSVGSVGElement svg = null;
	private OMSVGGElement viewport = null;
	private float availableSvgWidth = 0;
	private float availableSvgHeight = 0;

	/**
	 * @param svg
	 * @param viewport by convention, a "g" element within the main svg
	 * 	determining the visible area
	 * @param availableSvgWidth
	 * @param availableSvgHeight
	 */
	public SVGData(OMSVGSVGElement svg, OMSVGGElement viewport,
			float availableSvgWidth, float availableSvgHeight) {
		super();
		this.svg = svg;
		this.viewport = viewport;
		this.availableSvgWidth = availableSvgWidth;
		this.availableSvgHeight = availableSvgHeight;
	}

	public OMSVGSVGElement getSvg() {
		return svg;
	}

	/** by convention, a "g" element within the main svg
	 * 	containing all the visible elements
	 * @return
	 */
	public OMSVGGElement getViewport() {
		return viewport;
	}

	/**
	 * The width that contains viewable elements, even if not visible.
	 * 
	 * @return
	 */
	public float getAvailableSvgWidth() {
		return availableSvgWidth;
	}

	/**
	 * The height that contains viewable elements, even if not visible.
	 * 
	 * @return
	 */
	public float getAvailableSvgHeight() {
		return availableSvgHeight;
	}
}
