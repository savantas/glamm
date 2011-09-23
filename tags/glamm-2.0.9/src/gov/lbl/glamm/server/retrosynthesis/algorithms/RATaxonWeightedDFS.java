package gov.lbl.glamm.server.retrosynthesis.algorithms;

import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.util.MNNode;

public class RATaxonWeightedDFS extends RADepthFirstSearch {
	
	//********************************************************************************
	
	private final int COST_RXN_NATIVE		= 1;
	private final int COST_RXN_NOT_NATIVE 	= 100;

	//********************************************************************************

	protected RATaxonWeightedDFS(String algorithm, String taxonomyId, MetabolicNetwork network) {
		super(algorithm, taxonomyId, network);
	}

	//********************************************************************************

	@Override
	public int costForStep(MNNode node) {
		if(node.isNative())
			return COST_RXN_NATIVE;
		return COST_RXN_NOT_NATIVE;
	}
}
