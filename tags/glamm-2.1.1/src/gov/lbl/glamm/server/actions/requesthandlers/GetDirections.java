package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.client.presenter.RetrosynthesisPresenter;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.dao.AnnotatedMapDescriptorDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.AnnotatedMapDescriptorDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;
import gov.lbl.glamm.server.retrosynthesis.Route;
import gov.lbl.glamm.server.retrosynthesis.Route.Step;
import gov.lbl.glamm.server.retrosynthesis.algorithms.RetrosynthesisAlgorithm;
import gov.lbl.glamm.shared.RequestParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: this entire action is very KEGG-centric
public class GetDirections implements RequestHandler {

	private static GeneDAO 				geneDao 	= null;
	private static ReactionDAO 			rxnDao 		= null;
	private static OrganismDAO 			organismDao	= null;

	private static List<Route> getRoutes(GlammSession sm, String taxonomyId, String cpdSrcExtId, String cpdDstExtId, String mapId, String algorithm) {

		if(cpdSrcExtId == null || cpdDstExtId == null || mapId == null || algorithm == null )
			return null;

		AnnotatedMapDescriptorDAO amdDao = new AnnotatedMapDescriptorDAOImpl(sm);
		MetabolicNetwork network = amdDao.getAnnotatedMapDescriptor(mapId).getMetabolicNetwork();

		// in the event of taxon-weighted depth first search, set up the metabolic network
		// to indicate which reactions are native and which aren't
		if(taxonomyId != null && algorithm.equals(RetrosynthesisPresenter.View.ALGORITHM_TW_DFS_VALUE)) {

			geneDao = new GeneDAOImpl(sm);
			rxnDao = new ReactionGlammDAOImpl(sm);
			organismDao = new OrganismDAOImpl(sm);

			Set<String> ecNums = geneDao.getEcNumsForOrganism(taxonomyId);
			Set<String> rxnIds = rxnDao.getRxnIdsForEcNums(ecNums);

			network.setNativeRxns(rxnIds);
		}

		// calculate routes
		RetrosynthesisAlgorithm ra = RetrosynthesisAlgorithm.create(algorithm, taxonomyId, network);
		List<Route> routes = ra.calculateRoutes(cpdSrcExtId, cpdDstExtId);
		if(routes != null)
			sm.addRoutes(routes);
		return routes;
	}

	public static List<Pathway> getDirections(GlammSession sm, String taxonomyId, Compound cpdSrc, Compound cpdDst, String mapTitle, String algorithm) {

		if(cpdSrc == null || cpdDst == null)
			return null;

		Xref cpdSrcXref = cpdSrc.getXrefForDbName("LIGAND-CPD");
		Xref cpdDstXref = cpdDst.getXrefForDbName("LIGAND-CPD");

		if(cpdSrcXref == null || cpdDstXref == null)
			return null;

		List<Route> routes = getRoutes(sm, taxonomyId, cpdSrcXref.getXrefId(), cpdDstXref.getXrefId(), mapTitle, algorithm);
		
		// convert routes to pathways and add them to content
		if(routes != null && !routes.isEmpty())
			return toPathways(sm, routes);
		return null;
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String taxonomyId 	= request.getParameter(RequestParameters.TAXONOMY_ID.toString());
		String cpdSrcExtId	= request.getParameter(RequestParameters.CPD_SRC.toString());
		String cpdDstExtId	= request.getParameter(RequestParameters.CPD_DST.toString());
		String mapTitle		= request.getParameter(RequestParameters.MAPID.toString());
		String algorithm	= request.getParameter(RequestParameters.ALGORITHM.toString());
		String asText		= request.getParameter(RequestParameters.AS_TEXT.toString());
		
		if(cpdSrcExtId == null || cpdDstExtId == null || mapTitle == null || algorithm == null || asText == null) {
			return;
		}
		

		GlammSession sm = GlammSession.getGlammSession(request);
		List<Route> routes = sm.getRoutes(taxonomyId, cpdSrcExtId, cpdDstExtId, algorithm, mapTitle);
		
		if(routes == null) {
			// calculate routes as normal, add them to session
			routes = getRoutes(sm, taxonomyId, cpdSrcExtId, cpdDstExtId, mapTitle, algorithm);
		}
		
		if(Boolean.parseBoolean(asText))
			handleTextResponse(response, sm, routes);


	}

	private void handleTextResponse(HttpServletResponse response, GlammSession sm, List<Route> routes) 
	throws IOException {

		String content = "";

		// get rxnIds for all routes
		Set<String> rxnIds = getRxnIdsForRoutes(routes);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> rxns = rxnDao.getReactions(rxnIds);

		// hash resulting reactions by xrefId
		Map<String, Reaction> rxnId2Rxn = hashReactionsById(rxns);

		if(rxnId2Rxn != null) {
			for(Route route : routes) {
				for(Step step : route.getSteps()) {
					String rxnId = step.getNode().getRxnExtId();
					Reaction rxn = rxnId2Rxn.get(rxnId);
					if(rxn == null) {
						content += "Could not find reaction with id: " + rxnId + "\n";
						continue;
					}
					for(String ecNum : rxn.getEcNums())
						content += ecNum + " ";
					content += "\n" + rxn.getDefinition() + "\n";
				}
				content += "\n";
			}
		}

		ResponseHandler.asPlainTextAttachment(response, content, HttpServletResponse.SC_OK, "routes.txt");
	}

	private static List<Pathway> toPathways(GlammSession sm, List<Route> routes) {
		List<Pathway> pathways = new ArrayList<Pathway>();

		// get rxnIds for all routes
		Set<String> rxnIds = getRxnIdsForRoutes(routes);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> rxns = rxnDao.getReactions(rxnIds);

		// hash resulting reactions by xrefId
		Map<String, Reaction> rxnId2Rxn = hashReactionsById(rxns);

		if(rxnId2Rxn == null)
			return pathways;

		// do the conversion
		for(Route route : routes) {
			Pathway pathway = new Pathway();
			boolean shouldAddPathway = true;

			for(Step step : route.getSteps()) {

				String rxnId = step.getNode().getRxnExtId();
				Reaction rxn = rxnId2Rxn.get(rxnId);

				if(rxn != null) {
					rxn.setNative(step.getNode().isNative());
					pathway.addReaction(rxn);
				}
				else {
					shouldAddPathway = false;
					break;
				}

			}
			if(shouldAddPathway)
				pathways.add(pathway);
		}

		if(organismDao != null) {
			// add transgenic candidates to reactions, if applicable
			addTransgenicCandidates(rxns);
		}

		return pathways;
	}

	private static Set<String> getRxnIdsForRoutes(List<Route> routes) {
		Set<String> rxnIds = new HashSet<String>();

		for(Route route : routes) {
			for(Step step : route.getSteps()) {
				rxnIds.add(step.getNode().getRxnExtId());
			}
		}

		return rxnIds;
	}

	private static Map<String, Reaction> hashReactionsById(Collection<Reaction> rxns) {

		if(rxns == null)
			return null;

		Map<String, Reaction> rxnId2Rxn = new HashMap<String, Reaction>();

		for(Reaction rxn : rxns) {
			Set<Xref> xrefs = rxn.getXrefs();
			for(Xref xref : xrefs) {
				rxnId2Rxn.put(xref.getXrefId(), rxn);
			}
		}

		return rxnId2Rxn;
	}

	private static void addTransgenicCandidates(Set<Reaction> rxns) {

		// get all ec numbers
		Set<String> ecNums = new HashSet<String>();
		for(Reaction rxn : rxns) {
			if(rxn.isNative())
				continue;
			Set<String> ecNumsForRxn = rxn.getEcNums();
			if(ecNumsForRxn == null)
				continue;
			for(String ecNum : ecNumsForRxn)
				ecNums.add(ecNum);
		}

		// get organisms with genes for ecNums
		Map<String, Set<Organism>> ecNum2Organisms = organismDao.getTransgenicCandidatesForEcNums(ecNums);
		if(ecNum2Organisms != null) {
			for(Reaction rxn : rxns) {
				if(rxn.isNative())
					continue;
				Set<String> ecNumsForRxn = rxn.getEcNums();
				if(ecNumsForRxn == null) 
					continue;
				for(String ecNum : ecNumsForRxn) {
					Set<Organism> organisms = ecNum2Organisms.get(ecNum);
					if(organisms == null)
						continue;
					for(Organism organism : organisms) {
						rxn.addTransgenicCandidate(ecNum, organism);
					}
				}
				rxn.sortTransgenicCandidates();
			}
		}
	}
}
