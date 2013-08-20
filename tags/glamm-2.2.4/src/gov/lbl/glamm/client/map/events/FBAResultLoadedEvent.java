package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event signaling a MetabolicModel has been loaded.
 * @author wjriehl
 *
 */
public class FBAResultLoadedEvent extends GwtEvent<FBAResultLoadedEvent.Handler> {

	/**
	 * The EventHandler instance of this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onLoaded(FBAResultLoadedEvent event);
	}

	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private KBFBAResult fba;
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
	public FBAResultLoadedEvent(KBFBAResult fba) {
		this(fba, false);
	}
	
	public FBAResultLoadedEvent(KBFBAResult fba, boolean displayAllRxns) {
		this.fba = fba;
		this.displayAllRxns = displayAllRxns;
	}

	/**
	 * Gets the MetabolicModel associated with this event.
	 * @return the MetabolicModel associated with this event.
	 */
	public KBFBAResult getFBAResult() {
		return fba;
	}
	
	public boolean showAllReactions() {
		return displayAllRxns;
	}
}
