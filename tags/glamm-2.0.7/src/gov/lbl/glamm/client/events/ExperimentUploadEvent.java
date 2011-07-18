package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Organism;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that an experiment data upload has either been requested or has succeeded.
 * @author jtbates
 *
 */
public class ExperimentUploadEvent extends GwtEvent<ExperimentUploadEvent.Handler> {
	
	/**
	 * Enumerated type indicating what action is being indicated by this event
	 * @author jtbates
	 *
	 */
	public enum Action {
		REQUEST,
		SUCCESS;
	}
	
	public interface Handler extends EventHandler {
		public void onRequest(ExperimentUploadEvent event);
		public void onSuccess(ExperimentUploadEvent event);
	}
	
	public static Type<Handler> TYPE = new Type<Handler>();

	private Organism organism = null;
	private Action action;
	
	/**
	 * Constructor
	 * @param organism The organism for which an experiment is being uploaded
	 * @param action The action indicated by this event
	 */
	public ExperimentUploadEvent(final Organism organism, final Action action) {
		this.organism = organism;
		this.action = action;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		if(action == Action.REQUEST)
			handler.onRequest(this);
		else if(action == Action.SUCCESS)
			handler.onSuccess(this);
	}
	
	/**
	 * Gets the organism associated with this event
	 * @return the Organism associated with this event
	 */
	public Organism getOrganism() {
		return organism;
	}
	
}
