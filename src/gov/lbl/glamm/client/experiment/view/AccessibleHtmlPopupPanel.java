package gov.lbl.glamm.client.experiment.view;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * Pop-up panel overlay widget that stays visible when user mouses over the panel.
 * There is a configurable delay in hiding the panel - if set to zero, the
 * mouse-over-to-stay-visible effect would likely be negated.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public abstract class AccessibleHtmlPopupPanel extends DecoratedPopupPanel {
	protected HTML html = null;

	public static final int DEFAULT_HIDE_DELAY_MILLIS = 200;
	protected Timer hideTimer = null;
	protected int hideDelayMillis = DEFAULT_HIDE_DELAY_MILLIS;

	private FocusPanel focusPanel = new FocusPanel();

	public AccessibleHtmlPopupPanel() {
		super();
		this.init();
	}
	public AccessibleHtmlPopupPanel(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		this.init();
	}
	public AccessibleHtmlPopupPanel(boolean autoHide) {
		super(autoHide);
		this.init();
	}

	public void setHideDelayMillis( int delay ) {
		this.hideDelayMillis = delay;
	}

	protected void init() {
		this.add(focusPanel);

		focusPanel.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent e) {
				cancelTimers();
			}
		});
		focusPanel.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent e) {
				AccessibleHtmlPopupPanel.this.hide();
			}
		});

		html = new HTML();
		focusPanel.add(html);
	}

	public void show( final int x, final int y, final String text ) {
		html.setHTML(text);
		this.setPopupPositionAndShow(x, y);
	}

	public abstract void setPopupPositionAndShow( final int x, final int y );

	public void hide() {
		if ( hideTimer != null ) {
			hideTimer.cancel();
		}
		hideTimer = new Timer() {
			public void run() {
				AccessibleHtmlPopupPanel.super.hide();
				hideTimer = null;
			}
		};
		hideTimer.schedule(hideDelayMillis);
	}

	private void cancelTimer(Timer timer) {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	protected void cancelTimers() {
		cancelTimer(hideTimer);
	}
}
