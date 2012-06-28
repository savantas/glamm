package gov.lbl.glamm.client.presenter;

/**
 * Presenter for a popup that should be displayed when an operation is taking a "long" time, where "long" is defined by the implementor.
 * @author jtbates
 *
 */
public class LoadingPresenter {
	
	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Shows the loading popup.
		 */
		public void showLoading();
		
		/**
		 * Hides the loading popup.
		 */
		public void hideLoading();
	}
	
	private View view = null;
	private int loadingCount = 0;
	
	/**
	 * Constructor
	 * @param view The View object for this presenter.
	 */
	public LoadingPresenter(final View view) {
		this.view = view;
		loadingCount = 0;
	}
	
	/**
	 * Updates the loading popup.  There may be several "concurrent" operations that require the display of a loading popup.  
	 * These operations fire events that increment or decrement an internal counter based on whether or not they have completed.
	 * When the counter reaches 0, the loading popup is hidden.
	 * @param doneLoading Flag indicating that the originating operation is complete.
	 */
	public void update(final boolean doneLoading) {
		loadingCount = doneLoading ? --loadingCount : ++loadingCount;
		if(loadingCount <= 0) {
			loadingCount = 0;
			view.hideLoading();
		}
		else
			view.showLoading();
	}
	
	
}
