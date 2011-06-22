package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.util.Interpolator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;

public class InterpolatorPresenter {
	
	public interface View {
		public Grid 	getScale();
		public HasText 	getUnitsLabel();
	}
	
	private View 			view 			= null;
	@SuppressWarnings("unused")
	private Interpolator 	interpolator 	= null;
	
	public InterpolatorPresenter(final View view) {
		this.view = view;
	}
	
	public void setInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
		
		final String[] cssColorScale = interpolator.getCssColorScale();

		for(int i = 0; i < cssColorScale.length; i++) {
			DOM.setStyleAttribute(view.getScale().getCellFormatter().getElement(0, i), "background", cssColorScale[i]);
		}

		view.getScale().setText(0, 0, Float.toString(interpolator.getClampMin()));
		view.getScale().setText(0, 3, Float.toString(interpolator.getClampMid()));
		view.getScale().setText(0, 6, Float.toString(interpolator.getClampMax()));
		
		view.getUnitsLabel().setText(interpolator.getUnits());
	}
	
}
