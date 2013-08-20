package gov.lbl.glamm.client.experiment.events;

/**
 * Notification event for vertical scrolling of main pathway and experiment panels.
 * @author DHAP Digital, Inc - angie
 *
 */
public class PathwayVerticalScrollEvent
		extends ScrollEvent<PathwayVerticalScrollEvent.Handler> {
	public interface Handler extends ScrollEvent.Handler {
	}

	public static final Type<PathwayVerticalScrollEvent.Handler> ASSOCIATED_TYPE
			= new Type<PathwayVerticalScrollEvent.Handler>();

	public PathwayVerticalScrollEvent( float scrollRatio ) {
		super(scrollRatio);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}
}
