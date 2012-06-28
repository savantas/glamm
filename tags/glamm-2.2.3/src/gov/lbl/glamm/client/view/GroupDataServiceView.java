package gov.lbl.glamm.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import gov.lbl.glamm.client.presenter.GroupDataServicePresenter;

/**
 * A viewer for choosing an external web service from which to collect Overlay group data.
 * @author wjriehl
 *
 */
public class GroupDataServiceView extends DecoratedPopupPanel implements GroupDataServicePresenter.View {

	private static final String SUBMIT = "Submit";
	private static final String CANCEL = "Cancel";
	private static final String SERVICE = "Service: ";
	private static final String INPUT = "Input: ";
	
	private Button submitButton;
	private Button cancelButton;
	private TextBox inputTextBox;
	private ListBox serviceListBox;
	private Label serviceLabel;
	private Label inputLabel;
	
	private Grid grid;
	private HorizontalPanel buttonPanel;
	private VerticalPanel mainPanel;
	
	public GroupDataServiceView() {
		submitButton = new Button(SUBMIT);
		cancelButton = new Button(CANCEL);
		serviceLabel = new Label(SERVICE);
		inputLabel = new Label(INPUT);
		
		inputTextBox = new TextBox();
		serviceListBox = new ListBox();
		
		buttonPanel = new HorizontalPanel();
		mainPanel = new VerticalPanel();
		
		grid = new Grid(2,2);
		
		init();
	}
	
	private void init() {
		grid.setWidget(0, 0, serviceLabel);
		grid.setWidget(0, 1, serviceListBox);
		
		grid.setWidget(1, 0, inputLabel);
		grid.setWidget(1, 1, inputTextBox);
		
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(grid);
		mainPanel.add(buttonPanel);
		
		mainPanel.setStylePrimaryName("glamm-picker");
		this.add(mainPanel);
	}
	
	@Override
	public HasClickHandlers getSubmitButton() {
		return submitButton;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	@Override
	public TextBox getInputTextBox() {
		return inputTextBox;
	}

	@Override
	public ListBox getServiceListBox() {
		return serviceListBox;
	}

	@Override
	public void showView() {
		this.center();
		this.show();
	}

	@Override
	public void hideView() {
		this.hide();
	}

}
