package gov.lbl.glamm.shared.model;

import gov.lbl.glamm.shared.model.interfaces.HasType;
import gov.lbl.glamm.shared.model.util.Xref;

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

import com.google.gwt.core.client.GWT;
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
	public static final class Attribute {
		public static final String ABSENT			= "absent";
		public static final String CLASS			= "class";
		public static final String COMPOUND			= "compound";
		public static final String CPD_DST			= "cpddst";
		public static final String CPD_SRC			= "cpdsrc";
		public static final String DEFAULT_COLOR	= "defaultcolor";
		public static final String ENZYME			= "enzyme";
		public static final String HAS_DATA			= "hasdata";
		public static final String HEIGHT			= "height";
		public static final String ID				= "id";
		public static final String MAP				= "map";
		public static final String PATHWAY			= "pathway";
		public static final String REACTION			= "reaction";
		public static final String ROUTE			= "route";
		public static final String SEARCH_TARGET	= "searchtarget";
		public static final String STATE			= "state";
		public static final String WIDTH			= "width";
		public static final String GROUP		    = "group";
		public static final String STRENGTH			= "strength";
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

	private OMElement userElement;
	
	private OMSVGSVGElement	svgRoot;
	private OMSVGGElement 	viewport;
	private Set<String> cpdDbNames;
	private Set<String> rxnDbNames;

	private Map<String, Set<OMSVGElement>> id2SvgElements;
	private Set<OMSVGElement> cpdSvgElements;
	private Set<OMSVGElement> rxnSvgElements;
	
	private Map<String, Set<OMSVGElement>> userId2SvgElements;
	private Set<OMSVGElement> userCpdSvgElements;
	private Set<OMSVGElement> userRxnSvgElements;

	private AnnotatedMapDescriptor descriptor;
	private String svgUrl;
	private String iconUrl;
	
	private float svgWidth = 0;
	private float svgHeight = 0;


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
		
		/* crude and ugly hack to fix a bug where an empty set is returned.
		 * eventually, the cpdDbNames and rxnDbNames should be fetched directly
		 * from the database, ideally using Hibernate. I'm not sure of the best way
		 * to do that yet, though...
		 * 
		 * It may just invoke this query (or a variation for only reactions / compounds):
		 *     SELECT DISTINCT xrefDbName FROM GlammXref X, 
		 *   	 							   AMRxn R, 
		 *   								   AnnotatedMap M, 
		 *   							       AMRxnElement E 
		 *     WHERE R.am_id=M.id AND 
		 *   	     E.amRxn_id = R.id AND 
		 *   	     X.toXrefId = E.xrefId AND
		 *    		 M.mapId = "map01100" AND		// or whatever map we're on, based on the descriptor
		 *   	     E.type = "REACTION";           // or != REACTION to get the compound db set.
		 */

		cpdDbNames.add("LIGAND");
		cpdDbNames.add("LIGAND-CPD");
		rxnDbNames.add("LIGAND-RXN");

		// allocate space for id2SvgElements
		id2SvgElements = new HashMap<String, Set<OMSVGElement>>(); // contains all id and svg elements present in the map
		cpdSvgElements = new HashSet<OMSVGElement>();			   // contains all cpd svg elements present
		rxnSvgElements = new HashSet<OMSVGElement>();			   // contains all rxn elements present

		userId2SvgElements = new HashMap<String, Set<OMSVGElement>>();  // contains ONLY user-placed id/element sets
		userCpdSvgElements = new HashSet<OMSVGElement>();				// contains ONLY user-placed cpd elements
		userRxnSvgElements = new HashSet<OMSVGElement>();				// contains ONLY user-placed rxn elements
		
		userElement = null;

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
		if (GWT.isProdMode()) {
			iconUrl = urlBuilder.setPath(Window.Location.getPath() + PATH_ICON + descriptor.getIcon()).buildString();
			svgUrl = urlBuilder.setPath(Window.Location.getPath() + PATH_SVG + descriptor.getSvg()).buildString();	
		}
		else {
			iconUrl = urlBuilder.setPath(PATH_ICON + descriptor.getIcon()).buildString();
			svgUrl = urlBuilder.setPath(PATH_SVG + descriptor.getSvg()).buildString();	
		}

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
		return svgHeight;
//		float result = 0;
//		if(svgRoot.hasAttribute(Attribute.HEIGHT))
//			result = Float.parseFloat(svgRoot.getAttribute(Attribute.HEIGHT));
//		return result;
	}

	/**
	 * Gets the width attribute of the SVG root element, 0 if unspecified
	 * @return The width attribute the SVG root element.
	 */
	public float getSvgWidth() {
		return svgWidth;
//		float result = 0;
//		if(svgRoot.hasAttribute(Attribute.WIDTH))
//			result = Float.parseFloat(svgRoot.getAttribute(Attribute.WIDTH));
//		return result;
	}

	/**
	 * Gets the top level group with id viewport.
	 * @return The viewport group.
	 */
	public OMSVGGElement getViewport() {
		return viewport;
	}

	private void initCpdGroup(OMElement g) {

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
		
		setSvgDimensions();
	}

	private void setSvgDimensions() {
		
		if(svgRoot.hasAttribute(Attribute.WIDTH))
			svgWidth = Float.parseFloat(svgRoot.getAttribute(Attribute.WIDTH));

		if(svgRoot.hasAttribute(Attribute.HEIGHT))
			svgHeight = Float.parseFloat(svgRoot.getAttribute(Attribute.HEIGHT));
	
		if (userElement != null) {
			if (userElement.hasAttribute(Attribute.WIDTH))
				svgWidth = Float.parseFloat(userElement.getAttribute(Attribute.WIDTH));
			
			if (userElement.hasAttribute(Attribute.HEIGHT))
				svgHeight = Float.parseFloat(userElement.getAttribute(Attribute.HEIGHT));
		}
	}
	
	/**
	 * The KGML+ SVG format used in GLAMM follows a specific format, so that format must be followed in order to insert 
	 * arbitrary new elements into the map. In general, it looks like this:
	 * 
	 * <SVG>
	 *   <g id="viewport">
	 *     <g id="_G1">
	 *       <g class="rxn" reaction="R12345">
	 *         <path class="rxn" d="xxxxxxxxxxx" fill="none" .... />
	 *       </g>
	 *       <g class="cpd" compound="C12345">
	 *         <ellipse class="cpd" cx="x" cy="y" .... />
	 *       </g>
	 *     </g>
	 *   </g>
	 * </SVG>
	 * 
	 * We'll be adding either reactions (paths) or compounds (ellipses), so we really just need to make a 
	 * new group node and make sure it's a child of the viewport. The "_G1" (or "_G3878" or "_Gn" or whatever) are just
	 * another grouping. For our purposes, as long as we can keep track of those elements that have been added, it's fine.
	 * 
	 * @param x
	 * @param y
	 */
	public void setUserElement(OMElement userElement) {
		
		removeUserElement();
		
		this.userElement = userElement;
		getViewport().appendChild(userElement);
		
		for (OMElement g : userElement.getElementsByTagName(SVGConstants.SVG_G_TAG)) {
			if (g.hasAttribute(Attribute.CLASS) && g.getAttribute(Attribute.CLASS).equals(ElementClass.CPD.getCssClass()))
				addUserCpdGroup(g);
			if (g.hasAttribute(Attribute.CLASS) && g.getAttribute(Attribute.CLASS).equals(ElementClass.RXN.getCssClass()))
				addUserRxnGroup(g);
			if (g.hasAttribute(Attribute.CLASS) && g.getAttribute(Attribute.CLASS).equals(ElementClass.MAP.getCssClass()))
				addUserMapGroup(g);
		}
		
		setSvgDimensions();
	}
	
	private void addUserCpdGroup(OMElement g) {
		if (!g.hasAttribute(Attribute.COMPOUND))
			return;
		
		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_ELLIPSE_TAG);
		String cpdIds[] = g.getAttribute(Attribute.COMPOUND).split("\\+");
		for (String cpdId : cpdIds) {
			Set<OMSVGElement> elementsForId = this.id2SvgElements.get(cpdId);
			Set<OMSVGElement> userElementsForId = this.userId2SvgElements.get(cpdId);
			if (elementsForId == null) {
				elementsForId = new HashSet<OMSVGElement>();
				this.id2SvgElements.put(cpdId, elementsForId);
			}
			if (userElementsForId == null) {
				userElementsForId = new HashSet<OMSVGElement>();
				this.userId2SvgElements.put(cpdId, userElementsForId);
			}
			for (OMSVGElement element : elements) {
				element.setAttribute(Attribute.DEFAULT_COLOR, element.getAttribute(SVGConstants.SVG_FILL_ATTRIBUTE));
				if (!cpdSvgElements.contains(element)) {
					cpdSvgElements.add(element);
					userCpdSvgElements.add(element);
				}
				elementsForId.add(element);
				userElementsForId.add(element);
			}
		}
	}
	
	private void addUserRxnGroup(OMElement g) {
		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_PATH_TAG);

		for(OMSVGElement element : elements) {
			element.setAttribute(Attribute.DEFAULT_COLOR, element.getAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE));
			rxnSvgElements.add(element);
			userRxnSvgElements.add(element);
		}

		if(g.hasAttribute(Attribute.REACTION) && !g.getAttribute(Attribute.REACTION).isEmpty()) {
			String rxnString = g.getAttribute(Attribute.REACTION);
			String[] rxnIds = rxnString.split("\\+");
			for(String rxnId : rxnIds) {
				if(rxnId.isEmpty())
					continue;
				Set<OMSVGElement> elementsForId = this.id2SvgElements.get(rxnId);
				Set<OMSVGElement> userElementsForId = this.userId2SvgElements.get(rxnId);
				if(elementsForId == null) {
					elementsForId = new HashSet<OMSVGElement>();
					this.id2SvgElements.put(rxnId, elementsForId);
				}
				if (userElementsForId == null) {
					userElementsForId = new HashSet<OMSVGElement>();
					this.userId2SvgElements.put(rxnId, userElementsForId);
				}
				for(OMSVGElement element : elements) {
					elementsForId.add(element);
					userElementsForId.add(element);
				}
			}
			
			if(g.hasAttribute(Attribute.ENZYME)) {
				String ecNumString = g.getAttribute(Attribute.ENZYME);
				String[] ecNums = ecNumString.split("\\+");
				for(String ecNum : ecNums) {
					if(ecNum.isEmpty())
						continue;
					Set<OMSVGElement> elementsForId = this.id2SvgElements.get(ecNum);
					Set<OMSVGElement> userElementsForId = this.userId2SvgElements.get(ecNum);
					if(elementsForId == null) {
						elementsForId = new HashSet<OMSVGElement>();
						this.id2SvgElements.put(ecNum, elementsForId);
					}
					if(userElementsForId == null) {
						userElementsForId = new HashSet<OMSVGElement>();
						this.userId2SvgElements.put(ecNum, userElementsForId);
					}
					for(OMSVGElement element : elements) {
						elementsForId.add(element);
						userElementsForId.add(element);
					}
				}
			}
		}
	}
	
	private void addUserMapGroup(OMElement g) {

		if(!g.hasAttribute(Attribute.MAP))
			return;

		OMNodeList<OMSVGElement> elements = g.getElementsByTagName(SVGConstants.SVG_T_SPAN_TAG);
		String mapIds[] = g.getAttribute(Attribute.MAP).split("\\+");
		for(String mapId : mapIds) {
			Set<OMSVGElement> elementsForId = this.id2SvgElements.get(mapId);
			Set<OMSVGElement> userElementsForId = this.userId2SvgElements.get(mapId);
			if(elementsForId == null) {
				elementsForId = new HashSet<OMSVGElement>();
				this.id2SvgElements.put(mapId, elementsForId);
			}
			if(userElementsForId == null) {
				userElementsForId = new HashSet<OMSVGElement>();
				this.userId2SvgElements.put(mapId, userElementsForId);
			}
			for(OMSVGElement element : elements) {
				elementsForId.add(element);
				userElementsForId.add(element);
			}
		}
	}
	
	public void removeUserElement() {
		if (userElement != null) {
			
			for (String key : userId2SvgElements.keySet()) {
				Set<OMSVGElement> elements = userId2SvgElements.get(key);
				if (elements != null && elements.size() != 0) {
					for (OMSVGElement element : elements) {
						id2SvgElements.get(key).remove(element);
					}
				}
				if (id2SvgElements.get(key).isEmpty())
					id2SvgElements.remove(key);
			}
			
			for (OMSVGElement element : this.userCpdSvgElements) {
				this.cpdSvgElements.remove(element);
			}
			userCpdSvgElements.clear();
			
			for (OMSVGElement element : this.userRxnSvgElements) {
				this.rxnSvgElements.remove(element);
			}
			userRxnSvgElements.clear();
			
			getViewport().removeChild(userElement);
			
			userElement = null;
			setSvgDimensions();
		}
	}
}
