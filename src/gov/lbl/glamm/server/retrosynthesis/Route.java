package gov.lbl.glamm.server.retrosynthesis;

import gov.lbl.glamm.client.model.MNNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Route {

	//********************************************************************************

	public static class RouteComparator implements Comparator<Route> {

		@Override
		public int compare(Route o1, Route o2) {
			int tc1 = o1.getTotalCost();
			int tc2 = o2.getTotalCost();
			
			return (tc1 < tc2 ? -1 : (tc1 == tc2 ? 0 : 1));
		}
		
	}
	
	//********************************************************************************
	
	public static class Step {
		
		private MNNode 	node	= null;
		private int 	cost	= 0;
		
		public Step(MNNode node, int cost) {
			this.node = node;
			this.cost = cost;
		}
		
		public int 		getCost() 	{ return cost; };
		public MNNode 	getNode() 	{ return node; };
		
		public String toString() {
			return "(" + cost + ") " + node.toString();
		}
				
	}
	
	//********************************************************************************

	private int 			totalCost 	= 0;
	private String			algorithm	= null;
	private String 			cpdSrcId	= null;
	private String 			cpdDstId 	= null;
	private String 			taxonomyId	= null;
	private String			mapTitle	= null;
	private List<Step> steps 			= new ArrayList<Step>();
	private Set<String> visitedCpdIds 	= new HashSet<String>();
	private Set<String> visitedRxnIds	= new HashSet<String>();
	
	//********************************************************************************

	public Route(String taxonomyId, String cpdSrcId, String cpdDstId, String algorithm, String mapTitle) {
		this.algorithm = algorithm;
		this.taxonomyId = taxonomyId;
		this.cpdSrcId = cpdSrcId;
		this.cpdDstId = cpdDstId;
		this.mapTitle = mapTitle;
		
		visitedCpdIds.add(cpdSrcId);
	}
	
	//********************************************************************************
	
	// shallow copy constructor
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
	
	//********************************************************************************
	
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

	//********************************************************************************

	public void push(Step step) {
		totalCost += step.cost;
		steps.add(step);
		visitedCpdIds.add(step.node.getCpd1ExtId());
		visitedRxnIds.add(step.node.getRxnExtId());
	}
	
	//********************************************************************************

	public final String getAlgorithm() {
		return algorithm;
	}
	
	//********************************************************************************
		
	public final String getCpdSrcId() {
		return cpdSrcId;
	}
	
	//********************************************************************************

	public final String getCpdDstId() {
		return cpdDstId;
	}
	
	//********************************************************************************

	public final String getMapTitle() {
		return mapTitle;
	}
	
	//********************************************************************************

	public final String getTaxonomyId() {
		return taxonomyId;
	}
	
	//********************************************************************************

	public int getTotalCost() {
		return totalCost;
	}

	//********************************************************************************
	
	public Step getLastStep() {
		return steps.get(steps.size() - 1);
	}
	
	//********************************************************************************

	public int getLength() {
		return steps.size();
	}

	//********************************************************************************
	
	public List<Step> getSteps() {
		return steps;
	}
	
	//********************************************************************************

	public String toString() {
		String out = "";
		for(Step step: getSteps()) {
			out += step.toString() + "\n";
		}
		return out;
	}
	
	public static String toString(List<Route> routes) {
		String result = "";
		for(Route route : routes) {
			result += route.toString() + "\n\n";
		}
		return result;
	}
	
	//********************************************************************************
	
	public boolean visitedCompound(String cpdId) {
		return visitedCpdIds.contains(cpdId);
	}
	
	//********************************************************************************

	public boolean visitedReaction(String rxnId) {
		return visitedRxnIds.contains(rxnId);
	}
	
	//********************************************************************************
	

}
