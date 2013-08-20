package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.AppController;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.RawDataReceivedEvent;
import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.client.experiment.rpc.PathwayExperimentService;
import gov.lbl.glamm.client.experiment.rpc.PathwayExperimentServiceAsync;
import gov.lbl.glamm.client.experiment.util.ObjectCount;

import java.util.Date;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles retrieving raw data and
 * notifying the rest of the application when the data is available
 * via the application event bus.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class DataRetrievalPresenter {
	public interface View {

		public void displayRetrievalError( String serviceInfo, String errorMessage );
		public void displayRetrievalSuccess( String serviceInfo, String message );
		public void waitForResponse(String serviceInfo, String postData);

		public void setResponseText( String serviceInfo, String message );

		public void displayObjectCount( String message );
		public void displayExcessiveObjectCountWarning();

		public void displayDebugData( long requestResponseMillisecs, long processDaoMillisecs, long drawSVGMillisecs );
	}

	private PathwayExperimentServiceAsync pathwayExperimentService = null;

	private View view = null;
	private HandlerManager eventBus = null;
	private Date requestSentDate = null;
	private Date responseReceivedDate = null;

	private boolean waitingForResponse = false;

	public DataRetrievalPresenter( View view
			, HandlerManager eventBus ) {
		this.view = view;
		this.eventBus = eventBus;
		this.requestSentDate = new Date();
		this.responseReceivedDate = new Date();
		this.bind();
	}

	public void bind() {
		// Handle object count warnings and display
		@SuppressWarnings("unused")
		HandlerRegistration handlerReg = eventBus.addHandler(
				ObjectCountEvent.ASSOCIATED_TYPE
				, new ObjectCountEvent.Handler() {
					@Override
					public void handleObjectCountUpdated(
							ObjectCountEvent ocEvent ) {
						if ( ocEvent.getCountType()
								== ObjectCountEvent.CountType.TOTAL ) {
							ObjectCount objectCount = ocEvent.getCount();
							if ( objectCount.getSVGElementsTotal() > 30000 ) {
								view.displayExcessiveObjectCountWarning();
							}
							// TODO: display more info if in some sort of debug mode
						}
						AppController appInstance = AppController.getInstance();
						if ( appInstance.isInDebugMode() ) {
						// Display debug data
						appInstance.endSVGDate = new Date();
						appInstance.drawSVGMillisecs = appInstance.endSVGDate.getTime() - AppController.getInstance().startSVGDate.getTime();
						view.displayDebugData( appInstance.requestResponseMillisecs
								, appInstance.daoProcessingMillisecs
								, appInstance.drawSVGMillisecs );
						}
					}
				});
	}

	/**
	 * Handle the server response results.
	 */
	public void handleResponse( PathwayExperimentData peData ) {
		waitingForResponse = false;
		// Set text, but don't display anything on retrieval success
		view.setResponseText( pathwayExperimentService.toString()
			, "Status OK; response: " + peData.getExperiments().size() + " experiments, " + peData.getPathways() + " pathways" );
		eventBus.fireEvent( new RawDataReceivedEvent( peData
				, requestSentDate, responseReceivedDate ) );
	}

	public void handleOnError( Request request, Throwable exception ) {
		waitingForResponse = false;
		// Show the error message to the user
		if ( exception instanceof RequestTimeoutException ) {
			view.displayRetrievalError( pathwayExperimentService.toString(), " request timed out." );
		} else {
			view.displayRetrievalError( pathwayExperimentService.toString(), " error was " + exception );
		}
	}
	public void handleOnError( Throwable exception ) {
		waitingForResponse = false;
		// Show the error message to the user
		if ( exception instanceof RequestTimeoutException ) {
			view.displayRetrievalError( pathwayExperimentService.toString(), " request timed out." );
		} else {
			view.displayRetrievalError( pathwayExperimentService.toString(), " error was " + exception );
		}
	}
	public void handleRequestException( RequestException re ) {
		waitingForResponse = false;
		// Show the error message to the user
		view.displayRetrievalError( pathwayExperimentService.toString(), " error was " + re );
		re.printStackTrace();
	}

	/**
	 * Request the specified uri with post data and wait for a response.
	 * @param pathwayExperimentService
	 * @param url
	 */
	public void requestFromServer( PathwayExperimentServiceAsync pathwayExperimentService ) {
		if ( this.waitingForResponse == true ) {
			return;
		}
		if ( pathwayExperimentService == null ) {
			throw new RuntimeException( "Invalid parameter: pathwayExperimentService cannot be null." );
		}
		this.pathwayExperimentService = pathwayExperimentService;

		this.waitingForResponse = true;

		// Get information from cookie
		String pathwaySelection = null;
		String experimentSelection = null;
		if ( Cookies.isCookieEnabled() ) {
			pathwaySelection = Cookies.getCookie(
					PathwayExperimentService.PATHWAY_SELECTION_COOKIE );
			experimentSelection = Cookies.getCookie(
					PathwayExperimentService.EXPERIMENT_SELECTION_COOKIE );
		} else {

		}

		// Then, we send the request to the server via GWT RPC
		try {
			this.requestSentDate = new Date();
			pathwayExperimentService.getPathwayData( pathwaySelection, experimentSelection
					, new AsyncCallback<PathwayExperimentData>() {

						@Override
						public void onFailure( Throwable caught ) {
							DataRetrievalPresenter.this.responseReceivedDate
									= new Date();
							DataRetrievalPresenter.this.handleOnError( caught );
						}

						@Override
						public void onSuccess( PathwayExperimentData result ) {
							DataRetrievalPresenter.this.responseReceivedDate
									= new Date();
							DataRetrievalPresenter.this.handleResponse( result );
						}

			});
		} catch ( RequestException re ) {
			DataRetrievalPresenter.this.responseReceivedDate
					= new Date();
			this.handleRequestException( re );
		}
	}
}
