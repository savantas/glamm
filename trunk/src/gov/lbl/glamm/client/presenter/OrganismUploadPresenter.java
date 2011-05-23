package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.OrganismUploadEvent;
import gov.lbl.glamm.client.view.OrganismUploadView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HasText;

public class OrganismUploadPresenter {

	public interface View {
		public HasClickHandlers	getCancelButton();
		public FileUpload		getFileUpload();
		public FormPanel		getForm();
		public HasText			getNameField();
		public HasClickHandlers getSubmitField();
		public void				hideView();
		public void				showView();
	}
	
	private static final String ACTION_UPLOAD_ORGANISM	= "uploadOrganism";

	private View view = null;
	private SimpleEventBus eventBus = null;

	public OrganismUploadPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;

		bind();
	}

	private void bind() {

		view.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.hideView();
			}
		});

		view.getSubmitField().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.getForm().submit();
			}
		});
		
		// set up form
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("glammServlet");
		urlBuilder.setParameter("action", ACTION_UPLOAD_ORGANISM);
		view.getForm().setAction(urlBuilder.buildString());
		view.getForm().setEncoding(FormPanel.ENCODING_MULTIPART);
		view.getForm().setMethod(FormPanel.METHOD_POST);

		view.getForm().addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if(!validateForm())
					event.cancel();
			}
		});

		view.getForm().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String results = event.getResults();
				if(results != null && !results.isEmpty())
					Window.alert(results);
				else {
					view.hideView();
					eventBus.fireEvent(new OrganismUploadEvent(OrganismUploadEvent.Action.SUCCESS));
				}
			}
		});

	}

	private boolean validateForm() {

		boolean isValid = true;
		String errorMsg = "";

		if(view.getNameField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + OrganismUploadView.STRING_LABEL_NAME + "\n";
		}

		// ensure that the file upload points to a valid file
		String filename = view.getFileUpload().getFilename();
		if(filename.isEmpty()) {
			isValid		=	false;
			errorMsg 	+=	"Missing required field " + OrganismUploadView.STRING_LABEL_FILE + "\n";
		}

		// show error message
		if(!isValid)
			Window.alert(errorMsg);

		return isValid;
	}
}
