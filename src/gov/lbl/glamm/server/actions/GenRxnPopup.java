package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenRxnPopup {

	public static String genRxnPopup(GlammSession sm, Set<String> rxnIds, Set<String> dbNames, String taxonomyId) {

		String html = "<html>No reactions found.</html>";
		Map<String, List<Reaction>>	def2Rxns	= null;
		Map<String, List<Gene>> 		ecNum2Genes	= null;

		if(rxnIds == null || rxnIds.isEmpty() || dbNames == null)
			return html;

		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		List<Reaction> rxns = rxnDao.getReactions(rxnIds, dbNames);
		Set<String> ecNums = new HashSet<String>();

		if(rxns == null) 
			return html;

		// create definition to reaction hash
		def2Rxns = new HashMap<String, List<Reaction>>();
		for(Reaction rxn : rxns) {
			String definition = rxn.getDefinition();
			List<Reaction> r = def2Rxns.get(definition);

			if(r == null) {
				r = new ArrayList<Reaction>();
				def2Rxns.put(definition, r);
			}

			r.add(rxn);

			Set<String> ecNumsForRxn = rxn.getEcNums();
			if(ecNumsForRxn != null)
				ecNums.addAll(ecNumsForRxn);
		}

		List<Gene> genes = null;

		if(taxonomyId != null && 
				!taxonomyId.isEmpty() && 
				!taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID)) {

			GeneDAO geneDao = new GeneDAOImpl(sm);
			genes = geneDao.getGenesForEcNums(taxonomyId, ecNums);

			if(genes != null) {

				// create ecNum to gene hash
				ecNum2Genes = new HashMap<String, List<Gene>>();
				for(Gene gene : genes) {
					Set<String> ecNumsForGene = gene.getEcNums();
					if(ecNumsForGene != null) {
						for(String ecNum : ecNumsForGene) {
							List<Gene> g = ecNum2Genes.get(ecNum);

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

	public static String genRxnPopupFromQueryString(GlammSession sm, String query, String taxonomyId) {

		HashSet<String> rxnIds = null;
		HashSet<String> dbNames = null;

		for(String token : query.split("&")) {
			String[] kv = token.split("=");
			if(kv.length != 2)
				continue;
			if(kv[0].equals("extId")) {
				if(rxnIds == null)
					rxnIds = new HashSet<String>();
				rxnIds.add(kv[1]);
			}
			else if(kv[0].equals("extIdName")) {
				if(dbNames == null)
					dbNames = new HashSet<String>();
				dbNames.add(kv[1]);
			}
		}


		return genRxnPopup(sm, rxnIds, dbNames, taxonomyId);
	}

	//********************************************************************************

	private static String genHtml(GlammSession sm, String taxonomyId, 
			Map<String, List<Reaction>> def2Rxns, 
			Map<String, List<Gene>> ecNum2Genes) {

		String html = "<html>";

		for(String definition : def2Rxns.keySet()) {
			List<Reaction> rxns = def2Rxns.get(definition);
			html += definition + "<br>";

			for(Reaction rxn : rxns) {
				Set<String> ecNums = rxn.getEcNums();
				if(ecNums != null) {
					for(String ecNum : ecNums) {
						int numLoci = 0;
						List<Gene> genes = null;

						if(ecNum2Genes != null) {
							genes = ecNum2Genes.get(ecNum);
							if(genes != null)
								numLoci = genes.size();
						}

						html += genEcNumLink(sm, ecNum, taxonomyId, genes);
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

	private static String genEcNumLink(GlammSession sm, String ecNum, String taxonomyId, List<Gene> genes) {

		String link = "<b>No EC</b>";

		// annotate ecNum
		if(ecNum != null && !ecNum.equals("NULL")) {
			if(genes != null && !genes.isEmpty()) {
				if(sm != null && sm.isSessionOrganism(taxonomyId))
					link = genEcNumLinkForSessionOrganism(ecNum, sm.getOrganismForTaxonomyId(taxonomyId));
				else
					link = genEcNumLinkForMolOrganism(ecNum, taxonomyId, genes);
			}
			else {
				link = "<b>" + ecNum + "</b>"; 
			}
		}

		return link;
	}

	//********************************************************************************
	
	private static String genEcNumLinkForMolOrganism(String ecNum, String taxonomyId, List<Gene> genes) {
		String link = "<b>" + ecNum + "</b>";
		if(genes != null && !genes.isEmpty()) {
			link = "<a href=\"http://";
			link += Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID ? "meta." : "";
			link += "microbesonline.org/cgi-bin/fetchEC2.cgi?ec=" + ecNum;
			link += taxonomyId != null ? "&taxId=" + taxonomyId : "";
			link += "\" target=\"_new\">";
			link += "<b>" + ecNum + "</b></a>";
		}
		return link;
	}
	
	private static String genEcNumLinkForSessionOrganism(String ecNum, Organism organism) {
		String link = "<b>" + ecNum + "</b>";
		
			// get set of all molTaxonomyIds
			Set<String> metaMolTaxonomyIds = organism.getMolTaxonomyIds();
			
			if(metaMolTaxonomyIds == null || metaMolTaxonomyIds.isEmpty())
				return link;
			
			
			link = "<a href=\"http://meta.microbesonline.org/cgi-bin/fetchEC2.cgi?ec=" + ecNum;
			for(String taxonomyId : metaMolTaxonomyIds) 
				link += taxonomyId != null ? "&taxId=" + taxonomyId : "";
			link += "\" target=\"_new\">";
			link += "<b>" + ecNum + "</b></a>";
		
		return link;
	}
}
