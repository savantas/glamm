package gov.lbl.glammdb.kegg.kgml;

import gov.lbl.glammdb.domain.AMRxn;
import gov.lbl.glammdb.domain.AMRxnElement;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class KgmlReactions {
	
	private enum Attribute {
		NAME("name"),
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
	
	private enum Tag {
		PRODUCT("product"),
		REACTION("reaction"),
		SUBSTRATE("substrate");

		private String value;

		private Tag(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
	
	private KgmlDocument document;
	
	private KgmlReactions() {}
	
	public static KgmlReactions create(final KgmlDocument document) {
		KgmlReactions kgmlReactions = new KgmlReactions();
		kgmlReactions.document = document;
		return kgmlReactions;
	}
	
	private AMRxn processReactionNode(final Node reactionNode) {
		AMRxn amRxn = new AMRxn();
		Element element = (Element) reactionNode;
		
		// set the reaction type
		String rxnType = element.getAttribute(Attribute.TYPE.toString());
		amRxn.setReversible(rxnType.equals("reversible"));
		
		// add the reaction ids
		List<String> rxnIds = KgmlDocument.extractIdsFromAttributeValue(((Element) reactionNode).getAttribute(Attribute.NAME.toString()));
		for(String rxnId : rxnIds)
			amRxn.addElement(new AMRxnElement(rxnId, AMRxnElement.Type.REACTION));
		
		// add the substrates
		List<Node> substrateNodes = KgmlDocument.getElementsWithTag(reactionNode, Tag.SUBSTRATE.toString());
		for(Node node : substrateNodes) {
			List<String> substrateIds = KgmlDocument.extractIdsFromAttributeValue(((Element) node).getAttribute(Attribute.NAME.toString()));
			for(String id : substrateIds)
				amRxn.addElement(new AMRxnElement(id, AMRxnElement.Type.SUBSTRATE));
		}
		
		// add the products
		List<Node> productNodes = KgmlDocument.getElementsWithTag(reactionNode, Tag.PRODUCT.toString());
		for(Node node : productNodes) {
			List<String> productIds = KgmlDocument.extractIdsFromAttributeValue(((Element) node).getAttribute(Attribute.NAME.toString()));
			for(String id : productIds)
				amRxn.addElement(new AMRxnElement(id, AMRxnElement.Type.PRODUCT));
		}
		
		return amRxn;
	}
	
	public void process() {
		List<Node> reactionNodes =  KgmlDocument.getElementsWithTag(document.getReactionsNode(), Tag.REACTION.toString());
		for(Node node : reactionNodes) {
			AMRxn rxn = processReactionNode(node);
			document.getAnnotatedMap().addRxnToNetwork(rxn);
		}
	}
	
}
