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
	public static final GlammClientBundle 	INSTANCE 		= GWT.create(GlammClientBundle.class);

	@Source(RESOURCE_PATH + "glamm_logo.png")
	public ImageResource glammLogoDefault();
	
	@Source(RESOURCE_PATH + "glamm_logo_glowing.png")
	public ImageResource glammLogoMouseOver();
	
	@Source(RESOURCE_PATH + "help.png")
	public ImageResource helpIconDefault();	
	
	@Source(RESOURCE_PATH + "locusTrackBlue.png")
	public ImageResource locusTrackBlue();
	
	@Source(RESOURCE_PATH + "locusTrackCyan.png")
	public ImageResource locusTrackCyan();
	
	@Source(RESOURCE_PATH + "locusTrackGray.png")
	public ImageResource locusTrackGray();
	
	@Source(RESOURCE_PATH + "locusTrackGreen.png")
	public ImageResource locusTrackGreen();
	
	@Source(RESOURCE_PATH + "locusTrackMagenta.png")
	public ImageResource locusTrackMagenta();
	
	@Source(RESOURCE_PATH + "locusTrackOrange.png")
	public ImageResource locusTrackOrange();
	
	@Source(RESOURCE_PATH + "locusTrackRed.png")
	public ImageResource locusTrackRed();
	
	@Source(RESOURCE_PATH + "locusTrackViolet.png")
	public ImageResource locusTrackViolet();
	
	@Source(RESOURCE_PATH + "locusTrackWhite.png")
	public ImageResource locusTrackWhite();
	
	@Source(RESOURCE_PATH + "locusTrackYellow.png")
	public ImageResource locusTrackYellow();
	
	@Source(RESOURCE_PATH + "PanZoomControl.svg")
	public SVGResource panZoomControl();
	
}
