package gov.lbl.glamm.client;


import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
/**
 * Interface for resources contained in the client bundle
 * @author jtbates
 *
 */
public interface GlammClientBundle extends ClientBundle {
	
	static final String RESOURCE_PATH = "../../../../resources/";
	
	/**
	 * GlammClientBundle instance.
	 */
	public static final GlammClientBundle 	INSTANCE 		= GWT.create(GlammClientBundle.class);

	/**
	 * Gets the GLAMM logo in its default state.
	 * @return The GLAMM logo ImageResource.
	 */
	@Source(RESOURCE_PATH + "glamm_logo.png")
	public ImageResource glammLogoDefault();
	
	/**
	 * Gets the GLAMM logo in its MouseOver (glowing) state.
	 * @return The glowing GLAMM logo ImageResource.
	 */
	@Source(RESOURCE_PATH + "glamm_logo_glowing.png")
	public ImageResource glammLogoMouseOver();
	
	/**
	 * Gets the Help icon.
	 * @return The help icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "help.png")
	public ImageResource helpIconDefault();	
	
	/**
	 * Gets the blue locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackBlue.png")
	public ImageResource locusTrackBlue();
	
	/**
	 * Gets the cyan locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackCyan.png")
	public ImageResource locusTrackCyan();
	
	/**
	 * Gets the gray locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackGray.png")
	public ImageResource locusTrackGray();
	
	/**
	 * Gets the green locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackGreen.png")
	public ImageResource locusTrackGreen();
	
	/**
	 * Gets the magenta locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackMagenta.png")
	public ImageResource locusTrackMagenta();
	
	/**
	 * Gets the orange locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackOrange.png")
	public ImageResource locusTrackOrange();
	
	/**
	 * Gets the red locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackRed.png")
	public ImageResource locusTrackRed();
	
	/**
	 * Gets the violet locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackViolet.png")
	public ImageResource locusTrackViolet();
	
	/**
	 * Gets the white locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackWhite.png")
	public ImageResource locusTrackWhite();
	
	/**
	 * Gets the yellow locus track icon.
	 * @return A locus track icon ImageResource.
	 */
	@Source(RESOURCE_PATH + "locusTrackYellow.png")
	public ImageResource locusTrackYellow();
	
	/**
	 * Gets the pan/zoom control panel.
	 * @return The PanZoomControl SVGResource.
	 */
	@Source(RESOURCE_PATH + "PanZoomControl.svg")
	public SVGResource panZoomControl();
	
}
