package gov.lbl.glamm.server.retrosynthesis.algorithms;

import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.MetabolicNetwork.MNNode;
import gov.lbl.glamm.client.presenter.RetrosynthesisPresenter;
import gov.lbl.glamm.server.retrosynthesis.Route;
import gov.lbl.glamm.server.retrosynthesis.Route.RouteComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for retrosynthesis algorithms and container for the routes found.
 * @author jtbates
 *
 */
public abstract class RetrosynthesisAlgorithm {

	private static final int MAX_ROUTES 	= 10;
	
	private String					algorithm 			= null;
	private int						maxRouteCost 		= 0;
	protected MetabolicNetwork		network 			= null;
	protected List<Route> 			routes 				= new ArrayList<Route>();
	protected String				taxonomyId 			= null;
	
	
	/**
	 * Constructor.
	 * @param algorithm Name of the algorithm.
	 * @param taxonomyId Taxonomy id for the target organism.
	 * @param network Metabolic network for the global map.
	 */
	protected RetrosynthesisAlgorithm(String algorithm, String taxonomyId, MetabolicNetwork network) {
		this.algorithm = algorithm;
		this.taxonomyId	= taxonomyId;
		this.network 	= network;
	}
	
	
	/**
	 * Adds a route to the list of routes found.
	 * @param route The route.
	 */
	public void addRoute(Route route) {
		
		if(routes.size() <= MAX_ROUTES || route.getTotalCost() < maxRouteCost) {
			routes.add(route);
			Collections.sort(routes, new RouteComparator());
		}
		
		if(routes.size() > MAX_ROUTES) {
			routes.remove(routes.size() - 1);
		}
		
		maxRouteCost = routes.get(routes.size() -1).getTotalCost();
		
	}
	
	
	/**
	 * Factory class for creating a retrosynthesis algorithm instance.
	 * @param algorithm The name of the algorithm.
	 * @param taxonomyId The taxonomy id of the organism, if applicable.
	 * @param network The metabolic network of the map on which this algorithm is run.
	 * @return A retrosynthesis algorithm instance.
	 */
	public static RetrosynthesisAlgorithm create(String algorithm, String taxonomyId, MetabolicNetwork network) {
		RetrosynthesisAlgorithm ra = null;
		
		if(algorithm.equals(RetrosynthesisPresenter.Algorithm.DFS.getAlgorithm())) {
			ra = new RADepthFirstSearch(algorithm, taxonomyId, network);
		}
		
		else if(algorithm.equals(RetrosynthesisPresenter.Algorithm.TW_DFS.getAlgorithm())) {
			ra = new RATaxonWeightedDFS(algorithm, taxonomyId, network);
		}
		
		return ra;
	}
	
	/**
	 * Abstract method for evaluating the cost of a route step.
	 * @param node The node associated with this route step.
	 * @return The cost.
	 */
	public abstract int costForStep(MNNode node);
	
	/**
	 * Abstract method for calculating routes between compounds using this algorithm.
	 * @param cpdSrcExtId The source compound id.
	 * @param cpdDstExtId The destination compound id.
	 * @return The list of routes between the source and destination compound.
	 */
	public abstract List<Route> calculateRoutes(String cpdSrcExtId, String cpdDstExtId);
	
	
	/**
	 * Gets the lowest cost of the routes found so far.
	 * @return The lowest cost.
	 */
	public int getLowestRouteCost() {
		int lowestCost = -1;
		if(routes.size() > 0)
			lowestCost = routes.get(0).getTotalCost();
		return lowestCost;
	}
	
	/**
	 * Gets the name of the algorithm.
	 * @return The name.
	 */
	public String getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Gets the title of the metabolic network's base map.
	 * @return The map title.
	 */
	public String getMapTitle() {
		if(network != null)
			return network.getMapTitle();
		return null;
	}
	
}
