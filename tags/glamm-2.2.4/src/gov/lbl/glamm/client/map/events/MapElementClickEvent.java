package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.AnnotatedMapData;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating an element of the map has been clicked
 * @author jtbates
 *
 */
public class MapElementClickEvent extends GwtEvent<MapElementClickEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onMapElementClick(MapElementClickEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private AnnotatedMapData.ElementClass elementClass;
	private Set<String> ids;
	private int clientX;
	private int clientY;
	
	private boolean clickMod;
	
	/**
	 * Constructor
	 * @param elementClass The class of element just clicked
	 * @param ids The set of ids associated with the element just clicked.
	 * @param clientX The client X position.
	 * @param clientY The client Y position.
	 */
	public MapElementClickEvent(final AnnotatedMapData.ElementClass elementClass, final Set<String> ids, final int clientX, final int clientY, final boolean clickMod) {
		this.elementClass = elementClass;
		this.ids = ids;
		this.clientX = clientX;
		this.clientY = clientY;
		this.clickMod = clickMod;
	}
	
	public boolean isControlKeyDown() {
		return clickMod;
	}
	
	/**
	 * Gets the Type associated with this event.
	 * @return The Type associated with this event.
	 */
	@Override
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onMapElementClick(this);
	}

	/**
	 * Gets the class of element clicked.
	 * @return The class of element clicked.
	 */
	public AnnotatedMapData.ElementClass getElementClass() {
		return elementClass;
	}

	/**
	 * Gets the set of ids associated with this map element.
	 * @return The set of ids.
	 */
	public Set<String> getIds() {
		return ids;
	}

	/**
	 * Gets the client X position.
	 * @return The client X position.
	 */
	public int getClientX() {
		return clientX;
	}

	/**
	 * Gets the client Y position.
	 * @return The client Y position.
	 */
	public int getClientY() {
		return clientY;
	}

}
