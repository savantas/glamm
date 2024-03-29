package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.OrganismUploadPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for displaying the organism upload form.
 * @author jtbates
 *
 */
public class OrganismUploadView extends DecoratedPopupPanel 
implements OrganismUploadPresenter.View {
	
	public static final String STRING_BUTTON_CANCEL	= "Cancel";
	public static final String STRING_BUTTON_SUBMIT = "Submit";
	public static final String STRING_LABEL_FILE 	= "File";
	public static final String STRING_LABEL_NAME	= "Name";
	

	private FormPanel		form			= null;
	private VerticalPanel	wrapperPanel	= null;

	private Grid			grid			= null;
	private TextBox 		nameTextBox		= null;
	private FileUpload		fileUpload		= null;
	
	private HorizontalPanel	buttonPanel		= null;
	private Button			submitButton	= null;
	private Button			cancelButton	= null;
	
	/**
	 * Constructor
	 */
	public OrganismUploadView() {
		
		form			= new FormPanel();
		wrapperPanel	= new VerticalPanel();

		grid			= new Grid(2,2);
		nameTextBox		= new TextBox();
		fileUpload		= new FileUpload();
		
		buttonPanel		= new HorizontalPanel();
		submitButton	= new Button(STRING_BUTTON_SUBMIT);
		cancelButton	= new Button(STRING_BUTTON_CANCEL);
		
		this.add(form);
		
		// set up form elements
		nameTextBox.setWidth("90%");
		nameTextBox.setName(OrganismUploadPresenter.FormField.NAME.toString());

		// set up the file upload
		fileUpload.setName(OrganismUploadPresenter.FormField.FILE.toString());
		
		// set up grid
		grid.setWidget(0, 0, new Label(STRING_LABEL_NAME + ":"));
		grid.setWidget(0, 1, nameTextBox);
		grid.setWidget(1, 0, new Label(STRING_LABEL_FILE + ":"));
		grid.setWidget(1, 1, fileUpload);

		grid.getColumnFormatter().setWidth(0, "10em");
		grid.getColumnFormatter().setWidth(1, "60em");
		
		// set up button panel
		buttonPanel.setSpacing(5);
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		wrapperPanel.add(grid);
		wrapperPanel.add(buttonPanel);
		
		// set up form
		form.setStylePrimaryName("glamm-picker");
		form.add(wrapperPanel);
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
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
	public HasText getNameField() {
		return nameTextBox;
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
