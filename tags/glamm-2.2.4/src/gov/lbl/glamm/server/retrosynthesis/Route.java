package gov.lbl.glamm.server.retrosynthesis;

import gov.lbl.glamm.shared.model.MetabolicNetwork.MNNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for conveniently representing routes between source and destination compounds.  Includes accessory methods for pushing and popping
 * route steps for convenient DFS implementations.
 * @author jtbates
 *
 */
public class Route {

	/**
	 * Compares two routes by total cost.
	 * @author jtbates
	 *
	 */
	public static class RouteComparator implements Comparator<Route> {

		@Override
		public int compare(Route o1, Route o2) {
			int tc1 = o1.getTotalCost();
			int tc2 = o2.getTotalCost();
			
			return (tc1 < tc2 ? -1 : (tc1 == tc2 ? 0 : 1));
		}
		
	}
	
	/**
	 * Route step assigning a cost to a node in the metabolic network (where a node includes both source and destination compound, as well
	 * as the reaction used to link the two.)  
	 * @author jtbates
	 *
	 */
	public static class Step {
		
		private MNNode 	node	= null;
		private int 	cost	= 0;
		
		/**
		 * Constructor.
		 * @param node The metabolic network node.
		 * @param cost The algorithm-dependent cost of this step.
		 */
		public Step(MNNode node, int cost) {
			this.node = node;
			this.cost = cost;
		}
		
		/**
		 * Gets the cost of this step.
		 * @return The cost.
		 */
		public int 		getCost() 	{ return cost; };
		
		/**
		 * Gets the metabolic network node for this step.
		 * @return The node.
		 */
		public MNNode 	getNode() 	{ return node; };		
	}
	
	private int 			totalCost 	= 0;
	private String			algorithm	= null;
	private String 			cpdSrcId	= null;
	private String 			cpdDstId 	= null;
	private String 			taxonomyId	= null;
	private String			mapTitle	= null;
	private List<Step> steps 			= new ArrayList<Step>();
	private Set<String> visitedCpdIds 	= new HashSet<String>();
	private Set<String> visitedRxnIds	= new HashSet<String>();
	
	/**
	 * Constructor.
	 * @param taxonomyId Taxonomy id.
	 * @param cpdSrcId Source compound id.
	 * @param cpdDstId Destination compound id.
	 * @param algorithm Algorithm used to derive this route.
	 * @param mapTitle Title of the map associated with this route.
	 */
	public Route(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		this.algorithm = algorithm;
		this.taxonomyId = taxonomyId;
		this.cpdSrcId = cpdSrcId;
		this.cpdDstId = cpdDstId;
		this.mapTitle = mapTitle;
		
		visitedCpdIds.add(cpdSrcId);
	}
	
	/**
	 * Shallow copy constructor.
	 * @param route The source route.
	 */
	public Route(Route route) {
		
		this.algorithm = route.algorithm;
		this.taxonomyId = route.taxonomyId;
		this.totalCost	= route.totalCost;
		this.cpdSrcId 	= route.cpdSrcId;
		this.cpdDstId 	= route.cpdDstId;
		this.mapTitle 	= route.mapTitle;
		
		for(Step step : route.steps)
			this.steps.add(step);		
	}
		
	/**
	 * Removes the last step of the route and returns it.
	 * @return The last step of the route, null if the route is empty.
	 */
	public Step pop() {
		
		Step step = null;
		
		if(!steps.isEmpty()) {
			step = steps.remove(steps.size() - 1);
			visitedCpdIds.remove(step.node.getCpd1ExtId());
			visitedRxnIds.remove(step.node.getRxnExtId());
			totalCost -= step.cost;
		}
		
		return step;
	}

	/**
	 * Adds a step to the end of the route.
	 * @param step The step to be added.
	 */
	public void push(Step step) {
		totalCost += step.cost;
		steps.add(step);
		visitedCpdIds.add(step.node.getCpd1ExtId());
		visitedRxnIds.add(step.node.getRxnExtId());
	}
	
	/**
	 * Gets the algorithm used to derive this route.
	 * @return The algorithm.
	 */
	public final String getAlgorithm() {
		return algorithm;
	}
		
	/**
	 * Gets the source compound id of this route.
	 * @return The source compound id.
	 */
	public final String getCpdSrcId() {
		return cpdSrcId;
	}
	
	/**
	 * Gets the destination compound id of this route.
	 * @return The destination compound id.
	 */
	public final String getCpdDstId() {
		return cpdDstId;
	}
	
	/**
	 * Gets the map title of this route.
	 * @return The map title.
	 */
	public final String getMapTitle() {
		return mapTitle;
	}
	
	/**
	 * Gets the taxonomy id of this route.
	 * @return The taxonomy id.
	 */
	public final String getTaxonomyId() {
		return taxonomyId;
	}
	
	/**
	 * Gets the total cost of this route.
	 * @return The total cost.
	 */
	public int getTotalCost() {
		return totalCost;
	}

	/**
	 * Gets the steps of this route, in order.
	 * @return The steps of this route.
	 */
	public List<Step> getSteps() {
		return steps;
	}
		
	/**
	 * Indicates if the specified compound has been visited in this route.
	 * @param cpdId The compound id.
	 * @return Flag indicating if the compound has been visited.
	 */
	public boolean visitedCompound(String cpdId) {
		return visitedCpdIds.contains(cpdId);
	}
	
	/**
	 * Indicates if the specified reaction has been visited in this route.
	 * @param rxnId The reaction id.
	 * @return Flag indicating if the reaction has been visited.
	 */
	public boolean visitedReaction(String rxnId) {
		return visitedRxnIds.contains(rxnId);
	}
	
}
