package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.GlammPrimitive.Xref;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;

import java.util.HashSet;
import java.util.Set;

public class GenCpdPopup {

	public static String genCpdPopup(GlammSession sm, Compound cpd, String taxonomyId) {
		String html			= "<html>No results found for compound.</html>";

		if(cpd == null)
			return html;

		Set<String> dbNames = new HashSet<String>();
		dbNames.add("LIGAND-CPD");
		dbNames.add("GLYCAN");
		Xref xref = cpd.getXrefForDbNames(dbNames); // only KEGG compounds need apply

		if(xref == null)
			return html;

		String extId = xref.getXrefId();
		String name = cpd.getName();
		String formula = cpd.getFormula();
		String mass = cpd.getMass();

		if((name != null && !name.isEmpty()) || (formula != null && formula.isEmpty())) {
			html = "<html>";
			if(name != null && !name.isEmpty()) {
				html += genCpdLink(sm, extId, "<b>" + name + "</b>", taxonomyId) + "<br>";
				html += formula != null && !formula.isEmpty() ? formula + "<br>" : "";
			}
			else {
				html += formula != null && !formula.isEmpty() ? genCpdLink(sm, extId, formula, taxonomyId) + "<br>" : "";
			}

			html += mass != null && !mass.isEmpty() && Float.parseFloat(mass) > 0.0f ? "<b>Mass:</b> " + mass : "";

			html += genCpdImgLink(sm, extId);


			html += "</html>";
		}


		return html;
	}

	public static String genCpdPopup(GlammSession sm, String extId, String extIdName, String taxonomyId) {
		// ignore session organisms
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			taxonomyId = null;

		CompoundDAO cpdDao = new CompoundGlammDAOImpl(sm);
		Compound cpd = cpdDao.getCompound(extId, extIdName);

		return genCpdPopup(sm, cpd, taxonomyId);
	}

	public static String genCpdPopupFromQueryString(GlammSession sm, String query, String taxonomyId) {
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

	//********************************************************************************

	private static String genCpdImgLink(final GlammSession sm, final String cpdId) {
		final int MAX_DIM = 250;
		String imgUrlString = "http://" + sm.getServerConfig().getIsolateHost() + "/images/keggCompounds/" + cpdId + ".gif";
		return GlammUtils.genConstrainedImageLink(imgUrlString, MAX_DIM);
	}

	//********************************************************************************

	private static String genCpdLink(final GlammSession sm, final String cpdId, final String linkText, final String taxonomyId) {
		String cpdLink = "<a href=http://" + sm.getServerConfig().getIsolateHost() + "/cgi-bin/fetchCompound.cgi?keggCid=" + cpdId;
		cpdLink += 	taxonomyId != null && 
		!taxonomyId.isEmpty() && 
		!taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID) && 
		!taxonomyId.equals("undefined") ? "&taxId=" + taxonomyId : "";
		cpdLink += " target=\"_new\">" + linkText + "</a>";
		return cpdLink;
	}

}
