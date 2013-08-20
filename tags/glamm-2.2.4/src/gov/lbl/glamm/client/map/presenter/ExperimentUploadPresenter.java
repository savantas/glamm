package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.ExperimentUploadEvent;
import gov.lbl.glamm.client.map.view.ExperimentUploadView;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HasText;

/**
 * Presenter for the form that enables users to upload experiment data.
 * @author jtbates
 *
 */
public class ExperimentUploadPresenter {

	/**
	 * View interface
	 * @author jtbates
	 *
	 */
	public interface View {
		
		/**
		 * Adds a target type choice to the experiment upload form.
		 * @param targetType The target type.
		 * @return The HasClickHandlers interface for selecting a target type.
		 */
		public HasClickHandlers addTargetTypeChoice(final Sample.TargetType targetType);
		
		/**
		 * Gets the cancel button.
		 * @return The interface for that button.
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the maximum clamping value form field.
		 * @return The field.
		 */
		public HasText			getClampMaxField();
		
		/**
		 * Gets the mid-range clamping value form field.
		 * @return The field.
		 */
		public HasText			getClampMidField();
		
		/**
		 * Gets the minimum clamping value form field.
		 * @return The field.
		 */
		public HasText			getClampMinField();
		
		/**
		 * Gets the control description form field.
		 * @return The field.
		 */
		public HasText			getControlField();
		
		/**
		 * Gets the file upload widget.
		 * @return The file upload widget.
		 */
		public FileUpload		getFileUpload();
		
		/**
		 * Gets the form widget.
		 * @return The form.
		 */
		public FormPanel		getForm();
		
		/**
		 * Gets the stress description form field.
		 * @return The field.
		 */
		public HasText			getStressField();
		
		/**
		 * Gets the hidden target type form field.
		 * @return The field.
		 */
		public TakesValue<String>			getTargetTypeField();
		
		/**
		 * Gets the hidden taxonomy id form field.
		 * @return The field.
		 */
		public TakesValue<String>			getTaxonomyIdField();
		
		/**
		 * Gets the treatment description form field.
		 * @return The field.
		 */
		public HasText			getTreatmentField();
		
		/**
		 * Gets the units field.
		 * @return The field.
		 */
		public HasText			getUnitsField();
		
		/**
		 * Gets the submit form button.
		 * @return The button.
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
	 * Public enum for experiment upload form field names.
	 * @author jtbates
	 *
	 */
	public static enum FormField {
		
		CLAMP_MIN("clampMin"),
		CLAMP_MID("clampMid"),
		CLAMP_MAX("clampMax"),
		STRESS("stress"),
		TREATMENT("treatment"),
		CONTROL("control"),
		FILE("file"),
		TARGET_TYPE("targetType"),
		TAXONOMY_ID("taxonomyId"),
		UNITS("units");
		
		private String formField;
		
		private FormField(final String formField) {
			this.formField = formField;
		}
		
		@Override
		public String toString() {
			return formField;
		}
	}
	
	private static final String ACTION_UPLOAD_EXPERIMENT = "uploadExperiment";

	private View view;
	private SimpleEventBus eventBus;

	private Organism organism;
	private Sample.TargetType targetType;

	/**
	 * Constructor
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public ExperimentUploadPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		bindView();
	}
	
	private void addDataTypeChoices() {
		for(final Sample.TargetType targetType : Sample.TargetType.values()) {
			view.addTargetTypeChoice(targetType).addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					setTargetType(targetType);
				}
			});
		}
	}



	private void bindView() {
		
		addDataTypeChoices();

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
		if (GWT.isProdMode()) 
			urlBuilder.setPath(Window.Location.getPath() + "glamm/glammServlet");
		else
			urlBuilder.setPath("glamm/glammServlet");
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

	/**
	 * Sets the selected organism.
	 * @param organism The organism.
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;
		view.getTaxonomyIdField().setValue(this.organism.getTaxonomyId());
	}
	
	private void setTargetType(final Sample.TargetType targetType) {
		this.targetType = targetType;
		view.getTargetTypeField().setValue(this.targetType.toString());
	}
	
	private boolean validateForm() {

		boolean isValid = true;
		String errorMsg = "";

		// ensure that the required text boxes contain valid data
		if(view.getStressField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.LABEL_STRESS + "\n";
		}

		if(view.getTreatmentField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.LABEL_TREATMENT + "\n";
		}

		if(view.getControlField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.LABEL_CONTROL + "\n";
		}

		if(view.getUnitsField().getText().isEmpty()) {
			isValid		= 	false;
			errorMsg 	+= 	"Missing required field " + ExperimentUploadView.LABEL_UNITS + "\n";
		}

		// ensure that the file upload points to a valid file
		String filename = view.getFileUpload().getFilename();
		if(filename.isEmpty()) {
			isValid		=	false;
			errorMsg 	+=	"Missing required field " + ExperimentUploadView.LABEL_FILE + "\n";
		}

		if(view.getClampMinField().getText().isEmpty()) {
			isValid		=	false;
			errorMsg	+=	"Missing required field " + ExperimentUploadView.LABEL_MIN + "\n";
		}

		if(view.getClampMidField().getText().isEmpty()) {
			isValid		=	false;
			errorMsg	+=	"Missing required field " + ExperimentUploadView.LABEL_MID + "\n";
		}
		
		if(view.getClampMaxField().getText().isEmpty()) {
			isValid		=	false;
			errorMsg	+=	"Missing required field " + ExperimentUploadView.LABEL_MAX + "\n";
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
			errorMsg 	+=	"Clamping value " + ExperimentUploadView.LABEL_MIN + " is not a valid number\n";
		}

		try {
			clampMid = Float.parseFloat(view.getClampMidField().getText());
			if(Float.isNaN(clampMid) || Float.isInfinite(clampMid))
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe) {
			isValid		=	false;
			errorMsg 	+=	"Clamping value " + ExperimentUploadView.LABEL_MID + " is not a valid number\n";
		}

		try {
			clampMax = Float.parseFloat(view.getClampMaxField().getText());
			if(Float.isNaN(clampMax) || Float.isInfinite(clampMax))
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe) {
			isValid		=	false;
			errorMsg 	+=	"Clamping value " + ExperimentUploadView.LABEL_MAX + " is not a valid number\n";
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
