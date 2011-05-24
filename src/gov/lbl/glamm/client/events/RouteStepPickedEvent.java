package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Reaction;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating a route step has been selected.
 * @author jtbates
 *
 */
public class RouteStepPickedEvent extends GwtEvent<RouteStepPickedEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onPicked(RouteStepPickedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Reaction reaction = null;

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	public RouteStepPickedEvent(Reaction reaction) {
		this.reaction = reaction;
	}
	
	public Reaction getReaction() {
		return reaction;
	}
	
}
