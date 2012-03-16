package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.AnnotatedMapDataLoadedEvent;
import gov.lbl.glamm.client.events.LoadingEvent;
import gov.lbl.glamm.client.events.MapElementClickEvent;
import gov.lbl.glamm.client.events.MapElementMouseOutEvent;
import gov.lbl.glamm.client.events.MapElementMouseOverEvent;
import gov.lbl.glamm.client.events.MapUpdateEvent;
import gov.lbl.glamm.client.model.AnnotatedMapData;
import gov.lbl.glamm.client.model.AnnotatedMapData.State;
import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.MetabolicNetwork.MNNode;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasType;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.util.Interpolator;
import gov.lbl.glamm.client.util.ReactionColor;

import java.util.HashSet;
import java.util.Set;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGEllipseElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

/**
 * Presenter for annotated map data - handles all mouse events associated with the map including pan/zoom and 
 * clicking of elements.  Also handles all styling of map elements resulting from selecting an organism, overlaying
 * experimental data, and finding routes between compounds.
 * 
 * @author jtbates
 *
 */
public class AnnotatedMapPresenter {

	private interface MapElementCommand {
		public void execute(final OMSVGElement element);
	}

	/**
	 * AnnotatedMapViews must conform to this interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		/**
		 * Gets the panel in which the SVG map is displayed.
		 * @return The panel.
		 */
		public Panel 					getMapPanel();
	}

	private static enum Mode {
		INITIAL,
		NONE,
		PAN,
		ZOOM;
	}

	private OMSVGPoint			p0;
	private OMSVGMatrix			ctm0;

	private float				scaleMin;
	private float				scaleMax;
	private GlammServiceAsync rpc;

	private SimpleEventBus 	eventBus;
	private View			view;

	private AnnotatedMapData mapData;	
	private Set<OMSVGElement> previousSearchTargets;

	private Mode mode = Mode.INITIAL;

	private Organism organism = null;
	private int dyThreshold;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public AnnotatedMapPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {

		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
		organism = Organism.globalMap();

		dyThreshold = 3;
		if(Window.Navigator.getUserAgent().contains("Chrome") || Window.Navigator.getUserAgent().contains("Safari"))
			dyThreshold = 1;

		bindView();
	}

	/**
	 * Adds MouseOverHandlers and MouseOutHandlers to every SVG element of note - those with CPD, RXN, and MAP
	 * classes. These each trigger (or turn off) their attribute states to toggle between DEFAULT and SELECTED.
	 * 
	 * Additionally, these fire MapElementMouseOver MapElementMouseOut events, fetching the appropriate data.
	 * @param group the group of DOM elements to trigger events
	 * @param tagName the name of the tag of the appropriate elements in the group
	 */
	private void addMouseHandlersToElement(final OMElement group, final String tagName) {
		final String cssClass = group.getAttribute(AnnotatedMapData.Attribute.CLASS);
		for(final OMElement child : group.getElementsByTagName(tagName)) {
			addMouseOverHandlerToElement((HasMouseOverHandlers) child, tagName);
			addMouseOutHandlerToElement((HasMouseOutHandlers) child, tagName);
			
			((HasMouseOverHandlers) child).addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					Element element = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
					int clientX = event.getClientX();
					int clientY = event.getClientY();
					String idsString = null;

					Element parentElement = element.getParentElement();
					while(parentElement != null && !parentElement.hasAttribute(AnnotatedMapData.Attribute.CLASS))
						parentElement = parentElement.getParentElement();

					if(parentElement == null)
						return; // fail silently

					if(cssClass.equals(AnnotatedMapData.ElementClass.CPD.getCssClass()))
						idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.COMPOUND);
					else if(cssClass.equals(AnnotatedMapData.ElementClass.MAP.getCssClass()))
						idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.MAP);
					else if(cssClass.equals(AnnotatedMapData.ElementClass.RXN.getCssClass()))
						idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.REACTION);
					else
						return; // fail silently

					String[] idsArray = idsString.split("\\+");
					Set<String> ids = new HashSet<String>();
					for(String id : idsArray)
						ids.add(id);

					eventBus.fireEvent(new MapElementMouseOverEvent(AnnotatedMapData.ElementClass.fromCssClass(cssClass), ids, clientX, clientY));
				}
			});
			((HasMouseOutHandlers) child).addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					Element element = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
					int clientX = event.getClientX();
					int clientY = event.getClientY();
					String idsString = null;

					Element parentElement = element.getParentElement();
					while(parentElement != null && !parentElement.hasAttribute(AnnotatedMapData.Attribute.CLASS))
						parentElement = parentElement.getParentElement();

					if(parentElement == null)
						return; // fail silently

					if(cssClass.equals(AnnotatedMapData.ElementClass.CPD.getCssClass()))
						idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.COMPOUND);
					else if(cssClass.equals(AnnotatedMapData.ElementClass.MAP.getCssClass()))
						idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.MAP);
					else if(cssClass.equals(AnnotatedMapData.ElementClass.RXN.getCssClass()))
						idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.REACTION);
					else
						return; // fail silently

					String[] idsArray = idsString.split("\\+");
					Set<String> ids = new HashSet<String>();
					for(String id : idsArray)
						ids.add(id);

					eventBus.fireEvent(new MapElementMouseOutEvent(AnnotatedMapData.ElementClass.fromCssClass(cssClass), ids, clientX, clientY));
				}
			});
		}
	}

	private void addMouseOutHandlerToElement(final HasMouseOutHandlers element, final String tagName) {
		element.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				Element element = event.getRelativeElement();
				if(element != null) {
					// get the parent group
					Element parent = element.getParentElement();
					while(!parent.hasAttribute(AnnotatedMapData.Attribute.CLASS) && parent != null)
						parent = parent.getParentElement();

					if(parent == null)
						return;

					NodeList<Element> siblings = parent.getElementsByTagName(tagName);
					for(int i = 0; i < siblings.getLength(); i++) {
						final Element sibling = siblings.getItem(i);
						if(!sibling.hasAttribute(AnnotatedMapData.Attribute.STATE) || 
								!sibling.getAttribute(AnnotatedMapData.Attribute.STATE).equals(State.SELECTED))
							sibling.setAttribute(AnnotatedMapData.Attribute.STATE, State.DEFAULT);
					}
				}
			}
		});
	}

	private void addMouseOverHandlerToElement(final HasMouseOverHandlers element, final String tagName) {
		element.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				Element element = event.getRelativeElement();
				if(element != null) {

					// get the parent group
					Element parent = element.getParentElement();
					while(!parent.hasAttribute(AnnotatedMapData.Attribute.CLASS) && parent != null)
						parent = parent.getParentElement();

					if(parent == null)
						return;

					NodeList<Element> siblings = parent.getElementsByTagName(tagName);
					//NodeList<Element> siblings = element.getParentElement().getElementsByTagName(tagName);
					for(int i = 0; i < siblings.getLength(); i++) {
						final Element sibling = siblings.getItem(i);
						if(!sibling.hasAttribute(AnnotatedMapData.Attribute.STATE) || 
								!sibling.getAttribute(AnnotatedMapData.Attribute.STATE).equals(State.SELECTED))
							sibling.setAttribute(AnnotatedMapData.Attribute.STATE, State.MOUSEOVER);
					}
				}
			}
		});
	}

	private void bindMapEvents() {

		this.mapData.getSvg().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				event.preventDefault();
				Element element = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
				String cssClass = element.getAttribute(AnnotatedMapData.Attribute.CLASS);
				int clientX = event.getClientX();
				int clientY = event.getClientY();
				String idsString = null;

				Element parentElement = element.getParentElement();
				while(parentElement != null && !parentElement.hasAttribute(AnnotatedMapData.Attribute.CLASS))
					parentElement = parentElement.getParentElement();

				if(parentElement == null)
					return; // fail silently

				if(cssClass.equals(AnnotatedMapData.ElementClass.CPD.getCssClass()))
					idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.COMPOUND);
				else if(cssClass.equals(AnnotatedMapData.ElementClass.MAP.getCssClass()))
					idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.MAP);
				else if(cssClass.equals(AnnotatedMapData.ElementClass.RXN.getCssClass()))
					idsString = parentElement.getAttribute(AnnotatedMapData.Attribute.REACTION);
				else
					return; // fail silently

				String[] idsArray = idsString.split("\\+");
				Set<String> ids = new HashSet<String>();
				for(String id : idsArray)
					ids.add(id);

				eventBus.fireEvent(new MapElementClickEvent(AnnotatedMapData.ElementClass.fromCssClass(cssClass), ids, clientX, clientY, (event.isControlKeyDown() || event.isMetaKeyDown())));

			}
		});

		// MouseOver and MouseUp events don't appear to bubble up correctly from invalid SVG (like the global map)
		// Directly add handlers to the elements we care about
		for(OMElement group : this.mapData.getSvg().getElementsByTagName(SVGConstants.SVG_G_TAG)) {
			if(group.hasAttribute(AnnotatedMapData.Attribute.CLASS)) {
				String cssClass = group.getAttribute(AnnotatedMapData.Attribute.CLASS);
				if(cssClass.equals(AnnotatedMapData.ElementClass.CPD.getCssClass()))
					addMouseHandlersToElement(group, SVGConstants.SVG_ELLIPSE_TAG);
				else if(cssClass.equals(AnnotatedMapData.ElementClass.RXN.getCssClass()))
					addMouseHandlersToElement(group, SVGConstants.SVG_PATH_TAG);
				else if(cssClass.equals(AnnotatedMapData.ElementClass.MAP.getCssClass()))
					addMouseHandlersToElement(group, SVGConstants.SVG_T_SPAN_TAG);
			}
		}
	}

	private void bindView() {

		final AnnotatedMapPresenter thePresenter = this;
		final HasDoubleClickHandlers hasDoubleClickHandlers = (HasDoubleClickHandlers) (view.getMapPanel());
		final HasAllMouseHandlers hasAllMouseHandlers = (HasAllMouseHandlers) (view.getMapPanel());

		hasAllMouseHandlers.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
				ctm0 = mapData.getViewport().getCTM();
				p0 = mapData.getSvg().createSVGPoint(event.getClientX(), event.getClientY());
				mode = Mode.PAN;
			}
		});

		hasAllMouseHandlers.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {

				event.preventDefault();
				if(mode == Mode.PAN) {
					float oldScaleInv = 1.0f / mapData.getViewport().getCTM().getA();
					OMSVGPoint p = mapData.getSvg().createSVGPoint(event.getClientX(), event.getClientY());
					OMSVGMatrix m = ctm0.translate(oldScaleInv * (p.getX() - p0.getX()), oldScaleInv * (p.getY() - p0.getY()));
					thePresenter.setTransform(mapData.getViewport(), m);
				}
			}
		});

		hasAllMouseHandlers.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				event.preventDefault();
				mode = Mode.NONE;
			}
		});	

		hasAllMouseHandlers.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				event.preventDefault();
				mode = Mode.NONE;
			}
		});

		hasAllMouseHandlers.addMouseWheelHandler(new MouseWheelHandler() {

			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				event.preventDefault();

				int dy = event.getDeltaY();

				GWT.log("dy: " + dy);

				if(Math.abs(dy) > dyThreshold) {
					float delta = dy > 0 ? 0.9f : 1.1f;
					float scale = mapData.getViewport().getCTM().getA();
					thePresenter.scaleAboutPoint(delta * scale, event.getClientX(), event.getClientY());
					mode = Mode.ZOOM;
				}
			}
		});

		hasDoubleClickHandlers.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.preventDefault();

				float scale = mapData.getViewport().getCTM().getA();
				thePresenter.scaleAboutPoint(1.5f * scale, event.getClientX(), event.getClientY());

				mode = Mode.ZOOM;
			}
		});


	}

	private void centerMapAroundElements(final Set<OMSVGElement> svgElements) {

		if(svgElements == null || svgElements.isEmpty())
			return;

		OMSVGRect bBox = null;

		// compute the bounding box for all elements in the list in absolute coordinates
		for(OMSVGElement element : svgElements) {
			OMSVGRect elementBBox = getBoundingBoxForElement(element);

			if(bBox == null)
				bBox = elementBBox;
			else if(elementBBox != null)
				bBox = getBoundingBoxUnion(bBox, elementBBox);
		}

		if(bBox != null)
			centerMapAroundRect(bBox);
	}

	private void centerMapAroundRect(final OMSVGRect rect) {

		float x = rect.getCenterX();
		float y = rect.getCenterY();

		float s = 1.0f;

		// pad by a factor of about 0.75 to frame really large objects well
		if(rect.getWidth() > rect.getHeight())
			s = 0.75f * ((float) Window.getClientWidth()) / rect.getWidth();
		else
			s = 0.75f * ((float) Window.getClientHeight()) / rect.getHeight();

		if(s < scaleMin)
			s = scaleMin;
		else if(s > scaleMax)
			s = scaleMax  * 0.75f;	// a bit of padding - the tightest zoom is *really* tight

		setTransform(mapData.getViewport(), 
				mapData.getSvg().createSVGMatrix()
				.translate(0.5f * Window.getClientWidth(), 0.5f * Window.getClientHeight())
				.scale(s)
				.translate(-x, -y));
	}

	private void executeOnMapElements(final MapElementCommand command) {
		for(OMSVGElement element : mapData.getRxnSvgElements()) 
			command.execute(element);

		for(OMSVGElement element : mapData.getCpdSvgElements())
			command.execute(element);
	}


	/**
	 * Fits the map to the window's client width, preserving the aspect ratio of the SVG file from which
	 * this map is derived.  Note that the map may be too tall for the window's height as a result.
	 */
	public void fitMapToWindow() {

		float sx = ((float) Window.getClientWidth()) / mapData.getSvgWidth();
		float sy = sx;

		float tx = 0;
		float ty = 40;

		// set up limits for scaling
		scaleMin = 0.9f * sx;
		scaleMax = 2.5f * sx;

		OMSVGMatrix m = mapData.getSvg().createSVGMatrix(sx, 0, 0, sy, tx, ty);
		setTransform(mapData.getViewport(), m);
	}

	private OMSVGRect getBoundingBoxForElement(OMSVGElement element) {
		OMSVGRect bBox = null;
		String tagName = element.getTagName();

		if(tagName.equals("path")) 
			bBox = getBoundingBoxForPath((OMSVGPathElement) element);
		else if(tagName.equals("ellipse")) 
			bBox = getBoundingBoxForEllipse((OMSVGEllipseElement) element);

		return bBox;
	}

	private OMSVGRect getBoundingBoxForEllipse(OMSVGEllipseElement element) {
		float cx = Float.parseFloat(element.getAttribute("cx"));
		float cy = Float.parseFloat(element.getAttribute("cy"));
		float rx = Float.parseFloat(element.getAttribute("rx"));
		float ry = Float.parseFloat(element.getAttribute("ry"));

		return mapData.getSvg().createSVGRect(cx - rx, cy - ry, 2 * rx, 2 * ry);
	}

	private OMSVGRect getBoundingBoxForPath(OMSVGPathElement element) {
		return element.getBBox();
	}

	private OMSVGRect getBoundingBoxUnion(OMSVGRect r0, OMSVGRect r1) {

		float minX = Math.min(r0.getX(), r1.getX());
		float minY = Math.min(r0.getY(), r1.getY());
		float maxX = Math.max(r0.getMaxX(), r1.getMaxX());
		float maxY = Math.max(r0.getMaxY(), r1.getMaxY());

		return mapData.getSvg().createSVGRect(minX, minY, maxX - minX, maxY - minY);
	}

	private OMSVGRect getViewRectNorm() {

		OMSVGMatrix ctm = mapData.getViewport().getCTM();

		if(ctm == null)
			return null;

		float s 	= ctm.getA();
		float tx 	= -ctm.getE() / s;
		float ty 	= -ctm.getF() / s;

		float rX = tx / mapData.getSvgWidth();
		float rY = ty / mapData.getSvgHeight();
		float rWidth = ((float) Window.getClientWidth()) / (s * mapData.getSvgWidth());
		float rHeight = ((float) Window.getClientHeight()) / (s * mapData.getSvgHeight());

		return mapData.getSvg().createSVGRect(rX, rY, rWidth, rHeight);
	}

	/**
	 * Gets the normalized zoom factor for this window.  This value is in the range [0,1] where 0 maps to the minimum
	 * possible zoom factor, and 1 maps to the maximum.
	 * @return The normalized zoom factor.
	 */
	public float getZoomNorm() {
		if(mapData.getViewport().getCTM() == null)
			return 0.0f;
		return (mapData.getViewport().getCTM().getA() - scaleMin) / (scaleMax - scaleMin);
	}

	/**
	 * Given an annotated map descriptor, this sends a GET request to retrieve the SVG file and fires
	 * a AnnotatedMapDataLoadedEvent when the file is received.
	 * @param descriptor The descriptor.
	 */
	public void loadMapDataFromDescriptor(final AnnotatedMapDescriptor descriptor) {
		final AnnotatedMapData mapData = new AnnotatedMapData(descriptor);
		try {
			new RequestBuilder(RequestBuilder.GET, mapData.getSvgUrl())
			.sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					mapData.setSvgRoot(OMSVGParser.parse(response.getText()));
					eventBus.fireEvent(new AnnotatedMapDataLoadedEvent(mapData));
				}
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Could not open " + 
							mapData.getDescriptor().getMapId() + 
							" at " + 
							mapData.getSvgUrl());
				}
			});
		} catch(RequestException e) {
			Window.alert(e.getMessage());
		}
	}

	private void scaleAboutPoint(float scale, final int x, final int y) {

		if(scale >= scaleMax) 
			scale = scaleMax;
		else if (scale <= scaleMin)
			scale = scaleMin;

		OMSVGMatrix ctm = mapData.getViewport().getCTM();
		float oldScaleInv = 1.0f / ctm.getA();
		OMSVGPoint p = mapData.getSvg().createSVGPoint(x, y).matrixTransform(ctm.inverse());
		OMSVGMatrix k = mapData.getSvg().createSVGMatrix().translate(p.getX(), p.getY()).scale(scale).scale(oldScaleInv).translate(-(p.getX()), -(p.getY()));
		OMSVGMatrix m = ctm.multiply(k);

		setTransform(mapData.getViewport(), m);
	}

	/**
	 * Sets the map data presented.  Binds events to the interactive elements of this map.
	 * @param mapData The map data.
	 */
	public void setMapData(final AnnotatedMapData mapData) {

		if(mapData == null)
			return;
		
		eventBus.fireEvent(new LoadingEvent(false));

		if(this.mapData != null)
			view.getMapPanel().getElement().removeChild(this.mapData.getSvg().getElement());

		this.mapData = mapData;

		// attach the svg root to the browser element of the map panel
		view.getMapPanel().getElement().appendChild(this.mapData.getSvg().getElement());

		// size the map to fit the panel
		fitMapToWindow();

		// bind map events
		bindMapEvents();

		eventBus.fireEvent(new LoadingEvent(true));

	}

	private void setTransform(final OMSVGElement element, final OMSVGMatrix matrix) {
		String transformValue	= "matrix(" + 	matrix.getA() + "," + 
		matrix.getB() + "," + 
		matrix.getC() + "," + 
		matrix.getD() + "," + 
		matrix.getE() + "," + 
		matrix.getF() + ")";
		element.setAttribute("transform", transformValue);

		OMSVGRect viewRectNorm = getViewRectNorm();
		float zoomNorm = getZoomNorm();

		if(mode != Mode.INITIAL)
			eventBus.fireEvent(new MapUpdateEvent(matrix, viewRectNorm, zoomNorm));
	}

	/**
	 * Zooms by a normalized factor about a point in client space.
	 * @param zoomNorm Normalized zoom factor in the range [0,1]
	 * @param x Client X.
	 * @param y Client Y.
	 */
	public void setZoomNormAboutPoint(final float zoomNorm, final int x, final int y) {
		float scale = scaleMin + (zoomNorm * (scaleMax - scaleMin));
		scaleAboutPoint(scale, x, y);
	}

	/**
	 * Translates the map to normalized coordinates.
	 * @param txNorm Normalized translation parameter in the range [0,1], mapping to [0,Width of SVG map]
	 * @param tyNorm Normalized translation parameter in the range [0,1], mapping to [0,Height of SVG map]
	 */
	public void translateNorm(final float txNorm, final float tyNorm) {
		float invScale = 1.0f / mapData.getViewport().getCTM().getA();
		OMSVGMatrix m = mapData.getViewport().getCTM().translate(invScale * (txNorm * mapData.getSvgWidth()), invScale * (tyNorm * mapData.getSvgHeight()));
		setTransform(mapData.getViewport(), m);
	}

	private void updateMapForCompounds(final Sample sample, final Set<? extends HasMeasurements> cpds) {
		Interpolator interpolator = Interpolator.getInterpolatorForSample(sample);

		for(HasMeasurements cpd : cpds) {
			int numMeasurements = 0;
			float mean = 0.0f;
			for(Measurement measurement : cpd.getMeasurementSet().getMeasurements()) {
				mean += measurement.getValue();
				numMeasurements++;
			}
			mean /= (float) numMeasurements;

			// calculate the css color for the mean
			String cssColor = interpolator.calcCssColor(mean);

			// set the css color on all SVG elements associated with id
			Set<OMSVGElement> elements = mapData.getSvgElements((HasType) cpd);
			if(elements == null)
				continue;
			for(OMSVGElement element : elements) {
				if(element.hasAttribute(AnnotatedMapData.Attribute.ABSENT) && 
						element.getAttribute(AnnotatedMapData.Attribute.ABSENT).equals("true"))
					continue;
				element.setAttribute(AnnotatedMapData.Attribute.HAS_DATA, "true");
				element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, cssColor);
				element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, cssColor);
			}
		}
	}

	private void updateMapForGenes(final Sample sample, final Set<? extends HasMeasurements> rxns) {

		Interpolator interpolator = Interpolator.getInterpolatorForSample(sample);

		for(HasMeasurements rxn : rxns) {

			// we only deal with Genes associated with Reactions right now
			int numMeasurements = 0;
			float mean = 0.0f;

			for(Gene gene : ((Reaction) rxn).getGenes()) {
				for(Measurement measurement : gene.getMeasurementSet().getMeasurements()) {
					mean += measurement.getValue();
					numMeasurements++;
				}
			}

			mean /= (float) numMeasurements;

			// calculate the css color for the mean
			String cssColor = interpolator.calcCssColor(mean);

			// set the css color on all SVG elements associated with id
			Set<OMSVGElement> elements = mapData.getSvgElements((HasType) rxn);
			if(elements == null)
				continue;
			for(OMSVGElement element : elements) {
				if(element.hasAttribute(AnnotatedMapData.Attribute.ABSENT) && 
						element.getAttribute(AnnotatedMapData.Attribute.ABSENT).equals("true"))
					continue;
				element.setAttribute(AnnotatedMapData.Attribute.HAS_DATA, "true");
				element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, cssColor);
			}
		}
	}

	/**
	 * Darkens the reactions for which no genes are specified in the target organism.  If a compound
	 * has no reactions connected to it, it will also be darkened.
	 * @param organism The organism.
	 */
	public void updateMapForOrganism(final Organism organism) {

		this.organism = organism;

		if(organism.isGlobalMap()) {
			executeOnMapElements(new MapElementCommand() {
				@Override
				public void execute(OMSVGElement element) {
					String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
					element.removeAttribute(AnnotatedMapData.Attribute.ABSENT);
					element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
					element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
					element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
					element.removeAttribute(AnnotatedMapData.Attribute.PATHWAY);
					element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
					if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
						element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
					}
				}
			});
			return;
		}

		eventBus.fireEvent(new LoadingEvent(false));
		rpc.getRxnsForOrganism(organism, new AsyncCallback<Set<Reaction>>() {

			@Override
			public void onFailure(Throwable caught) {
				eventBus.fireEvent(new LoadingEvent(true));
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: getRxnsForOrganism");
			}

			@Override
			public void onSuccess(Set<Reaction> result) {

				eventBus.fireEvent(new LoadingEvent(true));

				if(mapData.getDescriptor().getMetabolicNetwork() == null) {
					Window.alert("Could not find map connectivity data for " + mapData.getDescriptor().getMapId());
					return;
				}

				executeOnMapElements(new MapElementCommand() {
					@Override
					public void execute(OMSVGElement element) {
						String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
						element.setAttribute(AnnotatedMapData.Attribute.ABSENT, "true");
						element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
						element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
						element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
						element.removeAttribute(AnnotatedMapData.Attribute.PATHWAY);
						element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
						if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
							element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
							element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
							element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
						}
					}
				});

				if(result == null)
					return;

				// get all reactions from the RPC call
				for(final Reaction rxn : result) {
					Set<Xref> xrefs = rxn.getXrefSet().getXrefs();

					for(final Xref xref : xrefs) {

						String rxnId = xref.getXrefId();

						// set all svg elements corresponding with this xref to present
						Set<OMSVGElement> elements = mapData.getSvgElementsForId(rxnId);
						if(elements == null) 
							continue;

						for(OMSVGElement element : elements) 
							element.setAttribute(AnnotatedMapData.Attribute.ABSENT, "false");

						// get the compound nodes associated with this reaction and set them to present
						Set<MNNode> mnNodes = mapData.getDescriptor().getMetabolicNetwork().getNodesForRxnId(rxnId);

						if(mnNodes == null)
							continue;

						for(final MNNode mnNode : mnNodes) {

							{
								String cpdId = mnNode.getCpd0ExtId();
								Set<OMSVGElement> cpdElements = mapData.getSvgElementsForId(cpdId);
								if(cpdElements != null) {
									for(OMSVGElement cpdElement : cpdElements) {
										cpdElement.setAttribute(AnnotatedMapData.Attribute.ABSENT, "false");
									}
								}
							}

							{
								String cpdId = mnNode.getCpd1ExtId();
								Set<OMSVGElement> cpdElements = mapData.getSvgElementsForId(cpdId);
								if(cpdElements != null) {
									for(OMSVGElement cpdElement : cpdElements) {
										cpdElement.setAttribute(AnnotatedMapData.Attribute.ABSENT, "false");
									}
								}
							}
						}
					}
				}
			}
		});
	}
	
	/**
	 * Highlights the given pathways in the main map window. If an organism is selected, only those map elements
	 * that are part of the organism are highlighted.
	 * 
	 * @param organism
	 * @param pathways
	 */
	public void updateMapForPathway(final Set<String> ids) {
		/* Reset the map to the default colors if there are no map ids to highlight
		 */
		if (ids == null || ids.size() == 0) {
			executeOnMapElements(new MapElementCommand() {
				@Override
				public void execute(OMSVGElement element) {
					String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
					element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
					element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
					element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
					element.removeAttribute(AnnotatedMapData.Attribute.PATHWAY);
					element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
					if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
						element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
					}
				}
			});
			return;
		}
		
		else {
			eventBus.fireEvent(new LoadingEvent(false));



			rpc.getPathways(ids, organism, null, new AsyncCallback<Set<Pathway>>() {
				public void onFailure(Throwable caught) {
					Window.alert("Remote procedure call failure: getPathways");
				}
				
				public void onSuccess(Set<Pathway> result) {
					executeOnMapElements(new MapElementCommand() {
						@Override
						public void execute(OMSVGElement element) {
							String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
							element.removeAttribute(AnnotatedMapData.Attribute.PATHWAY);
							element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
							element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
							element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
							element.setAttribute(AnnotatedMapData.Attribute.PATHWAY, "false");
							if(organism == null || organism.isGlobalMap())
								element.setAttribute(AnnotatedMapData.Attribute.ABSENT, "false");
							element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
							if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
								element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
								element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
								element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
							}
						}
					});
					
					/**
					 * 1. get pathway associated with the mapId
					 * 2. forall reactions in that pathway:
					 *    a. find the corresponding SVG element
					 *    b. highlight it.
					 * 3. done!
					 */
					for (Pathway p : result) {
						// set routes in reaction
						for(Reaction reaction : p.getReactions()) {
							for(OMSVGElement element : mapData.getSvgElements(reaction)) {
								// If there are no genes, then this reaction is either not a part of
								// the 
								element.setAttribute(AnnotatedMapData.Attribute.PATHWAY, "true");
							}
						}
					}
				}
			});
			eventBus.fireEvent(new LoadingEvent(true));
		}
	}

	private void updateMapForReactions(final Sample sample, final Set<? extends HasMeasurements> rxns) {
		Interpolator interpolator = Interpolator.getInterpolatorForSample(sample);

		for(HasMeasurements rxn : rxns) {

			// we only deal with Genes associated with Reactions right now
			int numMeasurements = 0;
			float mean = 0.0f;


			for(Measurement measurement : rxn.getMeasurementSet().getMeasurements()) {
				mean += measurement.getValue();
				numMeasurements++;
			}

			mean /= (float) numMeasurements;

			// calculate the css color for the mean
			String cssColor = interpolator.calcCssColor(mean);

			// set the css color on all SVG elements associated with id
			Set<OMSVGElement> elements = mapData.getSvgElements((HasType) rxn);
			if(elements == null)
				continue;
			for(OMSVGElement element : elements) {
				if(element.hasAttribute(AnnotatedMapData.Attribute.ABSENT) && 
						element.getAttribute(AnnotatedMapData.Attribute.ABSENT).equals("true"))
					continue;
				element.setAttribute(AnnotatedMapData.Attribute.HAS_DATA, "true");
				element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, cssColor);
			}
		}
	}

	/**
	 * Displays route information on the map.
	 * @param cpdSrc The source compound.
	 * @param cpdDst The destination compound.
	 * @param route The route.
	 */
	public void updateMapForRoute(final Compound cpdSrc, final Compound cpdDst, final Pathway route) {

		if (cpdSrc == null && cpdDst == null && route == null) {
			executeOnMapElements(new MapElementCommand() {
				@Override
				public void execute(OMSVGElement element) {
					String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
					element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
					element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
					element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
					element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
					if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
						element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
					}
				}
			});
			return;
		}
		
		// almost like resetting the map, but we're also setting the route attribute to none
		executeOnMapElements(new MapElementCommand() {
			@Override
			public void execute(OMSVGElement element) {
				String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
				element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
				element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
				element.setAttribute(AnnotatedMapData.Attribute.ROUTE, "none");
				if(organism == null || organism.isGlobalMap())
					element.setAttribute(AnnotatedMapData.Attribute.ABSENT, "false");
				element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
				if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
					element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
					element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
					element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
				}
			}
		});

		// set source compound
		if (cpdSrc != null) {
			for(OMSVGElement element : mapData.getSvgElements(cpdSrc)) {
				element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
				element.setAttribute(AnnotatedMapData.Attribute.CPD_SRC, "true");
			}
		}

		// set destination compound
		if (cpdDst != null) {
			for(OMSVGElement element : mapData.getSvgElements(cpdDst)) {
				element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
				element.setAttribute(AnnotatedMapData.Attribute.CPD_DST, "true");
			}
		}

		// set routes in reaction
		if (route != null) {
			for(Reaction reaction : route.getReactions()) {
				for(OMSVGElement element : mapData.getSvgElements(reaction)) 
					element.setAttribute(AnnotatedMapData.Attribute.ROUTE, reaction.getReactionColor().getCssAttributeValue());
			}
			final Set<OMSVGElement> svgElements = mapData.getSvgElements(route.getReactions());
			centerMapAroundElements(svgElements);
		}

	}

	/**
	 * Centers a map around a specific reaction in a route.
	 * @param reaction The reaction.
	 */
	public void updateMapForRouteStep(final Reaction reaction) {
		final Set<OMSVGElement> svgElements = mapData.getSvgElements(reaction);
		centerMapAroundElements(svgElements);
	}

	/**
	 * Paints *omics data onto the map.  The target of the omics data is specified by the target type of the sample.
	 * Currently, data may be painted onto reactions, compounds, and genes (which map to reactions.)
	 * @param sample The sample.
	 */
	public void updateMapForSample(final Sample sample) {

		// if the sample is null, reset to default state
		if(sample == null) {
			executeOnMapElements(new MapElementCommand() {
				@Override
				public void execute(OMSVGElement element) {
					String defaultColor = element.getAttribute(AnnotatedMapData.Attribute.DEFAULT_COLOR);
					element.removeAttribute(AnnotatedMapData.Attribute.HAS_DATA);
					element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
					element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
					element.removeAttribute(AnnotatedMapData.Attribute.PATHWAY);
					element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
					if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
						element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
						element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
					}
				}
			});
			return;
		}

		eventBus.fireEvent(new LoadingEvent(false));

		// otherwise, get measurements for the given sample
		rpc.getSample(sample,
				new AsyncCallback<Set<? extends HasMeasurements>>() {

			@Override
			public void onFailure(Throwable caught) {
				eventBus.fireEvent(new LoadingEvent(true));
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: getMeasurementsForExperiment");
			}

			@Override
			public void onSuccess(
					Set<? extends HasMeasurements> result) {

				eventBus.fireEvent(new LoadingEvent(true));

				if(result == null)
					return;

				// reset the hasdata attribute
				executeOnMapElements(new MapElementCommand() {
					@Override
					public void execute(OMSVGElement element) {
						element.removeAttribute(AnnotatedMapData.Attribute.ROUTE);
						element.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
						element.removeAttribute(AnnotatedMapData.Attribute.PATHWAY);
						element.setAttribute(AnnotatedMapData.Attribute.HAS_DATA, "false");
						if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
							element.removeAttribute(AnnotatedMapData.Attribute.CPD_DST);
							element.removeAttribute(AnnotatedMapData.Attribute.CPD_SRC);
						}
					}
				});

				switch(sample.getTargetType()) {
				case GENE:
					updateMapForGenes(sample, result);
					break;
				case REACTION:
					updateMapForReactions(sample, result);
					break;
				case COMPOUND:
					updateMapForCompounds(sample, result);
					break;
				}

			}

		});
	}

	/**
	 * Centers map around a set of targets.  Paints the targets a contrasting color to make them more visible.
	 * @param targets The set of targets.
	 */
	public void updateMapForSearchTarget(final Set<HasType> targets) {
		final Set<OMSVGElement> searchTargets = mapData.getSvgElements(targets);

		if(previousSearchTargets != null) {
			for(OMSVGElement target : previousSearchTargets)
				target.removeAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET);
		}

		if(searchTargets != null) {
			for(OMSVGElement target : searchTargets)
				target.setAttribute(AnnotatedMapData.Attribute.SEARCH_TARGET, "true");
			centerMapAroundElements(searchTargets);
		}

		previousSearchTargets = searchTargets;
	}

}
