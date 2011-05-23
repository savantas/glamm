package gov.lbl.glamm.client.util;

import gov.lbl.glamm.client.model.Sample;

import java.util.HashMap;

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
	
	public static final Interpolator getInterpolatorForSample(final Sample sample) {
		String key = genKeyForSample(sample);
		Interpolator interpolator = theInterpolators.get(key);
		
		if(interpolator == null) {
			interpolator = new Interpolator(sample);
			theInterpolators.put(key, interpolator);
		}
		
		return interpolator;
	}
	
	public Interpolator(final String units, final float clampMin, final float clampMid, final float clampMax) {
		this.units = units;
		this.clampMin = clampMin;
		this.clampMid = clampMid;
		this.clampMax = clampMax;
	}
	
	public Interpolator(final Sample sample) {
		this(sample.getUnits(), sample.getClampMin(), sample.getClampMid(), sample.getClampMax());
	}
	
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
	
	public String calcCssColor(final float value) {
		int color = calcColor(value);
		String cssColor = Integer.toHexString(color);
		
		// pad with zeroes, if necessary
		while(cssColor.length() < 6)
			cssColor = "0" + cssColor;
		
		return "#" + cssColor;
	}
	
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
	
	public final float getClampMin() {
		return clampMin;
	}

	public final float getClampMid() {
		return clampMid;
	}

	public final float getClampMax() {
		return clampMax;
	}

	public final String getUnits() {
		return units;
	}
	
}
