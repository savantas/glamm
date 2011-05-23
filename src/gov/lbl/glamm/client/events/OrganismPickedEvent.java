package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Organism;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that an organism has been picked
 * @author jtbates
 *
 */
public class OrganismPickedEvent extends GwtEvent<OrganismPickedEvent.Handler> {
	
	public interface Handler extends EventHandler {
		public void onOrganismPicked(OrganismPickedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();

	private Organism organism = null;
	
	/**
	 * Constructor
	 * @param organism The organism that has been picked
	 */
	public OrganismPickedEvent(Organism organism) {
		this.organism = organism;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onOrganismPicked(this);
	}

	/**
	 * 
	 * @return The organism that has been picked
	 */
	public final Organism getOrganism() {
		return organism;
	}
	
}
