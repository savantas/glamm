package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.GlammClientBundle;
import gov.lbl.glamm.client.events.PanZoomControlEvent;
import gov.lbl.glamm.client.events.PanZoomControlEvent.Action;
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

/**
 * Presenter for pan/zoom control panel.
 * @author jtbates
 *
 */
public class PanZoomControlPresenter {
	
	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Gets the mouse handlers for the pan/zoom control panel.
		 * @return The mouse handlers.
		 */
		public HasAllMouseHandlers getAllMouseHandlers();
		
		/**
		 * Gets the panel.
		 * @return The panel.
		 */
		public Panel getPanZoomControlPanel();
	}

	private interface Group {
		String ZOOM_SLIDER	= "zoom_slider";
		String ZOOM_PLUS	= "zoom_plus";
		String ZOOM_MINUS	= "zoom_minus";
		String ZOOM_TO_FIT	= "zoom_to_fit";
		String PAN_UP		= "pan_up";
		String PAN_DOWN		= "pan_down";
		String PAN_LEFT		= "pan_left";
		String PAN_RIGHT	= "pan_right";
	}

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

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
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

			if(groupId.equals(Group.ZOOM_SLIDER)) { 
				initZoomSlider(group);
			}
			else if(groupId.equals(Group.ZOOM_PLUS)) {  
				initButtonEvents(group, Action.ZOOM_IN, new Command() {
					public void execute() {
						setSliderValue(getSliderValue() * 1.1f);
					}
				});
			}
			else if(groupId.equals(Group.ZOOM_MINUS)) {  
				initButtonEvents(group, Action.ZOOM_OUT, new Command() {
					public void execute() {
						setSliderValue(getSliderValue() * 0.9f);
					}
				});
			}
			else if(groupId.equals(Group.ZOOM_TO_FIT)) {
				initButtonEvents(group, Action.ZOOM_TO_FIT, null);
			}
			else if(groupId.equals(Group.PAN_UP)) {  
				initButtonEvents(group, Action.PAN_UP, null);
			}
			else if(groupId.equals(Group.PAN_DOWN)) {  
				initButtonEvents(group, Action.PAN_DOWN, null);
			}
			else if(groupId.equals(Group.PAN_LEFT)) {  
				initButtonEvents(group, Action.PAN_LEFT, null);
			}
			else if(groupId.equals(Group.PAN_RIGHT)) {  
				initButtonEvents(group, Action.PAN_RIGHT, null);
			}
		}
	}

	private void initButtonEvents(final OMNode group, final Action action, final Command clickBehavior) {

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
					thePresenter.eventBus.fireEvent(new PanZoomControlEvent(Action.ZOOM_SLIDER, sliderValue));
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

	private float getSliderValue() {
		return sliderValue;
	}

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

	/**
	 * Sets the slider value and updates the slider position.
	 * @param value The value clamped to the range [0,1], inclusive.
	 */
	public void setSliderValue(float value) {

		if(value >= SLIDER_VALUE_MAX) sliderValue = SLIDER_VALUE_MAX;
		else if (value <= SLIDER_VALUE_MIN) sliderValue = SLIDER_VALUE_MIN;
		else sliderValue = value;

		float t = (sliderValue - SLIDER_VALUE_MIN) / (SLIDER_VALUE_MAX - SLIDER_VALUE_MIN);
		int yPos = (int) (SLIDER_YPOS_BOTTOM + t * (SLIDER_YPOS_TOP - SLIDER_YPOS_BOTTOM));

		setSliderPos(yPos, false);
	}



}
