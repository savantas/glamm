package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.AnnotatedMapPresenter;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Panel;

public class AnnotatedMapView extends Composite implements AnnotatedMapPresenter.View {

	private FocusPanel mainPanel = null;
	
	public AnnotatedMapView() {
		mainPanel = new FocusPanel();
		DOM.setStyleAttribute(mainPanel.getElement(), "background", "#000000");
		initWidget(mainPanel);
	}
	
	@Override
	public Panel getMapPanel() {
		return mainPanel;
	}

}
