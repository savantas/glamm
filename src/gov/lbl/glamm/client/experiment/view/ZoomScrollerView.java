package gov.lbl.glamm.client.experiment.view;

import gov.lbl.glamm.client.experiment.events.GenericScrollEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.presenter.ZoomScrollerPresenter;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.client.experiment.util.SVGUtil;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTransform;
import org.vectomatic.dom.svg.utils.DOMHelper;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * In the implementation, "alpha" indicates a direction to the "start" of the tool area.
 * For example, to the top in a vertically oriented tool
 * or to the left in a horizontally oriented tool.
 * Conversely, "omega" indicates a direction to the other "end" of the tool area.
 * In names, coordinates are by default meant in the lengthwise direction.
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class ZoomScrollerView extends FocusPanel
		implements ZoomScrollerPresenter.View {
	public static final float MIN_TOOL_LENGTH_RATIO_INITIAL_RATIO = 0.85f;
	/** Initially, the control tool starts at the "top" of the landscape */
	public static final float INITIAL_TOOL_ALPHA_COORD_RATIO = 0f;

	/** Initially, the landscape area length covers the entire control area length */
	public static final float INITIAL_LANDSCAPE_LENGTH_RATIO = 1f;

	public enum Orientation {
		VERTICAL(0)				// top-to-bottom
		, HORIZONTAL(-90)		// left-to-right
		;

		private float rotation = 0;
		private Orientation( float rotation ) {
			this.rotation = rotation;
		}
		public float getRotation() {
			return this.rotation;
		}
	}

	protected Orientation orientation = Orientation.VERTICAL;
	protected boolean toolLengthChangeable = true;
	protected DrawUtil drawUtil = null;

	protected float widgetViewableThickness = 0;
	protected float widgetViewableLength = 0;

	// control area
	protected float controlAreaPadding = 2.5f;
	protected float controlAreaThickness = 20;
	protected float halfControlAreaThickness = 0.5f*controlAreaThickness;
	protected float controlAreaAlphaCoord = 0;
	protected float controlAreaOmegaCoord = 0;
	// landscape area
	/** The landscape refers to the colored background against which
	 * the control tool calculations are made.
	 * The landscapeLengthRatio is the proportion of the length of the
	 * control area that the landscape takes up.
	 */
	protected float landscapeLengthRatio = INITIAL_LANDSCAPE_LENGTH_RATIO;
	// control tool; ratios are relative to the landscape length
	protected float controlToolAlphaCoordRatio = 0;
	protected float controlToolLengthRatio = 0;
	/** initialControlToolLengthRatio is the piece of data 
	 * that all "zoom" calculations are made from */
	protected float initialControlToolLengthRatio = 0;

	protected float[] landscapeSegRatios = null;
	protected String[] landscapeSegCssColors = null;
	protected String controlToolCssColor = SVGConstants.CSS_WHITE_VALUE;

	protected SVGData svgData = null;
	protected OMSVGRectElement[] landscapeSegments = null;
	protected Element svgElement = null;
	protected OMSVGGElement toolSVGGElement = null;
	protected OMSVGLineElement toolLeftLine = null;
	protected OMSVGLineElement toolRightLine = null;
	protected OMSVGGElement toolAlphaDragElement = null;
	protected OMSVGGElement toolOmegaDragElement = null;
	protected OMSVGRectElement toolAreaElement = null;

	protected float dragElementLength = 6;
	protected float halfDragElementLength = 0.5f*dragElementLength;

	/* mouse handling */
	protected boolean isToolDragging = false;
	protected OMSVGPoint mouseDragPointNought = null;
	protected float dragElementCoordNought = 0;

	/* communication with presenter */
	protected HandlerManager handlerManager = null;

	protected SVGData noDataSvg = null;

	/**
	 * TODO: make orientation a function of rotation
	 * if need "slanted" or "upside-down" orientations.
	 * 
	 * @param orientation
	 * @param toolLengthChangeable whether the tool ends can be dragged to expand or contract the length
	 * @param drawUtil
	 * @param widgetViewableWidth
	 * @param widgetViewableHeight
	 */
	public ZoomScrollerView( Orientation orientation, boolean toolLengthChangeable
			, DrawUtil drawUtil
			, float widgetViewableWidth
			, float widgetViewableHeight
	) {
		super();
		this.orientation = orientation;
		this.toolLengthChangeable = toolLengthChangeable;
		this.drawUtil = drawUtil;

		this.setWidgetViewHeight(widgetViewableHeight);
		this.setWidgetViewWidth(widgetViewableWidth);
		this.setAreaThicknessFromViewableThickness();
		this.reDraw();

		this.handlerManager = new HandlerManager(this);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	public void reDraw() {
		controlAreaAlphaCoord = controlAreaPadding;
		controlAreaOmegaCoord = widgetViewableLength 
				- controlAreaPadding;
		if ( this.landscapeSegRatios == null ) {
			noDataSvg = createNoDataControlAreaSvg();
			svgData = noDataSvg;
		} else {
			// TODO: distinguish between cases when svg needs to be recreated entirely
			//	and when only length adjustments need to be made
			//	- investigate performance impacts of both cases
			controlToolLengthRatio = initialControlToolLengthRatio;
			if ( controlToolLengthRatio > 1 ) {
				setLandscapeLengthRatio(1/controlToolLengthRatio);
			}
			svgData = createControlAreaSvg();
			createAddControlTool(svgData.getViewport());
//			Window.alert("Finished creating ZoomScrollerView SVG");
		}
		replaceSvg(svgData.getSvg());
	}

	protected void addSvg() {
		svgElement = this.getElement().appendChild(svgData.getSvg().getElement());
	}

	protected void replaceSvg(OMSVGSVGElement svg) {
		if ( svgElement != null ) {
			this.getElement().removeChild(svgElement);
		}
		this.addSvg();
	}

	// SVG creation methods
	protected SVGData createNoDataControlAreaSvg() {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		OMSVGGElement viewGroup = doc.createSVGGElement();
		svg.appendChild(viewGroup);

		OMSVGRectElement rect = doc.createSVGRectElement(
				controlAreaPadding, controlAreaAlphaCoord
				, controlAreaThickness, getControlAreaLength()
				, 1, 1);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_LIGHTGREY_VALUE);

		viewGroup.appendChild(rect);

		SVGData newSVGData = new SVGData( svg, viewGroup, 0, 0 );
		applyOrientation(newSVGData);
		return newSVGData;
	}

	protected SVGData createControlAreaSvg() {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		OMSVGGElement viewGroup = doc.createSVGGElement();
		svg.appendChild(viewGroup);

		landscapeSegments = new OMSVGRectElement[landscapeSegRatios.length];
		float segmentAlphaCoord = controlAreaAlphaCoord;
		for ( int i=0; i< landscapeSegRatios.length; i++ ) {
			float ratio = landscapeSegRatios[i];
			String cssColor = landscapeSegCssColors[i];
			float ratioLength =
				getControlAreaLength() * landscapeLengthRatio * ratio;
			landscapeSegments[i] = doc.createSVGRectElement(
					controlAreaPadding, segmentAlphaCoord
					, controlAreaThickness, ratioLength
					, 1, 1);
			landscapeSegments[i].getStyle().setSVGProperty(
					SVGConstants.CSS_FILL_PROPERTY, cssColor );
			viewGroup.appendChild(landscapeSegments[i]);
			segmentAlphaCoord += ratioLength;
		}

		SVGData newSVGData = new SVGData( svg, viewGroup, 0, 0 );
		applyOrientation(newSVGData);
		return newSVGData;
	}

	protected void createAddControlTool( OMSVGGElement viewGroup ) {
		float controlToolLength = getControlToolLength();

		OMSVGDocument doc = OMSVGParser.currentDocument();

		toolSVGGElement = doc.createSVGGElement();
		toolSVGGElement.setId("controlTool");

		toolAreaElement = doc.createSVGRectElement( -halfControlAreaThickness, 0
				, controlAreaThickness, controlToolLength, 0, 0);
		toolAreaElement.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY
				, controlToolCssColor );
		toolAreaElement.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0.35");
		toolAreaElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
				, SVGConstants.CSS_MOVE_VALUE);
		toolSVGGElement.appendChild(toolAreaElement);

		toolLeftLine = doc.createSVGLineElement(
				-halfControlAreaThickness, 0
				, -halfControlAreaThickness, controlToolLength );
		toolLeftLine.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		toolLeftLine.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
		toolSVGGElement.appendChild(toolLeftLine);
		toolRightLine = doc.createSVGLineElement(
				halfControlAreaThickness, 0
				, halfControlAreaThickness, controlToolLength );
		toolRightLine.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		toolRightLine.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
		toolSVGGElement.appendChild(toolRightLine);

		ObjectCount dummyObjCount = new ObjectCount();
		toolAlphaDragElement = SVGUtil.createTranslatedSVGGroup(
				svgData.getSvg(), doc
				, null, null, 0, 0
				, dummyObjCount);
		OMSVGRectElement alphaDragRect = doc.createSVGRectElement(
				-halfControlAreaThickness, -halfDragElementLength
				, controlAreaThickness, dragElementLength, 0, 0 );
		alphaDragRect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		alphaDragRect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		alphaDragRect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
		toolAlphaDragElement.appendChild(alphaDragRect);
		toolSVGGElement.appendChild(toolAlphaDragElement);

		toolOmegaDragElement = SVGUtil.createTranslatedSVGGroup(
				svgData.getSvg(), doc
				, null, null, 0, controlToolLength - halfDragElementLength
				, dummyObjCount);
		OMSVGRectElement omegaDragRect = doc.createSVGRectElement(
				-halfControlAreaThickness, -halfDragElementLength
				, controlAreaThickness, dragElementLength, 0, 0 );
		omegaDragRect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		omegaDragRect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		omegaDragRect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
		toolOmegaDragElement.appendChild(omegaDragRect);
		toolSVGGElement.appendChild(toolOmegaDragElement);

		if ( toolLengthChangeable ) {
			// alpha and omega drag element mouse indicators
			setDragElementsCursors();

			// drag elements pattern
			for ( float cx=(-halfControlAreaThickness+2.40f)
					; cx < halfControlAreaThickness; cx+=3 ) {
				OMSVGCircleElement dot = doc.createSVGCircleElement(
						cx, -halfDragElementLength+2, 0.65f );
				dot.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
				toolAlphaDragElement.appendChild(dot);
				dot = doc.createSVGCircleElement( cx, -halfDragElementLength+3.8f
						, 0.65f );
				dot.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
				toolAlphaDragElement.appendChild(dot);

				// omega drag element pattern
				dot = doc.createSVGCircleElement( cx, halfDragElementLength-3.8f
						, 0.65f );
				dot.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
				toolOmegaDragElement.appendChild(dot);
				dot = doc.createSVGCircleElement( cx, halfDragElementLength-2, 0.65f );
				dot.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
				toolOmegaDragElement.appendChild(dot);
			}
		}

		this.setControlToolAlphaCoordRatio(INITIAL_TOOL_ALPHA_COORD_RATIO);

		this.addToolAreaMouseHandlers();
		if ( toolLengthChangeable ) {
		this.addDragElementsMouseHandlers();
		}

		viewGroup.appendChild(toolSVGGElement);
	}

	// Orientation-related methods
	/**
	 * Note that whether the widget's viewable height is used
	 * for the lengthwise or thickness component of the tool 
	 * depends on the orientation.
	 * 
	 * @param widgetViewHeight
	 */
	public void setWidgetViewHeight( float widgetViewHeight ) {
		if ( Orientation.HORIZONTAL == orientation ) {
			this.widgetViewableThickness = widgetViewHeight;
		} else {
			this.widgetViewableLength = widgetViewHeight;
		}
	}

	/**
	 * Note that whether the widget's viewable width is used
	 * for the lengthwise or thickness component of the tool 
	 * depends on the orientation.
	 * 
	 * @param widgetViewWidth
	 */
	public void setWidgetViewWidth( float widgetViewWidth ) {
		if ( Orientation.HORIZONTAL == orientation ) {
			this.widgetViewableLength = widgetViewWidth;
		} else {
			this.widgetViewableThickness = widgetViewWidth;
		}
	}

	/** 
	 * @param svgData
	 */
	protected void applyOrientation( SVGData svgData ) {
		OMSVGSVGElement svg = svgData.getSvg();
		OMSVGGElement viewGroup = svgData.getViewport();
		if ( Orientation.VERTICAL != orientation ) {
			float rotationCenterOffset = controlAreaPadding
					+ halfControlAreaThickness;
			OMSVGTransform rotate = svg.createSVGTransform();
			rotate.setRotate( orientation.getRotation()
					, rotationCenterOffset, rotationCenterOffset );
			viewGroup.getTransform().getBaseVal().appendItem(rotate);
		}
	}

	/**
	 * Warning: simple behavior; meant to be used only within
	 * {@link ZoomScrollerView.setDragElementsCursors() }
	 * @param toolDragElement
	 */
	protected void setDragToolAlphaBoundCursor( OMSVGGElement toolDragElement ) {
		switch ( orientation ) {
		case HORIZONTAL:
				toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
						, SVGConstants.CSS_E_RESIZE_VALUE);
				break;
		default:
				toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
						, SVGConstants.CSS_S_RESIZE_VALUE);
				break;
		}
	}

	/**
	 * Warning: simple behavior; meant to be used only within
	 * {@link ZoomScrollerView.setDragElementsCursors() }
	 * @param toolDragElement
	 */
	protected void setDragToolOmegaBoundCursor( OMSVGGElement toolDragElement ) {
			if ( Orientation.HORIZONTAL == orientation ) {
				toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
						, SVGConstants.CSS_W_RESIZE_VALUE);
			} else {
				toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
						, SVGConstants.CSS_N_RESIZE_VALUE);
			}
	}

	/**
	 * Warning: simple behavior; meant to be used only within
	 * {@link ZoomScrollerView.setDragElementsCursors() }
	 * @param toolDragElement
	 */
	protected void setDragToolBidirectionCursor( OMSVGGElement toolDragElement ) {
			if ( Orientation.HORIZONTAL == orientation ) {
				toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
						, "col-resize");
			} else {
				toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
						, "row-resize");
			}
	}

	/** 
	 * Warning: simple behavior; meant to be used only within
	 * {@link ZoomScrollerView.setDragElementsCursors() }
	 * @param toolDragElement
	 */
	protected void setDragToolNoDirectionCursor( OMSVGGElement toolDragElement ) {
		toolDragElement.getStyle().setSVGProperty(SVGConstants.CSS_CURSOR_PROPERTY
				, SVGConstants.CSS_DEFAULT_VALUE);
	}

	protected void setDragElementsCursors() {
		if ( !toolLengthChangeable ) {
			return;
		}

		boolean alphaDragAtAlphaBound
			= ( controlToolAlphaCoordRatio == getAlphaDragElemRatioAlphaBound() );
		float alphaRatioBoundDiff = controlToolAlphaCoordRatio
				- getAlphaDragElemRatioOmegaBound();
		boolean atMinZoom
			= ( alphaRatioBoundDiff > -0.00000001 )
			&& ( alphaRatioBoundDiff < 0.00000001 );
		boolean omegaDragAtOmegaBound
			= ( getControlToolOmegaCoordRatio() == getOmegaDragElemRatioOmegaBound() );

		if ( atMinZoom ) {
			// alpha drag element cursor
			if ( alphaDragAtAlphaBound ) {
				setDragToolNoDirectionCursor(toolAlphaDragElement);
			} else {
				setDragToolOmegaBoundCursor(toolAlphaDragElement);
			}

			// omega drag element cursor
			if ( omegaDragAtOmegaBound ) {
				setDragToolNoDirectionCursor(toolOmegaDragElement);
			} else {
				setDragToolAlphaBoundCursor(toolOmegaDragElement);
			}
		} else {
			// alpha drag element cursor
			if ( alphaDragAtAlphaBound ) {
				setDragToolAlphaBoundCursor(toolAlphaDragElement);
			} else {
				setDragToolBidirectionCursor(toolAlphaDragElement);
			}

			// omega drag element cursor
			if ( omegaDragAtOmegaBound ) {
				setDragToolOmegaBoundCursor(toolOmegaDragElement);
			} else {
				setDragToolBidirectionCursor(toolOmegaDragElement);
			}
		}
	}

	// Calculation methods and associated setters/getters
	protected void setLandscapeLengthRatio( float landscapeLengthRatio ) {
		this.landscapeLengthRatio = landscapeLengthRatio;

		if ( landscapeSegments != null ) {
		float segmentAlphaCoord = controlAreaAlphaCoord;
		for ( int i=0; i< landscapeSegments.length; i++ ) {
			float ratio = landscapeSegRatios[i];
			float ratioLength =
				getControlAreaLength() * this.landscapeLengthRatio * ratio;
			landscapeSegments[i].getHeight().getBaseVal().setValue(ratioLength);
			landscapeSegments[i].getY().getBaseVal().setValue(segmentAlphaCoord);
			segmentAlphaCoord += ratioLength;
		}
		}
	}

	/**
	 * Convert an absolute lengthwise coordinate value
	 * to one in terms of the control tool.
	 * Returns a ratio relative to the landscape.
	 * 
	 * @param newToolAlphaCoordRatio
	 * @return
	 */
	protected float getToolRatioFromCoord( float coord ) {
		return (coord - controlAreaAlphaCoord)/(landscapeLengthRatio*getControlAreaLength());
	}

	protected float getControlAreaLength() {
		return controlAreaOmegaCoord - controlAreaAlphaCoord;
	}

	public void setControlToolLengthRatio(float ratio, boolean fireEvent) {
		controlToolLengthRatio = ratio;
		float controlToolLength = getControlToolLength();
		toolAreaElement.getHeight().getBaseVal().setValue(controlToolLength);
		toolOmegaDragElement.getTransform().getBaseVal().clear();
		SVGUtil.translate(svgData.getSvg(), toolOmegaDragElement, 0, controlToolLength);
		toolLeftLine.getY2().getBaseVal().setValue(controlToolLength);
		toolRightLine.getY2().getBaseVal().setValue(controlToolLength);
		if ( fireEvent ) {
			fireEvent( new PathwayZoomEvent(
					initialControlToolLengthRatio/controlToolLengthRatio ) );
		}
	}

	protected float getControlToolAlphaCoord() {
		return ( controlToolAlphaCoordRatio * landscapeLengthRatio
					* getControlAreaLength()
				)
				+ controlAreaAlphaCoord;
	}

	public void setControlToolAlphaCoordRatio( float caRatio ) {
		this.controlToolAlphaCoordRatio = caRatio;

		toolSVGGElement.getTransform().getBaseVal().clear();
		SVGUtil.translate( svgData.getSvg(), toolSVGGElement
				, halfControlAreaThickness + controlAreaPadding
				, getControlToolAlphaCoord() );
		fireEvent( new GenericScrollEvent( this.controlToolAlphaCoordRatio ) );
	}

	protected float getAlphaDragElemRatioAlphaBound() {
		return 0;
	}
	protected float getAlphaDragElemRatioOmegaBound() {
		return getControlToolOmegaCoordRatio()
		- initialControlToolLengthRatio*MIN_TOOL_LENGTH_RATIO_INITIAL_RATIO;
	}
	protected float getAlphaDragElemRatioScrollOmegaBound() {
		float bound = 0;
		bound = 1 - controlToolLengthRatio;
		if ( bound < 0 ) {
			bound = 0;
		}
		return bound;
	}

	protected float getControlToolOmegaCoordRatio() {
		return controlToolAlphaCoordRatio + controlToolLengthRatio;
	}

	protected float getControlToolOmegaCoord() {
		return ( (controlToolAlphaCoordRatio + controlToolLengthRatio)
					* landscapeLengthRatio * getControlAreaLength()
				)
				+ controlAreaAlphaCoord;
	}

	protected float getOmegaDragElemRatioAlphaBound() {
		return controlToolAlphaCoordRatio
		+ initialControlToolLengthRatio*MIN_TOOL_LENGTH_RATIO_INITIAL_RATIO;
	}
	protected float getOmegaDragElemRatioOmegaBound() {
		return 1;
	}

	protected float getControlToolLength() {
		return controlToolLengthRatio*landscapeLengthRatio*getControlAreaLength();
	}

	@Override
	public void setInitialToolLengthRatio(float initialToolLengthRatio) {
		this.initialControlToolLengthRatio = initialToolLengthRatio;
	}

	public void setControlAreaThickness( float thickness ) {
		controlAreaThickness = thickness;
		halfControlAreaThickness = 0.5f*controlAreaThickness;
	}

	public void setAreaThicknessFromViewableThickness() {
		setControlAreaThickness( widgetViewableThickness - 2*controlAreaPadding );
	}

	// Color settings
	@Override
	public void setTargetSegmentRatiosColors(float[] ratios, String[] cssColors) {
		this.landscapeSegRatios = ratios;
		this.landscapeSegCssColors = cssColors;
	}

	@Override
	public void setControlToolColor( String color ) {
		this.controlToolCssColor = color;
		if ( toolAreaElement != null ) {
			toolAreaElement.getStyle().setSVGProperty( SVGConstants.CSS_FILL_PROPERTY
					, controlToolCssColor );
		}
	}

	// Mouse handlers
	public void addToolAreaMouseHandlers() {
		this.toolAreaElement.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				isToolDragging = true;
				mouseDragPointNought = getToolMouseDragPoint(event);
				dragElementCoordNought = getControlToolAlphaCoord();
				DOMHelper.setCaptureElement( toolAreaElement, null );
				event.stopPropagation();
				event.preventDefault();
			}
		});

		this.toolAreaElement.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if ( isToolDragging ) {
					float newToolAlphaCoordRatio = getToolMouseDragLengthRatio(event);
					if ( newToolAlphaCoordRatio
							< getAlphaDragElemRatioAlphaBound() ) {
						newToolAlphaCoordRatio = getAlphaDragElemRatioAlphaBound();
					} else if ( newToolAlphaCoordRatio
							> getAlphaDragElemRatioScrollOmegaBound() ) {
						newToolAlphaCoordRatio = getAlphaDragElemRatioScrollOmegaBound();
					}
					setControlToolAlphaCoordRatio( newToolAlphaCoordRatio );
					setDragElementsCursors();
				}
				event.stopPropagation();
				event.preventDefault();
			}
		});
		this.toolAreaElement.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				isToolDragging = false;
				DOMHelper.releaseCaptureElement();
				event.stopPropagation();
				event.preventDefault();
			}
		});
	}

	public void addDragElementsMouseHandlers() {
		// omega end drag element
		this.toolOmegaDragElement.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				isToolDragging = true;
				mouseDragPointNought = getToolMouseDragPoint(event);
				dragElementCoordNought = getControlToolOmegaCoord();
				DOMHelper.setCaptureElement( toolOmegaDragElement, null );
				event.stopPropagation();
				event.preventDefault();
			}
		});

		this.toolOmegaDragElement.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if ( isToolDragging ) {
					float newToolCoordRatio = getToolMouseDragLengthRatio(event);
					if ( newToolCoordRatio < getOmegaDragElemRatioAlphaBound() ) {
						newToolCoordRatio = getOmegaDragElemRatioAlphaBound();
					} else if ( newToolCoordRatio > getOmegaDragElemRatioOmegaBound() ) {
						newToolCoordRatio = getOmegaDragElemRatioOmegaBound();
					}
					setControlToolLengthRatio( 
							newToolCoordRatio - controlToolAlphaCoordRatio
							, true );
					setDragElementsCursors();
				}
				event.stopPropagation();
				event.preventDefault();
			}
		});
		this.toolOmegaDragElement.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				isToolDragging = false;
				DOMHelper.releaseCaptureElement();
				event.stopPropagation();
				event.preventDefault();
			}
		});

		// alpha end drag element
		this.toolAlphaDragElement.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				isToolDragging = true;
				mouseDragPointNought = getToolMouseDragPoint(event);
				dragElementCoordNought = getControlToolAlphaCoord();
				DOMHelper.setCaptureElement( toolAlphaDragElement, null );
				event.stopPropagation();
				event.preventDefault();
			}
		});

		this.toolAlphaDragElement.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if ( isToolDragging ) {
					float newToolAlphaCoordRatio = getToolMouseDragLengthRatio(event);
					if ( newToolAlphaCoordRatio < getAlphaDragElemRatioAlphaBound() ) {
						newToolAlphaCoordRatio = getAlphaDragElemRatioAlphaBound();
					} else if ( newToolAlphaCoordRatio
							> getAlphaDragElemRatioOmegaBound() ) {
						newToolAlphaCoordRatio = getAlphaDragElemRatioOmegaBound();
					}
					// Note: order of setting control tool length
					//	and alpha coord ratios is important!
					setControlToolLengthRatio( 
							getControlToolOmegaCoordRatio() - newToolAlphaCoordRatio
							, true );
					setControlToolAlphaCoordRatio(newToolAlphaCoordRatio);
					setDragElementsCursors();
				}
				event.stopPropagation();
				event.preventDefault();
			}
		});
		this.toolAlphaDragElement.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				isToolDragging = false;
				DOMHelper.releaseCaptureElement();
				event.stopPropagation();
				event.preventDefault();
			}
		});
	}

	/**
	 * From example: http://www.vectomatic.org/gwt/lib-gwt-svg-samples/lib-gwt-svg-samples.html
	 * Returns the coordinates of a mouse event, converted
     * to the SVG coordinate system
     * 
     * @param e
     * A mouse event
     * @return
     * The coordinates of the mouse event, converted
     * to the SVG coordinate system
	 */
	@SuppressWarnings("rawtypes")
	protected OMSVGPoint getToolMouseDragPoint( MouseEvent e ) {
		OMSVGPoint p = svgData.getSvg().createSVGPoint( e.getClientX(), e.getClientY() );
		OMSVGMatrix m = svgData.getSvg().getScreenCTM().inverse();
		return p.matrixTransform(m);
	}

	@SuppressWarnings("rawtypes")
	protected float getToolMouseDragLengthRatio( MouseEvent e ) {
		OMSVGPoint delta = getToolMouseDragPoint(e)
				.substract( mouseDragPointNought );
		// TODO: work in ratios! dragElementCoordRatioNought
		float newToolCoord = dragElementCoordNought;
		if ( Orientation.HORIZONTAL == orientation ) {
			newToolCoord += delta.getX();
		} else {
			newToolCoord += delta.getY();
		}
		float newToolCoordRatio = getToolRatioFromCoord(
				newToolCoord );
		return newToolCoordRatio;
	}

	// Communication w/ presenter(s)
	@Override
	public void fireEvent(GwtEvent<?> event) {
		this.handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addToolScrollEventHandler(
			GenericScrollEvent.Handler handler ) {
		return handlerManager.addHandler(
				GenericScrollEvent.ASSOCIATED_TYPE, handler);
	}

	@Override
	public HandlerRegistration addToolZoomEventHandler(
			PathwayZoomEvent.Handler handler ) {
		return handlerManager.addHandler(
				PathwayZoomEvent.ASSOCIATED_TYPE, handler);
	}

	// Outside manipulation methods
	@Override
	public void setControlToolZoomRatio(float ratio) {
		if ( toolSVGGElement != null ) {
			scrollZoom(ratio);
		}
	}

	protected void scrollZoom(float zoomRatio) {
		if ( toolSVGGElement != null ) {
			float newLengthRatio = initialControlToolLengthRatio/zoomRatio;

			if ( newLengthRatio > INITIAL_LANDSCAPE_LENGTH_RATIO ) {
				this.setLandscapeLengthRatio(1/newLengthRatio);
			} else {
				this.setLandscapeLengthRatio(INITIAL_LANDSCAPE_LENGTH_RATIO);
			}

			// note that setting first length,
			//	then alpha coord ratios is necessary in that order
			// TODO: check if above still true
			setControlToolLengthRatio(newLengthRatio, false);

			if ( controlToolAlphaCoordRatio + newLengthRatio
					> getOmegaDragElemRatioOmegaBound() ) {
				float newAlphaCoordRatio = getOmegaDragElemRatioOmegaBound()
						- newLengthRatio;

				if ( newAlphaCoordRatio < 0 ) {
					newAlphaCoordRatio = 0;
				}

				setControlToolAlphaCoordRatio( newAlphaCoordRatio );
			}

		}

	}
}
