package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.Compound;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
/**
 * Event indicating that the source compound has been disambiguated in the case of the same
 * synonym referring to multiple Compounds.
 * @author jtbates
 *
 */
public class CpdSrcDisambiguatedEvent extends GwtEvent<CpdSrcDisambiguatedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onDisambiguated(CpdSrcDisambiguatedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Compound compound = null;

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
		handler.onDisambiguated(this);
	}
	/**
	 * Constructor
	 * @param compound The disambiguated compound
	 */
	public CpdSrcDisambiguatedEvent(Compound compound) {
		this.compound = compound;
	}
	
	/**
	 * Gets the disambiguated compound.
	 * @return the compound
	 */
	public Compound getCompound() {
		return compound;
	}
	
}
