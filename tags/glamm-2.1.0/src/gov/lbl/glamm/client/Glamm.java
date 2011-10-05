package gov.lbl.glamm.client;

import java.util.Collection;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Cookies;
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

	@SuppressWarnings("unused")
	private String getCookieInfoString() {
		boolean cookiesEnabled = Cookies.isCookieEnabled();
		String info = "Cookies are " + (cookiesEnabled ? "" : "not ") + "enabled.\n\n";

		if(cookiesEnabled) {
			String hostName = Window.Location.getHostName();
			Collection<String> cookieNames = Cookies.getCookieNames();
			if(cookieNames == null || cookieNames.isEmpty())
				info += "There are no cookies for " + hostName + ".\n";
			else {
				info += "Cookies for " + hostName + ":\n";
				for(String name : cookieNames) {
					info += "\t" + name + ": " + Cookies.getCookie(name) + "\n";
				}
			}
		}
		return info;
	}

	@Override
	public void onModuleLoad() {

		GWT.log("AppCodeName: "	+ Window.Navigator.getAppCodeName());
		GWT.log("AppName: " 	+ Window.Navigator.getAppName());
		GWT.log("AppVersion: " 	+ Window.Navigator.getAppVersion());
		GWT.log("Platform: " 	+ Window.Navigator.getPlatform());
		GWT.log("UserAgent: " 	+ Window.Navigator.getUserAgent());

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
