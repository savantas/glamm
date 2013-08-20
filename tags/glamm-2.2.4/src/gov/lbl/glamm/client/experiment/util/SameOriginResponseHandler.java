package gov.lbl.glamm.client.experiment.util;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * Interface for handling retrieving raw data and
 * notifying the rest of the application when the data is available
 * via the application event bus.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public interface SameOriginResponseHandler {
	public void handleResponse( Response response );
	public void handleOnError( Request request, Throwable exception );
	public void handleNonOkResponse( Request request, Response response );
	public void handleRequestException( RequestException exception );
}
