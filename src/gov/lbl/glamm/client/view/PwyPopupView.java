package gov.lbl.glamm.client.view;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.presenter.PwyPopupPresenter;

/**
 * Pathway Popup Viewer. This shows a pathway image (e.g. from KEGG) in a large-ish popup panel so the entire image
 * can be viewed. It also adjusts its position to ensure that it is on the screen.
 * 
 * @author John Bates and Bill Riehl wjriehl@lbl.gov
 *
 */

public class PwyPopupView extends PopupPanel implements PwyPopupPresenter.View {

	private final String 				ADD_ALL_TO_CART = "Add all reactions to cart";
	private final String				ADD_NATIVE_TO_CART = "Add reactions with genes to cart";
	private final String				KEGG_MAP_STYLE = "Show reaction table";		// these are opposite of what you might think. When the button
	private final String				REACTION_TABLE_STYLE = "Show KEGG map";		// is set to one style, it gives the option to jump to the other
	private final String				EMPTY_REACTION_TABLE = "No reactions available";

	private VerticalPanel				mainPanel;					// holds the status label and pwy panel
	private VerticalPanel				pwyPanel;					// holds the HTMl header label, pwy layout panel, and button panel

	private Label						statusLabel;
	private Label						emptyTableLabel;
	private HTML						pwyLinkHtml;

	private Button						addAllToCart;
	private Button						addNativeToCart;
	private Button						pwyViewStyle;

	private Image						pwyImage;
	private final ScrollPanel			imgScrollPanel;
	private final ResizeLayoutPanel 	tableLayoutPanel;

	private static final int 			PANEL_WIDTH = 500;   //px
	private static final int 			PANEL_HEIGHT = 500;  //px

	private HorizontalPanel				headerPanel;
	private HorizontalPanel				buttonPanel;
	
	private DataGrid<Reaction>			pwyTable = null;
	
	private int left, top;
	
	public PwyPopupView() {
		
		super();
		
		addAllToCart = new Button(ADD_ALL_TO_CART);
		addNativeToCart = new Button(ADD_NATIVE_TO_CART);
		pwyViewStyle = new Button(REACTION_TABLE_STYLE);

		headerPanel = new HorizontalPanel();
		buttonPanel = new HorizontalPanel();
		
		pwyImage = new Image();
		imgScrollPanel = new ScrollPanel(pwyImage);

		pwyLinkHtml = new HTML();

		pwyTable = new DataGrid<Reaction>();
		tableLayoutPanel = new ResizeLayoutPanel();

		mainPanel = new VerticalPanel();
		pwyPanel = new VerticalPanel();
		
		statusLabel = new Label();
		emptyTableLabel = new Label(EMPTY_REACTION_TABLE);
		init();
		
	}
	
	public void init() {
		imgScrollPanel.setSize(PANEL_WIDTH + "px", PANEL_HEIGHT + "px");

		pwyImage.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event)
			{
				imgScrollPanel.setWidth(Math.min(PANEL_WIDTH, pwyImage.getWidth() + 20) + "px");
				imgScrollPanel.setHeight(Math.min(PANEL_HEIGHT, pwyImage.getHeight() + 20) + "px");
			}
		});

		// Contains panel header info - the MO link and a button to toggle between KEGG and table views
		headerPanel.setSpacing(5);
		headerPanel.add(pwyLinkHtml);
		headerPanel.add(pwyViewStyle);

		tableLayoutPanel.add(pwyTable);
		tableLayoutPanel.setSize(PANEL_WIDTH + "px", PANEL_HEIGHT + "px");
		
		buttonPanel.setSpacing(5);
		buttonPanel.add(addAllToCart);
		buttonPanel.add(addNativeToCart);

		// Contains the bulk of the viewer - a reaction table and KEGG map view (toggle-able)
		// as well as buttons to add to cart (if the user is logged in)
		pwyPanel.setSpacing(5);
		pwyPanel.add(headerPanel);
		pwyPanel.add(tableLayoutPanel);
		pwyPanel.add(imgScrollPanel);
		pwyPanel.add(emptyTableLabel);
		emptyTableLabel.setVisible(false);
		imgScrollPanel.setVisible(false);
//		pwyPanel.add(buttonPanel);

		this.setWidget(mainPanel);
		this.setAutoHideEnabled(true);
		mainPanel.add(statusLabel);
		mainPanel.add(pwyPanel);
		mainPanel.setStylePrimaryName("glamm-picker");
	}
	
	public Panel getButtonPanel() {
		return buttonPanel;
	}
	
	public void toggleViewStyle() {
		boolean showKegg = pwyViewStyle.getText().equals(REACTION_TABLE_STYLE);
		pwyViewStyle.setText(showKegg ? KEGG_MAP_STYLE : REACTION_TABLE_STYLE);
		if (showKegg) {
			emptyTableLabel.setVisible(false);
			tableLayoutPanel.setVisible(false);
			imgScrollPanel.setVisible(true);
		}
		else {
			imgScrollPanel.setVisible(false);
			showTableIfNotEmpty();
			this.resetPopupPosition();
		}
	}
	
	public void showTableIfNotEmpty() {
		if (pwyTable.getVisibleItemCount() == 0) {
			emptyTableLabel.setVisible(true);
			tableLayoutPanel.setVisible(false);
		}
		else {
			tableLayoutPanel.setVisible(true);
			emptyTableLabel.setVisible(false);
			pwyTable.redraw();
		}		
	}
	
	public Button getViewStyleButton() {
		return pwyViewStyle;
	}
	
	@Override
	public Button getAddAllToCartButton() {
		return addAllToCart;
	}
	
	@Override
	public Button getAddNativeToCartButton() {
		return addNativeToCart;
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
	public DataGrid<Reaction> getPwyTable() {
		return pwyTable;
	}
	
	@Override
	public void hidePopup() {
		super.hide();
		tableLayoutPanel.setVisible(false);
		emptyTableLabel.setVisible(false);
		imgScrollPanel.setVisible(true);
		pwyViewStyle.setText(KEGG_MAP_STYLE);
	}

	@Override
	public void killPopup() {
		hidePopup();
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

