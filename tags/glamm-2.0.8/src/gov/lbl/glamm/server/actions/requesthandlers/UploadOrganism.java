package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.presenter.OrganismUploadPresenter;
import gov.lbl.glamm.server.FormRequestHandler;
import gov.lbl.glamm.server.FormRequestHandler.LineParser;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.impl.GeneMetaMolDAOImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadOrganism implements RequestHandler {

	private static final String FILE_PARSE_WARNING_MALFORMED_LINE 		= "Malformed line";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// get the session manager, create a new one if necessary
		GlammSession sm = GlammSession.getGlammSession(request);

		Organism organism = null;
		final Map<String, Gene> id2Gene = new HashMap<String, Gene>();

		// set up the file upload handler- simultaneously building the list of genes
		FormRequestHandler fuh = new FormRequestHandler(request, new LineParser() {
			public String parseLine(String line) {

				final int MIN_TOKENS	= 2;
				final int ID_INDEX 	= 0;
				final int ECNUM_INDEX	= 1;

				String errorMsg = null;
				String[] tokens = line.split("\t");

				// ensure that there are at least two tokens per line
				if(tokens.length < MIN_TOKENS) {
					errorMsg = FILE_PARSE_WARNING_MALFORMED_LINE + ": " + line;
				}
				else {
					String id 		= tokens[ID_INDEX];
					String ecNum	= tokens[ECNUM_INDEX];

					Gene gene = id2Gene.get(id);
					if(gene == null) {
						gene = new Gene();
						id2Gene.put(id, gene);
					}

					gene.addSynonym(id, Gene.SYNONYM_TYPE_SESSION);
					gene.addEcNum(ecNum);

				}

				return errorMsg;
			}
		});

		// construct the organism
		String organismName = fuh.getFormField(OrganismUploadPresenter.View.FIELD_ORGANISM_UPLOAD_NAME);
		String taxonomyId	= sm.nextAvailableTaxonomyId();

		if(organismName != null && taxonomyId != null) {
			organism = new Organism(taxonomyId, organismName, true);
		}

		// if the ids specified are VIMSS ids, return the set of taxonomyIds to which they belong and add them to the organism
		GeneMetaMolDAOImpl metaMolDao = new GeneMetaMolDAOImpl(sm);
		Set<String> molTaxonomyIds = metaMolDao.getTaxonomyIdsForVimssIds(id2Gene.keySet());
		if(molTaxonomyIds != null)
			organism.addMolTaxonomyIds(molTaxonomyIds);
		
		// add organism to session
		sm.addOrganism(organism);
		sm.addGenesForOrganism(organism, new ArrayList<Gene>(id2Gene.values()));
		
		// handle response
		ResponseHandler.asHtml(response, fuh.getErrorMessages(), HttpServletResponse.SC_OK);

	}

}
