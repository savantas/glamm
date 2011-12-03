package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.ViewResizedEvent;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

/**
 * Presenter for the mini-map (icon currently displayed in the lower left corner of the screen) for this map.
 * @author jtbates
 *
 */
public class MiniMapPresenter {

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Gets the mouse event handlers for the mini map.
		 * @return The mouse event handler interface.
		 */
		public HasAllMouseHandlers getAllMouseHandlers();
		
		/**
		 * Gets the image for the mini map background.
		 * @return The image.
		 */
		public Image getImage();
		
		/**
		 * Gets the panel containing the mini map.
		 * @return The panel.
		 */
		public Panel getMiniMapPanel();
	}

	private SimpleEventBus 	eventBus 	= null;
	private View			view		= null;

	private OMSVGSVGElement		svg		= null;
	private OMSVGImageElement	img		= null;
	private OMSVGRectElement	reticle	= null;

	private int imgWidth = 0;
	private int imgHeight = 0;

	/**
	 * Constructor
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public MiniMapPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		bindView();
	}

	private void bindView() {
		Event.setEventListener(view.getImage().getElement(), new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONLOAD == event.getTypeInt()) {
					
					imgWidth = view.getImage().getWidth();
					imgHeight = view.getImage().getHeight();

					// remove the old svg element from the panel
					if(svg != null)
						view.getMiniMapPanel().getElement().removeChild(svg.getElement());
					
					OMSVGDocument doc = OMSVGParser.currentDocument();
					svg =  doc.createSVGSVGElement();
					img = doc.createSVGImageElement(0, 0, imgWidth, imgHeight, view.getImage().getUrl());
					svg.appendChild(img);

					// Create the reticle
					reticle = doc.createSVGRectElement();
					reticle.setAttribute("class", "glamm-MiniMap-Reticle");
					svg.appendChild(reticle);

					// append the svg to the panel and resize
					view.getMiniMapPanel().getElement().appendChild(svg.getElement());
					view.getMiniMapPanel().setSize(imgWidth + "px", imgHeight + "px");
					eventBus.fireEvent(new ViewResizedEvent());
				}
			}
		});
	}

	/**
	 * Sets the url for the mini map background image.
	 * @param url The url.
	 */
	public void setMiniMapUrl(final String url) {
		view.getImage().setUrl(url);
	}

	/**
	 * Updates the view reticle for the mini map panel.
	 * @param viewRectNorm The normalized rectangle (coordinates normalized in the range [0,1] for the reticle.
	 */
	public void updateReticle(OMSVGRect viewRectNorm) {

		if(viewRectNorm == null)
			return;

		float x = (float) imgWidth * viewRectNorm.getX();
		float y = (float) imgHeight * viewRectNorm.getY();
		float width = (float) imgWidth * viewRectNorm.getWidth();
		float height = (float) imgHeight * viewRectNorm.getHeight();

		reticle.setAttribute("x", Float.toString(x));
		reticle.setAttribute("y", Float.toString(y));
		reticle.setAttribute("width", Float.toString(width));
		reticle.setAttribute("height", Float.toString(height));
	}

}
