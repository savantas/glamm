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
	
	public interface Handler extends EventHandler {
		public void onPicked(RoutePickedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Compound cpdSrc = null;
	private Compound cpdDst = null;
	private Pathway route = null;
	
	public RoutePickedEvent(final Compound cpdSrc, final Compound cpdDst, final Pathway route) {
		this.cpdSrc = cpdSrc;
		this.cpdDst = cpdDst;
		this.route = route;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	public Compound getCpdDst() {
		return cpdDst;
	}
	
	public Compound getCpdSrc() {
		return cpdSrc;
	}
	
	public Pathway getRoute() {
		return route;
	}
}
