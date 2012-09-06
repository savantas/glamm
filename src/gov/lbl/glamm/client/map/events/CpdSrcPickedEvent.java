package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.Compound;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that a source compound has been picked
 * @author jtbates
 *
 */
public class CpdSrcPickedEvent extends GwtEvent<CpdSrcPickedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onPicked(CpdSrcPickedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Set<Compound> compounds = null;

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
	 * Constructor
	 * @param compounds The set of compounds associated with this event.
	 */
	public CpdSrcPickedEvent(Set<Compound> compounds) {
		this.compounds = compounds;
	}
	
	/**
	 * Gets the set of compounds associated with this event.
	 * @return The set of compounds.
	 */
	public Set<Compound> getCompounds() {
		return compounds;
	}
	
}
