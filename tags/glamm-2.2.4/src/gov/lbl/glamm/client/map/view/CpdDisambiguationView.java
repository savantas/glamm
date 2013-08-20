package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.CpdDisambiguationPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View to allow the disambiguation between compounds with the same synonym.
 * @author jtbates
 *
 */
public class CpdDisambiguationView extends DecoratedPopupPanel implements CpdDisambiguationPresenter.View {
	
	private VerticalPanel			mainPanel					= null;
	private Label					label						= null;
	private ScrollPanel 			scrollPanel 				= null;
	private VerticalPanel 			scrollPanelInterior 		= null;
	
	/**
	 * Constructor
	 */
	public CpdDisambiguationView() {
		super();
		
		mainPanel = new VerticalPanel();
		label = new Label("Select desired compound:");
		scrollPanel = new ScrollPanel();
		scrollPanelInterior = new VerticalPanel();
		
		scrollPanel.setHeight("20em");
		scrollPanel.setWidth("25em");
		
		scrollPanelInterior.setSpacing(10);
		
		this.add(mainPanel);
		mainPanel.add(label);
		mainPanel.add(scrollPanel);
		scrollPanel.add(scrollPanelInterior);
		
		mainPanel.setStylePrimaryName("glamm-picker");
	}
	
	@Override
	public HasClickHandlers addCpdChoice(final String html) {
		final HTMLPanel htmlPanel = new HTMLPanel(html);
		final FocusPanel focusPanel = new FocusPanel();
		scrollPanelInterior.add(focusPanel);
		focusPanel.add(htmlPanel);
		return focusPanel;
	}
	
	@Override
	public void clearCpdChoices() {
		scrollPanelInterior.clear();
	}
	
	@Override
	public void hideView() {
		this.hide();
	}
	
	@Override
	public void showView() {
		this.center();
		this.show();
	}
	
}
