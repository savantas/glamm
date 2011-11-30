package gov.lbl.glamm.client.events;

import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGRect;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that the map has been panned or zoomed
 * @author jtbates
 *
 */
public class MapUpdateEvent extends GwtEvent<MapUpdateEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onMapUpdate(MapUpdateEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private OMSVGMatrix ctm = null;
	private OMSVGRect viewRectNorm = null;
	private float zoomNorm = 0.0f;
	
	/**
	 * Constructor
	 * @param ctm the current transform matrix for the map's designated viewport
	 * @param viewRectNorm the normalized view rect
	 * @param zoomNorm the normalized zoom factor
	 */
	public MapUpdateEvent(OMSVGMatrix ctm, OMSVGRect viewRectNorm, float zoomNorm) {
		this.ctm = ctm;
		this.viewRectNorm = viewRectNorm;
		this.zoomNorm = zoomNorm;
	}
	
	/**
	 * Gets the Type associated with this event.
	 * @return The Type associated with this event.
	 */
	@Override
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onMapUpdate(this);
	}
	
	/**
	 * Gets the CTM.
	 * @return the current transform matrix for the map's designated viewport
	 */
	public OMSVGMatrix getCtm() {
		return ctm;
	}
	
	/**
	 * Gets the normalized view rect.
	 * @return the normalized view rect
	 */
	public OMSVGRect getViewRectNorm() {
		return viewRectNorm;
	}
	
	/**
	 * Gets the normalized zoom factor.
	 * @return the normalized zoom factor
	 */
	public float getZoomNorm() {
		return zoomNorm;
	}
	
	
}
