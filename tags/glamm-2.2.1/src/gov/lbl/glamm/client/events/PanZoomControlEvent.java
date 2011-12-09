package gov.lbl.glamm.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired from the PanZoomControl, indicating the intended transform to be applied to map.
 * @author jtbates
 *
 */
public class PanZoomControlEvent extends GwtEvent<PanZoomControlEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onPanZoom(PanZoomControlEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
		
	/**
	 * Enum indicating which action is being reported by a PanZoomEvent
	 * @author jtbates
	 *
	 */
	public static enum Action {
		NONE,
		ZOOM_IN,
		ZOOM_OUT,
		PAN_UP,
		PAN_DOWN,
		PAN_LEFT,
		PAN_RIGHT,
		ZOOM_SLIDER,
		ZOOM_TO_FIT;
	}
	
	private Action action = Action.NONE;
	private float zoomNorm = 0.0f;
	
	/**
	 * Constructor
	 * @param action The action reported by this event.
	 * @param zoomNorm The normalized zoom factor.
	 */
	public PanZoomControlEvent(final Action action, final float zoomNorm) {
		this.action = action;
		this.zoomNorm = zoomNorm;
	}
	
	/**
	 * The Type associated with this event.
	 */
	@Override
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onPanZoom(this);
	}
	
	/**
	 * Gets the action reported by this event.
	 * @return The action reported by this event.
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Gets the normalized zoom for this event.
	 * @return The normalized zoom for this event.
	 */
	public float getZoomNorm() {
		return zoomNorm;
	}
	
}
