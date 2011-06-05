package gov.lbl.glamm.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

public class ImagePopupPresenter {

	public interface View {
		public HasClickHandlers 	getImageHasClickHandlers();
		public HasAllMouseHandlers 	getImageHasMouseHandlers();
		public void 				setImageUrl(String url);
		public void 				setImageSize(String width, String height);
		public void 				setPopupSize(String width, String height);
		public void 				showPopup(String url);
	}

	private View view = null;

	private String defaultImageUrl = null;
	private String mouseOverImageUrl = null;
	private String popupContentUrl = null;

	public ImagePopupPresenter(final View view) {

		this.view = view;

		bindView();
	}

	private void bindView() {

		view.getImageHasClickHandlers().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				view.showPopup(popupContentUrl);
			}
		});

		view.getImageHasMouseHandlers().addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				view.setImageUrl(defaultImageUrl);
			}
		});

		view.getImageHasMouseHandlers().addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(mouseOverImageUrl != null)
					view.setImageUrl(mouseOverImageUrl);
			}
		});

	}

	public void setDefaultImageUrl(String url) {
		this.defaultImageUrl = url;
		view.setImageUrl(defaultImageUrl);
	}

	public void setMouseOverImageUrl(String url) {
		this.mouseOverImageUrl = url;
	}

	public void setPopupContentUrl(String url) {
		this.popupContentUrl = url;
	}
}
