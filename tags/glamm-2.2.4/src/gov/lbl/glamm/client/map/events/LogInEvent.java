package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.User;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that a user has logged in.
 * @author jtbates
 *
 */
public class LogInEvent extends GwtEvent<LogInEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onLogIn(LogInEvent event);
	}

	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();

	private User user;
	
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
		handler.onLogIn(this);
	}
	
	/**
	 * Constructor
	 * @param user The user that just logged in.
	 */
	public LogInEvent(final User user) {
		this.user = user;
	}
	
	/**
	 * Gets the user associated with this event.
	 * @return The user.
	 */
	public User getUser() {
		return user;
	}
}
