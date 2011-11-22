package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.LoadingPresenter;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class LoadingView extends PopupPanel 
implements LoadingPresenter.View {
	
	private static final String STRING_LOADING_MSG = "Loading...";
	
	
	public LoadingView() {
		super();
		this.add(new HTML(STRING_LOADING_MSG));
	}

	@Override
	public void showLoading() {
		super.center();
		super.show();
	}

	@Override
	public void hideLoading() {
		super.hide();
	}
}
