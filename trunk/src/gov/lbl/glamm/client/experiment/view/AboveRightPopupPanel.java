package gov.lbl.glamm.client.experiment.view;


import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Pop-up panel overlay widget that stays visible when user mouses over the panel.
 * The panel appears above and to the right of the cursor when it first appears.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class AboveRightPopupPanel extends AccessibleHtmlPopupPanel {
	public AboveRightPopupPanel() {
		super(true, false);
	}

	public void setPopupPositionAndShow( final int x, final int y ) {
		this.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = x;
				int top = y - offsetHeight;
				if ( top < 0 ) {
					top = 0;
				}
				AboveRightPopupPanel.this.setPopupPosition(left, top);
			}
		});
	}
}
