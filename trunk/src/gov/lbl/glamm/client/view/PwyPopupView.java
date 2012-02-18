package gov.lbl.glamm.client.view;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import gov.lbl.glamm.client.presenter.PwyPopupPresenter;

/**
 * Pathway Popup Viewer. This shows a pathway image (e.g. from KEGG) in a large-ish popup panel so the entire image
 * can be viewed. It also adjusts its position to ensure that it is on the screen.
 * 
 * @author John Bates and Bill Riehl wjriehl@lbl.gov
 *
 */

public class PwyPopupView extends PopupPanel implements PwyPopupPresenter.View {

	private final String 		ADD_TO_CART = "Add to cart";
	private VerticalPanel		mainPanel;
	private VerticalPanel		pwyPanel;
	private Label				statusLabel;
	private HTML				pwyLinkHtml;
	private Button				addToCart;
	private Image				pwyImage;
	private final ScrollPanel	imgScrollPanel;
	private static final int 	SCROLL_WIDTH = 500;   //px
	private static final int 	SCROLL_HEIGHT = 500;  //px
	
	private int left, top;
	
	public PwyPopupView() {
		
		super();
		
		addToCart = new Button(ADD_TO_CART);
		pwyImage = new Image();
		imgScrollPanel = new ScrollPanel(pwyImage);
		imgScrollPanel.setSize(SCROLL_WIDTH + "px", SCROLL_HEIGHT + "px");
		
		pwyImage.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event)
			{
				imgScrollPanel.setWidth(Math.min(SCROLL_WIDTH, pwyImage.getWidth() + 20) + "px");
				imgScrollPanel.setHeight(Math.min(SCROLL_HEIGHT, pwyImage.getHeight() + 20) + "px");
			}
		});
		
		pwyLinkHtml = new HTML();

		mainPanel = new VerticalPanel();
		statusLabel = new Label();
		pwyPanel = new VerticalPanel();
		pwyPanel.setSpacing(5);
		pwyPanel.add(pwyLinkHtml);
		pwyPanel.add(imgScrollPanel);
		
		
		this.setWidget(mainPanel);
		this.setAutoHideEnabled(true);
		mainPanel.add(statusLabel);
		mainPanel.add(pwyPanel);
		mainPanel.add(addToCart);
		mainPanel.setStylePrimaryName("glamm-picker");
	}
	
	@Override
	public Button getAddToCartButton() {
		return addToCart;
	}

	@Override
	public Image getImage() {
		return pwyImage;
	}

	@Override
	public HTML getPwyLinkHtml() {
		return pwyLinkHtml;
	}
	
	@Override
	public Panel getPanel() {
		return pwyPanel;
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

	/**
	 * This is a little scary because it (with showPopup) is recursive. It should 
	 * break out as soon as it gets to a point where the popup is completely on the
	 * screen, but there's no concrete end case.
	 * 
	 * // TODO put in a specific cancel case.
	 */
	public void resetPopupPosition() {
		int newLeft = left;
		int newTop = top;
		
		/**
		 * If the size is off (via PopupPanel.getOffsetWidth/Height()), adjust the position.
		 */
		if (Window.getClientWidth() < left + super.getOffsetWidth())
			newLeft = Window.getClientWidth() - super.getOffsetWidth();
		
		if (Window.getClientHeight() < top + super.getOffsetHeight())
			newTop = Window.getClientHeight() - super.getOffsetHeight();

		/**
		 * Here's where it gets recursive - show the popup again at the new position.
		 * This will call resetPopupPosition() again, which might cause problems. However, as long
		 * as the position doesn't change before the call finishes, it will work fine (i.e., 
		 * top and left don't change).
		 */
		if (newTop != top || newLeft != left) {
			super.hide();
			showPopup(newLeft, newTop);
		}
	}
	
	@Override
	/**
	 * Shows the popup with the upper left corner initially on the mouse-click point.
	 * It then resets the position to try to keep the whole popup window in the 
	 * browser.
	 * 
	 * The problem is that the size of the popup panel isn't known until it's constructed
	 * and displayed in the browser. So it shows the popup panel, checks to see if
	 * its off screen, reorients it, THEN shows it again.
	 */
	public void showPopup(int left, int top) {
		this.left = left;
		this.top = top;

		super.setPopupPosition(left, top);
		super.show();
		
		// Checks if the window is off-screen, and moves it back in if so.
		resetPopupPosition();
	}
	
	public void updateImage(String imgUrl)
	{
		pwyImage.setUrl(imgUrl);
	}

}
