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
 * The annotated SVG map and associated data.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class AnnotatedMapData implements Serializable {

	private static final String PATH_ICON = "/images/";
	private static final String PATH_SVG = "/svg/";

	/**
	 * Interface containing GLAMM-specific SVG annotations.
	 * @author jtbates
	 *
	 */
	public static interface Attribute {
		String ABSENT			= "absent";
		String CLASS			= "class";
		String COMPOUND			= "compound";
		String CPD_DST			= "cpddst";
		String CPD_SRC			= "cpdsrc";
		String DEFAULT_COLOR	= "defaultcolor";
		String ENZYME			= "enzyme";
		String HAS_DATA			= "hasdata";
		String HEIGHT			= "height";
		String ID				= "id";
		String MAP				= "map";
		String REACTION			= "reaction";
		String ROUTE			= "route";
		String SEARCH_TARGET	= "searchtarget";
		String STATE			= "state";
		String WIDTH			= "width";
	}

	/**
	 * Enum enumerating the set of CSS classes associated with interactive map elements.
	 * @author jtbates
	 *
	 */
	public static enum ElementClass {

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

	/**
	 * Interface indicating the set of states an interactive map element may have.
	 * @author jtbates
	 *
	 */
	public interface State {
		String DEFAULT		= "default";
		String MOUSEOVER	= "mouseover";
		String SELECTED		= "selected";
	}

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

	/**
	 * Constructor
	 * @param descriptor The map descriptor associated with this map.
	 */
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
	 * Gets the set of names of the compound databases associated with this map.
	 * @return The set of names.
	 */
	public Set<String> getCpdDbNames() {
		return cpdDbNames;
	}

	/**
	 * Gets the set of SVG elements corresponding with compounds on this map.
	 * @return The set of SVG elements.
	 */
	public Set<OMSVGElement> getCpdSvgElements() {
		return cpdSvgElements;
	}

	/**
	 * Gets the annotated map descriptor for this map.
	 * @return The descriptor.
	 */
	public AnnotatedMapDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * Sets the annotated map descriptor.
	 * @param descriptor The descriptor.
	 */
	public void setDescriptor(final AnnotatedMapDescriptor descriptor) {
		this.descriptor = descriptor;
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();

		iconUrl = urlBuilder.setPath(PATH_ICON + descriptor.getIcon()).buildString();
		svgUrl = urlBuilder.setPath(PATH_SVG + descriptor.getSvg()).buildString();	
	}


	/**
	 * Gets the URL for the SVG file associated with this map.
	 * @return The URL.
	 */
	public String getSvgUrl() {
		return svgUrl;
	}

	/**
	 * Gets the URL for the icon image file associated with this map, typically PNG format.
	 * @return The URL.
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * Gets the set of names of the databases of reactions associated with this map.
	 * @return The set of names.
	 */
	public Set<String> getRxnDbNames() {
		return rxnDbNames;
	}

	/**
	 * Gets the set of SVG elements corresponding to reactions on this map.
	 * @return The set of elements.
	 */
	public Set<OMSVGElement> getRxnSvgElements() {
		return rxnSvgElements;
	}

	/**
	 * Gets the root SVG element.
	 * @return The root element.
	 */
	public OMSVGSVGElement getSvg() {
		return svgRoot;
	}

	/**
	 * Gets the set of SVG elements associated with this id
	 * @param id The id.
	 * @return The elements associated with this id, null if none.
	 */
	public Set<OMSVGElement> getSvgElementsForId(final String id) {
		return id2SvgElements.get(id);
	}

	/**
	 * Gets the set of SVG elements associated with a set of Xrefs.
	 * @param xrefs The xrefs.
	 * @return The SVG elements, empty set if none.
	 */
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
	 * Gets the set of SVG elements associated with an object that implements the HasType interface
	 * @param primitive The object implementing HasType.
	 * @return The elements associated with this primitive, null if none.
	 */
	public Set<OMSVGElement> getSvgElements(final HasType primitive) {

		if(primitive.getType() == Compound.TYPE) {
			Compound compound = (Compound) primitive;
			return getSvgElementsForXrefs(compound.getXrefSet().getXrefs());
		}
		else if(primitive.getType() == Reaction.TYPE) {
			Reaction reaction = (Reaction) primitive;
			return getSvgElementsForXrefs(reaction.getXrefSet().getXrefs());
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
	 * Gets the set of SVG elements associated with this collection of objects implementing the HasType interface.
	 * @param primitives The collection of objects implementing HasType.
	 * @return The SVG elements associated with this collection, null if none.
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
	 * @return The height attribute the SVG root element.
	 */
	public float getSvgHeight() {
		float result = 0;
		if(svgRoot.hasAttribute(Attribute.HEIGHT))
			result = Float.parseFloat(svgRoot.getAttribute(Attribute.HEIGHT));
		return result;
	}

	/**
	 * Gets the width attribute of the SVG root element, 0 if unspecified
	 * @return The width attribute the SVG root element.
	 */
	public float getSvgWidth() {
		float result = 0;
		if(svgRoot.hasAttribute(Attribute.WIDTH))
			result = Float.parseFloat(svgRoot.getAttribute(Attribute.WIDTH));
		return result;
	}

	/**
	 * Gets the top level group with id viewport.
	 * @return The viewport group.
	 */
	public OMSVGGElement getViewport() {
		return viewport;
	}

	void initCpdGroup(OMElement g) {

		if(!g.hasAttribute(Attribute.COMPOUND)) 
			return;

		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_ELLIPSE_TAG);
		String cpdIds[] = g.getAttribute(Attribute.COMPOUND).split("\\+");
		for(String cpdId : cpdIds) {
			Set<OMSVGElement> elementsForId = this.id2SvgElements.get(cpdId);
			if(elementsForId == null) {
				elementsForId = new HashSet<OMSVGElement>();
				this.id2SvgElements.put(cpdId, elementsForId);
			}
			for(OMSVGElement element : elements) {
				element.setAttribute(Attribute.DEFAULT_COLOR, element.getAttribute(SVGConstants.SVG_FILL_ATTRIBUTE));
				cpdSvgElements.add(element);
				elementsForId.add(element);
			}
		}

	}

	private void initMapGroup(OMElement g) {

		if(!g.hasAttribute(Attribute.MAP))
			return;

		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_T_SPAN_TAG);
		String mapIds[] = g.getAttribute(Attribute.MAP).split("\\+");
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

	private void initRxnGroup(OMElement g) {
		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_PATH_TAG);

		for(OMSVGElement element : elements) {
			element.setAttribute(Attribute.DEFAULT_COLOR, element.getAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE));
			rxnSvgElements.add(element);
		}

		if(g.hasAttribute(Attribute.REACTION) && !g.getAttribute(Attribute.REACTION).isEmpty()) {
			String rxnString = g.getAttribute(Attribute.REACTION);
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



			if(g.hasAttribute(Attribute.ENZYME)) {
				String ecNumString = g.getAttribute(Attribute.ENZYME);
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

	/**
	 * Sets the root SVG element.
	 * @param svgRoot The root element.
	 */
	public void setSvgRoot(final OMSVGSVGElement svgRoot) {
		this.svgRoot = svgRoot;

		// get viewport from resource - a bit more involved
		for(OMNode node : svgRoot.getChildNodes()) {
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				OMElement element = (OMElement) node;
				if(element.hasAttribute(Attribute.ID) && element.getAttribute(Attribute.ID).equals(VIEWPORT_ID))  {
					viewport = (OMSVGGElement) element;
					break;
				}
			}
		}

		for(OMElement g : viewport.getElementsByTagName(SVGConstants.SVG_G_TAG)) {
			if(g.hasAttribute(Attribute.CLASS) && g.getAttribute(Attribute.CLASS).equals(ElementClass.CPD.getCssClass()))
				initCpdGroup(g);
			if(g.hasAttribute(Attribute.CLASS) && g.getAttribute(Attribute.CLASS).equals(ElementClass.MAP.getCssClass()))
				initMapGroup(g);
			if(g.hasAttribute(Attribute.CLASS) && g.getAttribute(Attribute.CLASS).equals(ElementClass.RXN.getCssClass()))
				initRxnGroup(g);
		}
	}
}
