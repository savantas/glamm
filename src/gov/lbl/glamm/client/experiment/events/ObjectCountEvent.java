package gov.lbl.glamm.client.experiment.events;

import gov.lbl.glamm.client.experiment.util.ObjectCount;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Object count communication that can be marked as either
 * a partial or total count.
 * @author DHAP Digital, Inc - angie
 *
 */
public class ObjectCountEvent extends GwtEvent<ObjectCountEvent.Handler> {
	public interface Handler extends EventHandler {
		public void handleObjectCountUpdated(ObjectCountEvent ocEvent);
	}

	public enum CountType {
		TOTAL, VIEW
	}
	public static final Type<Handler> ASSOCIATED_TYPE = new Type<ObjectCountEvent.Handler>();

	private CountType countType = null;
	private ObjectCount count = null;

	public ObjectCountEvent(CountType countType, ObjectCount count ) {
		this.countType = countType;
		this.count = count;
	}

	public CountType getCountType() {
		return this.countType;
	}

	public ObjectCount getCount() {
		return this.count;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.handleObjectCountUpdated(this);
	}

}
