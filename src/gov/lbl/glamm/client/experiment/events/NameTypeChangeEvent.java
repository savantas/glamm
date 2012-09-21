package gov.lbl.glamm.client.experiment.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Indicates a toggle between long and short names.
 * 
 * @author DHAP Digital, Inc - angie
 */
public class NameTypeChangeEvent extends GwtEvent<NameTypeChangeEvent.Handler> {
	public interface Handler extends EventHandler {
		public void handleNameTypeChange( NameTypeChangeEvent ntcEvent );
	}

	public static NameTypeChangeEvent LONG_NAME_EVENT = new NameTypeChangeEvent("longName");
	public static NameTypeChangeEvent SHORT_NAME_EVENT = new NameTypeChangeEvent("shortName");

	public static final Type<Handler> ASSOCIATED_TYPE = new Type<NameTypeChangeEvent.Handler>();

	@SuppressWarnings("unused")
	private String type = null;

	private NameTypeChangeEvent( String type ) {
		this.type = type;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.handleNameTypeChange(this);
	}
}
