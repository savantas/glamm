package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AMDPickedEvent extends GwtEvent<AMDPickedEvent.Handler> {
	
	public interface Handler extends EventHandler {
		public void onPicked(AMDPickedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private AnnotatedMapDescriptor amd;
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	public AMDPickedEvent(final AnnotatedMapDescriptor amd) {
		this.amd = amd;
	}
	
	public AnnotatedMapDescriptor getDescriptor() {
		return amd;
	}
}
