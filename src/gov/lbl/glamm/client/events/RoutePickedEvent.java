package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Pathway;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating a route has been selected
 * @author jtbates
 *
 */
public class RoutePickedEvent extends GwtEvent<RoutePickedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onPicked(RoutePickedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Compound cpdSrc;
	private Compound cpdDst;
	private Pathway route;
	
	/**
	 * Constructor
	 * @param cpdSrc The source compound for this route.
	 * @param cpdDst The destination compound for this route.
	 * @param route The route.
	 */
	public RoutePickedEvent(final Compound cpdSrc, final Compound cpdDst, final Pathway route) {
		this.cpdSrc = cpdSrc;
		this.cpdDst = cpdDst;
		this.route = route;
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
		handler.onPicked(this);
	}
	
	/**
	 * Gets the destination compound for this route.
	 * @return The destination compound.
	 */
	public Compound getCpdDst() {
		return cpdDst;
	}
	
	/**
	 * Gets the source coumpound for this route.
	 * @return The source compound.
	 */
	public Compound getCpdSrc() {
		return cpdSrc;
	}
	
	/**
	 * Gets the route.
	 * @return The route.
	 */
	public Pathway getRoute() {
		return route;
	}
}
