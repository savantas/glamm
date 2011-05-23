package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.AnnotatedMapData;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

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
	
	public int clientX = 0;
	public int clientY = 0;
	public Element element = null;
	
	/**
	 * Constructor
	 * @param event The GWT ClickEvent corresponding with this event
	 */
	public MapElementClickEvent(ClickEvent event) {
		this.clientX = event.getClientX();
		this.clientY = event.getClientY();
		this.element = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
	}
	
	@Override
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onMapElementClick(this);
	}
	
	/**
	 * 
	 * @return the client x coordinate of the click event
	 */
	public int getClientX() {
		return this.clientX;
	}
	
	/**
	 * 
	 * @return the client y coordinate of the click event
	 */
	public int getClientY() {
		return this.clientY;
	}
	
	/**
	 * 
	 * @return the map element clicked
	 */
	public Element getElement() {
		return element;
	}
	
	/**
	 * Convenience method getting the "class" attribute of the clicked element
	 * @return the value of the clicked element's "class" attribute
	 */
	public String getElementClass() {
		if(element != null)
			return element.getAttribute(AnnotatedMapData.ATTRIBUTE_CLASS);
		return null;
	}
	
	/**
	 * Convenience method getting the "query" attribute of the clicked element
	 * @return the value of the clicked element's "query" attribute
	 */
	public String getElementQuery() {
		if(element != null)
			return element.getAttribute(AnnotatedMapData.ATTRIBUTE_QUERY);
		return null;
	}
}
