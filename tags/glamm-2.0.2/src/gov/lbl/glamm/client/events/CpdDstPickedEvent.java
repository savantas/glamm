package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.GlammPrimitive;

import java.util.HashSet;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that a destination compound has been picked
 * @author jtbates
 *
 */
public class CpdDstPickedEvent extends GwtEvent<CpdDstPickedEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onPicked(CpdDstPickedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private HashSet<Compound> compounds = null;

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	/**
	 * Constructor
	 * @param primitives The GlammPrimitives corresponding to the desired compound
	 */
	public CpdDstPickedEvent(HashSet<GlammPrimitive> primitives) {
		compounds = new HashSet<Compound>();
		for(GlammPrimitive primitive : primitives) {
			if(primitive.getType() == Compound.TYPE)
				compounds.add((Compound) primitive);
		}
	}
	
	/**
	 * @return the compounds associated with this event
	 */
	public HashSet<Compound> getCompounds() {
		return compounds;
	}
	
}
