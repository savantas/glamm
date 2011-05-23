package gov.lbl.glamm.client.presenter;

public class LoadingPresenter {
	
	public interface View {
		public void showLoading();
		public void hideLoading();
	}
	
	private View view = null;
	private int loadingCount = 0;
	
	public LoadingPresenter(final View view) {
		this.view = view;
		loadingCount = 0;
	}
	
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
