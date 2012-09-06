package gov.lbl.glamm.client.map.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that an organism upload has been requested or has succeeded.
 * @author jtbates
 *
 */
public class OrganismUploadEvent extends GwtEvent<OrganismUploadEvent.Handler> {
	
	/**
	 * Enum for Action specified by event
	 * @author jtbates
	 *
	 */
	public enum Action {
		REQUEST,
		SUCCESS;
	}
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onRequest(OrganismUploadEvent event);
		public void onSuccess(OrganismUploadEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static Type<Handler> TYPE = new Type<Handler>();

	private Action action;
	
	/**
	 * Constructor
	 * @param action The action for this event.
	 */
	public OrganismUploadEvent(final Action action) {
		this.action = action;
	}
	
	/**
	 * The Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		if(action == Action.REQUEST)
			handler.onRequest(this);
		else if(action == Action.SUCCESS)
			handler.onSuccess(this);
	}
	
}
