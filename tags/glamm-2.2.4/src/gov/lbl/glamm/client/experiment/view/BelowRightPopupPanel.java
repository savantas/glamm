package gov.lbl.glamm.client.experiment.view;

import com.google.gwt.user.client.ui.PopupPanel;

public class BelowRightPopupPanel extends AccessibleHtmlPopupPanel {
	public BelowRightPopupPanel() {
		super(true, false);
	}

	@Override
	public void setPopupPositionAndShow( final int x, final int y ) {
		this.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = x;
				int top = y;
				setPopupPosition(left, top);
			}
		});
	}
}
