package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.GlammClientBundle;
import gov.lbl.glamm.client.events.PanZoomControlEvent;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Panel;

public class PanZoomControlPresenter {
	public interface View {
		public HasAllMouseHandlers getAllMouseHandlers();
		public Panel getPanZoomControlPanel();
	}

	private static final String G_ID_ZOOM_SLIDER	= "zoom_slider";
	private static final String G_ID_ZOOM_PLUS		= "zoom_plus";
	private static final String G_ID_ZOOM_MINUS		= "zoom_minus";
	private static final String G_ID_PAN_UP			= "pan_up";
	private static final String G_ID_PAN_DOWN		= "pan_down";
	private static final String G_ID_PAN_LEFT		= "pan_left";
	private static final String G_ID_PAN_RIGHT		= "pan_right";

	private final float SLIDER_VALUE_MIN 	= 0.0f;
	private final float SLIDER_VALUE_MAX 	= 1.0f;
	private final int SLIDER_YPOS_TOP 		= -50;
	private final int SLIDER_YPOS_BOTTOM	= 0;

	private float sliderValue = SLIDER_VALUE_MIN;
	private int sliderYPos = SLIDER_YPOS_BOTTOM;
	
	private OMSVGPathElement 	slider = null;
	private boolean				sliderMoving	= false;
	private int					sliderOrigin	= sliderYPos;
	private int					mouseOrigin		= 0;

	@SuppressWarnings("unused")
	private GlammServiceAsync rpc 		= null;
	private SimpleEventBus 	eventBus 	= null;
	private View			view		= null;

	public PanZoomControlPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		bindView();
	}

	private void bindView() {
		SVGResource panZoomSvg = GlammClientBundle.INSTANCE.panZoomControl();
		OMSVGSVGElement svgRoot = panZoomSvg.getSvg();
		view.getPanZoomControlPanel().getElement().appendChild(svgRoot.getElement());
		view.getPanZoomControlPanel().setStylePrimaryName("glamm-PanZoomControl");

		OMNodeList<OMElement> groupNodes = svgRoot.getElementsByTagName("g");

		for(OMElement group : groupNodes) {

			final String groupId = group.getAttribute("id");

			if(groupId.equals(G_ID_ZOOM_SLIDER)) { 
				initZoomSlider(group);
			}
			else if(groupId.equals(G_ID_ZOOM_PLUS)) {  
				initButtonEvents(group, PanZoomControlEvent.ACTION_ZOOM_IN, new Command() {
					public void execute() {
						setSliderValue(getSliderValue() * 1.1f);
					}
				});
			}
			else if(groupId.equals(G_ID_ZOOM_MINUS)) {  
				initButtonEvents(group, PanZoomControlEvent.ACTION_ZOOM_OUT, new Command() {
					public void execute() {
						setSliderValue(getSliderValue() * 0.9f);
					}
				});
			}
			else if(groupId.equals(G_ID_PAN_UP)) {  
				initButtonEvents(group, PanZoomControlEvent.ACTION_PAN_UP, null);
			}
			else if(groupId.equals(G_ID_PAN_DOWN)) {  
				initButtonEvents(group, PanZoomControlEvent.ACTION_PAN_DOWN, null);
			}
			else if(groupId.equals(G_ID_PAN_LEFT)) {  
				initButtonEvents(group, PanZoomControlEvent.ACTION_PAN_LEFT, null);
			}
			else if(groupId.equals(G_ID_PAN_RIGHT)) {  
				initButtonEvents(group, PanZoomControlEvent.ACTION_PAN_RIGHT, null);
			}
		}
	}

	private void initButtonEvents(final OMNode group, final short action, final Command clickBehavior) {

		final PanZoomControlPresenter thePresenter = this;

		OMNodeList<OMNode> children = group.getChildNodes();
		for(OMNode childNode : children) {

			if(childNode instanceof OMSVGGElement) {
				initButtonEvents(childNode, action, clickBehavior);
			}


			if(childNode instanceof OMSVGRectElement) {
				OMSVGRectElement child = (OMSVGRectElement) childNode;
				child.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(clickBehavior != null)
							clickBehavior.execute();
						thePresenter.eventBus.fireEvent(new PanZoomControlEvent(action, sliderValue));
					}

				});
			}

			else if(childNode instanceof OMSVGPathElement) {
				OMSVGPathElement child = (OMSVGPathElement) childNode;
				child.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(clickBehavior != null) 
							clickBehavior.execute();
						thePresenter.eventBus.fireEvent(new PanZoomControlEvent(action, sliderValue));
					}
				});
			}
		}
	}

	private void initZoomSlider(OMElement group) {

		final PanZoomControlPresenter thePresenter = this;
		OMNodeList<OMSVGPathElement> pathNodes = group.getElementsByTagName("path");
		slider = pathNodes.getItem(0);

		slider.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
				sliderMoving = true;
				mouseOrigin = event.getClientY();
				sliderOrigin = sliderYPos;
			}
		});

		thePresenter.view.getAllMouseHandlers().addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				event.preventDefault();
				if(sliderMoving) {
					setSliderPos(sliderOrigin + event.getClientY() - mouseOrigin, true);
					thePresenter.eventBus.fireEvent(new PanZoomControlEvent(PanZoomControlEvent.ACTION_ZOOM_SLIDER, sliderValue));
				}
			}
		});

		thePresenter.view.getAllMouseHandlers().addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				event.preventDefault();
				if(sliderMoving)
					sliderMoving = false;
			}
		});
	}

	public float getSliderValue() {
		return sliderValue;
	}

	//********************************************************************************

	private void setSliderPos(int yPos, boolean setSliderValue) {

		sliderYPos = yPos;
		if(sliderYPos <= SLIDER_YPOS_TOP) sliderYPos = SLIDER_YPOS_TOP;
		if(sliderYPos >= SLIDER_YPOS_BOTTOM) sliderYPos = SLIDER_YPOS_BOTTOM;

		slider.setAttribute("transform", "translate(0," + sliderYPos + ")");

		if(setSliderValue) {
			float t = (float) (sliderYPos - SLIDER_YPOS_BOTTOM) / (float) (SLIDER_YPOS_TOP - SLIDER_YPOS_BOTTOM);
			sliderValue = SLIDER_VALUE_MIN + t * (SLIDER_VALUE_MAX - SLIDER_VALUE_MIN);
		}
	}

	public void setSliderValue(float value) {

		if(value >= SLIDER_VALUE_MAX) sliderValue = SLIDER_VALUE_MAX;
		else if (value <= SLIDER_VALUE_MIN) sliderValue = SLIDER_VALUE_MIN;
		else sliderValue = value;

		float t = (sliderValue - SLIDER_VALUE_MIN) / (SLIDER_VALUE_MAX - SLIDER_VALUE_MIN);
		int yPos = (int) (SLIDER_YPOS_BOTTOM + t * (SLIDER_YPOS_TOP - SLIDER_YPOS_BOTTOM));

		setSliderPos(yPos, false);
	}



}
