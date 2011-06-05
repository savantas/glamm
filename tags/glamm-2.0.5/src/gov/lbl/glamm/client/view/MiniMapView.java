package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.MiniMapPresenter;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Panel;

public class MiniMapView extends Composite implements MiniMapPresenter.View {
	
	private DecoratorPanel	decoratorPanel	= null;
	private FocusPanel		mainPanel		= null;
	
	public MiniMapView() {
		super();
		decoratorPanel = new DecoratorPanel();
		mainPanel = new FocusPanel();
		decoratorPanel.add(mainPanel);
		initWidget(decoratorPanel);
	}
	
	@Override
	public HasAllMouseHandlers getAllMouseHandlers() {
		return mainPanel;
	}

	@Override
	public Panel getMiniMapPanel() {
		return mainPanel;
	}

}
