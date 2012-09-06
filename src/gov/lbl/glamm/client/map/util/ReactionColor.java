package gov.lbl.glamm.client.map.util;

import java.util.ArrayList;

/**
 * Enumerated type relating locus track icon image resources in the client bundle to CSS attributes for reaction colors.
 * @author jtbates
 *
 */
public enum ReactionColor {
	
	BLUE	("blue"),
	CYAN	("cyan"),
	GRAY	("gray"),
	GREEN	("green"),
	MAGENTA	("magenta"),
	ORANGE	("orange"),
	RED		("red"),
	VIOLET	("violet"),
	WHITE	("white"),
	YELLOW	("yellow"),
	NATIVE 	("white");
	
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
	
	private String cssAttributeValue = null;
	
	private ReactionColor(ReactionColor color) {
		this.cssAttributeValue = color.cssAttributeValue;
	}
	
	private ReactionColor(String cssAttributeValue) {
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
