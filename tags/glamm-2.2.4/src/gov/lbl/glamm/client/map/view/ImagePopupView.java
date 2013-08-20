package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.ImagePopupPresenter;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;

/**
 * View for images that, when clicked, display popup windows.
 * @author jtbates
 *
 */
public class ImagePopupView extends Composite 
	implements ImagePopupPresenter.View {
	
	// image
	private Image image	= null;

	// popup
	private DecoratedPopupPanel popup 	= null;
	private Frame frame = null;

	/**
	 * Constructor
	 */
	public ImagePopupView() {
		super();
		image = new Image();
		popup = new DecoratedPopupPanel(true);
		frame = new Frame();
		
		popup.add(frame);
		initWidget(image);
	}

	@Override
	public HasAllMouseHandlers getImageMouseHandlers() {
		return image;
	}
	
	@Override
	public void setImageUrl(String url) {
		image.setUrl(url);
	}

	@Override
	public void setImageSize(String width, String height) {
		image.setSize(width, height);
	}

	@Override
	public void setPopupSize(String width, String height) {
		frame.setSize(width, height);
	}

	@Override
	public void showPopup(String url) {
		frame.setUrl(url);
		popup.center();
		popup.show();
	}

}
