package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.Sample;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating an experiment Sample has been selected.
 * @author jtbates
 *
 */
public class SamplePickedEvent extends GwtEvent<SamplePickedEvent.Handler> {
	public interface Handler extends EventHandler {
		public void onSamplePicked(SamplePickedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();

	private Sample sample = null;
	
	public SamplePickedEvent(Sample sample) {
		this.sample = sample;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSamplePicked(this);
	}
	
	public Sample getSample() {
		return sample;
	}
	
	
}
