package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.GroupDataUploadPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A viewer for a user to select and upload a file containing user group data.
 * @author wjriehl
 *
 */
public class GroupDataUploadView extends DecoratedPopupPanel implements GroupDataUploadPresenter.View {
	
	private static final String NAME = "Name: ";
	private static final String FILE = "File: ";
	private static final String SUBMIT = "Submit";
	private static final String CANCEL = "Cancel";
	
	private FormPanel form;
	
	private Grid formGrid;
	
	private Label nameLabel;
	private Label fileLabel;
	
	private ListBox serviceListBox;
	private TextBox nameBox;
	
	private Button submitButton;
	private Button cancelButton;
	
	private FileUpload fileUpload;
	
	public GroupDataUploadView() {
		form = new FormPanel();
		
		formGrid = new Grid(3,2);
		
		serviceListBox = new ListBox();
		
		nameLabel = new Label(NAME);
		nameBox = new TextBox();
		
		fileLabel = new Label(FILE);
		fileUpload = new FileUpload();
		
		submitButton = new Button(SUBMIT);
		cancelButton = new Button(CANCEL);

		init();
	}
	
	public void init() {
		
		formGrid.setWidget(0, 0, nameLabel);
		formGrid.setWidget(0, 1, nameBox);
		
		formGrid.setWidget(1, 0, fileLabel);
		formGrid.setWidget(1, 1, fileUpload);
		
		formGrid.setWidget(2, 0, submitButton);
		formGrid.setWidget(2, 1, cancelButton);
		
		form.setStylePrimaryName("glamm-picker");
		form.add(formGrid);
		
		this.add(form);
	}

	@Override
	public FileUpload getUpload() {
		return fileUpload;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getSubmitButton() {
		return submitButton;
	}

	@Override
	public ListBox getServiceList() {
		return serviceListBox;
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
