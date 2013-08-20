package gov.lbl.glamm.client.map.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * An event for signaling when Group Data has been uploaded by the user.
 * @author wjriehl
 *
 */
public class GroupDataUploadEvent extends GwtEvent<GroupDataUploadEvent.Handler> {

	/**
	 * Enum for the Action specified by the event.
	 */
	public enum Action {
		REQUEST,
		SUCCESS;
	}
	
	/**
	 * The EventHandler interface for this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onRequest(GroupDataUploadEvent event);
		public void onSuccess(GroupDataUploadEvent event);		
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static Type<Handler> TYPE = new Type<Handler>();
	
	private Action action;
	
	/**
	 * Constructor
	 * @param action the Action that this event should take when dispatched.
	 */
	public GroupDataUploadEvent(final Action action) {
		this.action = action;
	}

	/**
	 * Gets the Type associated with this event.
	 * @return the Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		if (action == Action.REQUEST)
			handler.onRequest(this);
		else if (action == Action.SUCCESS)
			handler.onSuccess(this);
	}
}