package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.PanZoomControlPresenter;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * View for displaying the pan/zoom control panel.
 * @author jtbates
 *
 */
public class PanZoomControlView extends Composite implements PanZoomControlPresenter.View {

	private DecoratorPanel	decoratorPanel	= null;
	private FocusPanel		focusPanel		= null;
	
	/**
	 * Constructor
	 */
	public PanZoomControlView() {
		decoratorPanel = new DecoratorPanel();
		focusPanel = new FocusPanel();
		
		decoratorPanel.add(focusPanel);
		
		initWidget(decoratorPanel);
	}
	
	@Override
	public HasAllMouseHandlers getAllMouseHandlers() {
		return focusPanel;
	}

	@Override
	public Panel getPanZoomControlPanel() {
		return focusPanel;
	}

}
