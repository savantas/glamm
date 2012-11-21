package gov.lbl.glamm.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import gov.lbl.glamm.client.presenter.MetabolicModelPresenter;

public class MetabolicModelView extends Composite implements MetabolicModelPresenter.View {

	private DecoratorPanel decoratorPanel;
	private HorizontalPanel mainPanel;
	private ListBox	listBox;
	
	/**
	 * Constructor
	 */
	public MetabolicModelView() {
		decoratorPanel = new DecoratorPanel();
		mainPanel = new HorizontalPanel();
		listBox = new ListBox();
		
		init();
	}
	
	private void init() {
		decoratorPanel.add(mainPanel);
		mainPanel.add(new Label("Model: "));
		mainPanel.add(listBox);
		
		mainPanel.setSpacing(1);
		mainPanel.setStylePrimaryName("glamm-picker");
		listBox.setWidth("20em");

		initWidget(decoratorPanel);
	}
	
	@Override
	public ListBox getListBox() {
		return listBox;
	}

}