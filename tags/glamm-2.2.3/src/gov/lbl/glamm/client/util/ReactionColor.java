package gov.lbl.glamm.client.util;

import gov.lbl.glamm.client.GlammClientBundle;

import java.util.ArrayList;

import com.google.gwt.resources.client.ImageResource;

/**
 * Enumerated type relating locus track icon image resources in the client bundle to CSS attributes for reaction colors.
 * @author jtbates
 *
 */
public enum ReactionColor {
	
	BLUE	(GlammClientBundle.INSTANCE.locusTrackBlue(),		"blue"),
	CYAN	(GlammClientBundle.INSTANCE.locusTrackCyan(), 		"cyan"),
	GRAY	(GlammClientBundle.INSTANCE.locusTrackGray(), 		"gray"),
	GREEN	(GlammClientBundle.INSTANCE.locusTrackGreen(), 		"green"),
	MAGENTA	(GlammClientBundle.INSTANCE.locusTrackMagenta(),	"magenta"),
	ORANGE	(GlammClientBundle.INSTANCE.locusTrackOrange(), 	"orange"),
	RED		(GlammClientBundle.INSTANCE.locusTrackRed(),	 	"red"),
	VIOLET	(GlammClientBundle.INSTANCE.locusTrackViolet(), 	"violet"),
	WHITE	(GlammClientBundle.INSTANCE.locusTrackWhite(), 		"white"),
	YELLOW	(GlammClientBundle.INSTANCE.locusTrackYellow(), 	"yellow"),
	NATIVE 	(GlammClientBundle.INSTANCE.locusTrackWhite(),		"white");
	
	private static final ArrayList<ReactionColor> nonNativeColors = new ArrayList<ReactionColor>();
	static {
		nonNativeColors.add(RED);
		nonNativeColors.add(ORANGE);
		nonNativeColors.add(YELLOW);
		nonNativeColors.add(GREEN);
		nonNativeColors.add(CYAN);
		nonNativeColors.add(BLUE);
		nonNativeColors.add(VIOLET);
		nonNativeColors.add(MAGENTA);
	}
	
	private static int nonNativeColorIndex = 0;
	
	private ImageResource imageResource = null;
	private String cssAttributeValue = null;
	
	private ReactionColor(ReactionColor color) {
		this.imageResource = color.imageResource;
		this.cssAttributeValue = color.cssAttributeValue;
	}
	
	private ReactionColor(ImageResource imageResource, String cssAttributeValue) {
		this.imageResource = imageResource;
		this.cssAttributeValue = cssAttributeValue;
	}
	
	/**
	 * Gets the CSS attribute string.
	 * @return The attribute string.
	 */
	public String getCssAttributeValue() {
		return cssAttributeValue;
	}
	
	/**
	 * Gets the locus track image resource.
	 * @return The image resource.
	 */
	public ImageResource getImageResource() {
		return imageResource;
	}
	
	/**
	 * Gets the next reaction color for non-native reactions.
	 * @return The next reaction color.
	 */
	public static ReactionColor getNextNonNativeColor() {
		return nonNativeColors.get(nonNativeColorIndex++ % nonNativeColors.size());
	}
	
	/**
	 * Resets the non-native reaction color index to 0 (i.e. the first non-native reaction color.)
	 */
	public static void resetNonNativeColors() {
		nonNativeColorIndex = 0;
	}
}
