package gov.lbl.glamm.shared.model;

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

	/**
	 * Representation of a node in a metabolic network.
	 * @author jtbates
	 *
	 */
	public static class MNNode implements Serializable {

	
		private String cpd0ExtId = null;
		private String cpd1ExtId = null;
		private String rxnExtId = null;	
		private boolean isNative = true;

	
		@SuppressWarnings("unused")
		private MNNode() {}

		/**
		 * Constructor
		 * @param rxnExtId Reaction id.
		 * @param cpd0ExtId Source compound id.
		 * @param cpd1ExtId Destination compound id.
		 */
		public MNNode(String rxnExtId, String cpd0ExtId, String cpd1ExtId) {
			this.rxnExtId = rxnExtId;
			this.cpd0ExtId = cpd0ExtId;
			this.cpd1ExtId = cpd1ExtId;
		}

	
		/**
		 * Constructor
		 * @param rxnExtId Reaction id.
		 * @param cpd0ExtId Source compound id.
		 * @param cpd1ExtId Destination compound id.
		 * @param isNative Flag indicating whether or not the reaction is native.
		 */
		public MNNode(String rxnExtId, String cpd0ExtId, String cpd1ExtId, boolean isNative) {
			this(rxnExtId, cpd0ExtId, cpd1ExtId);
			setNative(isNative);
		}

	
		/**
		 * Gets the source compound id.
		 * @return The id.
		 */
		public String getCpd0ExtId() {
			return cpd0ExtId;
		}

	
		/**
		 * Gets the destination compound id.
		 * @return The id.
		 */
		public String getCpd1ExtId() {
			return cpd1ExtId;
		}

			
		/**
		 * Gets the reaction id.
		 * @return The id.
		 */
		public String getRxnExtId() {
			return rxnExtId;
		}

	
		/**
		 * Gets isNative flag.
		 * @return The flag.
		 */
		public boolean isNative() {
			return isNative;
		}

	
		public void setNative(boolean isNative) {
			this.isNative = isNative;
		}

	
		@Override
		public String toString() {
			return rxnExtId + " (" + cpd0ExtId + ", " + cpd1ExtId + ")";
		}

	
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

	
	}


	private String mapTitle = null;	
	private List<MNNode> nodes = null;
	private Map<String, Set<MNNode>> cpdId2Nodes = null;
	private Map<String, Set<MNNode>> rxnId2Nodes = null;


	@SuppressWarnings("unused")
	private MetabolicNetwork() {}

	/**
	 * Constructor
	 * @param mapTitle The title of the map associated with this network.
	 */
	public MetabolicNetwork(String mapTitle) {
		super();
		this.mapTitle = mapTitle;
		nodes = new ArrayList<MNNode>();
		cpdId2Nodes = new HashMap<String, Set<MNNode>>();
		rxnId2Nodes = new HashMap<String, Set<MNNode>>();
	}


	/**
	 * Adds a node to this network.
	 * @param node The node.
	 */
	public void addNode(MNNode node) {

		if(node == null)
			throw new IllegalArgumentException("Attempted to add null node to metabolic network.");
		nodes.add(node);

		addNodeToHash(node.getCpd0ExtId(), cpdId2Nodes, node);
		addNodeToHash(node.getRxnExtId(), rxnId2Nodes, node);
	}


	private void addNodeToHash(String key, Map<String, Set<MNNode>> hash, MNNode node) {
		Set<MNNode> nodes = hash.get(key);
		if(nodes == null) {
			nodes = new HashSet<MNNode>();
			hash.put(key, nodes);
		}
		nodes.add(node);
	}


	/**
	 * Gets the map title.
	 * @return The map title.
	 */
	public String getMapTitle() {
		return mapTitle;
	}


	/**
	 * Gets the set of nodes adjacent to a compound.
	 * @param cpdId The compound id.
	 */
	public Set<MNNode> getNodesForCpdId(String cpdId) {
		return cpdId2Nodes.get(cpdId);
	}


	/**
	 * Gets the set of nodes adjacent to a reaction.
	 * @param rxnId The reaction id.
	 */
	public Set<MNNode> getNodesForRxnId(String rxnId) {
		return rxnId2Nodes.get(rxnId);
	}


	/**
	 * Sets all nodes with reaction id in rxnIds to native.
	 * @param rxnIds The set of rxnIds that are to be set native.
	 */
	public void setNativeRxns(Collection<String> rxnIds) {
		for(MNNode node : nodes) {
			String rxnId = node.getRxnExtId();
			node.setNative(rxnIds.contains(rxnId));
		}
	}


}
