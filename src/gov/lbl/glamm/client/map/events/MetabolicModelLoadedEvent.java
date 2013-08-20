package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;

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
	
	private KBMetabolicModel model;
	private boolean displayAllRxns;
	
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
	public MetabolicModelLoadedEvent(KBMetabolicModel model) {
		this(model, false);
	}
	
	public MetabolicModelLoadedEvent(KBMetabolicModel model, boolean displayAllRxns) {
		this.model = model;
		this.displayAllRxns = displayAllRxns;
	}

	/**
	 * Gets the MetabolicModel associated with this event.
	 * @return the MetabolicModel associated with this event.
	 */
	public KBMetabolicModel getModel() {
		return model;
	}
	
	public boolean showAllReactions() {
		return displayAllRxns;
	}
}