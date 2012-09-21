package gov.lbl.glamm.client.experiment.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Base class describing  scrolling notification.
 * @author DHAP Digital, Inc - angie
 *
 * @param <H> handler
 */
public abstract class ScrollEvent<H extends ScrollEvent.Handler>
		extends GwtEvent<H> {
	public interface Handler extends EventHandler {
		public void handleScroll( @SuppressWarnings("rawtypes") ScrollEvent se );
	}

	private float scrollRatio = 0;

	public ScrollEvent( float scrollRatio ) {
		this.scrollRatio = scrollRatio;
	}

	@Override
	protected void dispatch(H handler) {
		handler.handleScroll(this);
	}

	public float getScrollRatio() {
		return scrollRatio;
	}
}
