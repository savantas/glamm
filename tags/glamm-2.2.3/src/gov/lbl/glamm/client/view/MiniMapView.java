package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.MiniMapPresenter;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

/**
 * View for displaying the mini map.
 * @author jtbates
 *
 */
public class MiniMapView extends Composite implements MiniMapPresenter.View {
	
	private DecoratorPanel	decoratorPanel;
	private Image			image; // do not add to main panel - this is just for access to getWidth and getHeight methods.
	private FocusPanel		mainPanel;
	
	/**
	 * Constructor
	 */
	public MiniMapView() {
		super();
		
		decoratorPanel = new DecoratorPanel();
		image = new Image();
		mainPanel = new FocusPanel();
		decoratorPanel.add(mainPanel);
		initWidget(decoratorPanel);
	}
	
	@Override
	public HasAllMouseHandlers getAllMouseHandlers() {
		return mainPanel;
	}
	
	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public Panel getMiniMapPanel() {
		return mainPanel;
	}
	
}
