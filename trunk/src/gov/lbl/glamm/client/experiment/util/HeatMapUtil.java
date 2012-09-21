package gov.lbl.glamm.client.experiment.util;

/**
 * Class to generate colors according to a heat map defined by
 * interpolation ratio nodes and corresponding colors as set per object.
 * 
 * @author DHAP Digital, Inc - angie
 */
public class HeatMapUtil {
	
	/** range [0,1];
	 * in increasing order;
	 */
	private float[] interpolationNodes = { 0, 0.25f, 0.5f, 0.75f, 1};

	/** should be 1-1 with interpolationNodes
	 */
	private int[] interpolationNodeColors
			= { 0x0000ff, 0x00ff00, 0xffff00, 0xff3300, 0xff0000 };

	public HeatMapUtil(){
		// noop, use default node assignments
	}

	public HeatMapUtil( float[] interpolationNodes, int[] interpolationNodeColors ) {
		this.setNodes(interpolationNodes, interpolationNodeColors);
	}

	public void setNodes( float[] interpolationNodes, int[] interpolationNodeColors ) {
		if ( interpolationNodes == null || interpolationNodes.length == 0 ) {
			throw new RuntimeException("HeatMapUtil.setNodes: Error: interpolation nodes must have at least one point.");
		}
		if ( interpolationNodeColors == null
				|| interpolationNodes.length != interpolationNodeColors.length ) {
			throw new RuntimeException("HeatMapUtil.setNodes: Error: interpolation node colors must be in 1-to-1 relationship with interpolation nodes.");
		}
		this.interpolationNodes = interpolationNodes;
		this.interpolationNodeColors = interpolationNodeColors;
	}

	/**
	 * Calculates the integer rgb color corresponding to the ratio param
	 * based on this object's interpolationNodes ratios and interpolationNodeColors.
	 * 
	 * @param ratio strictly in range [0,1]
	 * @return
	 */
	public int getHeatMapping( float ratio ) {
		int color = 0;

		int leftNodeIndex = 0;
		int rightNodeIndex = interpolationNodes.length-1;
		for ( int i=0; i < interpolationNodes.length; i++ ) {
			if ( interpolationNodes[i] < ratio ) {
				leftNodeIndex = i;
			} else if ( interpolationNodes[i] == ratio ) {
				leftNodeIndex = rightNodeIndex = i;
			} else {
				rightNodeIndex = i;
				break;
			}
		}
		if ( ratio < interpolationNodes[0] ) {
			rightNodeIndex = 0;
		}

		if ( leftNodeIndex == rightNodeIndex ) {
			color = interpolationNodeColors[leftNodeIndex];
			return color;
		}

		float subRatio = (ratio - interpolationNodes[leftNodeIndex])
					/ (interpolationNodes[rightNodeIndex] - interpolationNodes[leftNodeIndex]);
		
		return this.blendColors( subRatio
				, interpolationNodeColors[leftNodeIndex]
				, interpolationNodeColors[rightNodeIndex] );
	}

	/**
	 * Returns a blend of colors ratio of the way from color1 to color2.
	 * @param ratio
	 * @param color1
	 * @param color2
	 * @return
	 */
	protected int blendColors( float ratio, int color1, int color2 ) {
		float remainderRatio = 1 - ratio;

		int red1 = (color1 >> 16 ) & 0xff;
		int green1 = (color1 >> 8) & 0xff;
		int blue1 = color1 & 0xff;

		int red2 = (color2 >> 16 ) & 0xff;
		int green2 = (color2 >> 8) & 0xff;
		int blue2 = color2 & 0xff;

		int color1part = (((int)(red1*remainderRatio)) << 16)
				+ ((int)(green1*remainderRatio) << 8) + (int)(blue1*remainderRatio);
		int color2part = (((int)(red2*ratio)) << 16)
				+ ((int)(green2*ratio) << 8) + (int)(blue2*ratio);
		return color1part + color2part;
	}

	/**
	 * Calculates the css color correponding to the measurementValue,
	 * given the range params.
	 * 
	 * @param value
	 * @param rangeMin
	 * @param rangeMax
	 * @return
	 */
	public String calculateCssColor( double value
			, float rangeMin, float rangeMax ) {
		float ratio = (float)(value - rangeMin)/(rangeMax-rangeMin);
		int color = getHeatMapping(ratio);
		return SVGUtil.convertToCssColor(color);
	}

	/**
	 * Generate array of colors with size equal to the argument's size
	 * where each color corresponds to how far along to whole the 
	 * corresponding value in the argument array is.
	 * 
	 * 
	 * @param ratios
	 * @return
	 */
	public int[] generateColors( float[] ratios ) {
		int[] colors = new int[ratios.length];
		float runningTotal = 0;
		for ( int i=0; i< ratios.length; i++ ) {
			colors[i] = this.getHeatMapping(runningTotal);

			float ratio = ratios[i];
			runningTotal += ratio;
		}
		return colors;
	}
}
