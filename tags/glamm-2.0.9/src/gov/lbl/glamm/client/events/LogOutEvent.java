package gov.lbl.glamm.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LogOutEvent extends GwtEvent<LogOutEvent.Handler> {
	
	public interface Handler extends EventHandler {
		public void onLogOut(LogOutEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLogOut(this);
	}
}
