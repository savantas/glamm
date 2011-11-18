package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.AnnotatedMapDataLoadedEvent;
import gov.lbl.glamm.client.events.LoadingEvent;
import gov.lbl.glamm.client.events.MapElementClickEvent;
import gov.lbl.glamm.client.events.MapUpdateEvent;
import gov.lbl.glamm.client.model.AnnotatedMapData;
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
import gov.lbl.glamm.client.model.interfaces.Mappable;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.util.Interpolator;

import java.util.HashSet;
import java.util.List;
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
import com.google.gwt.event.dom.client.HasClickHandlers;
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

public class AnnotatedMapPresenter {


	private interface MapElementCommand {
		public void execute(final OMSVGElement element);
	}

	public interface View {
		public HasAllMouseHandlers		getAllMouseHandlers();
		public HasClickHandlers 		getClickHandlers();
		public HasDoubleClickHandlers 	getDoubleClickHandlers();
		public Panel 					getMapPanel();
	}

	private enum Mode {
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

	public AnnotatedMapPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {

		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		dyThreshold = 3;
		if(Window.Navigator.getUserAgent().contains("Chrome") || Window.Navigator.getUserAgent().contains("Safari"))
			dyThreshold = 1;

		bindView();
	}

	private void addMouseHandlersToElement(final OMElement group, final String tagName) {
		for(final OMElement child : group.getElementsByTagName(tagName)) {
			addMouseOverHandlerToElement((HasMouseOverHandlers) child, tagName);
			addMouseOutHandlerToElement((HasMouseOutHandlers) child, tagName);
		}
	}

	private void addMouseOutHandlerToElement(final HasMouseOutHandlers element, final String tagName) {
		element.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				Element element = event.getRelativeElement();
				if(element != null) {
					// get the parent group
					Element parent = element.getParentElement();
					while(!parent.hasAttribute(AnnotatedMapData.ATTRIBUTE_CLASS) && parent != null)
						parent = parent.getParentElement();

					if(parent == null)
						return;

					NodeList<Element> siblings = parent.getElementsByTagName(tagName);
					for(int i = 0; i < siblings.getLength(); i++) {
						final Element sibling = siblings.getItem(i);
						if(!sibling.hasAttribute(AnnotatedMapData.ATTRIBUTE_STATE) || 
								!sibling.getAttribute(AnnotatedMapData.ATTRIBUTE_STATE).equals(AnnotatedMapData.STATE_SELECTED))
							sibling.setAttribute(AnnotatedMapData.ATTRIBUTE_STATE, AnnotatedMapData.STATE_DEFAULT);
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
					while(!parent.hasAttribute(AnnotatedMapData.ATTRIBUTE_CLASS) && parent != null)
						parent = parent.getParentElement();

					if(parent == null)
						return;

					NodeList<Element> siblings = parent.getElementsByTagName(tagName);
					//NodeList<Element> siblings = element.getParentElement().getElementsByTagName(tagName);
					for(int i = 0; i < siblings.getLength(); i++) {
						final Element sibling = siblings.getItem(i);
						if(!sibling.hasAttribute(AnnotatedMapData.ATTRIBUTE_STATE) || 
								!sibling.getAttribute(AnnotatedMapData.ATTRIBUTE_STATE).equals(AnnotatedMapData.STATE_SELECTED))
							sibling.setAttribute(AnnotatedMapData.ATTRIBUTE_STATE, AnnotatedMapData.STATE_MOUSEOVER);
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
				String cssClass = element.getAttribute(AnnotatedMapData.ATTRIBUTE_CLASS);
				int clientX = event.getClientX();
				int clientY = event.getClientY();
				String idsString = null;

				Element parentElement = element.getParentElement();
				while(parentElement != null && !parentElement.hasAttribute(AnnotatedMapData.ATTRIBUTE_CLASS))
					parentElement = parentElement.getParentElement();

				if(parentElement == null)
					return; // fail silently

				if(cssClass.equals(AnnotatedMapData.ElementClass.CPD.getCssClass()))
					idsString = parentElement.getAttribute(AnnotatedMapData.ATTRIBUTE_COMPOUND);
				else if(cssClass.equals(AnnotatedMapData.ElementClass.MAP.getCssClass()))
					idsString = parentElement.getAttribute(AnnotatedMapData.ATTRIBUTE_MAP);
				else if(cssClass.equals(AnnotatedMapData.ElementClass.RXN.getCssClass()))
					idsString = parentElement.getAttribute(AnnotatedMapData.ATTRIBUTE_REACTION);
				else
					return; // fail silently

				String[] idsArray = idsString.split("\\+");
				Set<String> ids = new HashSet<String>();
				for(String id : idsArray)
					ids.add(id);

				eventBus.fireEvent(new MapElementClickEvent(AnnotatedMapData.ElementClass.fromCssClass(cssClass), ids, clientX, clientY));

			}
		});

		// MouseOver and MouseUp events don't appear to bubble up correctly from invalid SVG (like the global map)
		// Directly add handlers to the elements we care about
		for(OMElement group : this.mapData.getSvg().getElementsByTagName(SVGConstants.SVG_G_TAG)) {
			if(group.hasAttribute(AnnotatedMapData.ATTRIBUTE_CLASS)) {
				String cssClass = group.getAttribute(AnnotatedMapData.ATTRIBUTE_CLASS);
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
		final HasDoubleClickHandlers hasDoubleClickHandlers = view.getDoubleClickHandlers();
		final HasAllMouseHandlers hasAllMouseHandlers = view.getAllMouseHandlers();

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



	public void fitMapToPanel() {

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

	public float getZoomNorm() {
		if(mapData.getViewport().getCTM() == null)
			return 0.0f;
		return (mapData.getViewport().getCTM().getA() - scaleMin) / (scaleMax - scaleMin);
	}

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

	private void overlayDataForGene(final Gene gene, final Interpolator interpolator) {
		Set<Measurement> measurements = gene.getMeasurements();
		Set<String> ecNums = gene.getEcNums();

		if(measurements == null || ecNums == null)
			return;

		float value = 0;
		for(Measurement measurement : measurements)
			value += measurement.getValue();
		value /= (float) (measurements.size());

		String cssColor = interpolator.calcCssColor(value);

		for(String ecNum : ecNums) {
			Set<OMSVGElement> elements = mapData.getSvgElementsForId(ecNum);
			if(elements == null)
				continue;
			for(OMSVGElement element : elements) {
				if(element.hasAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT) && 
						element.getAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT).equals("true"))
					continue;
				element.setAttribute(AnnotatedMapData.ATTRIBUTE_HAS_DATA, "true");
				element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, cssColor);
			}
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
		fitMapToPanel();

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

	public void setZoomNormAboutPoint(final float zoomNorm, final int x, final int y) {
		float scale = scaleMin + (zoomNorm * (scaleMax - scaleMin));
		scaleAboutPoint(scale, x, y);
	}

	public void translateNorm(final float txNorm, final float tyNorm) {
		float invScale = 1.0f / mapData.getViewport().getCTM().getA();
		OMSVGMatrix m = mapData.getViewport().getCTM().translate(invScale * (txNorm * mapData.getSvgWidth()), invScale * (tyNorm * mapData.getSvgHeight()));
		setTransform(mapData.getViewport(), m);
	}

	public void updateMapForOrganism(final Organism organism) {

		this.organism = organism;

		if(organism.isGlobalMap()) {
			executeOnMapElements(new MapElementCommand() {
				@Override
				public void execute(OMSVGElement element) {
					String defaultColor = element.getAttribute(AnnotatedMapData.ATTRIBUTE_DEFAULT_COLOR);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_HAS_DATA);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE);
					element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
					if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
						element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_DST);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_SRC);
					}
				}
			});
			return;
		}

		eventBus.fireEvent(new LoadingEvent(false));
		rpc.getRxnsForOrganism(organism.getTaxonomyId(), new AsyncCallback<Set<Reaction>>() {

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
						String defaultColor = element.getAttribute(AnnotatedMapData.ATTRIBUTE_DEFAULT_COLOR);
						element.setAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT, "true");
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_HAS_DATA);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE);
						element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
						if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
							element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
							element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_DST);
							element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_SRC);
						}
					}
				});

				if(result == null)
					return;

				// get all reactions from the RPC call
				for(final Reaction rxn : result) {
					Set<Xref> xrefs = rxn.getXrefs();

					for(final Xref xref : xrefs) {

						String rxnId = xref.getXrefId();

						// set all svg elements corresponding with this xref to present
						Set<OMSVGElement> elements = mapData.getSvgElementsForId(rxnId);
						if(elements == null) 
							continue;

						for(OMSVGElement element : elements) 
							element.setAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT, "false");

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
										cpdElement.setAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT, "false");
									}
								}
							}

							{
								String cpdId = mnNode.getCpd1ExtId();
								Set<OMSVGElement> cpdElements = mapData.getSvgElementsForId(cpdId);
								if(cpdElements != null) {
									for(OMSVGElement cpdElement : cpdElements) {
										cpdElement.setAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT, "false");
									}
								}
							}
						}
					}
				}
			}
		});
	}

	public void updateMapForRoute(final Compound cpdSrc, final Compound cpdDst, final Pathway route) {

		// almost like resetting the map, but we're also setting the route attribute to none
		executeOnMapElements(new MapElementCommand() {
			@Override
			public void execute(OMSVGElement element) {
				String defaultColor = element.getAttribute(AnnotatedMapData.ATTRIBUTE_DEFAULT_COLOR);
				element.removeAttribute(AnnotatedMapData.ATTRIBUTE_HAS_DATA);
				element.removeAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET);
				element.setAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE, "none");
				if(organism == null || organism.isGlobalMap())
					element.setAttribute(AnnotatedMapData.ATTRIBUTE_ABSENT, "false");
				element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
				if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
					element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_DST);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_SRC);
				}
			}
		});

		// set source compound
		for(OMSVGElement element : mapData.getSvgElements(cpdSrc)) {
			element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE);
			element.setAttribute(AnnotatedMapData.ATTRIBUTE_CPD_SRC, "true");
		}

		// set destination compound
		for(OMSVGElement element : mapData.getSvgElements(cpdDst)) {
			element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE);
			element.setAttribute(AnnotatedMapData.ATTRIBUTE_CPD_DST, "true");
		}


		// set routes in reaction
		for(Reaction reaction : route.getReactions()) {
			for(OMSVGElement element : mapData.getSvgElements(reaction)) 
				element.setAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE, reaction.getReactionColor().getCssAttributeValue());
		}

		final Set<OMSVGElement> svgElements = mapData.getSvgElements(route.getReactions());
		centerMapAroundElements(svgElements);
	}

	public void updateMapForRouteStep(final Reaction reaction) {
		final Set<OMSVGElement> svgElements = mapData.getSvgElements(reaction);
		centerMapAroundElements(svgElements);
	}

	public void updateMapForSample(final Sample sample) {

		// if the sample is null, reset to default state
		if(sample == null) {
			executeOnMapElements(new MapElementCommand() {
				@Override
				public void execute(OMSVGElement element) {
					String defaultColor = element.getAttribute(AnnotatedMapData.ATTRIBUTE_DEFAULT_COLOR);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_HAS_DATA);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET);
					element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE);
					element.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, defaultColor);
					if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
						element.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, defaultColor);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_DST);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_SRC);
					}
				}
			});
			return;
		}

		eventBus.fireEvent(new LoadingEvent(false));

		// otherwise, get measurements for the given sample
		rpc.getSample(sample.getExperimentId(), 
				sample.getSampleId(), 
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
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_ROUTE);
						element.removeAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET);
						element.setAttribute(AnnotatedMapData.ATTRIBUTE_HAS_DATA, "false");
						if(element.getTagName().equals(SVGConstants.SVG_ELLIPSE_TAG)) {
							element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_DST);
							element.removeAttribute(AnnotatedMapData.ATTRIBUTE_CPD_SRC);
						}
					}
				});

				Interpolator interpolator = Interpolator.getInterpolatorForSample(sample);

				for(HasMeasurements primitive : result) {
					if(((HasType) primitive).getType() == Gene.TYPE)
						overlayDataForGene((Gene) primitive, interpolator);
				}
			}

		});
	}

	public void updateMapForSearchTarget(final Set<Mappable> targets) {
		final Set<OMSVGElement> searchTargets = mapData.getSvgElements(targets);

		if(previousSearchTargets != null) {
			for(OMSVGElement target : previousSearchTargets)
				target.removeAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET);
		}

		if(searchTargets != null) {
			for(OMSVGElement target : searchTargets)
				target.setAttribute(AnnotatedMapData.ATTRIBUTE_SEARCH_TARGET, "true");
			centerMapAroundElements(searchTargets);
		}

		previousSearchTargets = searchTargets;
	}

}
