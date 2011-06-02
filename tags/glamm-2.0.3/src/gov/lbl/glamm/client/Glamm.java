package gov.lbl.glamm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point for Glamm module
 * @author jtbates
 *
 */
public class Glamm implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		GWT.log("AppCodeName:\t" + Window.Navigator.getAppCodeName());
		GWT.log("AppName:\t" + Window.Navigator.getAppName());
		GWT.log("AppVersion:\t" + Window.Navigator.getAppVersion());
		GWT.log("Platform:\t" + Window.Navigator.getPlatform());
		GWT.log("UserAgent:\t" + Window.Navigator.getUserAgent());
		
		// splash screen timer
		final Timer t = new Timer() {

			private int count = 0;
			@Override
			public void run() {
				if(++count > 3) {
					cancel();
					RootPanel.get("loading").setVisible(false);
				}
				else {
					Element loadingMsg = RootPanel.get("loadingMsg").getElement();
					String text = loadingMsg.getInnerText() + ".";
					loadingMsg.setInnerText(text);
				}

			}
		};
		t.scheduleRepeating(1000);
		
		AppController appController = AppController.instance();
		appController.start(RootLayoutPanel.get());
	}

}
