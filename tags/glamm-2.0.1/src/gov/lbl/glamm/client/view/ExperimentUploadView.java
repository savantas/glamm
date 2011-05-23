package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.ExperimentUploadPresenter;
import gov.lbl.glamm.shared.GlammConstants;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExperimentUploadView extends DecoratedPopupPanel 
implements ExperimentUploadPresenter.View {

	public static final String STRING_BUTTON_CANCEL			= "Cancel";
	public static final String STRING_BUTTON_SUBMIT 		= "Submit";
	public static final String STRING_LABEL_STRESS			= "Stress";
	public static final String STRING_LABEL_TREATMENT 		= "Treatment";
	public static final String STRING_LABEL_CONTROL 		= "Control";
	public static final String STRING_LABEL_UNITS 			= "Units";
	public static final String STRING_LABEL_FILE 			= "File";
	public static final String STRING_LABEL_CLAMP_VALUES	= "Clamp displayed values to: ";
	public static final String STRING_LABEL_MIN 			= "min";
	public static final String STRING_LABEL_MID 			= "mid";
	public static final String STRING_LABEL_MAX 			= "max";

	//********************************************************************************

	private FormPanel		form				= null;
	private VerticalPanel	wrapperPanel		= null;
	
	// hidden fields
	private Hidden			taxonomyIdHidden	= null;

	// the experiment grid
	private Grid			experimentGrid		= null;
	private TextBox 		stressTextBox		= null;
	private TextBox 		treatmentTextBox 	= null;
	private TextBox 		controlTextBox 		= null;
	private TextBox			unitsTextBox		= null;
	private FileUpload		fileUpload			= null;

	// the clamp values disclosure panel
	private Label			clampValuesLabel	= null;
	private Grid			clampValuesGrid		= null;
	private TextBox			clampMin			= null;
	private TextBox			clampMid			= null;
	private TextBox			clampMax			= null;

	private HorizontalPanel	buttonPanel			= null;
	private Button			submitButton		= null;
	private Button			cancelButton		= null;
	
	public ExperimentUploadView() {
		
		form				= new FormPanel();
		wrapperPanel		= new VerticalPanel();
		taxonomyIdHidden	= new Hidden();
		experimentGrid		= new Grid(5,2);
		stressTextBox		= new TextBox();
		treatmentTextBox 	= new TextBox();
		controlTextBox 		= new TextBox();
		unitsTextBox		= new TextBox();
		fileUpload			= new FileUpload();
		clampValuesLabel	= new Label(STRING_LABEL_CLAMP_VALUES);
		clampValuesGrid		= new Grid(2,3);
		clampMin			= new TextBox();
		clampMid			= new TextBox();
		clampMax			= new TextBox();
		buttonPanel			= new HorizontalPanel();
		submitButton		= new Button(STRING_BUTTON_SUBMIT);
		cancelButton		= new Button(STRING_BUTTON_CANCEL);
		
		taxonomyIdHidden.setName(GlammConstants.FIELD_EXP_UPLOAD_TAXONOMY_ID);  
		
		// set up the text boxes
		stressTextBox.setWidth("90%");
		stressTextBox.setName(GlammConstants.FIELD_EXP_UPLOAD_STRESS);

		treatmentTextBox.setWidth("90%");
		treatmentTextBox.setName(GlammConstants.FIELD_EXP_UPLOAD_TREATMENT);

		controlTextBox.setWidth("90%");
		controlTextBox.setName(GlammConstants.FIELD_EXP_UPLOAD_CONTROL);

		unitsTextBox.setWidth("90%");
		unitsTextBox.setName(GlammConstants.FIELD_EXP_UPLOAD_UNITS);

		// set up the file upload
		fileUpload.setName(GlammConstants.FIELD_EXP_UPLOAD_FILE);

		// set up grid
		experimentGrid.setWidget(0, 0, new Label(STRING_LABEL_STRESS + ":"));
		experimentGrid.setWidget(0, 1, stressTextBox);
		experimentGrid.setWidget(1, 0, new Label(STRING_LABEL_TREATMENT + ":"));
		experimentGrid.setWidget(1, 1, treatmentTextBox);
		experimentGrid.setWidget(2, 0, new Label(STRING_LABEL_CONTROL + ":"));
		experimentGrid.setWidget(2, 1, controlTextBox);
		experimentGrid.setWidget(3, 0, new Label(STRING_LABEL_UNITS + ":"));
		experimentGrid.setWidget(3, 1, unitsTextBox);
		experimentGrid.setWidget(4, 0, new Label(STRING_LABEL_FILE + ":"));
		experimentGrid.setWidget(4, 1, fileUpload);

		experimentGrid.getColumnFormatter().setWidth(0, "10em");
		experimentGrid.getColumnFormatter().setWidth(1, "60em");

		// set up clamp values grid
		clampValuesGrid.setWidget(0, 0, new Label(STRING_LABEL_MIN));
		clampValuesGrid.setWidget(0, 1, new Label(STRING_LABEL_MID));
		clampValuesGrid.setWidget(0, 2, new Label(STRING_LABEL_MAX));
		clampValuesGrid.setWidget(1, 0, clampMin);
		clampValuesGrid.setWidget(1, 1, clampMid);
		clampValuesGrid.setWidget(1, 2, clampMax);

		clampMin.setWidth("5em");
		clampMin.setName(GlammConstants.FIELD_EXP_UPLOAD_CLAMP_MIN);
		clampMid.setWidth("5em");
		clampMid.setName(GlammConstants.FIELD_EXP_UPLOAD_CLAMP_MID);
		clampMax.setWidth("5em");
		clampMax.setName(GlammConstants.FIELD_EXP_UPLOAD_CLAMP_MAX);

		// set up button panel
		buttonPanel.setSpacing(5);
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		// set up wrapper
		wrapperPanel.add(taxonomyIdHidden);
		wrapperPanel.add(experimentGrid);
		wrapperPanel.add(clampValuesLabel);
		wrapperPanel.add(clampValuesGrid);
		wrapperPanel.add(buttonPanel);

		// set up form
		form.setStylePrimaryName("glamm-picker");
		form.add(wrapperPanel);
		this.add(form);
		DOM.setStyleAttribute(this.getElement(), "zIndex", "200");
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	@Override
	public HasText getClampMaxField() {
		return clampMax;
	}

	@Override
	public HasText getClampMidField() {
		return clampMid;
	}

	@Override
	public HasText getClampMinField() {
		return clampMin;
	}

	@Override
	public HasText getControlField() {
		return controlTextBox;
	}
	
	@Override
	public FileUpload getFileUpload() {
		return fileUpload;
	}

	@Override
	public FormPanel getForm() {
		return form;
	}

	@Override
	public HasText getStressField() {
		return stressTextBox;
	}
	
	@Override
	public Hidden getTaxonomyIdField() {
		return taxonomyIdHidden;
	}

	@Override
	public HasText getTreatmentField() {
		return treatmentTextBox;
	}

	@Override
	public HasText getUnitsField() {
		return unitsTextBox;
	}

	@Override
	public HasClickHandlers getSubmitButton() {
		return submitButton;
	}

	@Override
	public void hideView() {
		this.hide();		
	}

	@Override
	public void showView() {
		this.center();
		this.show();
	}
}
