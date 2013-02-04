package gov.lbl.glamm.client.map.util;

import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;

import java.util.HashMap;

/**
 * Linear interpolator between colors stored as RGB hex for ease of use as CSS attributes.
 * @author jtbates
 *
 */
public class Interpolator {

	private int colorMin = 0x0099ff;
	private int colorMid = 0x999999; 
	private int colorMax = 0xffff00;
	
	private float clampMin = 0;
	private float clampMid = 0;
	private float clampMax = 0;
	
	private String units = null;
	
	private static final HashMap<String, Interpolator> theInterpolators = new HashMap<String, Interpolator>();
	
	private static final String genKeyForSample(final Sample sample) {
		return sample.getUnits() + Float.toString(sample.getClampMin()) + Float.toString(sample.getClampMid()) + Float.toString(sample.getClampMax());
	}
	
	private static final String genKeyForFBA(final KBFBAResult fba) {
		return "mmol/gDW*h" + fba.getMinFluxValue().toString() + fba.getMaxFluxValue().toString();
	}
	
	/**
	 * Access cached Interpolator for a Sample with a given set of clamping values and units
	 * @param sample 
	 * @return the Interpolator
	 */
	public static final Interpolator getInterpolatorForSample(final Sample sample) {
		String key = genKeyForSample(sample);
		Interpolator interpolator = theInterpolators.get(key);
		
		if(interpolator == null) {
			interpolator = new Interpolator(sample);
			theInterpolators.put(key, interpolator);
		}
		
		return interpolator;
	}
	
	public static final Interpolator getInterpolatorForFBA(final KBFBAResult fba) {
		String key = genKeyForFBA(fba);
		Interpolator interpolator = theInterpolators.get(key);
		
		if (interpolator == null) {
			interpolator = new Interpolator("mmol/gDW*h", fba.getMinFluxValue(), 0, fba.getMaxFluxValue());
			theInterpolators.put(key, interpolator);
		}
		
		return interpolator;
	}
	
	/**
	 * Constructor
	 * @param units Measurement units for which this Interpolator should operate
	 * @param clampMin The lower bound of the interpolation interval
	 * @param clampMid The midpoint of the interpolation interval
	 * @param clampMax The maximum of the interpolation interval
	 */
	public Interpolator(final String units, final float clampMin, final float clampMid, final float clampMax) {
		this.units = units;
		this.clampMin = clampMin;
		this.clampMid = clampMid;
		this.clampMax = clampMax;
	}
	
	/**
	 * Constructor
	 * @param sample The Sample for which measurements are being interpolated
	 */
	public Interpolator(final Sample sample) {
		this(sample.getUnits(), sample.getClampMin(), sample.getClampMid(), sample.getClampMax());
	}
	
	/**
	 * Maps a measurement value to a color specified by the Interpolator
	 * @param value
	 * @return The computed color as a RGB hex value
	 */
	public int calcColor(final float value) {	
		
		if(value <= clampMin)
			return colorMin;
		if(value >= clampMax)
			return colorMax;
		if(value == clampMid)
			return colorMid;
		
		int r0, g0, b0;
		int r1, g1, b1;
		int r, g, b;
		float t;
		int c0, c1;
		float offset;
		float range;
		
		if(value < clampMid) {
			c0 		= colorMin;
			c1 		= colorMid;
			offset 	= clampMin;
			range 	= clampMid - clampMin; 
		}
		else {
			c0 		= colorMid;
			c1 		= colorMax;
			offset 	= clampMid;
			range	= clampMax - clampMid;
		}
		
		// extract elements of colors
		r0 = (c0 >> 16) & 0xFF;
		g0 = (c0 >> 8) & 0xFF;
		b0 = c0 & 0xFF;
		
		r1 = (c1 >> 16) & 0xFF;
		g1 = (c1 >> 8) & 0xFF;
		b1 = c1 & 0xFF;
		
		// calculate interpolation factor
		t = (value - offset) / range;
		
		// interpolate
		r = r0 + (int)(t * (r1 - r0));
		g = g0 + (int)(t * (g1 - g0));
		b = b0 + (int)(t * (b1 - b0));
		
		// reassemble color
		return (r << 16) | (g << 8) | b; 
		
	}
	
	/**
	 * Maps a measurement value to a color specified by the Interpolator
	 * @param value
	 * @return The computed color as a CSS attribute string
	 */
	public String calcCssColor(final float value) {
		int color = calcColor(value);
		String cssColor = Integer.toHexString(color);
		
		// pad with zeroes, if necessary
		while(cssColor.length() < 6)
			cssColor = "0" + cssColor;
		
		return "#" + cssColor;
	}
	
	/**
	 * Retrieves the CSS color attributes of several points along the scale including the clampMin, clampMid, and clampMax values,
	 * suitable for display as a map legend
	 * @return An array of CSS color values
	 */
	public final String[] getCssColorScale() {
		
		final String[] cssColorScale = {
				calcCssColor(clampMin),
				calcCssColor(clampMin + 0.3333f * (clampMid - clampMin)), 
				calcCssColor(clampMin + 0.6666f * (clampMid - clampMin)), 
				calcCssColor(clampMid), 
				calcCssColor(clampMid + 0.3333f * (clampMax - clampMid)), 
				calcCssColor(clampMid + 0.6666f * (clampMax - clampMid)), 
				calcCssColor(clampMax)
		};
		
		return cssColorScale;
	}
	
	/**
	 * Gets the minimum clamping value.
	 * @return The minimum clamping value.
	 */
	public final float getClampMin() {
		return clampMin;
	}

	/**
	 * Gets the clamping value midpoint.
	 * @return The clamping value midpoint.
	 */
	public final float getClampMid() {
		return clampMid;
	}

	/**
	 * Gets the maximum clamping value.
	 * @return The maximum clamping value.
	 */
	public final float getClampMax() {
		return clampMax;
	}

	/**
	 * Gets the units for the interpolator.
	 * @return The units for this interpolator.
	 */
	public final String getUnits() {
		return units;
	}
	
}
