package gov.lbl.glamm.client.model;


import gov.lbl.glamm.client.model.util.MNNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")

public class MetabolicNetwork implements Serializable {

	//********************************************************************************
		
	private String mapTitle = null;	
	private List<MNNode> nodes = null;
	private Map<String, Set<MNNode>> cpdId2Nodes = null;
	private Map<String, Set<MNNode>> rxnId2Nodes = null;

	//********************************************************************************

	@SuppressWarnings("unused")
	private MetabolicNetwork() {}
	
	public MetabolicNetwork(String mapTitle) {
		super();
		this.mapTitle = mapTitle;
		nodes = new ArrayList<MNNode>();
		cpdId2Nodes = new HashMap<String, Set<MNNode>>();
		rxnId2Nodes = new HashMap<String, Set<MNNode>>();
	}

	//********************************************************************************

	public void addNode(MNNode node) {

		if(nodes == null)
			nodes = new ArrayList<MNNode>();
		nodes.add(node);

		addNodeToHash(node.getCpd0ExtId(), cpdId2Nodes, node);
	//	addNodeToHash(node.getCpd1ExtId(), cpdId2Nodes, node);
		addNodeToHash(node.getRxnExtId(), rxnId2Nodes, node);
	}

	//********************************************************************************

	private void addNodeToHash(String key, Map<String, Set<MNNode>> hash, MNNode node) {
		Set<MNNode> nodes = hash.get(key);
		if(nodes == null) {
			nodes = new HashSet<MNNode>();
			hash.put(key, nodes);
		}
		nodes.add(node);
	}
	
	//********************************************************************************

	public String getMapTitle() {
		return mapTitle;
	}

	//********************************************************************************

	public Set<MNNode> getNodesForCpdId(String cpdId) {
		return cpdId2Nodes.get(cpdId);
	}

	//********************************************************************************

	public Set<MNNode> getNodesForRxnId(String cpdId) {
		return rxnId2Nodes.get(cpdId);
	}
	
	//********************************************************************************

	public void setNativeRxns(Collection<String> rxnIds) {
		for(MNNode node : nodes) {
			String rxnId = node.getRxnExtId();
			node.setNative(rxnIds.contains(rxnId));
		}
	}

	//********************************************************************************

}
