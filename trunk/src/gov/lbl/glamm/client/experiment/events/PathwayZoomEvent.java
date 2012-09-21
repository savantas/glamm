package gov.lbl.glamm.client.experiment.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Notification event for zoom changes. By convention, the zoom has a fixed
 * point at the top of any viewable window, not the bottom.
 * 
 * @author DHAP Digital, Inc - angie
 */
public class PathwayZoomEvent extends GwtEvent<PathwayZoomEvent.Handler> {
	public interface Handler extends EventHandler {
		public void handleZoom(PathwayZoomEvent pzEvent);
	}

	public static final Type<Handler> ASSOCIATED_TYPE = new Type<Handler>();

	private float zoomRatio = 0;

	public PathwayZoomEvent( float zoomRatio ) {
		this.zoomRatio = zoomRatio;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.handleZoom(this);
	}

	public float getZoomRatio() {
		return zoomRatio;
	}
}
