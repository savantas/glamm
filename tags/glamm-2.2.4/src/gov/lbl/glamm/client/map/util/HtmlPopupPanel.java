package gov.lbl.glamm.client.map.util;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * Subclass of DecoratedPopupPanel - displays HTML content in a popup panel that disappears after a specific time interval
 * when the mouse leaves the popup frame.
 * @author jtbates
 *
 */
public class HtmlPopupPanel extends DecoratedPopupPanel {
	
	
	
	private static final int DEFAULT_HIDE_DELAY_MILLIS 	= 700;
	
	private Timer hideTimer = null;
	
	private int hideDelay = DEFAULT_HIDE_DELAY_MILLIS;
	
	private HTML html = null;
	private FocusPanel panel = null;
	
	private Command onHide = null;

	
	/**
	 * Constructor
	 */
	public HtmlPopupPanel() {
		this(DEFAULT_HIDE_DELAY_MILLIS);
	}

	

	/**
	 * Constructor
	 * @param hideDelay Time delay before popup is hidden in milliseconds.
	 */
	public HtmlPopupPanel(int hideDelay) {
		
		super();
		
		this.hideDelay = hideDelay;
		html = new HTML("");
		panel = new FocusPanel();
		
		panel.add(html);
		this.add(panel);
		this.setAutoHideEnabled(true);
		this.addStyleName("glamm-DelayedPopupPanel");
		
		panel.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent e) {
				cancelTimer();
			}
		});
		
		panel.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent e) {
				hidePanel();
			}
		});
		
		panel.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent e) {
				cancelTimer();
			}
		});
	}
	
	/**
	 * Sets the command to execute when the popup is hidden.
	 * @param onHide The command.
	 */
	public void setOnHideCommand(Command onHide) {
		this.onHide = onHide;
	}

	/**
	 * Shows the popup with a string representation of html content at a position in client space.
	 * @param content The html content of the panel.
	 * @param left The client x position.
	 * @param top The client y position.
	 */
	public void showPanel(final String content, final int left, final int top) {
		this.showPanel(new HTML(content), left, top);
	}
	
	
	/**
	 * Shows the popup with html content at a position in client space.
	 * @param html The html content of the panel.
	 * @param left The client x position.
	 * @param top The client y position.
	 */
	public void showPanel(final HTML html, final int left, final int top) {
		this.html.setHTML(html.getHTML());
		cancelTimer();
		setPopupPosition(left, top);
		show();
	}
	
	/**
	 * Shows the popup with html content in the center of the client window.
	 * @param html The html content of the panel.
	 */
	public void showPanelCenter(final HTML html) {
		this.html.setHTML(html.getHTML());
		cancelTimer();
		center();
		show();
	}
	
	
	/**
	 * Kills the panel - hides it immediately.
	 */
	public void killPanel() {
		cancelTimer();
		this.hide();
		if(onHide != null)
			onHide.execute();
	}
	
	/**
	 * Hides the panel after the hide delay specified in the constructor.
	 */
	public void hidePanel() {
		cancelTimer();
		hideTimer = new Timer() {
			@Override
			public void run() {
				killPanel();
			}
		};
		hideTimer.schedule(hideDelay);
	}

	private void cancelTimer() {
		if(hideTimer != null) {
			hideTimer.cancel();
			hideTimer = null;
		}
	}	
}
