package gov.lbl.glamm.client.experiment;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GlammExperimentView implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		AppController appController = AppController.getInstance();
		appController.start(RootPanel.get(), RootLayoutPanel.get());
	}
}
