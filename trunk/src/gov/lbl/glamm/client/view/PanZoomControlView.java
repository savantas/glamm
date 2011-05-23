package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.PanZoomControlPresenter;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Panel;

public class PanZoomControlView extends Composite implements PanZoomControlPresenter.View {

	private DecoratorPanel	decoratorPanel	= null;
	private FocusPanel		focusPanel		= null;
	
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
