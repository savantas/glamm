package gov.lbl.glamm.client.util;

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

public class HtmlPopupPanel extends DecoratedPopupPanel {
	
	//********************************************************************************
	
	private static final int DEFAULT_HIDE_DELAY_MILLIS 	= 700;
	
	private Timer hideTimer = null;
	
	private int hideDelay = DEFAULT_HIDE_DELAY_MILLIS;
	
	private HTML html = null;
	private FocusPanel panel = null;
	
	private Command onHide = null;

	//********************************************************************************

	public HtmlPopupPanel() {
		this(DEFAULT_HIDE_DELAY_MILLIS);
	}

	//********************************************************************************

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
	
	//********************************************************************************
	
	public void setOnHideCommand(Command onHide) {
		this.onHide = onHide;
	}

	//********************************************************************************

	public void showPanel(final String content, final int left, final int top) {
		this.showPanel(new HTML(content), left, top);
	}
	
	//********************************************************************************

	public void showPanel(final HTML html, final int left, final int top) {
		this.html.setHTML(html.getHTML());
		cancelTimer();
		setPopupPosition(left, top);
		show();
	}
	
	//********************************************************************************
	
	public void showPanelCenter(final HTML html) {
		this.html.setHTML(html.getHTML());
		cancelTimer();
		center();
		show();
	}
	
	//********************************************************************************
	
	public void killPanel() {
		cancelTimer();
		this.hide();
		if(onHide != null)
			onHide.execute();
	}
	//********************************************************************************

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

	//********************************************************************************

	private void cancelTimer() {
		if(hideTimer != null) {
			hideTimer.cancel();
			hideTimer = null;
		}
	}

	//********************************************************************************
	
}
