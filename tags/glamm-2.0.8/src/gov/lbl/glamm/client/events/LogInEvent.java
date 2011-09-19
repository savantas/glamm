package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.GlammUser;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LogInEvent extends GwtEvent<LogInEvent.Handler> {
	
	public interface Handler extends EventHandler {
		public void onLogIn(LogInEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private GlammUser user;
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLogIn(this);
	}
	
	public LogInEvent(final GlammUser user) {
		this.user = user;
	}
	
	public GlammUser getUser() {
		return user;
	}
}
