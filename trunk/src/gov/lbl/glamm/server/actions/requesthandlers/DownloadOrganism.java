package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.GlammPrimitive.Synonym;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.shared.RequestParameters;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadOrganism implements RequestHandler {

	private final String DEFAULT_FILE_NAME = "organism.tab";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String content		= "";
		String taxonomyId	= request.getParameter(RequestParameters.TAXONOMY_ID.toString());

		GeneDAO geneDao = new GeneDAOImpl(GlammSession.getGlammSession(request));

		List<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);

		if(genes != null) {
			for(Gene gene : genes) {

				Set<String> ecNums = gene.getEcNums();
				if(ecNums != null) {

					for(String ecNum : ecNums) {
						String types[] = { Gene.SYNONYM_TYPE_VIMSS, Gene.SYNONYM_TYPE_NAME, Gene.SYNONYM_TYPE_NCBI };
						Synonym synonym	= gene.getSynonymOfPreferredType(types);

						if(synonym != null && ecNum != null)
							content += synonym.getName() + "\t" + ecNum + "\n";
					}
				}
			}
		}

		ResponseHandler.asPlainTextAttachment(response, content, HttpServletResponse.SC_OK, DEFAULT_FILE_NAME);

	}

}
