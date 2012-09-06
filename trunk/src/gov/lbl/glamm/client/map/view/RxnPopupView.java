package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.RxnPopupPresenter;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for displaying reaction popups.
 * @author jtbates
 *
 */
public class RxnPopupView extends PopupPanel implements RxnPopupPresenter.View {

	private VerticalPanel 		mainPanel;
	private Label				statusLabel;
	private VerticalPanel		reactionsPanel;

	/**
	 * Constructor
	 */
	public RxnPopupView() {
		
		super();
		
		mainPanel 		= new VerticalPanel();
		statusLabel 	= new Label();
		reactionsPanel	= new VerticalPanel();
		reactionsPanel.setSpacing(5);
		
		this.setWidget(mainPanel);
		this.setAutoHideEnabled(true);
		mainPanel.add(statusLabel);
		mainPanel.add(reactionsPanel);
		mainPanel.setStylePrimaryName("glamm-picker");
	}
	
	@Override
	public Panel getPanel() {
		return reactionsPanel;
	}

	@Override
	public Label getStatusLabel() {	
		return statusLabel;
	}

	@Override
	public void hidePopup() {
		super.hide();
	}

	@Override
	public void killPopup() {
		super.hide();
	}

	@Override
	public void showPopup(int left, int top) {
		super.setPopupPosition(left, top);
		super.show();
	}
}
