package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.AnnotatedMapDescriptor;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event signaling that an AnnotatedMapDescriptor has been picked. 
 * @author jtbates
 *
 */
public class AMDPickedEvent extends GwtEvent<AMDPickedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onPicked(AMDPickedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private AnnotatedMapDescriptor amd;
	
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
	 * @param amd The AnnotatedMapDescriptor.
	 */
	public AMDPickedEvent(final AnnotatedMapDescriptor amd) {
		this.amd = amd;
	}
	
	/**
	 * Gets the AnnotatedMapDescriptor.
	 * @return The AnnotatedMapDescriptor.
	 */
	public AnnotatedMapDescriptor getDescriptor() {
		return amd;
	}
}
