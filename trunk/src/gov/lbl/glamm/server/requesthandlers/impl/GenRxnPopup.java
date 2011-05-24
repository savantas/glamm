package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;
import gov.lbl.glamm.server.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GenRxnPopup {

	public static String genRxnPopup(SessionManager sm, HashSet<String> rxnIds, String extIdName, String taxonomyId) {

		String html = "<html>No reactions found.</html>";
		HashMap<String, ArrayList<Reaction>>	def2Rxns	= null;
		HashMap<String, ArrayList<Gene>> 		ecNum2Genes	= null;

		if(rxnIds == null || rxnIds.isEmpty() || extIdName == null)
			return html;

		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		ArrayList<Reaction> rxns = rxnDao.getReactions(rxnIds, extIdName);
		HashSet<String> ecNums = new HashSet<String>();

		if(rxns == null) 
			return html;

		// create definition to reaction hash
		def2Rxns = new HashMap<String, ArrayList<Reaction>>();
		for(Reaction rxn : rxns) {
			String definition = rxn.getDefinition();
			ArrayList<Reaction> r = def2Rxns.get(definition);

			if(r == null) {
				r = new ArrayList<Reaction>();
				def2Rxns.put(definition, r);
			}

			r.add(rxn);

			HashSet<String> ecNumsForRxn = rxn.getEcNums();
			if(ecNumsForRxn != null)
				ecNums.addAll(ecNumsForRxn);
		}

		ArrayList<Gene> genes = null;

		if(taxonomyId != null && 
				!taxonomyId.isEmpty() && 
				!taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID)) {

			GeneDAO geneDao = new GeneDAOImpl(sm);
			//genes = geneDao.getGenesForRxnIds(taxonomyId, rxnIds);
			genes = geneDao.getGenesForEcNums(taxonomyId, ecNums);

			if(genes != null) {

				// create ecNum to gene hash
				ecNum2Genes = new HashMap<String, ArrayList<Gene>>();
				for(Gene gene : genes) {
					HashSet<String> ecNumsForGene = gene.getEcNums();
					if(ecNumsForGene != null) {
						for(String ecNum : ecNumsForGene) {
							ArrayList<Gene> g = ecNum2Genes.get(ecNum);

							if(g == null) {
								g = new ArrayList<Gene>();
								ecNum2Genes.put(ecNum, g);
							}
							g.add(gene);
						}
					}
				}
			}
		}

		return genHtml(sm, taxonomyId, def2Rxns, ecNum2Genes);


	}

	public static String genRxnPopupFromQueryString(SessionManager sm, String query, String taxonomyId) {
		
		HashSet<String> rxnIds = null;
		String extIdName = null;
		
		for(String token : query.split("&")) {
			String[] kv = token.split("=");
			if(kv.length != 2)
				continue;
			if(kv[0].equals("extId")) {
				if(rxnIds == null)
					rxnIds = new HashSet<String>();
				rxnIds.add(kv[1]);
			}
			else if(kv[0].equals("extIdName"))
				extIdName = kv[1];
		}
		
		return genRxnPopup(sm, rxnIds, extIdName, taxonomyId);
	}

	//********************************************************************************

	private static String genHtml(SessionManager sm, String taxonomyId, 
			HashMap<String, ArrayList<Reaction>> def2Rxns, 
			HashMap<String, ArrayList<Gene>> ecNum2Genes) {

		String html = "<html>";

		for(String definition : def2Rxns.keySet()) {
			ArrayList<Reaction> rxns = def2Rxns.get(definition);
			html += definition + "<br>";

			for(Reaction rxn : rxns) {
				HashSet<String> ecNums = rxn.getEcNums();
				if(ecNums != null) {
					for(String ecNum : ecNums) {
						int numLoci = 0;

						if(ecNum2Genes != null) {
							ArrayList<Gene> genes = ecNum2Genes.get(ecNum);
							if(genes != null)
								numLoci = genes.size();
						}

						html += genEcNumLink(sm, ecNum, taxonomyId, numLoci);
						html += ": " + numLoci + (numLoci == 1 ? " gene" : " genes") + "<br>";
					}
				}
				else
					html += "<b>No EC</b><br>";

			}

		}

		return html + "</html>";
	}

	//********************************************************************************

	private static String genEcNumLink(SessionManager sm, String ecNum, String taxonomyId, int numLoci) {

		String link = "<b>No EC</b>";

		// annotate ecNum
		if(ecNum != null && !ecNum.equals("NULL")) {
			if(numLoci > 0  && (sm == null || !sm.isSessionOrganism(taxonomyId))) {
				link = "<a href=\"http://microbesonline.org/cgi-bin/fetchEC2.cgi?ec=" + ecNum;
				link += taxonomyId != null ? "&taxId=" + taxonomyId + "\" target=\"_new\">": "\" target=\"_new\">";
				link += "<b>" + ecNum + "</b></a>";
			}
			else {
				link = "<b>" + ecNum + "</b>";
			}
		}

		return link;
	}

	//********************************************************************************

}
