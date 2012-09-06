package gov.lbl.glamm.client.map.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import gov.lbl.glamm.client.map.presenter.GroupDataServicePresenter;

/**
 * A viewer for choosing an external web service from which to collect Overlay group data.
 * @author wjriehl
 *
 */
public class GroupDataServiceView extends DecoratedPopupPanel implements GroupDataServicePresenter.View {

	private static final String SUBMIT = "Submit";
	private static final String CANCEL = "Cancel";
	private static final String SERVICE = "Service: ";
	
	private Button submitButton;
	private Button cancelButton;
	private ListBox serviceListBox;
	private Label serviceLabel;
	
	private Grid panelGrid,
				 parameterGrid;
	private HorizontalPanel buttonPanel;
	private VerticalPanel mainPanel;
	
	private Map<String, TextBox> parameterMap;
	
	public GroupDataServiceView() {
		parameterMap = new HashMap<String, TextBox>();
		
		submitButton = new Button(SUBMIT);
		cancelButton = new Button(CANCEL);
		serviceLabel = new Label(SERVICE);
		
		serviceListBox = new ListBox();
		
		buttonPanel = new HorizontalPanel();
		mainPanel = new VerticalPanel();

		panelGrid = new Grid(1,2);
		parameterGrid = new Grid(3,2);
		
		init();
	}
	
	private void init() {
		panelGrid.setWidget(0, 0, serviceLabel);
		panelGrid.setWidget(0, 1, serviceListBox);

		
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.add(panelGrid);
		mainPanel.add(parameterGrid);
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

	@Override
	public void setParameters(List<String> paramNames) {
		removeParameters();
		for (int i=0; i<paramNames.size(); i++) {
			String name = paramNames.get(i);
			TextBox paramBox = new TextBox();
			parameterMap.put(name, paramBox);
			parameterGrid.setWidget(i, 0, new Label(name));
			parameterGrid.setWidget(i, 1, paramBox);
		}
	}

	@Override
	public void removeParameters() {
		parameterGrid.clear();
		parameterMap.clear();
	}

	@Override
	public Map<String, TextBox> getParameters() {
		return parameterMap;
	}

}
