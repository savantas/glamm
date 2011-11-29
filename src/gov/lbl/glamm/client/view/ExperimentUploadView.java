package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.Sample.TargetType;
import gov.lbl.glamm.client.presenter.ExperimentUploadPresenter;

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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExperimentUploadView extends DecoratedPopupPanel 
implements ExperimentUploadPresenter.View {

	private static final String RB_GROUP			= "RB_GROUP";
	
	public static final String BUTTON_CANCEL		= "Cancel";
	public static final String BUTTON_SUBMIT 		= "Submit";
	public static final String LABEL_STRESS			= "Stress";
	public static final String LABEL_TREATMENT 		= "Treatment";
	public static final String LABEL_CONTROL 		= "Control";
	public static final String LABEL_UNITS 			= "Units";
	public static final String LABEL_FILE 			= "File";
	public static final String LABEL_TARGET_TYPE	= "Target ID refers to: ";
	public static final String LABEL_CLAMP_VALUES	= "Clamp displayed values to: ";
	public static final String LABEL_MIN 			= "min";
	public static final String LABEL_MID 			= "mid";
	public static final String LABEL_MAX 			= "max";

	//********************************************************************************

	private FormPanel		form;
	private VerticalPanel	wrapperPanel;
	
	// hidden fields
	private Hidden			taxonomyIdHidden;
	private Hidden			targetTypeHidden;

	// the experiment grid
	private Grid			experimentGrid;
	private TextBox 		stressTextBox;
	private TextBox 		treatmentTextBox;
	private TextBox 		controlTextBox;
	private TextBox			unitsTextBox;
	private FileUpload		fileUpload;
	
	// the target type panel
	private VerticalPanel	targetTypePanel;

	// the clamp values disclosure panel
	private Label			clampValuesLabel;
	private Grid			clampValuesGrid;
	private TextBox			clampMin;
	private TextBox			clampMid;
	private TextBox			clampMax;

	private HorizontalPanel	buttonPanel;
	private Button			submitButton;
	private Button			cancelButton;
	
	public ExperimentUploadView() {
		
		form				= new FormPanel();
		wrapperPanel		= new VerticalPanel();
		taxonomyIdHidden	= new Hidden();
		targetTypeHidden	= new Hidden();
		experimentGrid		= new Grid(5,2);
		stressTextBox		= new TextBox();
		treatmentTextBox 	= new TextBox();
		controlTextBox 		= new TextBox();
		unitsTextBox		= new TextBox();
		fileUpload			= new FileUpload();
		targetTypePanel		= new VerticalPanel();
		clampValuesLabel	= new Label(LABEL_CLAMP_VALUES);
		clampValuesGrid		= new Grid(2,3);
		clampMin			= new TextBox();
		clampMid			= new TextBox();
		clampMax			= new TextBox();
		buttonPanel			= new HorizontalPanel();
		submitButton		= new Button(BUTTON_SUBMIT);
		cancelButton		= new Button(BUTTON_CANCEL);
		
		taxonomyIdHidden.setName(ExperimentUploadPresenter.FormField.TAXONOMY_ID.toString());  
		targetTypeHidden.setName(ExperimentUploadPresenter.FormField.TARGET_TYPE.toString());
		
		// set up the text boxes
		stressTextBox.setWidth("90%");
		stressTextBox.setName(ExperimentUploadPresenter.FormField.STRESS.toString());

		treatmentTextBox.setWidth("90%");
		treatmentTextBox.setName(ExperimentUploadPresenter.FormField.TREATMENT.toString());

		controlTextBox.setWidth("90%");
		controlTextBox.setName(ExperimentUploadPresenter.FormField.CONTROL.toString());

		unitsTextBox.setWidth("90%");
		unitsTextBox.setName(ExperimentUploadPresenter.FormField.UNITS.toString());

		// set up the file upload
		fileUpload.setName(ExperimentUploadPresenter.FormField.FILE.toString());

		// set up grid
		experimentGrid.setWidget(0, 0, new Label(LABEL_STRESS + ":"));
		experimentGrid.setWidget(0, 1, stressTextBox);
		experimentGrid.setWidget(1, 0, new Label(LABEL_TREATMENT + ":"));
		experimentGrid.setWidget(1, 1, treatmentTextBox);
		experimentGrid.setWidget(2, 0, new Label(LABEL_CONTROL + ":"));
		experimentGrid.setWidget(2, 1, controlTextBox);
		experimentGrid.setWidget(3, 0, new Label(LABEL_UNITS + ":"));
		experimentGrid.setWidget(3, 1, unitsTextBox);
		experimentGrid.setWidget(4, 0, new Label(LABEL_FILE + ":"));
		experimentGrid.setWidget(4, 1, fileUpload);

		experimentGrid.getColumnFormatter().setWidth(0, "10em");
		experimentGrid.getColumnFormatter().setWidth(1, "60em");
		
		// set up target type radio button panel
		targetTypePanel.add(new Label(LABEL_TARGET_TYPE));

		// set up clamp values grid
		clampValuesGrid.setWidget(0, 0, new Label(LABEL_MIN));
		clampValuesGrid.setWidget(0, 1, new Label(LABEL_MID));
		clampValuesGrid.setWidget(0, 2, new Label(LABEL_MAX));
		clampValuesGrid.setWidget(1, 0, clampMin);
		clampValuesGrid.setWidget(1, 1, clampMid);
		clampValuesGrid.setWidget(1, 2, clampMax);

		clampMin.setWidth("5em");
		clampMin.setName(ExperimentUploadPresenter.FormField.CLAMP_MIN.toString());
		clampMid.setWidth("5em");
		clampMid.setName(ExperimentUploadPresenter.FormField.CLAMP_MID.toString());
		clampMax.setWidth("5em");
		clampMax.setName(ExperimentUploadPresenter.FormField.CLAMP_MAX.toString());

		// set up button panel
		buttonPanel.setSpacing(5);
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		// set up wrapper
		wrapperPanel.add(taxonomyIdHidden);
		wrapperPanel.add(targetTypeHidden);
		wrapperPanel.add(experimentGrid);
		wrapperPanel.add(targetTypePanel);
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
	public HasClickHandlers addTargetTypeChoice(final Sample.TargetType targetType) {
		RadioButton button = new RadioButton(RB_GROUP, targetType.getCaption());
		button.setValue(targetType.isDefault());
		if(targetType.isDefault()) 
			targetTypeHidden.setValue(targetType.toString());
		targetTypePanel.add(button);
		return button;
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
	public Hidden getTargetTypeField() {
		return targetTypeHidden;
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
