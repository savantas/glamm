package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.AnnotatedMapData;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event signaling annotated map data has been loaded.
 * @author jtbates
 *
 */
public class AnnotatedMapDataLoadedEvent extends GwtEvent<AnnotatedMapDataLoadedEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onLoaded(AnnotatedMapDataLoadedEvent event);
	}

	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	private AnnotatedMapData annotatedMapData;
	
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
		handler.onLoaded(this);
	}
	
	/**
	 * Constructor
	 * @param annotatedMapData The annotated map data for this event.
	 */
	public AnnotatedMapDataLoadedEvent(final AnnotatedMapData annotatedMapData) {
		this.annotatedMapData = annotatedMapData;
	}
	
	/**
	 * Gets the annotated map data.
	 * @return The annotated map data.
	 */
	public AnnotatedMapData getMapData() {
		return annotatedMapData;
	}
}
