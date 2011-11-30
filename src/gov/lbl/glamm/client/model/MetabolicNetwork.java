package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The metabolic network graph associated with a GLAMM annotated SVG map.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class MetabolicNetwork implements Serializable {

	public static class MNNode implements Serializable {

		//********************************************************************************

		private String cpd0ExtId = null;
		private String cpd1ExtId = null;
		private String rxnExtId = null;	
		private boolean isNative = true;

		//********************************************************************************

		@SuppressWarnings("unused")
		private MNNode() {}

		public MNNode(String rxnExtId, String cpd0ExtId, String cpd1ExtId) {
			this.rxnExtId = rxnExtId;
			this.cpd0ExtId = cpd0ExtId;
			this.cpd1ExtId = cpd1ExtId;
		}

		//********************************************************************************

		public MNNode(String rxnExtId, String cpd0ExtId, String cpd1ExtId, boolean isNative) {
			this(rxnExtId, cpd0ExtId, cpd1ExtId);
			setNative(isNative);
		}

		//********************************************************************************

		public String getCpd0ExtId() {
			return cpd0ExtId;
		}

		//********************************************************************************

		public String getCpd1ExtId() {
			return cpd1ExtId;
		}

		//********************************************************************************

		public String getRxnExtId() {
			return rxnExtId;
		}

		//********************************************************************************

		public boolean isNative() {
			return isNative;
		}

		//********************************************************************************

		public void setNative(boolean isNative) {
			this.isNative = isNative;
		}

		//********************************************************************************

		@Override
		public String toString() {
			return rxnExtId + " (" + cpd0ExtId + ", " + cpd1ExtId + ")";
		}

		//********************************************************************************

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result
			+ ((cpd0ExtId == null) ? 0 : cpd0ExtId.hashCode());
			result = prime * result
			+ ((cpd1ExtId == null) ? 0 : cpd1ExtId.hashCode());
			result = prime * result + (isNative ? 1231 : 1237);
			result = prime * result
			+ ((rxnExtId == null) ? 0 : rxnExtId.hashCode());
			return result;
		}

		//********************************************************************************

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			MNNode other = (MNNode) obj;
			if (cpd0ExtId == null) {
				if (other.cpd0ExtId != null)
					return false;
			} else if (!cpd0ExtId.equals(other.cpd0ExtId))
				return false;
			if (cpd1ExtId == null) {
				if (other.cpd1ExtId != null)
					return false;
			} else if (!cpd1ExtId.equals(other.cpd1ExtId))
				return false;
			if (isNative != other.isNative)
				return false;
			if (rxnExtId == null) {
				if (other.rxnExtId != null)
					return false;
			} else if (!rxnExtId.equals(other.rxnExtId))
				return false;
			return true;
		}

		//********************************************************************************

	}

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

		if(node == null)
			throw new IllegalArgumentException("Attempted to add null node to metabolic network.");
		nodes.add(node);

		addNodeToHash(node.getCpd0ExtId(), cpdId2Nodes, node);
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
