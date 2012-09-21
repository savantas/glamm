package gov.lbl.glamm.client.experiment.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import gov.lbl.glamm.client.experiment.presenter.DataRetrievalPresenter;

/**
 * Display for response info from data retrieval
 * and object counts from rendering the data.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class DataRetrievalView
		extends AbsolutePanel implements DataRetrievalPresenter.View {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// widgets
	private DialogBox responseInfoDialogBox = null;
	private DialogBox objCountDialogBox = null;
	private Button responseInfoCloseButton = null;
	private Button objCountCloseButton = null;
	private Label serviceLabel = null;
	private Label postDataLabel = null;
	private HTML serverResponseLabel = null;
	private HTML objectCountLabel = null;

	public DataRetrievalView() {
		super();
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		initResponseDialog();
	}

	protected void initResponseDialog() {
		// Obj count dialog box
		objCountCloseButton = new Button("Close");
		objCountCloseButton .addStyleName("closeButton");
		objCountCloseButton.getElement().setId("objCountCloseButton");
		objCountDialogBox = new DialogBox();
		objCountDialogBox.setAnimationEnabled(true);
		objCountDialogBox.setText("Object count");
		objectCountLabel = new HTML("unknown");
		VerticalPanel objCountDialogVPanel = new VerticalPanel();
		objCountDialogVPanel.addStyleName("dialogVPanel");
		objCountDialogVPanel.add(objectCountLabel);
		objCountDialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		objCountDialogVPanel.add(objCountCloseButton);
		objCountDialogBox.setWidget(objCountDialogVPanel);

		// Add a handler to close the DialogBox
		objCountCloseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				objCountDialogBox.hide();
			}
		});

		// Response info dialog box
		responseInfoCloseButton = new Button("Close");
		responseInfoCloseButton.addStyleName("closeButton");
		responseInfoCloseButton.getElement().setId("responseInfoCloseButton");
		responseInfoDialogBox = new DialogBox();
		responseInfoDialogBox.setAnimationEnabled(true);
		responseInfoDialogBox.setText("Response info ");
		serviceLabel = new Label();
		postDataLabel = new Label();
		postDataLabel.addStyleName("break-word");
		serverResponseLabel = new HTML();
		VerticalPanel responseInfoDialogVPanel = new VerticalPanel();
		responseInfoDialogVPanel.addStyleName("dialogVPanel");
		responseInfoDialogVPanel.add(new HTML("<b>Requesting from the server:</b>"));
		responseInfoDialogVPanel.add(serviceLabel);
		responseInfoDialogVPanel.add(new HTML("<b>With POST data:</b>"));
		responseInfoDialogVPanel.add(postDataLabel);
		responseInfoDialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		responseInfoDialogVPanel.add(serverResponseLabel);
		responseInfoDialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		responseInfoDialogVPanel.add(responseInfoCloseButton);
		responseInfoDialogBox.setWidget(responseInfoDialogVPanel);

		// Add a handler to close the DialogBox
		responseInfoCloseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				responseInfoDialogBox.hide();
			}
		});

	}

	@Override
	public void setResponseText(String serviceInfo, String message) {
		responseInfoDialogBox.setText("Remote post to " + serviceInfo);
		serverResponseLabel.removeStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(message);
	}

	@Override
	public void displayRetrievalSuccess( String serviceInfo, String message ) {
		responseInfoDialogBox.setText("Remote post to " + serviceInfo);
		serverResponseLabel.removeStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(message);
		responseInfoDialogBox.center();
		responseInfoCloseButton.setFocus(true);
	}

	@Override
	public void displayRetrievalError( String serviceInfo, String displayText ) {
		responseInfoDialogBox.setText("Post to " + serviceInfo + " - Failure");
		serverResponseLabel.addStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(SERVER_ERROR + displayText);
		responseInfoDialogBox.center();
		responseInfoCloseButton.setFocus(true);
	}

	@Override
	public void waitForResponse(String serviceInfo, String postData) {
		serviceLabel.setText(serviceInfo);
		postDataLabel.setText(postData);
		serverResponseLabel.setText("");
	}

	@Override
	public void displayObjectCount( String message ) {
		objCountDialogBox.setText("Object count");
		objectCountLabel.removeStyleName("serverResponseLabelError");
		objectCountLabel.setHTML(message);
		objCountDialogBox.center();
		objCountCloseButton.setFocus(true);
	}

	@Override
	public void displayExcessiveObjectCountWarning() {
		objCountDialogBox.setText("Data set size warning");
		objectCountLabel.setHTML("The application will perform slowly because of the amount of data being displayed. For best results, reduce the number of experiments or pathways.");
		objectCountLabel.addStyleName("serverResponseLabelError");
		objCountDialogBox.center();
		objCountCloseButton.setFocus(true);
	}

	@Override
	public void displayDebugData( long requestResponseMillisecs,
			long processDaoMillisecs
			, long drawSVGMillisecs
	) {
		responseInfoDialogBox.setText("Remote post to service");
		serverResponseLabel.removeStyleName("serverResponseLabelError");
		float[] responseTime = getMinSecParts( requestResponseMillisecs );
		float[] processTime = getMinSecParts( processDaoMillisecs );
		float[] svgTime = getMinSecParts( drawSVGMillisecs );
		long totalMillisecs = requestResponseMillisecs + processDaoMillisecs + drawSVGMillisecs;
		float[] totalTime = getMinSecParts( totalMillisecs );
		String message = "Request-response time: "
				+ responseTime[0] + ":" + responseTime[1]
				+ ";<br/> DAO processing time: "
				+ processTime[0] + ":" + processTime[1]
				+ ";<br/> SVG and other drawing time: "
				+ svgTime[0] + ":" + svgTime[1]
				+ ";<br/> Total time: "
				+ totalTime[0] + ":" + totalTime[1]
		;
		serverResponseLabel.setHTML(message);
		responseInfoDialogBox.center();
		responseInfoCloseButton.setFocus(true);
	}

	protected float[] getMinSecParts( long millisecs ) {
		float[] retVal = new float[2];
		int minPart = (int)(millisecs/60000);
		float minRemainder = millisecs - 60000*minPart;
		float secPart = ( minRemainder/1000f );

		retVal[0] = minPart;
		retVal[1] = secPart;
		return retVal;
	}
}
