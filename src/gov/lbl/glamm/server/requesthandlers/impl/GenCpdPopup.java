package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.GlammPrimitive.Xref;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenCpdPopup implements RequestHandler {
	
	public static String genCpdPopup(SessionManager sm, Compound cpd, String taxonomyId) {
		String html			= "<html>No results found for compound.</html>";
		
		if(cpd == null)
			return html;
		
		Xref xref = cpd.getXrefForDbName("LIGAND-CPD"); // only KEGG compounds need apply
		
		if(xref == null)
			return html;
		
		String extId = xref.getXrefId();
		String name = cpd.getName();
		String formula = cpd.getFormula();
		String mass = cpd.getMass();

		if((name != null && !name.isEmpty()) || (formula != null && formula.isEmpty())) {
			html = "<html>";
			if(name != null && !name.isEmpty()) {
				html += genCpdLink(extId, "<b>" + name + "</b>", taxonomyId) + "<br>";
				html += formula != null && !formula.isEmpty() ? formula + "<br>" : "";
			}
			else {
				html += formula != null && !formula.isEmpty() ? genCpdLink(extId, formula, taxonomyId) + "<br>" : "";
			}

			html += mass != null && !mass.isEmpty() && Float.parseFloat(mass) > 0.0f ? "<b>Mass:</b> " + mass : "";

			html += genCpdImgLink(extId);


			html += "</html>";
		}


		return html;
	}

	public static String genCpdPopup(SessionManager sm, String extId, String extIdName, String taxonomyId) {
		// ignore session organisms
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			taxonomyId = null;

		CompoundDAO cpdDao = new CompoundGlammDAOImpl();
		Compound cpd = cpdDao.getCompound(extId, extIdName);

		return genCpdPopup(sm, cpd, taxonomyId);
	}
	
	public static String genCpdPopupFromQueryString(SessionManager sm, String query, String taxonomyId) {
		String cpdId = null;
		String extIdName = null;
		String html = "<html>No results found for query: " + query + ".</html>";
		
		String[] tokens = query.split("&");
		for(String token : tokens) {
			String[] kv = token.split("=");
			if(kv.length != 2)
				continue;
			if(kv[0].equals("extId"))
				cpdId = kv[1];
			else if(kv[0].equals("extIdName"))
				extIdName = kv[1];
		}
		
		if(cpdId == null || extIdName == null)
			return html;
		
		return genCpdPopup(sm, cpdId, extIdName, taxonomyId);
		
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String cpdId 		= request.getParameter(GlammConstants.PARAM_EXTID);
		String extIdName 	= request.getParameter(GlammConstants.PARAM_EXTID_NAME);
		String taxonomyId 	= request.getParameter(GlammConstants.PARAM_TAXONOMY_ID);
		String html 		= genCpdPopup(SessionManager.getSessionManager(request, false), cpdId, extIdName, taxonomyId);

		ResponseHandler.asHtml(response, html, HttpServletResponse.SC_OK);

	}

	//********************************************************************************

	private static String genCpdImgLink(String cpdId) {

		String imgLink = "";
		String molImgLink = "http://microbesonline.org/images/keggCompounds/" + cpdId + ".gif";

		try {
			URL molImgUrl = new URL(molImgLink); 
			BufferedImage img = ImageIO.read(molImgUrl);
			if(img != null) {

				final int MAX_DIM = 250;

				int imgWidth 	= img.getWidth();
				int imgHeight 	= img.getHeight();
				int width 		= imgWidth;
				int height 		= imgHeight;


				if(imgWidth > MAX_DIM || imgHeight > MAX_DIM) {
					if(imgWidth < imgHeight) {
						// set height to MAX_DIM, scale width
						height = MAX_DIM;
						width = (int) (height * (float) imgWidth / (float) imgHeight);
					}
					else {
						// set width to MAX_DIM, scale height
						width = MAX_DIM;
						height = (int) (width * (float) imgHeight / (float) imgWidth);
					}
				}

				imgLink = "<br><img width=" + width + " height=" + height + " src=\"" + molImgLink + "\"/>";
			}
		} catch(IOException e) {
			// do nothing
		}

		return imgLink;
	}

	//********************************************************************************

	private static String genCpdLink(String cpdId, String linkText, String taxonomyId) {
		String cpdLink = "<a href=http://www.microbesonline.org/cgi-bin/fetchCompound.cgi?keggCid=" + cpdId;
		cpdLink += 	taxonomyId != null && 
		!taxonomyId.isEmpty() && 
		!taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID) && 
		!taxonomyId.equals("undefined") ? "&taxId=" + taxonomyId : "";
		cpdLink += " target=\"_new\">" + linkText + "</a>";
		return cpdLink;
	}

}
