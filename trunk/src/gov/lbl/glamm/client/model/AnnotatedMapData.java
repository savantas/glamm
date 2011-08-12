package gov.lbl.glamm.client.model;

import gov.lbl.glamm.client.model.GlammPrimitive.Xref;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.ui.SVGResource;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.Node;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;


/**
 * Wrapper class for GLAMM annotated svg data.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class AnnotatedMapData implements Serializable {

	public static final String ATTRIBUTE_ABSENT				= "absent";
	public static final String ATTRIBUTE_CLASS 				= "class";
	public static final String ATTRIBUTE_CPD_DST			= "cpddst";
	public static final String ATTRIBUTE_CPD_SRC			= "cpdsrc";
	public static final String ATTRIBUTE_DEFAULT_COLOR		= "defaultcolor";
	public static final String ATTRIBUTE_ENZYME				= "enzyme";
	public static final String ATTRIBUTE_HAS_DATA			= "hasdata";
	public static final String ATTRIBUTE_HEIGHT				= "height";
	public static final String ATTRIBUTE_ID					= "id";
	public static final String ATTRIBUTE_KEGGID				= "keggid";
	public static final String ATTRIBUTE_QUERY				= "query";
	public static final String ATTRIBUTE_REACTION			= "reaction";
	public static final String ATTRIBUTE_ROUTE				= "route";
	public static final String ATTRIBUTE_SEARCH_TARGET		= "searchtarget";
	public static final String ATTRIBUTE_STATE				= "state";
	public static final String ATTRIBUTE_WIDTH				= "width";

	public static final String CLASS_BACKGROUND 	= "background";
	public static final String CLASS_CPD 			= "cpd";
	public static final String CLASS_MAP 			= "map";
	public static final String CLASS_RXN 			= "rxn";

	public static final String STATE_DEFAULT		= "default";
	public static final String STATE_MOUSEOVER		= "mouseover";
	public static final String STATE_SELECTED		= "selected";

	public static final String VIEWPORT_ID	= "viewport";

	private OMSVGSVGElement	svgRoot 	= null;
	private OMSVGGElement 	viewport 	= null;
	private Set<String> cpdDbNames = null;
	private String mapId = null;
	private Set<String> rxnDbNames = null;

	private Map<String, Set<OMSVGElement>> id2SvgElements = null;
	private Set<OMSVGElement> cpdSvgElements = null;
	private Set<OMSVGElement> rxnSvgElements = null;

	private AnnotatedMapData() {}

	/**
	 * Constructor
	 * @param resource The SVGResource containing the annotated map
	 */

	private AnnotatedMapData(final String mapId, final String[] cpdDbNames, final String[] rxnDbNames) {

		this.mapId = mapId;

		this.cpdDbNames = new HashSet<String>();
		for(int i = 0; i < cpdDbNames.length; i++) 
			this.cpdDbNames.add(cpdDbNames[i]);

		this.rxnDbNames = new HashSet<String>();
		for(int i = 0; i < rxnDbNames.length; i++) 
			this.rxnDbNames.add(rxnDbNames[i]);

		// allocate space for id2SvgElements
		id2SvgElements = new HashMap<String, Set<OMSVGElement>>();
		cpdSvgElements = new HashSet<OMSVGElement>();
		rxnSvgElements = new HashSet<OMSVGElement>();
	}

	public static AnnotatedMapData createFromSvgResource(final SVGResource resource, 
			final String mapId, 
			final String[] cpdDbNames, 
			final String[] rxnDbNames) {
		AnnotatedMapData mapData = new AnnotatedMapData(mapId, cpdDbNames, rxnDbNames);
		mapData.svgRoot = resource.getSvg();
		mapData.init();
		return mapData;
	}

	public static AnnotatedMapData createFromRawSvg(final String rawSvg, 
			final String mapId, 
			final String[] cpdDbNames, 
			final String[] rxnDbNames) {
		AnnotatedMapData mapData = new AnnotatedMapData(mapId, cpdDbNames, rxnDbNames);
		mapData.svgRoot = OMSVGParser.parse(rawSvg);
		mapData.init();
		return mapData;
	}

	public static AnnotatedMapData createFromUrl(final String url, 
			final String mapId, 
			final String[] cpdDbNames, 
			final String[] rxnDbNames, 
			final Command onLoad) {

		final AnnotatedMapData mapData = new AnnotatedMapData(mapId, cpdDbNames, rxnDbNames);

		try {
			new RequestBuilder(RequestBuilder.GET, url).sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					mapData.svgRoot = OMSVGParser.parse(response.getText());
					mapData.init();
					onLoad.execute();
				}
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Could not open " + mapId + " at " + url);
				}
			});
		} catch(RequestException e) {
			Window.alert(e.getMessage());
		}

		return mapData;
	}


	/**
	 * Accessor
	 * @return The names of the databases of compounds associated with this map.
	 */
	public Set<String> getCpdDbNames() {
		return cpdDbNames;
	}

	/**
	 * Accessor
	 * @return The SVG elements corresponding to compounds on this map.
	 */
	public Set<OMSVGElement> getCpdSvgElements() {
		return cpdSvgElements;
	}

	/**
	 * Accessor
	 * @return The map's ID.
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * Accessor
	 * @return The names of the databases of reactions associated with this map.
	 */
	public Set<String> getRxnDbNames() {
		return rxnDbNames;
	}

	/**
	 * Accessor
	 * @return The SVG elements corresponding to reactions on this map.
	 */
	public Set<OMSVGElement> getRxnSvgElements() {
		return rxnSvgElements;
	}

	/**
	 * Gets the root SVG element.
	 * @return OMSVGSVGElement the root element
	 */
	public OMSVGSVGElement getSvg() {
		return svgRoot;
	}

	/**
	 * Gets the set of SVG elements associated with this id
	 * @param id
	 * @return HashSet<OMSVGElement> the elements associated with this id, null if none.
	 */
	public Set<OMSVGElement> getSvgElementsForId(final String id) {
		return id2SvgElements.get(id);
	}

	/**
	 * Gets the set of SVG elements associated with this GlammPrimitive
	 * @param primitive
	 * @return HashSet<OMSVGElement> the elements associated with this primitive, null if none.
	 */
	public Set<OMSVGElement> getSvgElementsForGlammPrimitive(final GlammPrimitive primitive) {

		if(primitive.getType() == Compound.TYPE) {
			Xref xref = primitive.getXrefForDbNames(getCpdDbNames());
			String id = xref.getXrefId();
			if(id != null)
				return getSvgElementsForId(id);
		}
		else if(primitive.getType() == Reaction.TYPE) {
			Xref xref = primitive.getXrefForDbNames(getRxnDbNames());
			String id = xref.getXrefId();
			if(id != null)
				return getSvgElementsForId(id);
		}
		else if(primitive.getType() == Gene.TYPE) {
			Gene gene = (Gene) primitive;
			Set<String> ecNums = gene.getEcNums();
			if(ecNums == null)
				return null;
			Set<OMSVGElement> allSvgElements = null;
			for(String ecNum : ecNums) {
				Set<OMSVGElement> svgElements = getSvgElementsForId(ecNum);
				if(svgElements == null || svgElements.isEmpty())
					continue;
				if(allSvgElements == null) 
					allSvgElements = new HashSet<OMSVGElement>();
				allSvgElements.addAll(svgElements);
			}
			return allSvgElements;
		}


		return null;
	}

	/**
	 * Gets the set of SVG elements associated with this collection of GlammPrimitives
	 * @param primitive - The collection of GlammPrimitives
	 * @return HashSet<OMSVGElement> the elements associated with this collection, null if none.
	 */
	public Set<OMSVGElement> getSvgElementsForGlammPrimitives(final Collection<? extends GlammPrimitive> primitives) {
		HashSet<OMSVGElement> svgElements = null;
		for(GlammPrimitive primitive : primitives) {
			Set<OMSVGElement> s = getSvgElementsForGlammPrimitive(primitive);
			if(s == null)
				continue;
			if(svgElements == null)
				svgElements = new HashSet<OMSVGElement>();
			svgElements.addAll(s);
		}
		return svgElements;
	}

	/**
	 * Gets the height attribute of the SVG root element, 0 if unspecified
	 * @return float the height attribute the SVG root element.
	 */
	public float getSvgHeight() {
		float result = 0;
		if(svgRoot.hasAttribute(ATTRIBUTE_HEIGHT))
			result = Float.parseFloat(svgRoot.getAttribute(ATTRIBUTE_HEIGHT));
		return result;
	}

	/**
	 * Gets the width attribute of the SVG root element, 0 if unspecified
	 * @return float the width attribute the SVG root element.
	 */
	public float getSvgWidth() {
		float result = 0;
		if(svgRoot.hasAttribute(ATTRIBUTE_WIDTH))
			result = Float.parseFloat(svgRoot.getAttribute(ATTRIBUTE_WIDTH));
		return result;
	}

	/**
	 * Gets the top level group with id viewport.
	 * @return The viewport group.
	 */
	public OMSVGGElement getViewport() {
		return viewport;
	}


	/**
	 * Initializes all SVG groups with class attributes set to cpd, map, or rxn.
	 */
	private void init() {

		// get viewport from resource - a bit more involved
		for(OMNode node : svgRoot.getChildNodes()) {
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				OMElement element = (OMElement) node;
				if(element.hasAttribute(ATTRIBUTE_ID) && element.getAttribute(ATTRIBUTE_ID).equals(VIEWPORT_ID))  {
					viewport = (OMSVGGElement) element;
					break;
				}
			}
		}

		for(OMElement g : viewport.getElementsByTagName(SVGConstants.SVG_G_TAG)) {
			if(g.hasAttribute(ATTRIBUTE_CLASS) && g.getAttribute(ATTRIBUTE_CLASS).equals(CLASS_CPD))
				initCpdGroup(g);
			if(g.hasAttribute(ATTRIBUTE_CLASS) && g.getAttribute(ATTRIBUTE_CLASS).equals(CLASS_MAP))
				initMapGroup(g);
			if(g.hasAttribute(ATTRIBUTE_CLASS) && g.getAttribute(ATTRIBUTE_CLASS).equals(CLASS_RXN))
				initRxnGroup(g);
		}
	}

	/**
	 * Initializes Compound groups
	 * @param g The group
	 */
	private void initCpdGroup(OMElement g) {
		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_ELLIPSE_TAG);
		if(g.hasAttribute(ATTRIBUTE_KEGGID)) {
			String cpdId = g.getAttribute(ATTRIBUTE_KEGGID);
			cpdId = cpdId.startsWith("cpd") ? cpdId.substring(4) : cpdId.substring(3); // compounds can either start with cpd or gl
			Set<OMSVGElement> elementsForId = this.id2SvgElements.get(cpdId);
			if(elementsForId == null) {
				elementsForId = new HashSet<OMSVGElement>();
				this.id2SvgElements.put(cpdId, elementsForId);
			}
			for(OMSVGElement element : elements) {
				element.setAttribute(ATTRIBUTE_DEFAULT_COLOR, element.getAttribute(SVGConstants.SVG_FILL_ATTRIBUTE));
				cpdSvgElements.add(element);
				elementsForId.add(element);
			}
		}
	}

	/**
	 * Initializes map groups
	 * @param g The group
	 */
	private void initMapGroup(OMElement g) {
		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_T_SPAN_TAG);

		if(g.hasAttribute(ATTRIBUTE_KEGGID)) {
			String mapId = g.getAttribute(ATTRIBUTE_KEGGID).substring(5);
			Set<OMSVGElement> elementsForId = this.id2SvgElements.get(mapId);
			if(elementsForId == null) {
				elementsForId = new HashSet<OMSVGElement>();
				this.id2SvgElements.put(mapId, elementsForId);
			}
			for(OMSVGElement element : elements)
				elementsForId.add(element);
		}
	}

	/**
	 * Initializes Reaction groups
	 * @param g The group.
	 */
	private void initRxnGroup(OMElement g) {
		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_PATH_TAG);

		for(OMSVGElement element : elements) {
			element.setAttribute(ATTRIBUTE_DEFAULT_COLOR, element.getAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE));
			rxnSvgElements.add(element);
		}

		if(g.hasAttribute(ATTRIBUTE_REACTION) && !g.getAttribute(ATTRIBUTE_REACTION).isEmpty()) {
			String rxnString = g.getAttribute(ATTRIBUTE_REACTION);
			String[] rxnIds = rxnString.split("\\+");
			for(String rxnId : rxnIds) {
				if(rxnId.isEmpty())
					continue;
				rxnId = rxnId.substring(3);
				Set<OMSVGElement> elementsForId = this.id2SvgElements.get(rxnId);
				if(elementsForId == null) {
					elementsForId = new HashSet<OMSVGElement>();
					this.id2SvgElements.put(rxnId, elementsForId);
				}
				for(OMSVGElement element : elements) {
					elementsForId.add(element);
				}

			}



			if(g.hasAttribute(ATTRIBUTE_ENZYME)) {
				String ecNumString = g.getAttribute(ATTRIBUTE_ENZYME);
				String[] ecNums = ecNumString.split("\\+");
				for(String ecNum : ecNums) {
					if(ecNum.isEmpty())
						continue;
					ecNum = ecNum.substring(3);
					Set<OMSVGElement> elementsForId = this.id2SvgElements.get(ecNum);
					if(elementsForId == null) {
						elementsForId = new HashSet<OMSVGElement>();
						this.id2SvgElements.put(ecNum, elementsForId);
					}
					for(OMSVGElement element : elements) {
						elementsForId.add(element);
					}
				}
			}
		}

	}
}
