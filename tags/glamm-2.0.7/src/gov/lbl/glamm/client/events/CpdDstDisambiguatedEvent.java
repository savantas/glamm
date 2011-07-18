package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Compound;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
/**
 * Event indicating that the destination compound has been disambiguated in the case of the same
 * synonym referring to multiple Compounds.
 * @author jtbates
 *
 */
public class CpdDstDisambiguatedEvent extends GwtEvent<CpdDstDisambiguatedEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onDisambiguated(CpdDstDisambiguatedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Compound compound = null;

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
	public CpdDstDisambiguatedEvent(Compound compound) {
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
