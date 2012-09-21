package gov.lbl.glamm.client.experiment.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

import gov.lbl.glamm.client.experiment.presenter.DisplayControlPresenter;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class DisplayControlView extends FlowPanel
		implements DisplayControlPresenter.View {

	private RadioButton longNameButton = null;
	private RadioButton shortNameButton = null;

	public DisplayControlView() {
		super();
		addStyleName("controlPanel");
		longNameButton = new RadioButton("nameType", "Long Name");
		longNameButton.addStyleName("nameSelection");
		add(longNameButton);
		shortNameButton = new RadioButton("nameType", "Short Name");
		shortNameButton.addStyleName("nameSelection");
		add(shortNameButton);
		shortNameButton.setValue(true);
		disableNameSelection();
	}

	@Override
	public void enableNameSelection() {
		longNameButton.setEnabled(true);
		shortNameButton.setEnabled(true);
		shortNameButton.setValue(true);
	}

	@Override
	public void disableNameSelection() {
		longNameButton.setEnabled(false);
		shortNameButton.setEnabled(false);
	}

	@Override
	public HasClickHandlers getLongNameSelector() {
		return longNameButton;
	}

	@Override
	public HasClickHandlers getShortNameSelector() {
		return shortNameButton;
	}
}
