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

/**
 * KGML+ document handler - extracts svg and reaction network information and stores GLAMM annotated maps in the GLAMM database
 * via the KgmlReactions and KgmlSvg handlers.
 * @author jtbates
 *
 */
public final class KgmlPlusDocument {
	
	private static interface Tag {
		final static String REACTIONS	= "reactions";
		final static String SVG 		= "svg";
	}
	
	private Document document;
	private PersistentAnnotatedMap annotatedMap;
	
	@SuppressWarnings("unused")
	private String kgmlPath;
	private String svgPath;

	/**
	 * Creates a KgmlPlusDocument instance.
	 * @param mapId The KEGG map id.
	 * @param mapTitle The map title.
	 * @param kgmlPath The path to the KGML+ document file.
	 * @param svgPath The path to the resulting GLAMM-annotated SVG file.
	 * @return The document instance.
	 */
	public static KgmlPlusDocument create(final String mapId, final String mapTitle, final String kgmlPath, final String svgPath) {
		try {
			File file = new File(kgmlPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return new KgmlPlusDocument(mapId, mapTitle, kgmlPath, svgPath, db.parse(file));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	private KgmlPlusDocument(final String mapId, final String mapTitle, final String kgmlPath, final String svgPath, final Document document) {
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
	
	private static void getElementsWithTag(final List<Node> list, final Node node, final String tag) {

		if(node == null)
			return;

		if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tag))
			list.add(node);

		for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			getElementsWithTag(list, child, tag);
	}

	/**
	 * Gets a list of elements with the given tag rooted at the given node.
	 * @param node The root node.
	 * @param tag The tag.
	 * @return The list of elements (as Nodes.)
	 */
	public static List<Node> getElementsWithTag(final Node node, final String tag) {
		List<Node> list = new ArrayList<Node>();
		getElementsWithTag(list, node, tag);
		return list;
	}
	
	/**
	 * Extracts a list of ids from an attribute value string.
	 * @param attributeValue The attribute value string.
	 * @return The list of ids.
	 */
	public static List<String> extractIdsFromAttributeValue(final String attributeValue) {
		List<String> ids = new ArrayList<String>();
		for(String value : attributeValue.split("\\+")) {
			ids.add(value.split("\\:")[1]);
		}
		return ids;
	}

	/**
	 * Takes a KGML+ style attribute value and normalizes it to a GLAMM style attribute value.
	 * @param attributeValue The KGML+ style attribute value.
	 * @return The GLAMM style attribute value.
	 */
	public static String normalizeAttributeValue(final String attributeValue) {
		String normalized = "";
		for(String id : extractIdsFromAttributeValue(attributeValue))
			normalized += id + "+";
		return normalized.substring(0, normalized.length() - 1);
	}
	
	/**
	 * Gets the persistent annotated map.
	 * @return The persistent annotated map.
	 */
	public PersistentAnnotatedMap getAnnotatedMap() {
		return annotatedMap;
	}

	/**
	 * Gets the reactions node.
	 * @return The reactions node.
	 */
	public Node getReactionsNode() {
		return getFirstNodeWithTag(Tag.REACTIONS);
	}

	/**
	 * Gets the svg node.
	 * @return The svg node.
	 */
	public Node getSvgNode() {
		return getFirstNodeWithTag(Tag.SVG);
	}
	
	/**
	 * Outputs the contents of a node to a file.
	 * @param node The node.
	 * @param path The path to the file.
	 */
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
	
	/**
	 * Processes a KGML+ document.
	 */
	public void process() {
		KgmlSvg svg = KgmlSvg.create(this);
		KgmlReactions reactions = KgmlReactions.create(this);
		svg.process();
		reactions.process();
		outputNodeToPath(getSvgNode(), svgPath);
	}
	
	/**
	 * Stores the persistent annotated map in the GLAMM database.
	 * @return The id of the map.
	 */
	public Long storeAnnotatedMap() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Long amId = (Long) session.save(getAnnotatedMap());

		session.getTransaction().commit();
		return amId;
	}
	
}
