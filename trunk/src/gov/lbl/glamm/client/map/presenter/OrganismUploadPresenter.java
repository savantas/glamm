package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.OrganismUploadEvent;
import gov.lbl.glamm.client.map.view.OrganismUploadView;

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

/**
 * Presenter for the form that enables users to upload organism data.
 * @author jtbates
 *
 */
public class OrganismUploadPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Gets the cancel button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers	getCancelButton();
		
		/**
		 * Gets the file upload widget.
		 * @return The file upload widget.
		 */
		public FileUpload		getFileUpload();
		
		/**
		 * Gets the form panel.
		 * @return The form panel.
		 */
		public FormPanel		getForm();
		
		/**
		 * Gets the organism name field.
		 * @return The organism name field.
		 */
		public HasText			getNameField();
		
		/**
		 * Gets the submit button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers getSubmitButton();
		
		/**
		 * Hides the view.
		 */
		public void				hideView();
		
		/**
		 * Shows the view.
		 */
		public void				showView();
	}
	
	/**
	 * Public enum for organism upload form field names.
	 * @author jtbates
	 *
	 */
	public static enum FormField {
		NAME("name"),
		FILE("file");
		
		private String formField;
		
		private FormField(final String formField) {
			this.formField = formField;
		}
		
		@Override
		public String toString() {
			return formField;
		}
	}
	
	private static final String ACTION_UPLOAD_ORGANISM	= "uploadOrganism";

	private View view = null;
	private SimpleEventBus eventBus = null;

	/**
	 * Constructor
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public OrganismUploadPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;

		bindView();
	}

	private void bindView() {

		view.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.hideView();
			}
		});

		view.getSubmitButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.getForm().submit();
			}
		});
		
		// set up form
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("glamm/glammServlet");
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
