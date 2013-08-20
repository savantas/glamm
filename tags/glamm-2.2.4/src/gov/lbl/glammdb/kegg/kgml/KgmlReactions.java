package gov.lbl.glammdb.kegg.kgml;

import gov.lbl.glammdb.domain.PersistentAMRxn;
import gov.lbl.glammdb.domain.PersistentAMRxnElement;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class for processing the KGML+ document reactions node - a description of the connectivity of the reaction network
 * in the context of the map.
 * @author jtbates
 *
 */
class KgmlReactions {
	
	private static interface Attribute {
		final static String NAME 	= "name";
		final static String TYPE 	= "type";
	}
	
	private static interface Tag {
		final static String PRODUCT 	= "product";
		final static String REACTION 	= "reaction";
		final static String SUBSTRATE 	= "substrate";
	}
	
	private KgmlPlusDocument document;
	
	private KgmlReactions() {}
	
	/**
	 * Creates a KgmlReactions instance from a KgmlPlusDocument.
	 * @param document The document.
	 * @return The instance.
	 */
	public static KgmlReactions create(final KgmlPlusDocument document) {
		KgmlReactions kgmlReactions = new KgmlReactions();
		kgmlReactions.document = document;
		return kgmlReactions;
	}
	
	private PersistentAMRxn processReactionNode(final Node reactionNode) {
		PersistentAMRxn amRxn = new PersistentAMRxn();
		Element element = (Element) reactionNode;
		
		// set the reaction type
		String rxnType = element.getAttribute(Attribute.TYPE);
		amRxn.setReversible(rxnType.equals("reversible"));
		
		// add the reaction ids
		List<String> rxnIds = KgmlPlusDocument.extractIdsFromAttributeValue(((Element) reactionNode).getAttribute(Attribute.NAME));
		for(String rxnId : rxnIds)
			amRxn.addElement(new PersistentAMRxnElement(rxnId, PersistentAMRxnElement.Type.REACTION));
		
		// add the substrates
		List<Node> substrateNodes = KgmlPlusDocument.getElementsWithTag(reactionNode, Tag.SUBSTRATE);
		for(Node node : substrateNodes) {
			List<String> substrateIds = KgmlPlusDocument.extractIdsFromAttributeValue(((Element) node).getAttribute(Attribute.NAME));
			for(String id : substrateIds)
				amRxn.addElement(new PersistentAMRxnElement(id, PersistentAMRxnElement.Type.SUBSTRATE));
		}
		
		// add the products
		List<Node> productNodes = KgmlPlusDocument.getElementsWithTag(reactionNode, Tag.PRODUCT);
		for(Node node : productNodes) {
			List<String> productIds = KgmlPlusDocument.extractIdsFromAttributeValue(((Element) node).getAttribute(Attribute.NAME));
			for(String id : productIds)
				amRxn.addElement(new PersistentAMRxnElement(id, PersistentAMRxnElement.Type.PRODUCT));
		}
		
		return amRxn;
	}
	
	/**
	 * Processes the reactions node.
	 */
	public void process() {
		List<Node> reactionNodes =  KgmlPlusDocument.getElementsWithTag(document.getReactionsNode(), Tag.REACTION);
		for(Node node : reactionNodes) {
			PersistentAMRxn rxn = processReactionNode(node);
			document.getAnnotatedMap().addRxnToNetwork(rxn);
		}
	}
	
}
