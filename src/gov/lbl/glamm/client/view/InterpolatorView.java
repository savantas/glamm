package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.InterpolatorPresenter;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for displaying the properties of Interpolators.
 * @author jtbates
 *
 */
public class InterpolatorView extends Composite
implements InterpolatorPresenter.View {

	private VerticalPanel	mainPanel	= null;
	private Label			unitLabel	= null;
	private Grid 			scale 		= null;
	
	/**
	 * Constructor
	 */
	public InterpolatorView() {
		mainPanel = new VerticalPanel();
		unitLabel = new Label();
		scale = new Grid(1, 7);
		
		mainPanel.add(unitLabel);
		mainPanel.add(scale);
		
		mainPanel.addStyleName("glamm-InterpolationPicker");
		for(int i = 0; i < scale.getColumnCount(); i++)
			scale.getCellFormatter().addStyleName(0, i, "glamm-InterpolationPicker-Cell");
		
		initWidget(mainPanel);
	}
	
	@Override
	public Grid getScale() {
		return scale;
	}

	@Override
	public HasText getUnitsLabel() {
		return unitLabel;
	}

	/**
	 * Hides the view.
	 */
	public void hide() {
		mainPanel.setVisible(false);
	}
	
	/**
	 * Shows the view.
	 */
	public void show() {
		mainPanel.setVisible(true);
	}

}
