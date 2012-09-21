package gov.lbl.glamm.client.experiment.events;

/**
 * Notification event for vertical scrolling of
 * secondary metabolite key and experiment panels.
 * @author DHAP Digital, Inc - angie
 *
 */
public class SecondaryVertScrollEvent
		extends ScrollEvent<SecondaryVertScrollEvent.Handler> {
	public interface Handler extends ScrollEvent.Handler {
	}

	public static final Type<SecondaryVertScrollEvent.Handler> ASSOCIATED_TYPE
		= new Type<SecondaryVertScrollEvent.Handler>();

	public SecondaryVertScrollEvent(float scrollRatio) {
		super(scrollRatio);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}
}
