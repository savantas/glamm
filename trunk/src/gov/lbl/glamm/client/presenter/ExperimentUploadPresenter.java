package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.ExperimentUploadEvent;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.view.ExperimentUploadView;

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
import com.google.gwt.user.client.ui.Hidden;

public class ExperimentUploadPresenter {

	public interface View {
		public HasClickHandlers getCancelButton();
		public HasText			getClampMaxField();
		public HasText			getClampMidField();
		public HasText			getClampMinField();
		public HasText			getControlField();
		public FileUpload		getFileUpload();
		public FormPanel		getForm();
		public HasText			getStressField();
		public Hidden			getTaxonomyIdField();
		public HasText			getTreatmentField();
		public HasText			getUnitsField();
		public HasClickHandlers getSubmitButton();
		public void				hideView();
		public void				showView();
	}

	private static final String ACTION_UPLOAD_EXPERIMENT = "uploadExperiment";

	private View view = null;
	private SimpleEventBus eventBus = null;

	private Organism organism = null;

	public ExperimentUploadPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		bind();
	}

	public void setOrganism(final Organism organism) {
		this.organism = organism;
		view.getTaxonomyIdField().setValue(this.organism.getTaxonomyId());
	}

	private void bind() {

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

		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("glammServlet");
		urlBuilder.setParameter("action", ACTION_UPLOAD_EXPERIMENT);
		
		view.getForm().setAction(urlBuilder.buildString());
		view.getForm().setEncoding(FormPanel.ENCODING_MULTIPART);
		view.getForm().setMethod(FormPanel.METHOD_POST);

		// Add an event handler to the form.
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
					eventBus.fireEvent(new ExperimentUploadEvent(organism, ExperimentUploadEvent.Action.SUCCESS));
				}
			}
		});

	}

	private boolean validateForm() {

		boolean isValid = true;
		String errorMsg = "";

		// ensure that the required text boxes contain valid data
		if(view.getStressField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.STRING_LABEL_STRESS + "\n";
		}

		if(view.getTreatmentField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.STRING_LABEL_TREATMENT + "\n";
		}

		if(view.getControlField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.STRING_LABEL_CONTROL + "\n";
		}

		if(view.getUnitsField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.STRING_LABEL_UNITS + "\n";
		}

		// ensure that the file upload points to a valid file
		String filename = view.getFileUpload().getFilename();
		if(filename.isEmpty()) {
			isValid		=	false;
			errorMsg 	+=	"Missing required field " + ExperimentUploadView.STRING_LABEL_FILE + "\n";
		}

		if(view.getClampMinField().getText().isEmpty()) {
			isValid		=	false;
			errorMsg	+=	"Missing required field " + ExperimentUploadView.STRING_LABEL_MIN + "\n";
		}

		if(view.getClampMidField().getText().isEmpty()) {
			isValid		=	false;
			errorMsg	+=	"Missing required field " + ExperimentUploadView.STRING_LABEL_MID + "\n";
		}
		
		if(view.getClampMaxField().getText().isEmpty()) {
			isValid		=	false;
			errorMsg	+=	"Missing required field " + ExperimentUploadView.STRING_LABEL_MAX + "\n";
		}

		// if we've made it this far, make sure that the text values actually specify floating point numbers
		float clampMin = 0f, clampMid = 0f, clampMax = 0f;
		try {
			clampMin = Float.parseFloat(view.getClampMinField().getText());
			if(Float.isNaN(clampMin) || Float.isInfinite(clampMin))
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe) {
			isValid		=	false;
			errorMsg 	+=	"Clamping value " + ExperimentUploadView.STRING_LABEL_MIN + " is not a valid number\n";
		}

		try {
			clampMid = Float.parseFloat(view.getClampMidField().getText());
			if(Float.isNaN(clampMid) || Float.isInfinite(clampMid))
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe) {
			isValid		=	false;
			errorMsg 	+=	"Clamping value " + ExperimentUploadView.STRING_LABEL_MID + " is not a valid number\n";
		}

		try {
			clampMax = Float.parseFloat(view.getClampMaxField().getText());
			if(Float.isNaN(clampMax) || Float.isInfinite(clampMax))
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe) {
			isValid		=	false;
			errorMsg 	+=	"Clamping value " + ExperimentUploadView.STRING_LABEL_MAX + " is not a valid number\n";
		}

		// the clamp values all exist, and they're all floating point - make sure they're in strictly increasing order
		if(isValid && !((clampMin < clampMid) && (clampMid < clampMax))) {
			isValid		=	false;
			errorMsg	+= 	"Clamp values are not in increasing order (i.e. min < mid < max)\n";
		}


		// show error message
		if(!isValid)
			Window.alert(errorMsg);

		return isValid;
	}
}
