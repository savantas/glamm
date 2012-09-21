package gov.lbl.glamm.client.experiment.util;


import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * Base class for requests to the same origin.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public abstract class SameOriginRequest {
	
	/**
	 * If postData is not null, it is assumed to have Content-Type: application/x-www-form-urlencoded.
	 * @param method
	 * @param postData
	 * @param url
	 * @param handler
	 */
	public static void sendRequest( final RequestBuilder.Method method
			, String postData, final String url
			, final SameOriginResponseHandler handler ) {
		
		RequestBuilder builder = new RequestBuilder(method, url);
		if ( postData != null ) {
			builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
			builder.setHeader("Content-Length", String.valueOf(postData.length()));
		}

		try {
			builder.sendRequest( postData, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					handler.handleOnError(request, exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if ( Response.SC_OK == response.getStatusCode()) {
						// handle OK response from the server 
						handler.handleResponse(response);
					} else {
						handler.handleNonOkResponse(request, response);
					}
				}
			} );
		} catch ( RequestException exception ) {
			handler.handleRequestException(exception);
		}
	}
}
