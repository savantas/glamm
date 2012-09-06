package gov.lbl.glamm.server.retrosynthesis.algorithms;

import gov.lbl.glamm.server.retrosynthesis.Route;
import gov.lbl.glamm.server.retrosynthesis.Route.Step;
import gov.lbl.glamm.shared.model.MetabolicNetwork;
import gov.lbl.glamm.shared.model.MetabolicNetwork.MNNode;

import java.util.List;
import java.util.Set;

/**
 * Depth first search for retrosynthesis.
 * @author jtbates
 *
 */
public class RADepthFirstSearch extends RetrosynthesisAlgorithm {

	private final int MAX_DEPTH = 10;

	/**
	 * Constructor.
	 * @param algorithm Algorithm used for this search.
	 * @param taxonomyId The taxonomy id of the organism for which the metabolic network is being searched.
	 * @param network The metabolic network.
	 */
	protected RADepthFirstSearch(String algorithm, String taxonomyId, MetabolicNetwork network) {
		super(algorithm, taxonomyId, network);
	}

	

	@Override
	public int costForStep(MNNode node) {
		return 1;	// steps have unit cost
	}

	

	@Override
	public List<Route> calculateRoutes(String cpdSrcExtId, String cpdDstExtId) {
		_calculateSubRoute(null, cpdSrcExtId, cpdDstExtId, 0);
		return routes;
	}

	

	protected void _calculateSubRoute(Route subRoute, String cpdSrcExtId, String cpdDstExtId, int depth) {

		// if we don't have a subRoute, make one
		if(subRoute == null)
			subRoute = new Route(taxonomyId, cpdSrcExtId, cpdDstExtId, this.getAlgorithm(), this.getMapTitle());

		// if the subroute is already more expensive than the lowest cost route already found,
		// disregard it and move to the next one
		int lowestCost = this.getLowestRouteCost();
		if(lowestCost > 0 && subRoute.getTotalCost() > lowestCost)
			return;

		// if we've overshot the max depth, return
		if(depth > MAX_DEPTH)
			return;

		// if we've found our endpoint, save the route
		if(cpdSrcExtId.equals(cpdDstExtId)) {
			addRoute(new Route(subRoute));
			return;
		}

		// depth first search
		Set<MNNode> nodes = network.getNodesForCpdId(cpdSrcExtId);
		if(nodes == null)
			return;

		for(MNNode node : nodes) {
			String cpdId = node.getCpd1ExtId();
			if(!subRoute.visitedCompound(cpdId) && 
					!subRoute.visitedReaction(node.getRxnExtId())) {
				subRoute.push(new Step(node, costForStep(node)));
				depth++;
				_calculateSubRoute(subRoute, cpdId, cpdDstExtId, depth);
				subRoute.pop();
				depth--;
			}
		}

	}

	

}
