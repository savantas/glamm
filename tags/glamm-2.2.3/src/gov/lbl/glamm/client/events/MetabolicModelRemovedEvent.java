package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.MetabolicModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event signaling a MetabolicModel has been removed.
 * @author wjriehl
 *
 */
public class MetabolicModelRemovedEvent extends GwtEvent<MetabolicModelRemovedEvent.Handler> {
	
	/**
	 * The EventHandler instance of this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onRemoval(MetabolicModelRemovedEvent event);
	}

	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private MetabolicModel model;
	
	/**
	 * Constructor
	 * @param model the MetabolicModel
	 */
	public MetabolicModelRemovedEvent(MetabolicModel model) {
		this.model = model;
	}
	
	/**
	 * Gets the MetabolicModel associated with this event.
	 * @return the MetabolicModel associated with this event.
	 */
	public MetabolicModel getModel() {
		return model;
	}
	
	/**
	 * Gets the Type associated with this event.
	 * @return the Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * Dispatches this event.
	 */
	@Override
	protected void dispatch(Handler handler) {
		handler.onRemoval(this);
	}

}
