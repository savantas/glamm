package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.shared.RequestParameters;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Non-RPC request handler for downloading an organism as a tab-delimited file of the format [Gene]tab[EC number].
 * @author jtbates
 *
 */
public class DownloadOrganism implements RequestHandler {

	private final String DEFAULT_FILE_NAME = "organism.tab";

	private Synonym getSynonymOfPreferredType(final Set<Synonym> synonyms) {
		String types[] = { Gene.SYNONYM_TYPE_VIMSS };
//		String types[] = { Gene.SYNONYM_TYPE_VIMSS, Gene.SYNONYM_TYPE_NAME, Gene.SYNONYM_TYPE_NCBI };
		for(Synonym synonym : synonyms) 
			for(String type : types) 
				if(synonym.getType().equals(type))
					return synonym;
		return null;
	}
	
	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String content		= "";
		String taxonomyId	= request.getParameter(RequestParameters.TAXONOMY_ID.toString());

		GeneDAO geneDao = new GeneDAOImpl(GlammSession.getGlammSession(request));

		Set<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);

		if(genes != null) {
			for(Gene gene : genes) {

				Set<String> ecNums = gene.getEcNums();
				if(ecNums != null) {

					for(String ecNum : ecNums) {
						
						Synonym synonym	= getSynonymOfPreferredType(gene.getSynonyms());

						if(synonym != null)
							content += synonym.getName() + "\t" + ecNum + "\n";
					}
				}
			}
		}

		ResponseHandler.asPlainTextAttachment(response, content, HttpServletResponse.SC_OK, DEFAULT_FILE_NAME);

	}

}
