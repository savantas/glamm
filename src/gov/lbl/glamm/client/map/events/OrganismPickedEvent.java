package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.Organism;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that an organism has been picked
 * @author jtbates
 *
 */
public class OrganismPickedEvent extends GwtEvent<OrganismPickedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onOrganismPicked(OrganismPickedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();

	private Organism organism = null;
	
	/**
	 * Constructor
	 * @param organism The organism that has been picked
	 */
	public OrganismPickedEvent(Organism organism) {
		this.organism = organism;
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
		handler.onOrganismPicked(this);
	}

	/**
	 * Gets the organism.
	 * @return The organism that has been picked
	 */
	public final Organism getOrganism() {
		return organism;
	}
	
}
