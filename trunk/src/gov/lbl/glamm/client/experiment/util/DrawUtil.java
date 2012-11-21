package gov.lbl.glamm.client.experiment.util;

import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.model.ViewGene;
import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.model.ViewReaction;
import gov.lbl.glamm.client.experiment.view.AccessibleHtmlPopupPanel;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.Sample.TargetType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDefsElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPointList;
import org.vectomatic.dom.svg.OMSVGPolygonElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGStyleElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMSVGTransform;
import org.vectomatic.dom.svg.OMSVGUseElement;
import org.vectomatic.dom.svg.events.HasGraphicalHandlers;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * Utility class to draw elements common across widgets.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class DrawUtil {
	public static final String SCND_METAB_EVEN_SEG_CSS_COLOR = "#737CA1";
	public static final String SCND_METAB_ODD_SEG_CSS_COLOR = "#98AFC7";
	public static final String EXPERIMENT_EVEN_SEG_CSS_COLOR = "#C6DEFF";
	public static final String EXPERIMENT_ODD_SEG_CSS_COLOR = "#E3E4FA";

	public static final String VIEWPORT_SVG_GROUP_CLASS = "viewport";

	public static final String SHORT_NAME_CLASS_VALUE = "shortName";
	public static final String LONG_NAME_CLASS_VALUE = "longName";
	public static final String ELEMENT_GRAPH_CLASS_NAME = "elementGraph";

	public static final String BASE_TEXT_CLASS = "baseText";
	public static final String HIGHLIGHT_TEXT_CLASS = "highlightedText";
	public static final String BASE_SYMBOL_CLASS = "baseSymbol";
	public static final String HIGHLIGHTED_SYMBOL_CLASS = "highlightedSymbol";
	public static final String BASE_SECONDARY_SYMBOL_CLASS = "baseSecondarySymbol";
	public static final String HIGHLIGHTED_SECONDARY_SYMBOL_CLASS = "highlightedSecondarySymbol";
	public static final float BASE_SECONDARY_SYMBOL_SCALE = 0.75f;
	public static final float HIGHLIGHTED_SECONDARY_SYMBOL_SCALE = 1.0f;

	protected static final String ARROW_DEF_ID = "ArrowDef";
	protected static final String ARROW_DEF_ID_HREF = "#" + ARROW_DEF_ID;

	protected static final String ELECTRON_NAME;
	protected static final String HYDROGEN_NAME;
	protected static final String H2O_NAME;
	protected static final String NAD_NAME;
	protected static final String NADH_NAME;
	protected static final String NADP_NAME;
	protected static final String NADPH_NAME;
	protected static final String FAD_NAME;
	protected static final String FADH2_NAME;
	protected static final String CO2_NAME;
	protected static final String ATP_NAME;
	protected static final String ADP_NAME;
	protected static final String AMP_NAME;
	protected static final String GTP_NAME;
	protected static final String GDP_NAME;
	protected static final String GMP_NAME;
	protected static final String NTP_NAME;
	protected static final String NDP_NAME;
	protected static final String NMP_NAME;
	protected static final BinarySortedSet<String> metabolitesWithDefs;

	static {
		metabolitesWithDefs = new BinarySortedSet<String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		ELECTRON_NAME = "e-";
		HYDROGEN_NAME = "H+";
		H2O_NAME = "H2O";
		NAD_NAME = "NAD+";
		NADH_NAME = "NADH";
		NADP_NAME = "NADP+";
		NADPH_NAME = "NADPH";
		FAD_NAME = "FAD";
		FADH2_NAME = "FADH2";
		CO2_NAME = "CO2";
		ATP_NAME = "ATP";
		ADP_NAME = "ADP";
		AMP_NAME = "AMP";
		GTP_NAME = "GTP";
		GDP_NAME = "GDP";
		GMP_NAME = "GMP";
		NTP_NAME = "NTP";
		NDP_NAME = "NDP";
		NMP_NAME = "NMP";
		metabolitesWithDefs.add(ELECTRON_NAME);
		metabolitesWithDefs.add(HYDROGEN_NAME);
		metabolitesWithDefs.add(H2O_NAME);
		metabolitesWithDefs.add(NAD_NAME);
		metabolitesWithDefs.add(NADH_NAME);
		metabolitesWithDefs.add(NADP_NAME);
		metabolitesWithDefs.add(NADPH_NAME);
		metabolitesWithDefs.add(FAD_NAME);
		metabolitesWithDefs.add(FADH2_NAME);
		metabolitesWithDefs.add(CO2_NAME);
		metabolitesWithDefs.add(ATP_NAME);
		metabolitesWithDefs.add(ADP_NAME);
		metabolitesWithDefs.add(AMP_NAME);
		metabolitesWithDefs.add(GTP_NAME);
		metabolitesWithDefs.add(GDP_NAME);
		metabolitesWithDefs.add(GMP_NAME);
		metabolitesWithDefs.add(NTP_NAME);
		metabolitesWithDefs.add(NDP_NAME);
		metabolitesWithDefs.add(NMP_NAME);
	}

	// length layout
	private float mainSymbolDiameter = 12;
	private float mainSymbolRadius = mainSymbolDiameter/2;
	private float pathwaySpacing = mainSymbolDiameter*2;
	// width layout
	protected static final float DEFAULT_SVG_WIDTH = 340;
	protected static final float DEFAULT_NODE_NAME_OFFSET = -50;
	protected static final float DEFAULT_REACTION_NAME_OFFSET = -200;
	private float pathwaySvgWidth = 340;
	private float mainlineCenterAxis = 300;
	private float sidelineOffset = -mainSymbolDiameter;
	private float nodeNameOffset = -50;
	private float reactionNameOffset = -200;
	private float secondaryNameOffset = -2*mainSymbolDiameter;

	protected AccessibleHtmlPopupPanel pathwayPopup = null;
	protected AccessibleHtmlPopupPanel dataPopup = null;
	protected AccessibleHtmlPopupPanel sampleGuidePopup = null;

	public DrawUtil( float mainSymbolSize
			, AccessibleHtmlPopupPanel pathwayPopup
			, AccessibleHtmlPopupPanel dataPopup
			, AccessibleHtmlPopupPanel sampleGuidePopup ) {
		super();
		this.setMainSymbolSize(mainSymbolSize);
		this.pathwayPopup = pathwayPopup;
		this.dataPopup = dataPopup;
		this.sampleGuidePopup = sampleGuidePopup;
	}

	protected String getDefId( String metaboliteName ) {
		return metaboliteName + "Def";
	}
	protected String getDefHref( String metaboliteName ) {
		return "#" + getDefId(metaboliteName);
	}

	public void addElectronDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGCircleElement circle = doc.createSVGCircleElement(
				0, 0
				, mainSymbolRadius*0.6f );
		objCount.svgObjCount++;
		circle.setId(getDefId(ELECTRON_NAME));
		circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#ff8800" );

		defs.appendChild(circle);
	}

	public void addHydrogenDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGCircleElement circle = doc.createSVGCircleElement(
				0, 0
				, mainSymbolRadius*0.8f );
		objCount.svgObjCount++;
		circle.setId(getDefId(HYDROGEN_NAME));
		circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_BLACK_VALUE );

		defs.appendChild(circle);
	}
	public void addCircleDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, String baseName, String fillCssColor
			, ObjectCount objCount ) {
		OMSVGCircleElement circle = doc.createSVGCircleElement(
				0, 0
				, mainSymbolRadius*0.8f );
		objCount.svgObjCount++;
		circle.setId(getDefId(baseName));
		circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fillCssColor);

		defs.appendChild(circle);
	}

	public void addH2ODef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGGElement group = doc.createSVGGElement();
		objCount.svgObjCount++;
		group.setId(getDefId(H2O_NAME));
		defs.appendChild(group);

		float oRadius = mainSymbolRadius*0.6f;
		float hRadius = mainSymbolRadius*0.35f;
		float oCenterX = (oRadius+hRadius);

		OMSVGGElement shiftGroup = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, null, -0.85f*oCenterX, 0, objCount);
		group.appendChild(shiftGroup);

		OMSVGCircleElement oxygen = doc.createSVGCircleElement(
				0, 0
				, oRadius );
		objCount.svgObjCount++;
		oxygen.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE );
		oxygen.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE );
		oxygen.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px" );
		OMSVGTransform oxygenTranslate = svg.createSVGTransform();
		oxygenTranslate.setTranslate(oCenterX, 0);
		oxygen.getTransform().getBaseVal().appendItem(oxygenTranslate);
		shiftGroup.appendChild(oxygen);

		OMSVGCircleElement hydrogen1 = doc.createSVGCircleElement(
				0, 0
				, hRadius );
		objCount.svgObjCount++;
		hydrogen1.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_BLACK_VALUE );
		OMSVGTransform h1Rotate = svg.createSVGTransform();
		h1Rotate.setRotate(-54, oCenterX, 0);
		hydrogen1.getTransform().getBaseVal().appendItem(h1Rotate);
		shiftGroup.appendChild(hydrogen1);

		OMSVGCircleElement hydrogen2 = doc.createSVGCircleElement(
				0, 0
				, hRadius );
		objCount.svgObjCount++;
		hydrogen2.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_BLACK_VALUE );
		OMSVGTransform h2Rotate = svg.createSVGTransform();
		h2Rotate.setRotate(54, oCenterX, 0);
		hydrogen2.getTransform().getBaseVal().appendItem(h2Rotate);
		shiftGroup.appendChild(hydrogen2);
	}

	public void addNADDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(NAD_NAME), SVGConstants.CSS_WHITE_VALUE, "#ffcc00"
				, objCount );

		defs.appendChild(arrowElement);
	}
	public void addNADHDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(NADH_NAME), "#ffcc00", "#ffcc00", objCount );

		defs.appendChild(arrowElement);
	}
	public void addNADPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(NADP_NAME), SVGConstants.CSS_WHITE_VALUE, "#ff6600"
				, objCount );

		defs.appendChild(arrowElement);
	}
	public void addNADPHDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(NADPH_NAME), "#ff6600", "#ff6600", objCount );

		defs.appendChild(arrowElement);
	}
	public void addFADDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(FAD_NAME), SVGConstants.CSS_WHITE_VALUE, "#6600ff"
				, objCount );

		defs.appendChild(arrowElement);
	}
	public void addFADH2Def( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(FADH2_NAME), "#6600ff", "6600ff", objCount );

		defs.appendChild(arrowElement);
	}
	public void addCO2Def( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, getDefId(CO2_NAME), "#c17753", "#c17753", objCount );

		defs.appendChild(arrowElement);
	}

	public void addATPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, ATP_NAME, "#009966", 3, objCount);
	}
	public void addADPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, ADP_NAME, "#009966", 2, objCount);
	}
	public void addAMPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, AMP_NAME, "#009966", 1, objCount);
	}
	public void addGTPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, GTP_NAME, "#0033ff", 3, objCount);
	}
	public void addGDPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, GDP_NAME, "#0033ff", 2, objCount);
	}
	public void addGMPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, GMP_NAME, "#0033ff", 2, objCount);
	}
	public void addNTPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, NTP_NAME, "#996600", 3, objCount);
	}
	public void addNDPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, NDP_NAME, "#996600", 2, objCount);
	}
	public void addNMPDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		addXNPDef(svg, doc, defs, NMP_NAME, "#996600", 2, objCount);
	}

	public void addXNPDef( OMSVGSVGElement svg, OMSVGDocument doc, OMSVGDefsElement defs
			, String baseName, String cssColor, int number
			, ObjectCount objCount ) {
		OMSVGGElement group = doc.createSVGGElement();
		objCount.svgObjCount++;
		group.setId( getDefId(baseName) );
		OMSVGElement arrowElement = createSecondaryMetaboliteArrow( svg, doc
				, null, SVGConstants.CSS_WHITE_VALUE, cssColor, objCount );
		group.appendChild(arrowElement);

		// circles
		float circleCX = -0.6f*this.mainSymbolRadius;
		float deltaCircleCX = -circleCX;
		for ( int i=0; i< number; i++ ) {
			OMSVGElement circle = doc.createSVGCircleElement(
					circleCX, 0, this.mainSymbolRadius*0.2f );
			objCount.svgObjCount++;
			circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "#cc0000");
			group.appendChild(circle);

			circleCX += deltaCircleCX;
		}

		defs.appendChild(group);
	}
	public OMSVGElement createSecondaryMetaboliteArrow( OMSVGSVGElement svg
			, OMSVGDocument doc, String id, String cssFill, String cssStroke
			, ObjectCount objCount
	) {
		float edgeRadius = this.mainSymbolRadius;
		float barrelRadius = 0.5f*this.mainSymbolRadius;

		OMSVGPolygonElement arrowPolygon = doc.createSVGPolygonElement();
		objCount.svgObjCount++;
		arrowPolygon.setId(id);
		OMSVGPointList arrowPoints = arrowPolygon.getPoints();
		arrowPoints.appendItem(svg.createSVGPoint(0, -edgeRadius));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(edgeRadius+2, 0));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(0, edgeRadius));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(0, barrelRadius));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(-edgeRadius, barrelRadius));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(-edgeRadius, -barrelRadius));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(0, -barrelRadius));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(0, -edgeRadius));
		objCount.svgObjCount++;
		arrowPolygon.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, cssFill);
		arrowPolygon.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, cssStroke );
		arrowPolygon.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px" );

		return arrowPolygon;
	}

	public void drawSecondaryMetaboliteKey( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, final ViewCompound compound
			, float cx
			, ObjectCount objCount ) {
		OMSVGGElement metaboliteSVGGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
				, null
				, compound.getBaseObject().getGuid()
				, 0, compound.getRelY()
				, objCount );

		OMSVGElement compoundSvg = createCompoundSymbol( doc
				, cx, 0, objCount );
		compoundSvg.addClassNameBaseVal(DrawUtil.BASE_SYMBOL_CLASS);
		metaboliteSVGGroup.appendChild(compoundSvg);

		String name = compound.getBaseObject().getName();
		String symbolHref = getDefHref(name);
		if ( symbolHref != null ) {
			SVGUtil.useDef(svg, doc, metaboliteSVGGroup, symbolHref
					, this.nodeNameOffset, 0, 0, BASE_SECONDARY_SYMBOL_SCALE
					, objCount );
		}

		OMSVGElement compoundText = this.createSecondaryMetaboliteText( doc
				, 0, 0, SHORT_NAME_CLASS_VALUE, compound.getBaseObject().getName()
				, objCount );
		metaboliteSVGGroup.appendChild(compoundText);
		OMSVGElement compoundLongText = this.createSecondaryMetaboliteText( doc
				, 0, 0, LONG_NAME_CLASS_VALUE, compound.getBaseObject().getName()
				, objCount );
		metaboliteSVGGroup.appendChild(compoundLongText);

		setHighlightBehavior( svg, metaboliteSVGGroup
				, compound.getBaseObject().getGuid()
				, SecondaryElementOnExecutor.instance()
				, SecondaryElementOffExecutor.instance() );
		this.setPopup( metaboliteSVGGroup, pathwayPopup
				, compound.toHtml(), false );
		svgGroup.appendChild(metaboliteSVGGroup);
	}

	public OMSVGElement createSecondaryMetaboliteText(
			OMSVGDocument doc
			, float cx, float cy, String classValue, String text
			, ObjectCount objCount
	) {
		OMSVGTextElement svgText = doc.createSVGTextElement(
				cx + nodeNameOffset + secondaryNameOffset, cy + mainSymbolRadius/2
				, OMSVGLength.SVG_LENGTHTYPE_PX
				, text );
		objCount.svgObjCount++;
		svgText.setClassNameBaseVal(classValue);
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_SIZE_PROPERTY, "7pt" );
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_TEXT_ANCHOR_PROPERTY, SVGConstants.CSS_END_VALUE );

		return svgText;
	}

	/**
	 * Draw secondary metabolite key.
	 *
	 * @param secondaryMetabolites
	 * @param dynamicDefBaseNameList
	 * @param heatMapUtil
	 * @param svgWidth
	 * @param svgHeight
	 * @param objCount
	 * @return
	 */
	public SVGData createSecondaryMetaboliteLayout(
			BinarySortedSet<ViewCompound> secondaryMetabolites
			, ArrayList<String> dynamicDefBaseNameList, HeatMapUtil heatMapUtil
			, float svgWidth, float svgHeight
			, ObjectCount objCount
	) {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		objCount.svgObjCount++;
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);
		svg.setWidth( OMSVGLength.SVG_LENGTHTYPE_PX, svgWidth );

		svg.setClassNameBaseVal(ELEMENT_GRAPH_CLASS_NAME);

		OMSVGDefsElement defs = doc.createSVGDefsElement();
		objCount.svgObjCount++;
		svg.appendChild(defs);
		addElectronDef(svg, doc, defs, objCount);
		addHydrogenDef(svg, doc, defs, objCount);
		addH2ODef(svg, doc, defs, objCount);
		addNADDef(svg, doc, defs, objCount);
		addNADHDef(svg, doc, defs, objCount);
		addNADPDef(svg, doc, defs, objCount);
		addNADPHDef(svg, doc, defs, objCount);
		addFADDef(svg, doc, defs, objCount);
		addFADH2Def(svg, doc, defs, objCount);
		addCO2Def(svg, doc, defs, objCount);
		addATPDef(svg, doc, defs, objCount);
		addADPDef(svg, doc, defs, objCount);
		addAMPDef(svg, doc, defs, objCount);
		addGTPDef(svg, doc, defs, objCount);
		addGDPDef(svg, doc, defs, objCount);
		addGMPDef(svg, doc, defs, objCount);
		addNTPDef(svg, doc, defs, objCount);
		addNDPDef(svg, doc, defs, objCount);
		addNMPDef(svg, doc, defs, objCount);
		float dynamicDefTotal = dynamicDefBaseNameList.size();
		float dynamicDefDelta = 1/dynamicDefTotal;
		float dynamicDefArea = 0;
		for ( String baseName : dynamicDefBaseNameList ) {
			String fillCssColor = SVGUtil.convertToCssColor(
					heatMapUtil.getHeatMapping(dynamicDefArea) );
			this.addCircleDef( svg, doc, defs, baseName, fillCssColor, objCount );
			dynamicDefArea += dynamicDefDelta;
		}

		// all visual elements will be added to viewport group,
		//	which is used for "zooming"
		OMSVGGElement viewport = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, VIEWPORT_SVG_GROUP_CLASS, 0, 0, objCount);
		svg.appendChild(viewport);

		float length = this.mainSymbolDiameter;
		OMSVGGElement group = SVGUtil.createTranslatedSVGGroup( svg, doc, null
				, null, this.mainlineCenterAxis, length, objCount );

		float metabolitesLength = 0;
		for ( ViewCompound metabolite : secondaryMetabolites ) {
			this.drawSecondaryMetaboliteKey( svg, doc, group, metabolite, 0
					, objCount );
			metabolitesLength = metabolite.getRelY();
		}
		length += metabolitesLength + this.mainSymbolDiameter;

		viewport.appendChild(group);

		svg.setHeight( OMSVGLength.SVG_LENGTHTYPE_PX, svgHeight );
		return new SVGData( svg, viewport, this.pathwaySvgWidth, length);
	}


	/**
	 * Creates display-sorted set of view objects with vertical displacement values
	 * from sorted set of secondary metabolite model objects.
	 *
	 * @param secondaryMetaboliteSet
	 * @param dynamicDefBaseNameList
	 * @param objCount
	 * @return
	 */
	public BinarySortedSet<ViewCompound> createDisplaySecondaryMetaboliteList(
			BinarySortedSet<ViewCompound> secondaryMetaboliteSet
			, ArrayList<String> dynamicDefBaseNameList
			, ObjectCount objCount
	) {
		BinarySortedSet<ViewCompound> displaySymbolList
				= new BinarySortedSet<ViewCompound>( new Comparator<ViewCompound>() {
					@Override
					public int compare(ViewCompound o1,
							ViewCompound o2) {
						return o1.getBaseObject().getName().compareTo(o2.getBaseObject().getName());
					}
				});
		objCount.listCount++;
		for ( ViewCompound compound : secondaryMetaboliteSet ) {
			displaySymbolList.add( compound );
		}
		float yOffset = 0;
		for ( ViewCompound compoundSymbol : displaySymbolList ) {
			compoundSymbol.setRelY(yOffset);
			yOffset += 1.5f*this.mainSymbolDiameter;
			String name = compoundSymbol.getBaseObject().getName();
			if ( !DrawUtil.metabolitesWithDefs.contains(name) ) {
				dynamicDefBaseNameList.add(name);
			}
		}
		return displaySymbolList;
	}

	/**
	 * Draw secondary metabolite experimental data.
	 *
	 * @param secondaryMetabolites
	 * @param experiments
	 * @param dataType
	 * @param elementType
	 * @param svgWidth
	 * @param svgHeight
	 * @param heatMapUtil
	 * @param rangeMin
	 * @param rangeMax
	 * @param objCount
	 * @return
	 */
	public SVGData createSecondaryExperimentDataSvg(
			BinarySortedSet<ViewCompound> secondaryMetabolites
			, List<Experiment> experiments
			, String dataType, String elementType
			, float svgWidth, float svgHeight
			, HeatMapUtil heatMapUtil, float rangeMin, float rangeMax
			, ObjectCount objCount
	) {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		objCount.svgObjCount++;
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		// all visual elements will be added to viewport group,
		//	which is used for "zooming"
		OMSVGGElement viewport = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, VIEWPORT_SVG_GROUP_CLASS, 0, 0
				, objCount);
		svg.appendChild(viewport);

		int totalSampleIndex = 0;
		float length = 0;
		for ( Experiment experiment : experiments ) {
			for ( Sample sample : experiment.getSamples() ) {
				HashMap<String,Measurement> locusIdDataTypeMeasurementMap
						= sample.getElementIdDataTypeMeasurementMap();

				// reset length for each sample
				length = this.mainSymbolDiameter;

				OMSVGGElement sampleSVGGroup = SVGUtil.createTranslatedSVGGroup(
						svg, doc, null
						, sample.getId()
						, this.mainSymbolRadius
								+ totalSampleIndex*this.mainSymbolDiameter
						, length
						, objCount );
				viewport.appendChild(sampleSVGGroup);

				// secondary metabolites
				if ( elementType.equals( TargetType.COMPOUND.toString() ) ) {
					float lastMetaboliteRelY = 0;
					for ( ViewCompound metabolite : secondaryMetabolites ) {
						Measurement measurement = locusIdDataTypeMeasurementMap.get(
								metabolite.getBaseObject().getGuid() + dataType );
						this.drawCompoundExpData(svg, doc, sampleSVGGroup
								, metabolite, 0, measurement
								, heatMapUtil, rangeMin, rangeMax
								, objCount);
						lastMetaboliteRelY = metabolite.getRelY();
					}
					length += lastMetaboliteRelY + this.mainSymbolDiameter;
				}
				totalSampleIndex++;
			}
		}
		svg.setWidth( OMSVGLength.SVG_LENGTHTYPE_PX, svgWidth );
		svg.setHeight( OMSVGLength.SVG_LENGTHTYPE_PX, svgHeight );
		return new SVGData( svg, viewport
				, totalSampleIndex*this.mainSymbolDiameter, length );
	}

	/**
	 * Re-usable method to create the basic compound symbol.
	 *
	 * @param doc
	 * @param cx x coord of center of symbol
	 * @param cy y coord of center of symbol
	 * @param objCount TODO
	 * @return
	 */
	public OMSVGElement createCompoundSymbol(
			OMSVGDocument doc
			, float cx, float cy, ObjectCount objCount ) {
		OMSVGCircleElement circle = doc.createSVGCircleElement(
				cx, cy
				, mainSymbolRadius );
		objCount.svgObjCount++;
		return circle;
	}

	/**
	 * Re-usable method to create the basic gene symbol.
	 *
	 * @param doc
	 * @param cx x coord of center of symbol
	 * @param cy y coord of center of symbol
	 * @param fillCssColor
	 * @param strokeCssColor
	 * @param objCount TODO
	 * @return
	 */
	public OMSVGRectElement createGeneSymbol(
			OMSVGDocument doc
			, float cx, float cy, String fillCssColor, String strokeCssColor
			, ObjectCount objCount ) {
		OMSVGRectElement rect = doc.createSVGRectElement(
				cx - mainSymbolRadius
				,  cy - mainSymbolRadius
				, mainSymbolDiameter, mainSymbolDiameter
				, 1, 1);
		objCount.svgObjCount++;
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fillCssColor);
		//rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1" );
		rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, strokeCssColor);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
		return rect;
	}

	/**
	 * Re-usable method to create the basic reaction symbol.
	 *
	 * @param doc
	 * @param cx
	 * @param cy
	 * @param length
	 * @param fillCssColor
	 * @param strokeCssColor
	 * @param objCount
	 * @return
	 */
	public OMSVGElement createReactionSymbol(
			OMSVGDocument doc
			, float cx, float cy
			, float length, String fillCssColor, String strokeCssColor
			, ObjectCount objCount ) {
		OMSVGRectElement rect = doc.createSVGRectElement(
				cx - mainSymbolRadius
				,  cy - mainSymbolRadius
				, mainSymbolDiameter, length + mainSymbolDiameter
				, 1, 1);
		objCount.svgObjCount++;
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fillCssColor);
		//rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "1" );
		rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, strokeCssColor);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1px");
		return rect;
	}

	public OMSVGElement createGeneText(
			OMSVGDocument doc
			, float cx, float cy, String classValue, String text, ObjectCount objCount
	) {
		OMSVGTextElement svgText = doc.createSVGTextElement(
				cx + nodeNameOffset, cy + mainSymbolRadius/2
				, OMSVGLength.SVG_LENGTHTYPE_PX
				, text );
		objCount.svgObjCount++;
		svgText.setClassNameBaseVal(classValue);
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_SIZE_PROPERTY, "8pt" );
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_WEIGHT_PROPERTY, SVGConstants.CSS_BOLD_VALUE );
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_TEXT_ANCHOR_PROPERTY, SVGConstants.CSS_END_VALUE );

		return svgText;
	}

	public OMSVGElement createCompoundText(
			OMSVGDocument doc
			, float cx, float cy, String classValue, String text
			, ObjectCount objCount ) {
		OMSVGTextElement svgText = doc.createSVGTextElement(
				cx + nodeNameOffset, cy + mainSymbolRadius/2
				, OMSVGLength.SVG_LENGTHTYPE_PX
				, text );
		objCount.svgObjCount++;
		svgText.setClassNameBaseVal(classValue);
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_SIZE_PROPERTY, "7pt" );
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_TEXT_ANCHOR_PROPERTY, SVGConstants.CSS_END_VALUE );

		return svgText;
	}

	public OMSVGElement createReactionText(
			OMSVGDocument doc
			, float cx, float cy, String text, ObjectCount objCount ) {
		OMSVGTextElement svgText = doc.createSVGTextElement(
				cx + reactionNameOffset, cy + mainSymbolRadius/2
				, OMSVGLength.SVG_LENGTHTYPE_PX
				, text );
		objCount.svgObjCount++;
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_SIZE_PROPERTY, "8pt" );
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_WEIGHT_PROPERTY, SVGConstants.CSS_BOLD_VALUE );
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_TEXT_ANCHOR_PROPERTY, SVGConstants.CSS_END_VALUE );

		return svgText;
	}

	/**
	 * Draw compound diagram with associated tooltips and name labels.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param compound
	 * @param cx
	 * @param objCount
	 */
	public void drawCompound( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, final ViewCompound compound
			, float cx, ObjectCount objCount ) {
		OMSVGGElement compoundSVGGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
				, null
				, compound.getBaseObject().getGuid()
				, 0, compound.getRelY(), objCount );

		OMSVGElement compoundSvg = createCompoundSymbol( doc
				, cx, 0, objCount );
		compoundSvg.addClassNameBaseVal(DrawUtil.BASE_SYMBOL_CLASS);
		compoundSVGGroup.appendChild(compoundSvg);
		OMSVGElement compoundText = this.createCompoundText( doc
				, 0, 0, SHORT_NAME_CLASS_VALUE, compound.getBaseObject().getName()
				, objCount );
		compoundSVGGroup.appendChild(compoundText);
		OMSVGElement compoundLongText = this.createCompoundText( doc
				, 0, 0, LONG_NAME_CLASS_VALUE, compound.getBaseObject().getName()
				, objCount );
		compoundSVGGroup.appendChild(compoundLongText);

		setHighlightBehavior( svg
				, compoundSVGGroup, compound.getBaseObject().getGuid()
				, ElementOnExecutor.instance()
				, ElementOffExecutor.instance() );
		this.setPopup( compoundSVGGroup, pathwayPopup
				, compound.toHtml(), false );
		svgGroup.appendChild(compoundSVGGroup);
	}

	/**
	 * Draw gene diagram with associated tooltips and name labels.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param gene
	 * @param cx
	 * @param emptyGenePlacementY
	 * @param objCount
	 */
	public void drawGene( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewGene gene
			, float cx, float emptyGenePlacementY
			, ObjectCount objCount ) {
		OMSVGGElement geneSVGGroup = doc.createSVGGElement();
		objCount.svgObjCount++;

		float relY = emptyGenePlacementY;
		String synonym = "N/A";
		String name = "N/A";
		String id = null;
		if ( gene != null ) {
			relY = gene.getRelY();
			synonym = gene.getBaseObject().getSynonymWithType( Gene.SYNONYM_TYPE_NAME ); //Gene.SYNONYM_TYPE_SYNONYM );
			for ( String molTaxonomyId : gene.getBaseObject().getMolTaxonomyIds() ) {
				name =  molTaxonomyId;
				break;
			}
			id = gene.getBaseObject().getVimssId();

			// popup
			this.setPopup( geneSVGGroup, pathwayPopup
					, gene.toHtml(), false );
		}
		geneSVGGroup.setClassNameBaseVal(id);
		SVGUtil.translate( svg, geneSVGGroup, 0, relY );

		OMSVGRectElement geneSvg = this.createGeneSymbol( doc
				, 0, 0, SVGConstants.CSS_WHITE_VALUE, SVGConstants.CSS_BLACK_VALUE
				, objCount);
		geneSVGGroup.appendChild(geneSvg);
		OMSVGElement geneText = this.createGeneText( doc
				, 0, 0, SHORT_NAME_CLASS_VALUE, synonym, objCount );
		geneSVGGroup.appendChild(geneText);
		OMSVGElement geneLongText = this.createGeneText( doc
				, 0, 0, LONG_NAME_CLASS_VALUE, name, objCount );
		geneSVGGroup.appendChild(geneLongText);

		svgGroup.appendChild(geneSVGGroup);
	}

	/**
	 * Includes logic for business rules for reaction directionality arrows.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param mainReactant
	 * @param geneEndY
	 * @param objCount
	 */
	public void drawMainReactantGeneEdge( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup
			, ViewReaction reaction, ViewCompound mainReactant
			, float geneEndY, ObjectCount objCount
	) {
		OMSVGLineElement line = doc.createSVGLineElement(0, mainReactant.getRelY(), 0, geneEndY);
		objCount.svgObjCount++;
		line.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		if ( reaction.getDirection() == ViewReaction.REVERSE_DIRECTION
				|| reaction.getDirection() == ViewReaction.BOTH_DIRECTION ) {
			this.drawArrow( svg, doc, svgGroup
					, 0, this.mainSymbolDiameter, 180
					, objCount );
		}
		svgGroup.appendChild(line);
	}

	/**
	 * Includes logic for business rules for reaction directionality arrows.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param mainProduct
	 * @param geneEndY
	 * @param objCount
	 */
	public void drawMainProductGeneEdge( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup
			, ViewReaction reaction, ViewCompound mainProduct
			, float geneEndY, ObjectCount objCount
	) {
		OMSVGLineElement line = doc.createSVGLineElement(0, mainProduct.getRelY(), 0, geneEndY);
		line.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		if ( reaction.getDirection() == ViewReaction.FORWARD_DIRECTION
				|| reaction.getDirection() == ViewReaction.BOTH_DIRECTION ) {
			this.drawArrow( svg, doc, svgGroup
					, 0, mainProduct.getRelY() - this.mainSymbolDiameter, 0
					, objCount );
		}
		svgGroup.appendChild(line);
	}

	/**
	 * Includes logic for business rules for reaction directionality arrows.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param reactant non-null
	 * @param geneEndY
	 * @param objCount TODO
	 */
	protected void drawSidelineReactantGeneEdge( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup
			, ViewReaction reaction, ViewCompound reactant
			, float geneEndY, ObjectCount objCount
	) {
		OMSVGPathElement path = doc.createSVGPathElement();
		objCount.svgObjCount++;
		OMSVGPathSegList segs = path.getPathSegList();
		segs.appendItem(path.createSVGPathSegMovetoAbs(0, geneEndY));
		objCount.svgObjCount++;
		float ry = geneEndY - reactant.getRelY();
		segs.appendItem( path.createSVGPathSegArcRel( this.sidelineOffset, -ry
				, -this.sidelineOffset, ry, 0
				, false, false ) );
		objCount.svgObjCount++;
		path.setAttribute(SVGConstants.CSS_FILL_VALUE, SVGConstants.CSS_NONE_VALUE);
		path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		if ( reaction.getDirection() == ViewReaction.FORWARD_DIRECTION ) {
			this.drawArrow( svg, doc, svgGroup
					, this.sidelineOffset*0.25f, reactant.getRelY()+ry*0.36f, -35
					, objCount );
		} else if ( reaction.getDirection() == ViewReaction.REVERSE_DIRECTION
				|| reaction.getDirection() == ViewReaction.BOTH_DIRECTION ) {
			this.drawArrow( svg, doc, svgGroup
					, this.sidelineOffset*0.25f, reactant.getRelY()+ry*0.36f, 155
					, objCount );
		}
		svgGroup.appendChild(path);
	}

	/**
	 * Includes logic for business rules for reaction directionality arrows.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param product non-null
	 * @param geneEndY
	 * @param objCount TODO
	 */
	protected void drawSidelineProductGeneEdge( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup
			, ViewReaction reaction, ViewCompound product
			, float geneEndY, ObjectCount objCount
	) {
		OMSVGPathElement path = doc.createSVGPathElement();
		objCount.svgObjCount++;
		OMSVGPathSegList segs = path.getPathSegList();
		segs.appendItem(path.createSVGPathSegMovetoAbs(0, geneEndY));
		objCount.svgObjCount++;
		float ry = product.getRelY() - geneEndY;
		segs.appendItem( path.createSVGPathSegArcRel( this.sidelineOffset, ry
				, -this.sidelineOffset, ry, 0
				, false, true ) );
		objCount.svgObjCount++;
		path.setAttribute(SVGConstants.CSS_FILL_VALUE, SVGConstants.CSS_NONE_VALUE);
		path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
		if ( reaction.getDirection() == ViewReaction.FORWARD_DIRECTION
				|| reaction.getDirection() == ViewReaction.BOTH_DIRECTION ) {
			this.drawArrow( svg, doc, svgGroup
					, this.sidelineOffset*0.25f, product.getRelY()-ry*0.36f, 35
					, objCount );
		} else if ( reaction.getDirection() == ViewReaction.REVERSE_DIRECTION ) {
			this.drawArrow( svg, doc, svgGroup
					, this.sidelineOffset*0.25f, product.getRelY()-ry*0.36f, -155
					, objCount );
		}
		svgGroup.appendChild(path);
	}

	public void drawArrow(  OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup
			, float cx, float cy, float rotation
			, ObjectCount objCount
	) {
		OMSVGUseElement useArrow = doc.createSVGUseElement();
		useArrow.getHref().setBaseVal(ARROW_DEF_ID_HREF);
		objCount.svgUseCount++;

		OMSVGTransform arrowTranslate = svg.createSVGTransform();
		arrowTranslate.setTranslate(cx, cy);
		useArrow.getTransform().getBaseVal().appendItem(arrowTranslate);
		if ( rotation != 0 ) {
			OMSVGTransform arrowRotate = svg.createSVGTransform();
			arrowRotate.setRotate(rotation, 0, 0);
			useArrow.getTransform().getBaseVal().appendItem(arrowRotate);
		}

		svgGroup.appendChild(useArrow);
	}

	/**
	 * Sets mouse handlers for highlighting all same elements.
	 * Uses gov.lbl.glamm.experiment.client.util.Executor interface to re-use search algorithm.
	 *
	 * @param svg
	 * @param targetElement
	 * @param elementClassIdentifier TODO
	 * @param highlightOnExecutor
	 * @param highlightOffExecutor TODO
	 */
	public void setHighlightBehavior( final OMSVGSVGElement svg
			, final OMSVGGElement targetElement, final String elementClassIdentifier
			, final Executor<OMSVGGElement> highlightOnExecutor
			, final Executor<OMSVGGElement> highlightOffExecutor
	) {
		if ( targetElement instanceof HasGraphicalHandlers ) {
			HasGraphicalHandlers hasHandlers = (HasGraphicalHandlers)targetElement;
			hasHandlers.addMouseOverHandler( new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					SVGUtil.executeOnSimilarGElements( svg.getOwnerDocument()
							, ELEMENT_GRAPH_CLASS_NAME, elementClassIdentifier
							, highlightOnExecutor );
				}
			});
			hasHandlers.addMouseOutHandler( new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					SVGUtil.executeOnSimilarGElements( svg.getOwnerDocument()
							, ELEMENT_GRAPH_CLASS_NAME, elementClassIdentifier
							, highlightOffExecutor );
				}
			});
			hasHandlers.addMouseDownHandler( new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					SVGUtil.executeOnSimilarGElements( svg.getOwnerDocument()
							, ELEMENT_GRAPH_CLASS_NAME, elementClassIdentifier
							, highlightOnExecutor );
				}
			});
		} else {
			targetElement.addDomHandler( new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					SVGUtil.executeOnSimilarGElements( svg.getOwnerDocument()
							, ELEMENT_GRAPH_CLASS_NAME, elementClassIdentifier
							, highlightOnExecutor );
				}
			}, MouseOverEvent.getType() );
			targetElement.addDomHandler( new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					SVGUtil.executeOnSimilarGElements( svg.getOwnerDocument()
							, ELEMENT_GRAPH_CLASS_NAME, elementClassIdentifier
							, highlightOffExecutor );
				}
			}, MouseOutEvent.getType() );
			targetElement.addDomHandler( new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					SVGUtil.executeOnSimilarGElements( svg.getOwnerDocument()
							, ELEMENT_GRAPH_CLASS_NAME, elementClassIdentifier
							, highlightOnExecutor );
				}
			}, MouseDownEvent.getType() );
		}
	}

	public void setPopup( OMSVGElement svgElement
			, final AccessibleHtmlPopupPanel popup, final String str
			, boolean clickOnOnly
	) {
		final String wrappedStr = str;
		if ( svgElement instanceof HasGraphicalHandlers ) {
			HasGraphicalHandlers hasHandlers = (HasGraphicalHandlers)svgElement;
			if ( !clickOnOnly ) {
			hasHandlers.addMouseOverHandler( new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					popup.show( event.getClientX(), event.getClientY(), wrappedStr );
				}
			});
			}
			hasHandlers.addMouseOutHandler( new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					popup.hide();
				}
			});
			hasHandlers.addMouseDownHandler( new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					popup.show( event.getClientX(), event.getClientY(), wrappedStr );
				}
			});
		} else {
			if ( !clickOnOnly ) {
			svgElement.addDomHandler( new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					popup.show( event.getClientX(), event.getClientY(), wrappedStr );
				}
			}, MouseOverEvent.getType() );
			}
			svgElement.addDomHandler( new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					popup.hide();
				}
			}, MouseOutEvent.getType() );
			svgElement.addDomHandler( new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					popup.show( event.getClientX(), event.getClientY(), wrappedStr );
				}
			}, MouseDownEvent.getType() );
		}
	}

	/**
	 * Draw secondary metabolite diagram part in
	 * main pathway diagram with associated tooltips.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param compound
	 * @param isReactant
	 * @param cx
	 * @param cy
	 * @param objCount
	 */
	public void drawSecondaryMetabolite( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewCompound compound, boolean isReactant
			, float cx, float cy, ObjectCount objCount ) {
		OMSVGGElement compoundSVGGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
				, null
				, compound.getBaseObject().getGuid(), cx, cy, objCount );

		SVGUtil.useDef(svg, doc, compoundSVGGroup
				, getDefHref(compound.getBaseObject().getName()), 0, 0
				, (isReactant ? 0 : 180 ), DrawUtil.BASE_SECONDARY_SYMBOL_SCALE
				, objCount );

		setHighlightBehavior( svg, compoundSVGGroup, compound.getBaseObject().getGuid()
				, SecondaryElementOnExecutor.instance()
				, SecondaryElementOffExecutor.instance() );
		this.setPopup( compoundSVGGroup, pathwayPopup
				, compound.toHtml(), false );
		svgGroup.appendChild(compoundSVGGroup);
	}

	public void drawSecondaryMetabolites( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup
			, ArrayList<ViewCompound> secondaryMetabolites, boolean areReactants
			, float relY, ObjectCount objCount
	) {
		float relX = -1.25f*this.mainSymbolDiameter;
		float deltaRelX = -this.mainSymbolDiameter;
		for ( ViewCompound compound : secondaryMetabolites ) {
			drawSecondaryMetabolite( svg, doc, svgGroup
					, compound, areReactants, relX, relY
					, objCount );
			relX += deltaRelX;
		}
	}

	/**
	 * Draw reaction diagram with associated tooltips and name labels,
	 * containing the appropriate metabolites, genes, and edges.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param reactionLengthOffset
	 * @param isLastReaction
	 * @param objCount
	 */
	public void drawReaction( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewReaction reaction
			, float reactionLengthOffset, boolean isLastReaction
			, ObjectCount objCount
	) {
		// container group
		OMSVGGElement reactionSVGGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
				, null
				, reaction.getBaseReaction().getGuid()
				, this.mainlineCenterAxis, reactionLengthOffset
				, objCount );

		// group just for the popup
		OMSVGGElement reactionPopupSVGGroup = doc.createSVGGElement();
		reactionPopupSVGGroup.setClassNameBaseVal(
				reaction.getBaseReaction().getGuid() + "Popup" );
		// put this group in background because edges are in this group
		reactionSVGGroup.appendChild(reactionPopupSVGGroup);
		// transparent background so reaction popup appears over area
		/*OMSVGElement background = doc.createSVGRectElement( -this.mainlineCenterAxis, 0
				, this.svgWidth, reaction.getTotalLength(), 5, 5 );
		background.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
		background.getStyle().setSVGProperty(SVGConstants.CSS_FILL_OPACITY_PROPERTY, "0" );
		reactionPopupSVGGroup.appendChild(background);*/

		// values to be used in drawing
		ViewCompound mainReactant = reaction.getMainReactant();
		ArrayList<ViewCompound> otherMainReactants = reaction.getOtherMainReactants();
		ArrayList<ViewGene> genes = reaction.getGenes();
		ArrayList<ViewCompound> otherMainProducts = reaction.getOtherMainProducts();
		ViewCompound mainProduct = reaction.getMainProduct();

		// line from main reactant to first gene
		//	handle alternate cases when one or other doesn't exist
		float reactantToGeneEndY = reaction.getGenePlacementY();
		if ( genes.size() > 0 ) {
			reactantToGeneEndY = genes.get(0).getRelY();
		}
		if ( mainReactant != null ) {
			this.drawMainReactantGeneEdge( svg, doc, reactionPopupSVGGroup
					, reaction, mainReactant, reactantToGeneEndY, objCount );
		} else {
			if ( otherMainReactants.size() > 0 ) {
				this.drawSidelineReactantGeneEdge( svg, doc, reactionPopupSVGGroup
						, reaction, otherMainReactants.get(otherMainReactants.size()-1)
						, reactantToGeneEndY, objCount );
			}
		}
		// line from main product to last gene
		//	handle alternate cases when one or other doesn't exist
		float productToGeneEndY = reaction.getGenePlacementY();
		if ( genes.size() > 0 ) {
			productToGeneEndY = genes.get(genes.size()-1).getRelY();
		}
		if ( mainProduct != null ) {
			this.drawMainProductGeneEdge( svg, doc, reactionPopupSVGGroup
					, reaction, mainProduct, productToGeneEndY
					, objCount );
		} else {
			if ( otherMainProducts.size() > 0 )
			this.drawSidelineProductGeneEdge( svg, doc, reactionPopupSVGGroup
					, reaction, otherMainProducts.get(otherMainProducts.size()-1)
					, productToGeneEndY, objCount );
		}

		// reaction name
		OMSVGElement reactionText = null;
		if ( reaction.getGenes().size() == 0 ) {
			reactionText = this.createReactionText( doc
					, 0, reaction.getGenePlacementY()
					, reaction.getBaseReaction().getEcNums().toString(), objCount );
		} else {
			float geneMidline = reaction.getGenes().get(0).getRelY();
			if ( genes.size() > 1 ) {
				geneMidline = (genes.get(genes.size()-1).getRelY() + geneMidline)/2;
			}
			reactionText = this.createReactionText( doc
					, 0, geneMidline
					, reaction.getBaseReaction().getEcNums().toString(), objCount );
		}
		reactionPopupSVGGroup.appendChild(reactionText);

		// popup
		this.setPopup( reactionPopupSVGGroup, pathwayPopup
				, reaction.toHtml(true), false );

		// main reactant
		if ( mainReactant != null ) {
			this.drawCompound( svg, doc, reactionSVGGroup, mainReactant, 0
					, objCount );
		}
		// Other main reactants
		for ( ViewCompound reactant : otherMainReactants ) {
			this.drawCompound( svg, doc, reactionSVGGroup, reactant
					, this.sidelineOffset, objCount );
		}
		// genes
		for ( ViewGene gene : genes ) {
			this.drawGene( svg, doc, reactionSVGGroup, gene
					, 0, reaction.getGenePlacementY(), objCount );
		}
		// empty gene
		if ( genes.size() == 0 ) {
			this.drawGene( svg, doc, reactionSVGGroup, null
					, 0, reaction.getGenePlacementY(), objCount );
		}
		// Other main products
		for ( ViewCompound product : otherMainProducts ) {
			this.drawCompound( svg, doc, reactionSVGGroup, product
					, this.sidelineOffset, objCount );
		}
		// end of pathway means drawing the mainline product explicitly
		if ( isLastReaction ) {
			if ( mainProduct != null ) {
				this.drawCompound( svg, doc, reactionSVGGroup, mainProduct, 0
						, objCount );
			}
		}

		// secondary metabolites
		float secondaryReactantY = 0;
		float secondaryProductY = 0;
		if ( genes.size() == 0 || genes.size() == 1 ) {
			secondaryReactantY = reaction.getGenePlacementY() - 0.8f*this.mainSymbolRadius;
			secondaryProductY = reaction.getGenePlacementY() + 0.8f*this.mainSymbolRadius;
		} else {
			secondaryReactantY = genes.get(0).getRelY();
			secondaryProductY = genes.get(genes.size()-1).getRelY();
		}
		drawSecondaryMetabolites( svg, doc, reactionSVGGroup
				, reaction.getSecondaryReactants(), true, secondaryReactantY
				, objCount );
		drawSecondaryMetabolites( svg, doc, reactionSVGGroup
				, reaction.getSecondaryProducts(), false, secondaryProductY
				, objCount );

		svgGroup.appendChild(reactionSVGGroup);
	}

	public void addArrowDef( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGDefsElement defs, ObjectCount objCount ) {
		// arrow
		OMSVGPolygonElement arrowPolygon = doc.createSVGPolygonElement();
		objCount.svgObjCount++;
		arrowPolygon.setId(ARROW_DEF_ID);
		OMSVGPointList arrowPoints = arrowPolygon.getPoints();
		arrowPoints.appendItem(svg.createSVGPoint(-3, -3));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(0, 4));
		objCount.svgObjCount++;
		arrowPoints.appendItem(svg.createSVGPoint(3, -3));
		objCount.svgObjCount++;
		arrowPolygon.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_BLACK_VALUE);

		defs.appendChild(arrowPolygon);
	}

	public void addPathwayStyles( OMSVGSVGElement svg, OMSVGDocument doc ) {
		OMSVGStyleElement style = doc.createSVGStyleElement();
		style.setType(SVGConstants.CSS_TYPE);
		style.appendChild( doc.createTextNode(
				" ." + DrawUtil.BASE_SYMBOL_CLASS
				+ " { stroke: black; stroke-width: 1px; fill: white; }"
				+ " ." + DrawUtil.HIGHLIGHTED_SYMBOL_CLASS
				+ " { stroke: #9900ff; stroke-width: 2px; fill: white }"
				+ " ." + DrawUtil.BASE_TEXT_CLASS
				+ " { fill: black; }"
				+ " ." + DrawUtil.HIGHLIGHT_TEXT_CLASS
				+ " { fill: #9900ff; }"
				+ " ." + DrawUtil.BASE_SECONDARY_SYMBOL_CLASS
				+ " { fill: #9900ff; }"
				+ " " )
		);
		svg.appendChild(style);
	}

	/**
	 * Draw pathway graph with text labels.
	 * TODO: parameterize whether text labels will be drawn.
	 *
	 * @param pathways
	 * @param svgWidth
	 * @param svgHeight
	 * @param objCount
	 * @return
	 */
	public SVGData createPathwaySvg( List<ViewPathway> pathways
			, float svgWidth, float svgHeight, ObjectCount objCount ) {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		objCount.svgObjCount++;
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);
		svg.setWidth( OMSVGLength.SVG_LENGTHTYPE_PX, svgWidth);

		svg.setClassNameBaseVal(ELEMENT_GRAPH_CLASS_NAME);

		OMSVGDefsElement defs = doc.createSVGDefsElement();
		objCount.svgObjCount++;
		svg.appendChild(defs);
		addArrowDef(svg, doc, defs, objCount);

		addPathwayStyles(svg, doc);

		// all visual elements will be added to viewport group,
		//	which is used for "zooming"
		OMSVGGElement viewport = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, VIEWPORT_SVG_GROUP_CLASS, 0, 0, objCount);
		svg.appendChild(viewport);

		float pathwayLengthOffset = 0;
		for ( ViewPathway pathway : pathways ) {
			pathwayLengthOffset += this.pathwaySpacing;
			OMSVGGElement pathwaySVGGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
					, null
					, pathway.getId()
					, 0, pathwayLengthOffset
					, objCount );

			float reactionLengthOffset = 0;
			ArrayList<ViewReaction> viewReactions = pathway.getViewReactions();
			for ( int i=0; i<viewReactions.size(); i++ ) {
				ViewReaction reaction = pathway.getViewReactions().get(i);
				this.drawReaction( svg, doc
						, pathwaySVGGroup
						, reaction
						, reactionLengthOffset, ( i == viewReactions.size()-1 )
						, objCount
				);
				reactionLengthOffset += reaction.getTotalLength();
			}

			viewport.appendChild(pathwaySVGGroup);
			pathwayLengthOffset += reactionLengthOffset + this.pathwaySpacing;
		}
		svg.setHeight( OMSVGLength.SVG_LENGTHTYPE_PX, svgHeight );
		return new SVGData(svg, viewport
				, this.pathwaySvgWidth, pathwayLengthOffset);
	}

	/**
	 * Draw pathway guide color strip.
	 * @param lengths
	 * @param cssColors
	 * @param svgWidth
	 * @param svgHeight
	 * @param objCount
	 * @return
	 */
	public SVGData createColorSegmentsSvg( float[] lengths, String[] cssColors
			, float svgWidth, float svgHeight, ObjectCount objCount ) {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		objCount.svgObjCount++;
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);
		svg.setWidth( OMSVGLength.SVG_LENGTHTYPE_PX, svgWidth);
		svg.setHeight( OMSVGLength.SVG_LENGTHTYPE_PX, svgHeight );

		svg.setClassNameBaseVal(ELEMENT_GRAPH_CLASS_NAME);

		// all visual elements will be added to viewport group,
		//	which is used for "scrolling" and "zooming"
		OMSVGGElement viewport = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, VIEWPORT_SVG_GROUP_CLASS, 0, 0, objCount);
		svg.appendChild(viewport);

		float lengthOffset = 0;
		for ( int i=0; i< lengths.length; i++ ) {
			float length = lengths[i];
			OMSVGRectElement segment = doc.createSVGRectElement(
					0, lengthOffset, svgWidth, length, 0, 0 );
			segment.getStyle().setSVGProperty(
					SVGConstants.CSS_FILL_PROPERTY
					, cssColors[i]
			);
			viewport.appendChild(segment);
			lengthOffset += length;
		}
		return new SVGData(svg, viewport
				, svgWidth, lengthOffset);
	}

	/**
	 * Includes tooltips and empty element logic.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param gene
	 * @param cx
	 * @param emptyGeneY
	 * @param measurement
	 * @param heatMapUtil
	 * @param rangeMin
	 * @param rangeMax
	 * @param objCount
	 */
	public void drawGeneExpData( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewGene gene
			, float cx, float emptyGeneY
			, Measurement measurement, HeatMapUtil heatMapUtil
			, float rangeMin, float rangeMax
			, ObjectCount objCount ) {
		if ( gene == null ) {
			return;
		}
		String id = null;
		float relY = emptyGeneY;
		String fillCssColor = SVGConstants.CSS_WHITE_VALUE;
		String strokeCssColor = SVGConstants.CSS_LIGHTGREY_VALUE;

			id = gene.getBaseObject().getVimssId();
			relY = gene.getRelY();

		if ( measurement != null ) {
			fillCssColor = heatMapUtil.calculateCssColor( measurement.getValue()
					, rangeMin, rangeMax );
			strokeCssColor = SVGConstants.CSS_WHITE_VALUE;
		}	// otherwise, draw empty square with grey outline by default

		OMSVGGElement geneGroup = SVGUtil.createTranslatedSVGGroup(
				svg, doc, id, null
				, cx, relY, objCount );
		svgGroup.appendChild(geneGroup);
		OMSVGRectElement geneSvg = this.createGeneSymbol( doc
				, 0, 0, fillCssColor, strokeCssColor
				, objCount );
		geneGroup.appendChild(geneSvg);

		// popup
		if ( measurement != null ) {
			setPopup( geneSvg, dataPopup
					, "<span class='type'>Gene</span>: " + gene.toHtml()
					+ "<br/>" + measurement.toHtml(true)
					, true
			);
		}

	}

	/**
	 * Includes tooltips and empty element logic.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param cx
	 * @param measurement
	 * @param heatMapUtil
	 * @param rangeMin
	 * @param rangeMax
	 * @param objCount
	 */
	public void drawReactionExpData( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewReaction reaction
			, float cx, Measurement measurement
			, HeatMapUtil heatMapUtil, float rangeMin, float rangeMax
			, ObjectCount objCount ) {
		float relStartY = reaction.getGenePlacementY();
		float relEndY = reaction.getGenePlacementY();
		if ( reaction.getGenes().size() > 1 ) {
			relEndY = reaction.getGenes().get(reaction.getGenes().size()-1).getRelY();
		}

		String fillCssColor = SVGConstants.CSS_WHITE_VALUE;
		String strokeCssColor = SVGConstants.CSS_LIGHTGREY_VALUE;
		if ( measurement != null ) {
			fillCssColor = heatMapUtil.calculateCssColor( measurement.getValue()
					, rangeMin, rangeMax );
			strokeCssColor = SVGConstants.CSS_WHITE_VALUE;
		}

		OMSVGElement reactionSvg = this.createReactionSymbol( doc
				, cx, relStartY, relEndY-relStartY
				, fillCssColor, strokeCssColor
				, objCount );

		if ( measurement != null ) {
			// popup
			setPopup( reactionSvg, dataPopup
					, "<span class='type'>Reaction</span>: "
					+ reaction.toHtml(false)
					+ "<br/>" + measurement.toHtml(true)
					, true
			);
		}

		svgGroup.appendChild(reactionSvg);
	}

	/**
	 * Includes tooltips and empty element logic.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param compound
	 * @param cx
	 * @param measurement
	 * @param heatMapUtil
	 * @param rangeMin
	 * @param rangeMax
	 * @param objCount
	 */
	public void drawCompoundExpData( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewCompound compound
			, float cx, Measurement measurement
			, HeatMapUtil heatMapUtil, float rangeMin, float rangeMax
			, ObjectCount objCount ) {
		String fillCssColor = SVGConstants.CSS_WHITE_VALUE;
		String strokeCssColor = SVGConstants.CSS_LIGHTGREY_VALUE;
		if ( measurement != null ) {
			fillCssColor = heatMapUtil.calculateCssColor( measurement.getValue()
					, rangeMin, rangeMax );
			strokeCssColor = SVGConstants.CSS_WHITE_VALUE;
		}

		OMSVGElement compoundSvg = this.createCompoundSymbol( doc
				, cx, compound.getRelY()
				, objCount );
		SVGUtil.setSymbolStyle( compoundSvg, fillCssColor, strokeCssColor, "1px" );

		if ( measurement != null ) {
			// popup
			setPopup( compoundSvg, dataPopup
					, "<span class='type'>Compound</span>: "
					+ compound.toHtml()
					+ "<br/>" + measurement.toHtml(true)
					, true
			);
		}

		svgGroup.appendChild(compoundSvg);
	}

	/**
	 * Draw reaction experiment data.
	 * Switch logic uses elementType to determine whether the diagram will
	 * be for compounds/metabolites, genes, or reactions.
	 *
	 * @param svg
	 * @param doc
	 * @param svgGroup
	 * @param reaction
	 * @param reactionLengthOffset
	 * @param isLastReaction
	 * @param sample
	 * @param dataType
	 * @param elementType
	 * @param heatMapUtil
	 * @param rangeMin
	 * @param rangeMax
	 * @param objCount
	 */
	public void drawReactionExpDataByType( OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, ViewReaction reaction
			, float reactionLengthOffset, boolean isLastReaction
			, Sample sample, String dataType, String elementType
			, HeatMapUtil heatMapUtil, float rangeMin, float rangeMax
			, ObjectCount objCount
	) {
		// container group
		OMSVGGElement reactionSVGGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
				, null
				, reaction.getBaseReaction().getGuid()
				, 0, reactionLengthOffset, objCount );

		// values to be used in drawing
		HashMap<String,Measurement> elementIdDataTypeMeasurementMap
			= sample.getElementIdDataTypeMeasurementMap();

		if ( elementType.equals( TargetType.GENE.toString() ) ) {
			ArrayList<ViewGene> genes = reaction.getGenes();
			// genes
			for ( ViewGene gene : genes ) {
				Measurement measurement = elementIdDataTypeMeasurementMap
						.get(gene.getBaseObject().getVimssId() + dataType);
				this.drawGeneExpData( svg, doc, reactionSVGGroup
						, gene, 0, reaction.getGenePlacementY()
						, measurement
						, heatMapUtil, rangeMin, rangeMax
						, objCount );
			}
		} else if ( elementType.equals( TargetType.REACTION.toString() ) ) {
			// reaction
			Measurement measurement = elementIdDataTypeMeasurementMap
					.get(reaction.getBaseReaction().getGuid() + dataType);
			this.drawReactionExpData( svg, doc, reactionSVGGroup, reaction
					, 0, measurement
					, heatMapUtil, rangeMin, rangeMax
					, objCount );
		} else if ( elementType.equals( TargetType.COMPOUND.toString() ) ) {
			ViewCompound mainReactant = reaction.getMainReactant();
			ArrayList<ViewCompound> otherMainReactants = reaction.getOtherMainReactants();
			ArrayList<ViewCompound> otherMainProducts = reaction.getOtherMainProducts();
			ViewCompound mainProduct = reaction.getMainProduct();
			// main reactant
			if ( mainReactant != null ) {
				Measurement measurement = elementIdDataTypeMeasurementMap.get(
						mainReactant.getBaseObject().getGuid() + dataType );
				this.drawCompoundExpData(svg, doc, reactionSVGGroup
						, mainReactant, 0, measurement
						, heatMapUtil, rangeMin, rangeMax
						, objCount);
			}
			// Other main reactants
			for ( ViewCompound reactant : otherMainReactants ) {
				Measurement measurement = elementIdDataTypeMeasurementMap.get(
						reactant.getBaseObject().getGuid() + dataType );
				this.drawCompoundExpData(svg, doc, reactionSVGGroup
						, reactant, 0, measurement
						, heatMapUtil, rangeMin, rangeMax
						, objCount);
			}
			// Other main products
			for ( ViewCompound product : otherMainProducts ) {
				Measurement measurement = elementIdDataTypeMeasurementMap.get(
						product.getBaseObject().getGuid() + dataType );
				this.drawCompoundExpData(svg, doc, reactionSVGGroup
						, product, 0, measurement
						, heatMapUtil, rangeMin, rangeMax
						, objCount);
			}
			// end of pathway means drawing the mainline product explicitly
			if ( isLastReaction ) {
				if ( mainProduct != null ) {
					Measurement measurement = elementIdDataTypeMeasurementMap.get(
							mainProduct.getBaseObject().getGuid() + dataType );
					this.drawCompoundExpData(svg, doc, reactionSVGGroup
							, mainProduct, 0, measurement
							, heatMapUtil, rangeMin, rangeMax
							, objCount);
				}
			}
		}

		svgGroup.appendChild(reactionSVGGroup);
	}

	/**
	 * Entry point method to draw experiment data.
	 *
	 * @param pathways
	 * @param experiments
	 * @param dataType
	 * @param elementType
	 * @param svgWidth
	 * @param svgHeight
	 * @param heatMapUtil
	 * @param rangeMin
	 * @param rangeMax
	 * @param objCount
	 * @return
	 */
	public SVGData createExperimentDataSvg(
			List<ViewPathway> pathways
			, List<Experiment> experiments
			, String dataType, String elementType
			, float svgWidth, float svgHeight
			, HeatMapUtil heatMapUtil, float rangeMin, float rangeMax
			, ObjectCount objCount
	) {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		objCount.svgObjCount++;
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		// all visual elements will be added to viewport group,
		//	which is used for "zooming"
		OMSVGGElement viewport = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, VIEWPORT_SVG_GROUP_CLASS, 0, 0, objCount);
		svg.appendChild(viewport);

		int totalSampleIndex = 0;
		float pathwayLengthOffset = 0;
		for ( Experiment experiment : experiments ) {
			for ( Sample sample : experiment.getSamples() ) {
				// reset pathwayLengthOffset for each sample
				pathwayLengthOffset = 0;
				OMSVGGElement sampleSVGGroup = SVGUtil.createTranslatedSVGGroup(
						svg, doc
						, null
						, sample.getId()
						, this.mainSymbolRadius
								+ totalSampleIndex*this.mainSymbolDiameter
						, 0
						, objCount );
				for ( ViewPathway pathway : pathways ) {
					pathwayLengthOffset += this.pathwaySpacing;
					OMSVGGElement pathwaySVGGroup = SVGUtil.createTranslatedSVGGroup(
							svg, doc, null, pathway.getId()
							, 0, pathwayLengthOffset
							, objCount );

					// reset reactionLengthOffset for each pathway
					float reactionLengthOffset = 0;
					ArrayList<ViewReaction> viewReactions = pathway.getViewReactions();
					for ( int i=0; i<viewReactions.size(); i++ ) {
						ViewReaction reaction = pathway.getViewReactions().get(i);
						this.drawReactionExpDataByType( svg, doc
								, pathwaySVGGroup
								, reaction
								, reactionLengthOffset
								, ( i == viewReactions.size()-1 )
								, sample, dataType, elementType
								, heatMapUtil, rangeMin, rangeMax
								, objCount
						);
						reactionLengthOffset += reaction.getTotalLength();
					}

					sampleSVGGroup.appendChild(pathwaySVGGroup);
					pathwayLengthOffset += reactionLengthOffset + this.pathwaySpacing;
				}
				totalSampleIndex++;
				viewport.appendChild(sampleSVGGroup);
			}
		}
		svg.setWidth( OMSVGLength.SVG_LENGTHTYPE_PX, svgWidth );
		svg.setHeight( OMSVGLength.SVG_LENGTHTYPE_PX, svgHeight );
		return new SVGData( svg, viewport
				, totalSampleIndex*this.mainSymbolDiameter, pathwayLengthOffset );
	}

	public OMSVGElement createSampleText(
			OMSVGSVGElement svg, OMSVGDocument doc
			, float cx, float cy, String classValue, String text
			, ObjectCount objCount ) {
		OMSVGTextElement svgText = doc.createSVGTextElement(
				cx + 2, cy + mainSymbolRadius/2
				, OMSVGLength.SVG_LENGTHTYPE_PX
				, text );

		objCount.svgObjCount++;
		svgText.setClassNameBaseVal(classValue);
		svgText.getStyle().setSVGProperty(
				SVGConstants.CSS_FONT_SIZE_PROPERTY, "7pt" );
//		svgText.getStyle().setSVGProperty(
//				SVGConstants.CSS_TEXT_ANCHOR_PROPERTY, SVGConstants.CSS_END_VALUE );

		return svgText;
	}

	/**
	 *
	 * Entry point method to draw the (horizontal) strip
	 * with experiment sample information.
	 * @param experiments
	 * @param svgWidth
	 * @param svgHeight
	 * @param objCount
	 * @return
	 */
	public SVGData createExperimentGuideSvg(
			List<Experiment> experiments
			, float svgWidth
			, float svgHeight, ObjectCount objCount
	) {
		OMSVGDocument doc = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = doc.createSVGSVGElement();
		objCount.svgObjCount++;
		svg.setAttribute(SVGConstants.XMLNS_PREFIX, "http://www.w3.org/2000/svg");
		svg.setAttribute(SVGConstants.SVG_VERSION_ATTRIBUTE, SVGConstants.SVG_VERSION);

		// all visual elements will be added to viewport group,
		//	which is used for "zooming"
		OMSVGGElement viewport = SVGUtil.createTranslatedSVGGroup(svg, doc
				, null, VIEWPORT_SVG_GROUP_CLASS, 0, 0, objCount);
		svg.appendChild(viewport);

		// Transform group: the concept behind this SVG is to draw text
		//	in the typical format: left-to-right text (horizontal),
		//	then transform the SVG elements so that the text becomes vertical
		OMSVGGElement transformGroup = SVGUtil.createTranslatedSVGGroup( svg, doc
				, null, null, 0, 0, objCount );
		float rotationCenterOffset = 0.5f * svgHeight;
		OMSVGTransform rotate = svg.createSVGTransform();
		rotate.setRotate(-90, rotationCenterOffset, rotationCenterOffset);
		transformGroup.getTransform().getBaseVal().appendItem(rotate);
		viewport.appendChild(transformGroup);

		int totalSampleIndex = 0;
		for ( int i=0; i< experiments.size(); i++ ) {
			Experiment experiment = experiments.get(i);

			// Color differentiating b/n experiments
			String fillColor = DrawUtil.EXPERIMENT_EVEN_SEG_CSS_COLOR;
			if ( (i%2) == 1 ) {
				fillColor = DrawUtil.EXPERIMENT_ODD_SEG_CSS_COLOR;
			}

			// Sample text
			for ( Sample sample : experiment.getSamples() ) {
				OMSVGGElement sampleSVGGroup = SVGUtil.createTranslatedSVGGroup(
						svg, doc
						, null
						, sample.getId()
						, 0
						, mainSymbolRadius
							+ totalSampleIndex*mainSymbolDiameter
						, objCount );
				transformGroup.appendChild(sampleSVGGroup);

				// pop-up
				this.setPopup( sampleSVGGroup, sampleGuidePopup
						, "<span class='title'>Sample</span>: " + sample.toHtml()
							+ "<br/><span class='title'>Experiment</span>: "
							+ sample.getExperiment().toHtml()
						, true );

				// color background and popup-triggering element
				OMSVGRectElement sampleRect = doc.createSVGRectElement(
						0, -mainSymbolRadius
						, 3*svgHeight, mainSymbolDiameter, 1, 1 );
				sampleRect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fillColor);
				objCount.svgObjCount++;
				sampleSVGGroup.appendChild(sampleRect);


				// text
				OMSVGElement text = this.createSampleText( svg, doc
						, 0, 0, BASE_TEXT_CLASS, sample.getId()
						, objCount );
				objCount.svgObjCount++;
				sampleSVGGroup.appendChild(text);

				totalSampleIndex++;
			}
		}
		svg.setWidth( OMSVGLength.SVG_LENGTHTYPE_PX, svgWidth );
		svg.setHeight( OMSVGLength.SVG_LENGTHTYPE_PX, svgHeight );
		return new SVGData( svg, viewport
				, totalSampleIndex*this.mainSymbolDiameter, svgHeight );
	}

	/**
	 * Calculate the vertical offset positions of the main pathway diagram's
	 * components and sub-components.
	 *
	 * @param pathways
	 */
	public void setPathwayComponentPositions( List<ViewPathway> pathways ) {
		for ( ViewPathway pathway : pathways ) {
			float totalPathwayLength = 0;
			ArrayList<ViewReaction> viewReactions = pathway.getViewReactions();
			for ( int i=0; i< viewReactions.size(); i++ ) {
				ViewReaction viewReaction = viewReactions.get(i);
				float reactionTotalLength = this.calculateReactionComponentPositions(
						viewReaction
						, ( i == viewReactions.size()-1 )
				);
				viewReaction.setTotalLength(reactionTotalLength);
				totalPathwayLength += reactionTotalLength;
			}
			pathway.setTotalLength(totalPathwayLength);
		}
	}

	/**
	 * Calculate the vertical offset positions of the main pathway diagram's
	 * reaction components and sub-components.
	 *
	 * @param reaction
	 * @param isLastReaction
	 * @return
	 */
	protected float calculateReactionComponentPositions(
			ViewReaction reaction
			, boolean isLastReaction
	) {
		float reactionTotalLength = 0;
		// main reactant
		ViewCompound mainReactant = reaction.getMainReactant();
		if ( mainReactant != null ) {
			mainReactant.setRelY(0);
		}
		// Other main reactants
		ArrayList<ViewCompound> otherMainReactants = reaction.getOtherMainReactants();
		int sidelineReactantCount = otherMainReactants.size();
		for ( int i=0; i<sidelineReactantCount; i++ ) {
			if ( i == 0 ) {
				reactionTotalLength += mainSymbolDiameter;
			} else {
				reactionTotalLength += mainSymbolDiameter*1.5;
			}
			otherMainReactants.get(i).setRelY(reactionTotalLength);
		}
		// adjust "otherMainReactant" layout for extra distance from gene(s)
		//	if there is no mainReactant and exactly one otherMainReactant
		if ( mainReactant == null && sidelineReactantCount == 1 ) {
			reactionTotalLength += mainSymbolDiameter*0.5;
		} else if ( sidelineReactantCount == 0 ) {
			// placeholder spacing if there are no sideline reactants
			reactionTotalLength += mainSymbolDiameter;
		}
		// adjust layout for extra distance for secondaryReactants
		//	between genes and otherMainReactants
		//	if there are secondaryReactants, there are otherMainReactants,
		//	and there is zero or one gene
		ArrayList<ViewGene> genes = reaction.getGenes();
		if ( reaction.getSecondaryReactants().size() > 0
				&& sidelineReactantCount > 0
				&& genes.size() <= 1 ) {
			reactionTotalLength += mainSymbolRadius;
		}
		// genes
		reaction.setGenePlacementY(reactionTotalLength+mainSymbolDiameter);
		for ( int i=0; i<genes.size(); i++ ) {
			ViewGene gene = genes.get(i);
			reactionTotalLength += mainSymbolDiameter;
			gene.setRelY(reactionTotalLength);
		}
		// if no genes, placeholder
		if ( genes.size() == 0 ) {
			reactionTotalLength += mainSymbolDiameter;
		}
		// adjust layout for extra distance for secondaryProducts
		//	between genes and otherMainProducts
		//	if there are secondaryProducts, there are otherMainProducts,
		//	and there is zero or one gene
		ArrayList<ViewCompound> otherMainProducts = reaction.getOtherMainProducts();
		int sidelineProductCount = otherMainProducts.size();
		if ( reaction.getSecondaryProducts().size() > 0
				&& sidelineProductCount > 0
				&& genes.size() <= 1 ) {
			reactionTotalLength += mainSymbolRadius;
		}
		// Other main products
		for ( int i=0; i<sidelineProductCount; i++ ) {
			if ( i == 0 ) {
				reactionTotalLength += mainSymbolDiameter;
			} else {
				reactionTotalLength += mainSymbolDiameter*1.5;
			}
			otherMainProducts.get(i).setRelY(reactionTotalLength);
		}
		// final length
		if ( sidelineProductCount > 0 ) {
			reactionTotalLength += mainSymbolDiameter;
		} else {
			reactionTotalLength += 2*mainSymbolDiameter;
		}
		// end of pathway means drawing the mainline product explicitly
		ViewCompound mainProduct = reaction.getMainProduct();
		if ( mainProduct != null ) {
			mainProduct.setRelY(reactionTotalLength);
		} else {
			// adjust "otherMainProduct" layout for extra distance from gene(s)
			//	if there is no mainProduct and exactly one otherMainProduct
			if ( sidelineProductCount == 1 ) {
				otherMainProducts.get(0).setRelY(
						reactionTotalLength - (mainSymbolDiameter*0.5f) );
				reactionTotalLength += (mainSymbolDiameter*0.5);
			}
		}

		return reactionTotalLength;
	}

	public void setMainSymbolSize( float size ) {
		this.mainSymbolDiameter = size;
		this.mainSymbolRadius = this.mainSymbolDiameter/2;
		this.sidelineOffset = -this.mainSymbolDiameter;
		this.pathwaySpacing = this.mainSymbolDiameter*2;

		this.pathwaySvgWidth = this.mainlineCenterAxis+2*this.mainSymbolDiameter;
		this.pathwaySvgWidth = ( DEFAULT_SVG_WIDTH < this.pathwaySvgWidth ) ? this.pathwaySvgWidth : DEFAULT_SVG_WIDTH;
		this.nodeNameOffset = ( (DEFAULT_NODE_NAME_OFFSET > -3*this.mainSymbolDiameter) ?
				-3*this.mainSymbolDiameter : DEFAULT_NODE_NAME_OFFSET );
		this.reactionNameOffset = ( (DEFAULT_REACTION_NAME_OFFSET > -3*this.mainSymbolDiameter - 160 ) ?
				-3*this.mainSymbolDiameter - 160 : DEFAULT_REACTION_NAME_OFFSET );
		this.secondaryNameOffset = -2*this.mainSymbolDiameter;
	}

	/**
	 * Length data of all the pathways.
	 * Ratios of the lengths of each pathway in the list to the total length.
	 *
	 * @param pathways
	 * @return
	 */
	public LengthsAndRatios getPathwayLengthsAndRatios( List<ViewPathway> pathways ) {
		float[] lengths = new float[pathways.size()];
		float[] ratios = new float[pathways.size()];

		float totalLength = 0;
		for ( int i=0; i< ratios.length; i++ ) {
			ViewPathway pathway = pathways.get(i);
			lengths[i] = this.pathwaySpacing
				+ pathway.getTotalLength() + this.pathwaySpacing;
			totalLength += lengths[i];
		}

		for ( int i=0; i< ratios.length; i++ ) {
			ratios[i] = lengths[i]/totalLength;
		}
		return new LengthsAndRatios( lengths, totalLength, ratios );
	}

	/**
	 * Length data of all the experiments.
	 * Ratios of the widths of each experiment in the list to the total width.
	 *
	 * @param experiments
	 * @return
	 */
	public LengthsAndRatios getExperimentLengthsAndRatios(
			List<Experiment> experiments ) {
		int experimentsSize = experiments.size();
		int[] expSampleCount = new int[experimentsSize];
		int total = 0;
		for ( int i=0; i<experimentsSize; i++ ) {
			Experiment experiment = experiments.get(i);
			expSampleCount[i] = experiment.getSamples().size();
			total += expSampleCount[i];
		}
		float[] lengths = new float[experimentsSize];
		float[] ratios = new float[experimentsSize];
		for ( int i=0; i<experiments.size(); i++ ) {
			lengths[i] = (float)expSampleCount[i]*mainSymbolDiameter;
			ratios[i] = (float)expSampleCount[i]/(float)total;
		}
		return new LengthsAndRatios( lengths, total*mainSymbolDiameter, ratios );
	}

	@SuppressWarnings("unused")
	public float getSecondaryMetabKeyTotalLength(
			BinarySortedSet<ViewCompound> secondaryMetabList ) {
		float totalLength = mainSymbolDiameter;
		for ( ViewCompound compoundSymbol : secondaryMetabList ) {
			totalLength += 1.5f*this.mainSymbolDiameter;
		}
		return totalLength;
	}
}
