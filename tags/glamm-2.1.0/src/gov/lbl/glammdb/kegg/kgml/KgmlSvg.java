package gov.lbl.glammdb.kegg.kgml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class KgmlSvg {
	private enum Attribute {
		CLASS("class"),
		COMPOUND("compound"),
		ENZYME("enzyme"),
		FILL("fill"),
		ID("id"),
		KEGGID("keggid"),
		MAP("map"),
		REACTION("reaction"),
		STATE("state"),
		TYPE("type");

		private String value;

		private Attribute(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private enum CssClass {
		CPD("cpd"),
		MAP("map"),
		RXN("rxn");
		private String value;

		private CssClass(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}	
	}

	private enum Tag {
		DESC("desc"),
		ELLIPSE("ellipse"),
		ENTRY("entry"),
		G("g"),
		MAP("map"),
		PATH("path"),
		RECT("rect"),
		SVG("svg"),
		TEST("foobar"),
		TITLE("title"),
		TSPAN("tspan");

		private String value;

		private Tag(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private enum Text {
		BACKGROUND("__background__"),
		BASE("Base");

		private String value;

		private Text(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private enum Type {
		COMPOUND("compound"),
		ENZYME("enzyme"),
		GENE("gene"),
		MAP("map"),
		ORTHOLOG("ortholog"),
		OTHER("other");

		private String value;

		private Type(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
	
	private KgmlDocument document;
	
	private KgmlSvg() {}
	
	public static KgmlSvg create(final KgmlDocument document) {
		KgmlSvg svg = new KgmlSvg();
		svg.document = document;
		return svg;
	}

	private void flattenGroup(final Element group, final Tag tag) {
		List<Node> subgroups = KgmlDocument.getElementsWithTag(group, Tag.G.toString());
		if(subgroups.size() < 2)
			return;

		for(Node node : KgmlDocument.getElementsWithTag(group, tag.toString()))
			group.appendChild(node);

		for(Node subgroup: subgroups) {
			if(!subgroup.equals(group)) {
				group.removeChild(subgroup.getNextSibling());
				group.removeChild(subgroup);
			}
		}
	}

	private void processAnnotatedMapElementGroup(final Element groupElement) {
		List<Node> entryNodes = KgmlDocument.getElementsWithTag(groupElement, Tag.ENTRY.toString());
		if(entryNodes.isEmpty())
			processEntrylessGroup(groupElement);
		else {
			Element entry = (Element) entryNodes.get(0);
			String type = entry.getAttribute(Attribute.TYPE.toString());

			if(type.equals(Type.COMPOUND.toString()))
				processCompoundGroup(groupElement, entry);
			else if(type.equals(Type.MAP.toString()))
				processMapGroup(groupElement, entry);
			else {
				processReactionGroup(groupElement, entry);
				flattenGroup(groupElement, Tag.PATH);
			}

			groupElement.removeChild(entry.getNextSibling());
			groupElement.removeChild(entry);
		}
		groupElement.removeAttribute(Attribute.ID.toString());
	}

	private void processAnnotatedMapElements() {
		List<Node> titleElements = KgmlDocument.getElementsWithTag(document.getSvgNode(), Tag.TITLE.toString());
		Node baseParentNode = null;
		for(Node node : titleElements) {
			if(node.getFirstChild().getTextContent().equals(Text.BASE.toString())){
				baseParentNode = node.getParentNode();
				break;
			}
		}

		if(baseParentNode == null)
			throw new RuntimeException("Could not find " + Tag.TITLE + " element with text " + Text.BASE);

		for(Node node = baseParentNode.getFirstChild(); node != null; node = node.getNextSibling()) {
			if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(Tag.G.toString())) 
				processAnnotatedMapElementGroup((Element) node);
		}
	}

	private void processBackgroundRect() {

		List<Node> descElements = KgmlDocument.getElementsWithTag(document.getSvgNode(), Tag.DESC.toString());
		Node descParent = null;
		for(Node node : descElements) {
			if(node.getFirstChild().getTextContent().equals(Text.BACKGROUND.toString())) {
				descParent = node.getParentNode();
				break;
			}
		}

		if(descParent == null)
			throw new RuntimeException("Could not find " + Tag.DESC + " element with text " + Text.BACKGROUND);

		List<Node> rectElements = KgmlDocument.getElementsWithTag(descParent, Tag.RECT.toString());
		if(rectElements.isEmpty())
			throw new RuntimeException("Could not find background rect.");
		else {
			Element rect = (Element) rectElements.get(0);
			rect.setAttribute(Attribute.FILL.toString(), "#000000");
		}
	}

	private void processCompoundGroup(final Element group, final Element entry) {
		for(Node node : KgmlDocument.getElementsWithTag(group, Tag.ELLIPSE.toString())) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS.toString(), CssClass.CPD.toString());
			element.setAttribute(Attribute.STATE.toString(), "default");
		}

		group.setAttribute(Attribute.CLASS.toString(), CssClass.CPD.toString());
	}

	private void processEntrylessGroup(final Element group) {
		List<Node> pathNodes = KgmlDocument.getElementsWithTag(group, Tag.PATH.toString());
		for(Node node : pathNodes) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS.toString(), CssClass.RXN.toString());
			element.setAttribute(Attribute.STATE.toString(), "default");
		}
		group.setAttribute(Attribute.CLASS.toString(), CssClass.RXN.toString());
	}

	private void processMapGroup(final Element group, final Element entry) {
		for(Node node : KgmlDocument.getElementsWithTag(group, Tag.RECT.toString()))
			((Element) node).setAttribute(Attribute.FILL.toString(), "none");

		for(Node node : KgmlDocument.getElementsWithTag(group, Tag.TSPAN.toString())) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS.toString(), CssClass.MAP.toString());
			element.setAttribute(Attribute.STATE.toString(), "default");
			element.setAttribute(Attribute.FILL.toString(), "#FFFFFF");
		}

		group.setAttribute(Attribute.CLASS.toString(), CssClass.MAP.toString());
	}

	private void processReactionGroup(final Element group, final Element entry) {
		for(Node node : KgmlDocument.getElementsWithTag(group, Tag.PATH.toString())) {
			Element element = (Element) node;
			element.setAttribute(Attribute.CLASS.toString(), CssClass.RXN.toString());
			element.setAttribute(Attribute.STATE.toString(), "default");
		}

		group.setAttribute(Attribute.CLASS.toString(), CssClass.RXN.toString());
	}

	public void process() {
		processBackgroundRect();
		processAnnotatedMapElements();
		removeTitleElements();
		wrapSvgChildrenInViewport();
	}

	private void removeTitleElements() {
		List<Node> nodes = KgmlDocument.getElementsWithTag(document.getSvgNode(), Tag.TITLE.toString());
		for(Node node : nodes) {
			node.getParentNode().removeChild(node.getNextSibling());
			node.getParentNode().removeChild(node);
		}
	}

	private void wrapSvgChildrenInViewport() {
		// create the viewport element
		Node svgNode = document.getSvgNode();
		Element viewport = svgNode.getOwnerDocument().createElement(Tag.G.toString());
		viewport.setAttribute(Attribute.ID.toString(), "viewport");

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
