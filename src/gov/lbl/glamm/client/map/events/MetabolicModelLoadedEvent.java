package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.MetabolicModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event signaling a MetabolicModel has been loaded.
 * @author wjriehl
 *
 */
public class MetabolicModelLoadedEvent extends GwtEvent<MetabolicModelLoadedEvent.Handler> {

	/**
	 * The EventHandler instance of this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onLoaded(MetabolicModelLoadedEvent event);
	}

	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private MetabolicModel model;
	
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
		handler.onLoaded(this);
	}
	
	/**
	 * Constructor
	 * @param model the MetabolicModel
	 */
	public MetabolicModelLoadedEvent(MetabolicModel model) {
		this.model = model;
	}
	
	/**
	 * Gets the MetabolicModel associated with this event.
	 * @return the MetabolicModel associated with this event.
	 */
	public MetabolicModel getModel() {
		return model;
	}
}
