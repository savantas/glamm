package gov.lbl.glamm.client.events;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired from the PanZoomControl, indicating the intended transform to be applied to map.
 * @author jtbates
 *
 */
public class PanZoomControlEvent extends GwtEvent<PanZoomControlEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onPanZoom(PanZoomControlEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
		
	public static final short ACTION_NONE 			= 0;
	public static final short ACTION_ZOOM_IN 		= 1;
	public static final short ACTION_ZOOM_OUT 		= 2;
	public static final short ACTION_PAN_UP 		= 3;
	public static final short ACTION_PAN_DOWN 		= 4;
	public static final short ACTION_PAN_LEFT 		= 5;
	public static final short ACTION_PAN_RIGHT 		= 6;
	public static final short ACTION_ZOOM_SLIDER	= 7;
	
	private static final String ACTION_DESC_NONE 		= "none";
	private static final String ACTION_DESC_ZOOM_IN 	= "zoom in";
	private static final String ACTION_DESC_ZOOM_OUT 	= "zoom out";
	private static final String ACTION_DESC_PAN_UP 		= "pan up";
	private static final String ACTION_DESC_PAN_DOWN 	= "pan down";
	private static final String ACTION_DESC_PAN_LEFT 	= "pan left";
	private static final String ACTION_DESC_PAN_RIGHT 	= "pan right";
	private static final String ACTION_DESC_ZOOM_SLIDER = "zoom slider";
	
	private static final Map<Short, String> action2Desc;
	static {
		Map<Short, String> aMap = new HashMap<Short, String>();
		aMap.put(ACTION_NONE, ACTION_DESC_NONE);
		aMap.put(ACTION_ZOOM_IN, ACTION_DESC_ZOOM_IN);
		aMap.put(ACTION_ZOOM_OUT, ACTION_DESC_ZOOM_OUT);
		aMap.put(ACTION_PAN_UP, ACTION_DESC_PAN_UP);
		aMap.put(ACTION_PAN_DOWN, ACTION_DESC_PAN_DOWN);
		aMap.put(ACTION_PAN_LEFT, ACTION_DESC_PAN_LEFT);
		aMap.put(ACTION_PAN_RIGHT, ACTION_DESC_PAN_RIGHT);
		aMap.put(ACTION_ZOOM_SLIDER, ACTION_DESC_ZOOM_SLIDER);
		action2Desc = Collections.unmodifiableMap(aMap);
	}
	
	
	private short action = ACTION_NONE;
	private float zoomNorm = 0.0f;
	
	
	public PanZoomControlEvent(short action, float zoomNorm) {
		this.action = action;
		this.zoomNorm = zoomNorm;
	}
	
	@Override
	public GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onPanZoom(this);
	}
	
	public short getAction() {
		return action;
	}
	
	public float getZoomNorm() {
		return zoomNorm;
	}
	
	public String getDescription() {
		return "action: " + action2Desc.get(action) + " (" + action + ") zoomNorm: " + zoomNorm;
	}
}
