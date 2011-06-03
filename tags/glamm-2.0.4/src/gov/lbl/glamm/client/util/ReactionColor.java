package gov.lbl.glamm.client.util;

import gov.lbl.glamm.client.GlammClientBundle;

import java.util.ArrayList;

import com.google.gwt.resources.client.ImageResource;

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
	
	public String getCssAttributeValue() {
		return cssAttributeValue;
	}
	
	public ImageResource getImageResource() {
		return imageResource;
	}
	
	public static ReactionColor getNextNonNativeColor() {
		return nonNativeColors.get(nonNativeColorIndex++ % nonNativeColors.size());
	}
	
	public static void resetNonNativeColors() {
		nonNativeColorIndex = 0;
	}
}
