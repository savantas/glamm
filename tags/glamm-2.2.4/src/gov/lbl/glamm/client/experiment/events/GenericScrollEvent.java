package gov.lbl.glamm.client.experiment.events;


/**
 * Generic class describing scrolling notification.
 * Can be used in local cases where the event is understood through context only.
 * @author DHAP Digital, Inc - angie
 *
 */
public class GenericScrollEvent
		extends ScrollEvent<GenericScrollEvent.Handler> {
	public interface Handler extends ScrollEvent.Handler {
	}

	public static final Type<GenericScrollEvent.Handler> ASSOCIATED_TYPE
		= new Type<GenericScrollEvent.Handler>();

	public GenericScrollEvent(float scrollRatio) {
		super(scrollRatio);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}

}
