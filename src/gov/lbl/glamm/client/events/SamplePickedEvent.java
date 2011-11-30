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
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onSamplePicked(SamplePickedEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();

	private Sample sample = null;
	
	/**
	 * Constructor
	 * @param sample The sample associated with this event.
	 */
	public SamplePickedEvent(Sample sample) {
		this.sample = sample;
	}
	
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
		handler.onSamplePicked(this);
	}
	
	/**
	 * Gets the sample associated with this event.
	 * @return The sample associated with this event.
	 */
	public Sample getSample() {
		return sample;
	}
	
	
}
