package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.RxnPopupPresenter;
import gov.lbl.glamm.client.util.HtmlPopupPanel;

public class RxnPopupView extends HtmlPopupPanel implements RxnPopupPresenter.View {

	@Override
	public void hidePopup() {
		super.hidePanel();
	}

	@Override
	public void killPopup() {
		super.killPanel();
	}

	@Override
	public void showPopup(String content, int left, int top) {
		super.showPanel(content, left, top);
	}

}
