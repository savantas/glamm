package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.Reaction;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating a route step has been selected.
 * @author jtbates
 *
 */
public class RouteStepPickedEvent extends GwtEvent<RouteStepPickedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onPicked(RouteStepPickedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Reaction reaction = null;

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
		handler.onPicked(this);
	}
	
	/**
	 * Construction
	 * @param reaction Reaction associated with this route step.
	 */
	public RouteStepPickedEvent(Reaction reaction) {
		this.reaction = reaction;
	}
	
	/**
	 * Gets the reaction associated with this route step.
	 * @return The reaction associated with this route step.
	 */
	public Reaction getReaction() {
		return reaction;
	}
	
}
