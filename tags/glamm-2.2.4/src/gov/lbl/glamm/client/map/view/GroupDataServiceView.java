package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.GroupDataServicePresenter;
import gov.lbl.glamm.shared.ExternalServiceParameter;
import gov.lbl.glamm.shared.ExternalServiceParameter.ParameterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A viewer for choosing an external web service from which to collect Overlay group data.
 * @author wjriehl
 *
 */
public class GroupDataServiceView extends DecoratedPopupPanel implements GroupDataServicePresenter.View {

	private static final String SUBMIT =  "Submit";
	private static final String CANCEL =  "Cancel";
	private static final String SERVICE = "Service: ";
	
	private Button submitButton;
	private Button cancelButton;
	private ListBox serviceListBox;
	private Label serviceLabel;
	
	private Grid panelGrid,
				 parameterGrid;
	private HorizontalPanel buttonPanel;
	private VerticalPanel mainPanel;
	
	private Map<ExternalServiceParameter, Widget> parameterMap;
	
	public GroupDataServiceView() {
		parameterMap = new HashMap<ExternalServiceParameter, Widget>();
		
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
	public void setParameters(List<ExternalServiceParameter> parameterList) {
		removeParameters();
		for (int i=0; i<parameterList.size(); i++) {
			ExternalServiceParameter param = parameterList.get(i);
			if (param.isVisible()) {
				String name = param.getHumanReadableName();
				Widget paramInput = null;
				if (param.getParameterType() == ParameterType.STRING) {
					paramInput = new TextBox();
					if (param.getValue() != null)
						((TextBox)paramInput).setText(param.getValue());
				} else if (param.getParameterType() == ParameterType.BOOLEAN) {
					paramInput = new CheckBox();
					if (param.getValue() != null)
						((CheckBox)paramInput).setValue(!param.getValue().equalsIgnoreCase("0"));
				}
				parameterGrid.setWidget(i, 0, new Label(name));
				parameterGrid.setWidget(i, 1, paramInput);
				parameterMap.put(param, paramInput);
			}
		}
	}
	
	@Override
	public void removeParameters() {
		parameterGrid.clear();
		parameterMap.clear();
	}

	@Override
	public Map<ExternalServiceParameter, Widget> getParameters() {
		return parameterMap;
	}

}
