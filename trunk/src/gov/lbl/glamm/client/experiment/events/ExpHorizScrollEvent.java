package gov.lbl.glamm.client.experiment.events;


/**
 * Notification event for horizontal scrolling of experiment panels.
 * @author DHAP Digital, Inc - angie
 *
 */
public class ExpHorizScrollEvent extends ScrollEvent<ExpHorizScrollEvent.Handler> {
	public interface Handler extends ScrollEvent.Handler {
	}

	public static final Type<ExpHorizScrollEvent.Handler> ASSOCIATED_TYPE
		= new Type<ExpHorizScrollEvent.Handler>();

	public ExpHorizScrollEvent(float scrollRatio) {
		super(scrollRatio);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}
}
