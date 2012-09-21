package gov.lbl.glamm.client.experiment.events;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Indicates that raw data has been received and is ready for processing.
 * @author DHAP Digital, Inc - angie
 *
 */
public class RawDataReceivedEvent extends GwtEvent<RawDataReceivedEvent.Handler> {
	public interface Handler extends EventHandler {
		public void handleDataReceived(RawDataReceivedEvent drEvent);
	}

	public static final Type<Handler> ASSOCIATED_TYPE = new Type<Handler>();

	private PathwayExperimentData peData = null;
	private long requestResponseMillisecs = 0;

	public RawDataReceivedEvent( PathwayExperimentData peData
			, Date requestSentDate, Date responseReceivedDate ) {
		if ( peData == null ) {
			throw new RuntimeException("DataReceivedEvent constructor Error: pathwayExperimentData should not be null.");
		}
		this.peData = peData;
		this.requestResponseMillisecs
				= responseReceivedDate.getTime() - requestSentDate.getTime();
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.handleDataReceived(this);
	}

	public PathwayExperimentData getPEData() {
		return this.peData;
	}
	public long getRequestResponseMillisecs() {
		return requestResponseMillisecs;
	}
}
