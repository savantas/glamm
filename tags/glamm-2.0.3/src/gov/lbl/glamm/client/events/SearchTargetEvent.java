package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.GlammPrimitive;

import java.util.HashSet;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating a search target has been found.
 * @author jtbates
 *
 */
public class SearchTargetEvent extends GwtEvent<SearchTargetEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onPicked(SearchTargetEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private HashSet<GlammPrimitive> primitives = null;

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	public SearchTargetEvent(GlammPrimitive primitive) {
		if(primitive != null) {
			this.primitives = new HashSet<GlammPrimitive>();
			this.primitives.add(primitive);
		}
	}
	
	public SearchTargetEvent(HashSet<GlammPrimitive> primitives) {
		this.primitives = primitives;
	}
	
	public HashSet<GlammPrimitive> getPrimitives() {
		return primitives;
	}
	
}
