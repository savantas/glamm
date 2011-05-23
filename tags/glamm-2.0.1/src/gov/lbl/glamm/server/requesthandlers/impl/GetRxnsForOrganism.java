package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetRxnsForOrganism implements RequestHandler {
	
	public static ArrayList<Reaction> getRxnsForOrganism(final SessionManager sm, final String taxonomyId, final String dbName) {
		
		GeneDAO geneDao = new GeneDAOImpl(sm);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		ArrayList<Reaction> rxns = null;
		
		ArrayList<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);
	
		if(genes != null && !genes.isEmpty()) {
			HashSet<String> ecNums = new HashSet<String>();
			for(Gene gene : genes) {
				HashSet<String> ecNumsForGene = gene.getEcNums();
				if(ecNumsForGene != null)
					ecNums.addAll(ecNumsForGene);
			}
			rxns = rxnDao.getReactionsForEcNums(ecNums, dbName);
		}
		
		return rxns;
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String taxonomyId	= request.getParameter(GlammConstants.PARAM_TAXONOMY_ID);
		String dbName		= request.getParameter(GlammConstants.PARAM_EXTID_NAME);
		
		ReactionFunData rfd = new ReactionFunData();
		
		GeneDAO geneDao = new GeneDAOImpl(SessionManager.getSessionManager(request, false));
		
		
		ArrayList<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);
	
		if(genes != null && !genes.isEmpty()) {
			HashSet<String> ecNums = new HashSet<String>();
			for(Gene gene : genes) {
				HashSet<String> ecNumsForGene = gene.getEcNums();
				if(ecNumsForGene != null)
					ecNums.addAll(ecNumsForGene);
			}
			
			ReactionDAO rxnDao = new ReactionGlammDAOImpl();
			ArrayList<Reaction> rxns = rxnDao.getReactionsForEcNums(ecNums, dbName);
			
			if(rxns != null) {
				for(Reaction rxn : rxns) {
					rfd.addPrimitive(rxn);
				}
			}
			
		}

		ResponseHandler.asXStreamXml(response, rfd, HttpServletResponse.SC_OK);

	}

}
