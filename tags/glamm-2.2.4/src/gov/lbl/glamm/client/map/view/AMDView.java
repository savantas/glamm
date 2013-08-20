package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.AMDPresenter;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Annotated Map Descriptor view.
 * @author jtbates
 *
 */
public class AMDView extends Composite implements AMDPresenter.View {

	private DecoratorPanel 	decoratorPanel;
	private HorizontalPanel mainPanel;
	private ListBox	listBox;
	
	/**
	 * Constructor
	 */
	public AMDView() {
		decoratorPanel = new DecoratorPanel();
		mainPanel = new HorizontalPanel();
		listBox = new ListBox();
		
		init();
	}
	
	private void init() {
		decoratorPanel.add(mainPanel);
		mainPanel.add(new Label("Map: "));
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
