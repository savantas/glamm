package gov.lbl.glamm.client.events;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating an element of the map has been clicked
 * @author jtbates
 *
 */
public class MapElementClickEvent extends GwtEvent<MapElementClickEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onMapElementClick(MapElementClickEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private String elementClass;
	private Set<String> ids;
	private int clientX;
	private int clientY;
	
	/**
	 * Constructor
	 * @param event The GWT ClickEvent corresponding with this event
	 */
	
	public MapElementClickEvent(final String elementClass, final Set<String> ids, final int clientX, final int clientY) {
		this.elementClass = elementClass;
		this.ids = ids;
		this.clientX = clientX;
		this.clientY = clientY;
	}
	
	@Override
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onMapElementClick(this);
	}

	public String getElementClass() {
		return elementClass;
	}

	public Set<String> getIds() {
		return ids;
	}

	public int getClientX() {
		return clientX;
	}

	public int getClientY() {
		return clientY;
	}

}
