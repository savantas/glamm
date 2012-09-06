package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.AnnotatedMapData;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating a map element has the mouse hovering over it. Used primarily
 * for adjusting the color of pathways when mousing over the pathway name.
 * @author wjriehl
 *
 */

public class MapElementMouseOverEvent extends GwtEvent<MapElementMouseOverEvent.Handler> {
	
	/**
	 * The event handler interface for this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onMapElementMouseOver(MapElementMouseOverEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private AnnotatedMapData.ElementClass elementClass;
	private Set<String> ids;
	private int clientX;
	private int clientY;
	
	/**
	 * Constructor
	 * @param elementClass The class of element just clicked
	 * @param ids The set of ids associated with the element just clicked.
	 * @param clientX The client X position.
	 * @param clientY The client Y position.
	 */
	public MapElementMouseOverEvent(final AnnotatedMapData.ElementClass elementClass, final Set<String> ids, final int clientX, final int clientY) {
		this.elementClass = elementClass;
		this.ids = ids;
		this.clientX = clientX;
		this.clientY = clientY;
	}
	
	/**
	 * Gets the handler type associated with this event.
	 * @return the Type associated with this event.
	 */
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	/**
	 * Activated this event when dispatched.
	 */
	protected void dispatch(Handler handler) {
		handler.onMapElementMouseOver(this);
	}
	
	/**
	 * Gets the class of the map element that's moused over.
	 * @return the class of the map element that's been moused over.
	 */
	public AnnotatedMapData.ElementClass getElementClass() {
		return elementClass;
	}
	
	/**
	 * Gets the set of ids associated with the moused over map element.
	 * @return a set of ids
	 */
	public Set<String> getIds() {
		return ids;
	}
	
	/**
	 * Gets the client X position. This will be the point at which the element was moused over.
	 * @return the client X position
	 */
	public int getClientX() {
		return clientX;
	}
	
	/**
	 * Gets the client Y position. This will be the point at which the element was moused over.
	 * @return
	 */
	public int getClientY() {
		return clientY;
	}
}
