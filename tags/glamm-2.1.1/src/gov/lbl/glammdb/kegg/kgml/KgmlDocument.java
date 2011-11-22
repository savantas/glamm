package gov.lbl.glammdb.kegg.kgml;

import gov.lbl.glammdb.domain.PersistentAnnotatedMap;
import gov.lbl.glammdb.util.HibernateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class KgmlDocument {
	
	private enum Tag {
		REACTIONS("reactions"),
		SVG("svg");
		
		private String value;

		private Tag(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public static KgmlDocument create(final String mapId, final String mapTitle, final String kgmlPath, final String svgPath) {
		try {
			File file = new File(kgmlPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return new KgmlDocument(mapId, mapTitle, kgmlPath, svgPath, db.parse(file));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	private Document document;
	private Node svgNode;
	private Node reactionsNode;
	
	private PersistentAnnotatedMap annotatedMap;
	
	@SuppressWarnings("unused")
	private String kgmlPath;
	private String svgPath;

	private KgmlDocument(final String mapId, final String mapTitle, final String kgmlPath, final String svgPath, final Document document) {
		this.kgmlPath = kgmlPath;
		this.svgPath = svgPath;
		this.document = document;
		
		this.annotatedMap = new PersistentAnnotatedMap();
		annotatedMap.setMapId(mapId);
		annotatedMap.setTitle(mapTitle);
		annotatedMap.setIcon(mapId + ".png");
		annotatedMap.setSvg(mapId + ".svg");
	}
		
	private Node getFirstNodeWithTag(final String tag) {
		NodeList nodeList = document.getElementsByTagName(tag);
		if(nodeList == null)
			throw new RuntimeException("Could not find tag " + tag + "in document " + document.getDocumentURI());
		return nodeList.item(0);
	}
	
	private static void _getElementsWithTag(final List<Node> list, final Node node, final String tag) {

		if(node == null)
			return;

		if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tag))
			list.add(node);

		for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			_getElementsWithTag(list, child, tag);
	}

	public static List<Node> getElementsWithTag(final Node node, final String tag) {
		List<Node> list = new ArrayList<Node>();
		_getElementsWithTag(list, node, tag);
		return list;
	}
	
	public static List<String> extractIdsFromAttributeValue(final String attributeValue) {
		List<String> ids = new ArrayList<String>();
		for(String value : attributeValue.split("\\+")) {
			ids.add(value.split("\\:")[1]);
		}
		return ids;
	}

	public static String normalizeAttributeValue(final String attributeValue) {
		String normalized = "";
		for(String id : extractIdsFromAttributeValue(attributeValue))
			normalized += id + "+";
		return normalized.substring(0, normalized.length() - 1);
	}
	
	public PersistentAnnotatedMap getAnnotatedMap() {
		return annotatedMap;
	}
	
	public Node getReactionsNode() {
		if(reactionsNode == null) {
			reactionsNode = getFirstNodeWithTag(Tag.REACTIONS.toString());
		}
		return reactionsNode;
	}

	public Node getSvgNode() {
		if(svgNode == null) {
			svgNode = getFirstNodeWithTag(Tag.SVG.toString());
		}
		return svgNode;
	}
	
	public static void outputNodeToPath(final Node node, final String path) {
		try {
			// create an empty document
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			
			// adopt the node into this document
			document.adoptNode(node);
			document.appendChild(node);
			document.normalize();
			
			// output the document to path
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult streamResult = new StreamResult(new File(path));
			DOMSource source = new DOMSource(document);
			transformer.transform(source, streamResult);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void process() {
		KgmlSvg svg = KgmlSvg.create(this);
		KgmlReactions reactions = KgmlReactions.create(this);
		svg.process();
		reactions.process();
		outputNodeToPath(getSvgNode(), svgPath);
	}
	
	private Long storeAnnotatedMap() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Long amId = (Long) session.save(getAnnotatedMap());

		session.getTransaction().commit();
		return amId;
	}
	
	
	public void store() {
		storeAnnotatedMap();
	}
	
}
