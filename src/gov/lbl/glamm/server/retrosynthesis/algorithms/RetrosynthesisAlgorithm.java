package gov.lbl.glamm.server.retrosynthesis.algorithms;

import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.util.MNNode;
import gov.lbl.glamm.client.presenter.RetrosynthesisPresenter;
import gov.lbl.glamm.server.retrosynthesis.Route;
import gov.lbl.glamm.server.retrosynthesis.Route.RouteComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RetrosynthesisAlgorithm {

	private static final int MAX_ROUTES 	= 10;
	
	//********************************************************************************

	private String					algorithm 			= null;
	private int						maxRouteCost 		= 0;
	protected MetabolicNetwork		network 			= null;
	protected List<Route> 			routes 				= new ArrayList<Route>();
	protected String				taxonomyId 			= null;
	
	//********************************************************************************

	protected RetrosynthesisAlgorithm(String algorithm, String taxonomyId, MetabolicNetwork network) {
		this.algorithm = algorithm;
		this.taxonomyId	= taxonomyId;
		this.network 	= network;
	}
	
	//********************************************************************************
	
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
	
	//********************************************************************************
	
	public static RetrosynthesisAlgorithm create(String algorithm, String taxonomyId, MetabolicNetwork network) {
		RetrosynthesisAlgorithm ra = null;
		
		if(algorithm.equals(RetrosynthesisPresenter.View.ALGORITHM_DFS_VALUE)) {
			ra = new RADepthFirstSearch(algorithm, taxonomyId, network);
		}
		
		else if(algorithm.equals(RetrosynthesisPresenter.View.ALGORITHM_TW_DFS_VALUE)) {
			ra = new RATaxonWeightedDFS(algorithm, taxonomyId, network);
		}
		
		return ra;
	}
	
	//********************************************************************************
	
	public abstract int costForStep(MNNode node);
	
	//********************************************************************************

	public abstract List<Route> calculateRoutes(String cpdSrcExtId, String cpdDstExtId);
	
	//********************************************************************************
	
	public int getLowestRouteCost() {
		int lowestCost = -1;
		if(routes.size() > 0)
			lowestCost = routes.get(0).getTotalCost();
		return lowestCost;
	}
	
	//********************************************************************************

	public String getAlgorithm() {
		return algorithm;
	}
	
	//********************************************************************************

	public String getMapTitle() {
		if(network != null)
			return network.getMapTitle();
		return null;
	}
	
	//********************************************************************************

}
