package gov.lbl.glamm.client.map.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating the state of a long loading event, where long is defined by the implementor
 * @author jtbates
 *
 */
public class LoadingEvent extends GwtEvent<LoadingEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onLoading(LoadingEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private boolean doneLoading;
	
	/**
	 * Constructor
	 * @param doneLoading boolean indicating whether or not loading is complete
	 */
	public LoadingEvent(final boolean doneLoading) {
		this.doneLoading = doneLoading;
	}

	/**
	 * Gets the Type associated with this event.
	 * @return The Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLoading(this);
	}
	
	/**
	 * Gets flag indicating whether or not loading is complete.
	 * @return boolean indicating whether or not loading is complete
	 */
	public final boolean isDoneLoading() {
		return doneLoading;
	}

}
