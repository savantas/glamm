package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings({ "unused", "serial" })

public class MetabolicNetwork extends GlammPrimitive implements Serializable {

	//********************************************************************************
	
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	
	private String mapTitle = null;	
	private String taxonomyId = null;
	private ArrayList<MNNode> nodes = null;
	private HashMap<String, HashSet<MNNode>> cpdId2Nodes = null;
	private HashMap<String, HashSet<MNNode>> rxnId2Nodes = null;

	//********************************************************************************

	private MetabolicNetwork() {}
	
	public MetabolicNetwork(String mapTitle) {
		super();
		this.mapTitle = mapTitle;
		nodes = new ArrayList<MNNode>();
		cpdId2Nodes = new HashMap<String, HashSet<MNNode>>();
		rxnId2Nodes = new HashMap<String, HashSet<MNNode>>();
	}

	//********************************************************************************

	public MetabolicNetwork(String mapTitle, String taxonomyId) {
		this(mapTitle);
		setTaxonomyId(taxonomyId);
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

	private void addNodeToHash(String key, HashMap<String, HashSet<MNNode>> hash, MNNode node) {
		HashSet<MNNode> nodes = hash.get(key);
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

	public HashSet<MNNode> getNodesForCpdId(String cpdId) {
		return cpdId2Nodes.get(cpdId);
	}

	//********************************************************************************

	public HashSet<MNNode> getNodesForRxnId(String cpdId) {
		return rxnId2Nodes.get(cpdId);
	}

	@Override
	public Type getType() {
		return TYPE;
	}
	
	//********************************************************************************

	public void setNativeRxns(String taxonomyId, Collection<String> rxnIds) {
		for(MNNode node : nodes) {
			String rxnId = node.getRxnExtId();
			node.setNative(rxnIds.contains(rxnId));
		}
	}

	//********************************************************************************

	public void setTaxonomyId(String taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

	//********************************************************************************

}
