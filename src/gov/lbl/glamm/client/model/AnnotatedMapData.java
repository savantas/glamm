package gov.lbl.glamm.client.model;

import gov.lbl.glamm.client.model.interfaces.HasType;
import gov.lbl.glamm.client.model.util.Xref;

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
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.Node;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;


/**
 * Wrapper class for GLAMM annotated svg data.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class AnnotatedMapData implements Serializable {

	private static final String PATH_ICON = "/images/";
	private static final String PATH_SVG = "/svg/";

	public static final String ATTRIBUTE_ABSENT				= "absent";
	public static final String ATTRIBUTE_CLASS 				= "class";
	public static final String ATTRIBUTE_COMPOUND			= "compound";
	public static final String ATTRIBUTE_CPD_DST			= "cpddst";
	public static final String ATTRIBUTE_CPD_SRC			= "cpdsrc";
	public static final String ATTRIBUTE_DEFAULT_COLOR		= "defaultcolor";
	public static final String ATTRIBUTE_ENZYME				= "enzyme";
	public static final String ATTRIBUTE_HAS_DATA			= "hasdata";
	public static final String ATTRIBUTE_HEIGHT				= "height";
	public static final String ATTRIBUTE_ID					= "id";
	public static final String ATTRIBUTE_MAP				= "map";
	public static final String ATTRIBUTE_REACTION			= "reaction";
	public static final String ATTRIBUTE_ROUTE				= "route";
	public static final String ATTRIBUTE_SEARCH_TARGET		= "searchtarget";
	public static final String ATTRIBUTE_STATE				= "state";
	public static final String ATTRIBUTE_WIDTH				= "width";

	public enum ElementClass {
		
		BACKGROUND("background"),
		CPD("cpd"),
		MAP("map"),
		RXN("rxn");

		private String cssClass;

		private ElementClass(final String cssClass) {
			this.cssClass = cssClass;
		}

		public static ElementClass fromCssClass(final String cssClass) {
			ElementClass result = null;
			if(cssClass != null) {
				for(ElementClass value : ElementClass.values()) 
					if(value.cssClass.equals(cssClass))
						result = value;
			}
			if(result == null) 
				throw new IllegalArgumentException("cssClass is invalid or null.");
			return result;
		}
		
		public String getCssClass() {
			return cssClass;
		}
	}

	public static final String STATE_DEFAULT		= "default";
	public static final String STATE_MOUSEOVER		= "mouseover";
	public static final String STATE_SELECTED		= "selected";

	public static final String VIEWPORT_ID	= "viewport";

	private OMSVGSVGElement	svgRoot;
	private OMSVGGElement 	viewport;
	private Set<String> cpdDbNames;
	private Set<String> rxnDbNames;

	private Map<String, Set<OMSVGElement>> id2SvgElements;
	private Set<OMSVGElement> cpdSvgElements;
	private Set<OMSVGElement> rxnSvgElements;
	
	private AnnotatedMapDescriptor descriptor;
	private String svgUrl;
	private String iconUrl;


	@SuppressWarnings("unused")
	private AnnotatedMapData() {}

	public AnnotatedMapData(final AnnotatedMapDescriptor descriptor) {
		this.setDescriptor(descriptor);
		this.cpdDbNames = new HashSet<String>();
		this.rxnDbNames = new HashSet<String>();

		// allocate space for id2SvgElements
		id2SvgElements = new HashMap<String, Set<OMSVGElement>>();
		cpdSvgElements = new HashSet<OMSVGElement>();
		rxnSvgElements = new HashSet<OMSVGElement>();		
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

	public AnnotatedMapDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(final AnnotatedMapDescriptor descriptor) {
		this.descriptor = descriptor;
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();

		iconUrl = urlBuilder.setPath(PATH_ICON + descriptor.getIcon()).buildString();
		svgUrl = urlBuilder.setPath(PATH_SVG + descriptor.getSvg()).buildString();	
	}


	public String getSvgUrl() {
		return svgUrl;
	}

	public String getIconUrl() {
		return iconUrl;
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

	public Set<OMSVGElement> getSvgElementsForXrefs(final Collection<Xref> xrefs) {
		Set<OMSVGElement> svgElements = new HashSet<OMSVGElement>();
		if(xrefs == null || xrefs.isEmpty())
			return svgElements;

		for(Xref xref : xrefs) {
			Set<OMSVGElement> svgElementsForXref = id2SvgElements.get(xref.getXrefId());
			if(svgElementsForXref != null && !svgElementsForXref.isEmpty())
				svgElements.addAll(svgElementsForXref);
		}

		return svgElements;
	}

	/**
	 * Gets the set of SVG elements associated with this GlammPrimitive
	 * @param primitive
	 * @return HashSet<OMSVGElement> the elements associated with this primitive, null if none.
	 */
	public Set<OMSVGElement> getSvgElements(final HasType primitive) {

		if(primitive.getType() == Compound.TYPE) {
			Compound compound = (Compound) primitive;
			return getSvgElementsForXrefs(compound.getXrefs());
		}
		else if(primitive.getType() == Reaction.TYPE) {
			Reaction reaction = (Reaction) primitive;
			return getSvgElementsForXrefs(reaction.getXrefs());
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
	public Set<OMSVGElement> getSvgElements(final Collection<? extends HasType> primitives) {
		HashSet<OMSVGElement> svgElements = null;
		for(HasType primitive : primitives) {
			Set<OMSVGElement> s = getSvgElements(primitive);
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
	 * Initializes Compound groups
	 * @param g The group
	 */
	private void initCpdGroup(OMElement g) {

		if(!g.hasAttribute(ATTRIBUTE_COMPOUND)) 
			return;

		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_ELLIPSE_TAG);
		String cpdIds[] = g.getAttribute(ATTRIBUTE_COMPOUND).split("\\+");
		for(String cpdId : cpdIds) {
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

		if(!g.hasAttribute(ATTRIBUTE_MAP))
			return;

		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_T_SPAN_TAG);
		String mapIds[] = g.getAttribute(ATTRIBUTE_MAP).split("\\+");
		for(String mapId : mapIds) {
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

	public void setSvgRoot(final OMSVGSVGElement svgRoot) {
		this.svgRoot = svgRoot;

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
			if(g.hasAttribute(ATTRIBUTE_CLASS) && g.getAttribute(ATTRIBUTE_CLASS).equals(ElementClass.CPD.getCssClass()))
				initCpdGroup(g);
			if(g.hasAttribute(ATTRIBUTE_CLASS) && g.getAttribute(ATTRIBUTE_CLASS).equals(ElementClass.MAP.getCssClass()))
				initMapGroup(g);
			if(g.hasAttribute(ATTRIBUTE_CLASS) && g.getAttribute(ATTRIBUTE_CLASS).equals(ElementClass.RXN.getCssClass()))
				initRxnGroup(g);
		}
	}
}
