package gov.lbl.glamm.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating the state of a long loading event, where long is defined by the implementor
 * @author jtbates
 *
 */
public class LoadingEvent extends GwtEvent<LoadingEvent.Handler> {
	
	public interface Handler extends EventHandler {
		public void onLoading(LoadingEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private boolean doneLoading;
	
	/**
	 * Constructor
	 * @param doneLoading boolean indicating whether or not loading is complete
	 */
	public LoadingEvent(final boolean doneLoading) {
		this.doneLoading = doneLoading;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLoading(this);
	}
	
	/**
	 * 
	 * @return boolean indicating whether or not loading is complete
	 */
	public final boolean isDoneLoading() {
		return doneLoading;
	}

}
