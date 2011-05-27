package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.MapElementPresenter;
import gov.lbl.glamm.client.util.HtmlPopupPanel;

public class MapElementView extends HtmlPopupPanel implements MapElementPresenter.View {

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
