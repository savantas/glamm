package gov.lbl.glamm.client.map.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;

/**
 * Presenter for an image that displays a popup when clicked.  The image may change depending on whether or not the
 * mouse cursor is over it.
 * @author jtbates
 *
 */
public class ImagePopupPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Gets the mouse event handlers for the image.
		 * @return The mouse event handlers.
		 */
		public HasAllMouseHandlers 	getImageMouseHandlers();
		
		/**
		 * Sets the image url.
		 * @param url The url.
		 */
		public void 				setImageUrl(String url);
		
		/**
		 * Sets the image size.
		 * @param width The image width.
		 * @param height The image height.
		 */
		public void 				setImageSize(String width, String height);
		
		/**
		 * Sets the popup size.
		 * @param width The popup width.
		 * @param height The popup height.
		 */
		public void 				setPopupSize(String width, String height);
		
		/**
		 * Shows the popup with content from url.
		 * @param url The popup content url.
		 */
		public void 				showPopup(String url);
	}

	public enum LinkTarget {
		POPUP,
		NEW_WINDOW
	}
	
	
	private View view = null;

	private String defaultImageUrl = null;
	private String mouseOverImageUrl = null;
	private String popupContentUrl = null;

	private LinkTarget linkTarget;
	
	/**
	 * Constructor
	 * @param view The View object for this presenter.
	 */
	public ImagePopupPresenter(final View view) {

		this.view = view;

		linkTarget = LinkTarget.POPUP;
		bindView();
	}
	
	private void bindView() {

		((HasClickHandlers) view.getImageMouseHandlers()).addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				activatePopup(popupContentUrl);
			}
		});

		view.getImageMouseHandlers().addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				view.setImageUrl(defaultImageUrl);
			}
		});

		view.getImageMouseHandlers().addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(mouseOverImageUrl != null)
					view.setImageUrl(mouseOverImageUrl);
			}
		});

	}

	public void setLinkTarget(LinkTarget target) {
		this.linkTarget = target;
	}
	
	public void activatePopup(String url) {
		System.out.println("here!");
		switch (linkTarget) {
			case NEW_WINDOW :
				Window.open(url, "_blank", "");
				// open in new window
				break;
			case POPUP :
				view.showPopup(url);
				break;
			default :
				view.showPopup(url);
				break;
		}
	}
	
	/**
	 * Sets the default image url, i.e. the image that is displayed when the mouse cursor is not over the image.
	 * @param url The url.
	 */
	public void setDefaultImageUrl(String url) {
		this.defaultImageUrl = url;
		view.setImageUrl(defaultImageUrl);
	}

	/**
	 * Sets the image url for the image displayed when the mouse cursor is over the image.
	 * @param url The url.
	 */
	public void setMouseOverImageUrl(String url) {
		this.mouseOverImageUrl = url;
	}

	/**
	 * Sets the popup content url.
	 * @param url The url.
	 */
	public void setPopupContentUrl(String url) {
		this.popupContentUrl = url;
	}
}
