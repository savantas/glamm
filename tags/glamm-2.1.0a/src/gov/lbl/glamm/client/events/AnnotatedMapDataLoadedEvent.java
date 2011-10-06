package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.AnnotatedMapData;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AnnotatedMapDataLoadedEvent extends GwtEvent<AnnotatedMapDataLoadedEvent.Handler> {
	
	public interface Handler extends EventHandler {
		public void onLoaded(AnnotatedMapDataLoadedEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();
	private AnnotatedMapData annotatedMapData;
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLoaded(this);
	}
	
	public AnnotatedMapDataLoadedEvent(final AnnotatedMapData annotatedMapData) {
		this.annotatedMapData = annotatedMapData;
	}
	
	public AnnotatedMapData getMapData() {
		return annotatedMapData;
	}
}
