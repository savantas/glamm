package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.util.Interpolator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;

/**
 * Presenter for summarizing the interpolator used to map measurements onto their target type for a given sample.
 * @author jtbates
 *
 */
public class InterpolatorPresenter {
	
	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Gets the the grid for the scale of the interpolator.
		 * @return The grid.
		 */
		public Grid 	getScale();
		
		/**
		 * Gets the units label.
		 * @return The label.
		 */
		public HasText 	getUnitsLabel();
	}
	
	private View 			view 			= null;
	@SuppressWarnings("unused")
	private Interpolator 	interpolator 	= null;
	
	/**
	 * Constructor
	 * @param view The View object for this presenter.
	 */
	public InterpolatorPresenter(final View view) {
		this.view = view;
	}
	
	/**
	 * Sets the interpolator.
	 * @param interpolator The interpolator
	 */
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
