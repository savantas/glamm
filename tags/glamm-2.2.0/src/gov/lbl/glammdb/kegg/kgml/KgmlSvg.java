package gov.lbl.glammdb.kegg.kgml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class for processing the KGML+ SVG node, annotating it in a format acceptable by GLAMM.
 * @author jtbates
 *
 */
class KgmlSvg {
	
	@SuppressWarnings("unused")
	private static interface Attribute {
		final static String CLASS		= "class";
		final static String COMPOUND	= "compound";
		final static String ENZYME		= "enzyme";
		final static String FILL		= "fill";
		final static String ID			= "id";
		final static String KEGGID		= "keggid";
		final static String MAP			= "map";
		final static String REACTION	= "reaction";
		final static String STATE		= "state";
		final static String TYPE		= "type";
	}

	private static interface CssClass {
		final static String CPD ="cpd";
		final static String MAP ="map";
		final static String RXN ="rxn";
	}
	
	@SuppressWarnings("unused")
	private static interface Tag {
		final static String DESC 	= "desc";
		final static String ELLIPSE = "ellipse";
		final static String ENTRY 	= "entry";
		final static String G 		= "g";
		final static String MAP 	= "map";
		final static String PATH 	= "path";
		final static String RECT 	= "rect";
		final static String SVG 	= "svg";
		final static String TITLE 	= "title";
		final static String TSPAN 	= "tspan";
	}

	private static interface Text {
		final static String BACKGROUND	= "__background__";
		final static String BASE 		= "Base";
	}
	
	@SuppressWarnings("unused")
	private static interface Type {
		final static String COMPOUND	= "compound";
		final static String ENZYME 		= "enzyme";
		final static String GENE 		= "gene";
		final static String MAP 		= "map";
		final static String ORTHOLOG 	= "ortholog";
		final static String OTHER 		= "other";
	}
	
	private KgmlPlusDocument document;
	
	private KgmlSvg() {}
	
	/**
	 * Creates a KgmlSvg instance from a KGML+ document.
	 * @param document The document.
	 * @return The instance.
	 */
	public static KgmlSvg create(final KgmlPlusDocument document) {
		KgmlSvg svg = new KgmlSvg();
		svg.document = document;
		return svg;
	}

	private void flattenGroup(final Element group, final String tag) {
		List<Node> subgroups = KgmlPlusDocument.getElementsWithTag(group, Tag.G);
		if(subgroups.size() < 2)
			return;

		for(Node node : KgmlPlusDocument.getElementsWithTag(group, tag))
			group.appendChild(node);

		for(Node subgroup: subgroups) {
			if(!subgroup.equals(group)) {
				group.removeChild(subgroup.getNextSibling());
				group.removeChild(subgroup);
			}
		}
	}

	private void processAnnotatedMapElementGroup(final Element groupElement) {
		List<Node> entryNodes = KgmlPlusDocument.getElementsWithTag(groupElement, Tag.ENTRY);
		if(entryNodes.isEmpty())
			processEntrylessGroup(groupElement);
		else {
			Element entry = (Element) entryNodes.get(0);
			String type = entry.getAttribute(Attribute.TYPE);

			if(type.equals(Type.COMPOUND))
				processCompoundGroup(groupElement, entry);
			else if(type.equals(Type.MAP))
				processMapGroup(groupElement, entry);
			else {
				processReactionGroup(groupElement, entry);
				flattenGroup(groupElement, Tag.PATH);
			}

			groupElement.removeChild(entry.getNextSibling());
			groupElement.removeChild(entry);
		}
		groupElement.removeAttribute(Attribute.ID);
	}

	private void processAnnotatedMapElements() {
		List<Node> titleElements = KgmlPlusDocument.getElementsWithTag(document.getSvgNode(), Tag.TITLE);
		Node baseParentNode = null;
		for(Node node : titleElements) {
			if(node.getFirstChild().getTextContent().equals(Text.BASE)){
				baseParentNode = node.getParentNode();
				break;
			}
		}

		if(baseParentNode == null)
			throw new RuntimeException("Could not find " + Tag.TITLE + " element with text " + Text.BASE);

		for(Node node = baseParentNode.getFirstChild(); node != null; node = node.getNextSibling()) {
			if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(Tag.G)) 
				processAnnotatedMapElementGroup((Element) node);
		}
	}

	private void processBackgroundRect() {

		List<Node> descElements = KgmlPlusDocument.getElementsWithTag(document.getSvgNode(), Tag.DESC);
		Node descParent = null;
		for(Node node : descElements) {
			if(node.getFirstChild().getTextContent().equals(Text.BACKGROUND)) {
				descParent = node.getParentNode();
				break;
			}
		}

		if(descParent == null)
			throw new RuntimeException("Could not find " + Tag.DESC + " element with text " + Text.BACKGROUND);

		List<Node> rectElements = KgmlPlusDocument.getElementsWithTag(descParent, Tag.RECT);
		if(rectElements.isEmpty())
			throw new RuntimeException("Could not find background rect.");
		else {
			Element rect = (Element) rectElements.get(0);
			rect.setAttribute(Attribute.FILL, "#000000");
		}
	}

	private void processCompoundGroup(final Element group, final Element entry) {
		for(Node node : KgmlPlusDocument.getElementsWithTag(group, Tag.ELLIPSE)) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS, CssClass.CPD);
			element.setAttribute(Attribute.STATE, "default");
		}

		group.setAttribute(Attribute.CLASS, CssClass.CPD);
	}

	private void processEntrylessGroup(final Element group) {
		List<Node> pathNodes = KgmlPlusDocument.getElementsWithTag(group, Tag.PATH);
		for(Node node : pathNodes) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS, CssClass.RXN);
			element.setAttribute(Attribute.STATE, "default");
		}
		group.setAttribute(Attribute.CLASS, CssClass.RXN);
	}

	private void processMapGroup(final Element group, final Element entry) {
		for(Node node : KgmlPlusDocument.getElementsWithTag(group, Tag.RECT))
			((Element) node).setAttribute(Attribute.FILL, "none");

		for(Node node : KgmlPlusDocument.getElementsWithTag(group, Tag.TSPAN)) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS, CssClass.MAP);
			element.setAttribute(Attribute.STATE, "default");
			element.setAttribute(Attribute.FILL, "#FFFFFF");
		}

		group.setAttribute(Attribute.CLASS, CssClass.MAP);
	}

	private void processReactionGroup(final Element group, final Element entry) {
		for(Node node : KgmlPlusDocument.getElementsWithTag(group, Tag.PATH)) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS, CssClass.RXN);
			element.setAttribute(Attribute.STATE, "default");
		}

		group.setAttribute(Attribute.CLASS, CssClass.RXN);
	}

	/**
	 * Processes and annotates the KgmlSvg instance.
	 */
	public void process() {
		processBackgroundRect();
		processAnnotatedMapElements();
		removeTitleElements();
		wrapSvgChildrenInViewport();
	}

	private void removeTitleElements() {
		List<Node> nodes = KgmlPlusDocument.getElementsWithTag(document.getSvgNode(), Tag.TITLE);
		for(Node node : nodes) {
			node.getParentNode().removeChild(node.getNextSibling());
			node.getParentNode().removeChild(node);
		}
	}

	private void wrapSvgChildrenInViewport() {
		// create the viewport element
		Node svgNode = document.getSvgNode();
		Element viewport = svgNode.getOwnerDocument().createElement(Tag.G);
		viewport.setAttribute(Attribute.ID, "viewport");

		// add child nodes to the viewport element, while removing them from the svgNode
		for(Node node = svgNode.getFirstChild(); node != null; ) {
			Node nextNode = node.getNextSibling();
			viewport.appendChild(node.cloneNode(true));
			svgNode.removeChild(node);
			node = nextNode;
		}

		svgNode.appendChild(viewport);

	}
}
