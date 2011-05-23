package gov.lbl.glamm.client.presenter;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Panel;

public class MiniMapPresenter {
	
	public interface View {
		public HasAllMouseHandlers getAllMouseHandlers();
		public Panel getMiniMapPanel();
	}
	
	@SuppressWarnings("unused")
	private SimpleEventBus 	eventBus 	= null;
	private View			view		= null;
	
	private OMSVGSVGElement		svg		= null;
	private OMSVGImageElement	img		= null;
	private OMSVGRectElement	reticle	= null;
	
	private int imgWidth = 0;
	private int imgHeight = 0;
	
	public MiniMapPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
	}
	
	public void setMiniMap(ImageResource imgResource) {
		
		OMSVGDocument doc = OMSVGParser.currentDocument();
        
        // Create the root svg element
        svg =  doc.createSVGSVGElement();

        // Create the background image
        imgWidth = imgResource.getWidth();
        imgHeight = imgResource.getHeight();
        img = doc.createSVGImageElement(0, 0, imgWidth, imgHeight, imgResource.getURL());
        svg.appendChild(img);
        
        // Create the reticle
        reticle = doc.createSVGRectElement();
        reticle.setAttribute("class", "glamm-MiniMap-Reticle");
        svg.appendChild(reticle);
               
        // append the svg to the panel and resize
        view.getMiniMapPanel().getElement().appendChild(svg.getElement());
		view.getMiniMapPanel().setSize(imgWidth + "px", imgHeight + "px");
	}
	
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
