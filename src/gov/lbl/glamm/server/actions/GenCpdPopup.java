package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class for generating the HTML content of compound popups.
 * @author jtbates
 *
 */
public class GenCpdPopup {
	
	private static Set<String> keggDbNames = new HashSet<String>();
	static {
		keggDbNames.add("LIGAND-CPD");
		keggDbNames.add("GLYCAN");
	}

	/**
	 * Generates a compound popup's html content for a single compound id.
	 * @param sm The GLAMM session.
	 * @param cpdId The compound id.
	 * @param organism The optional organism.
	 * @return The popup html content.
	 */
	public static String genCpdPopupForId(final GlammSession sm, final String cpdId, final Organism organism) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl(sm);
		Set<String> cpdIds = new HashSet<String>();
		cpdIds.add(cpdId);
		Set<Compound> cpds = cpdDao.getCompounds(cpdIds);

		return genCpdPopup(sm, cpds, organism);
	}

	/**
	 * Generates a compound popup's html content for a set of compound ids.
	 * @param sm The GLAMM session.
	 * @param cpdIds The set of compound ids.
	 * @param organism The optional organism.
	 * @return The popup html content.
	 */
	public static String genCpdPopupForIds(final GlammSession sm, final Set<String> cpdIds, final Organism organism) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl(sm);
		Set<Compound> cpds = cpdDao.getCompounds(cpdIds);
		return genCpdPopup(sm, cpds, organism);
	}
	
	/**
	 * Generates a compound popup's html content for a single compound.
	 * @param sm The GLAMM session.
	 * @param cpd The compound.
	 * @param organism The optional organism.
	 * @return The popup html content.
	 */
	public static String genCpdPopup(final GlammSession sm, final Compound cpd, final Organism organism) {
		Set<Compound> cpds = new HashSet<Compound>();
		cpds.add(cpd);
		return genCpdPopup(sm, cpds, organism);
	}
	
	/**
	 * Generates a compound popup's html content for a set of compounds.
	 * @param sm The GLAMM session.
	 * @param cpds The set of compounds.
	 * @param organism The optional organism.
	 * @return The popup html content.
	 */
	public static String genCpdPopup(final GlammSession sm, final Set<Compound> cpds, final Organism organism) {

		if(cpds == null || cpds.isEmpty())
			return "<html>No results found for compound.</html>";
		
		String taxId = organism != null ? organism.getTaxonomyId() : null;

		StringBuilder builder = new StringBuilder().append("<html>");
		
		for(Compound cpd : cpds) {
			
			Xref keggXref = cpd.getXrefSet().getXrefForDbNames(keggDbNames);
			String keggId = keggXref != null ? keggXref.getXrefId() : null;
			
			String name = cpd.getName();
			String formula = cpd.getFormula();
			String mass = cpd.getMass();
			
			if(name != null && !name.isEmpty())
				builder.append(genCpdLink(sm, keggId, "<b>" + name + "</b>", taxId)).append("<br>");
			
			if(formula != null && !formula.isEmpty())
				builder.append(formula).append("<br>");
			
			if(mass != null && !mass.isEmpty() && Float.parseFloat(mass) > 0.0f)
				builder.append("</b>Mass: </b>").append(mass);
			
			builder.append(genCpdImgLink(sm, keggId)).append("<br>");
		}

		return builder.append("</html>").toString();
	}


	

	private static String genCpdImgLink(final GlammSession sm, final String keggId) {
		if(keggId == null)
			return "";
		final int MAX_DIM = 250;
		String imgUrlString = "http://" + sm.getServerConfig().getIsolateHost() + "/images/keggCompounds/" + keggId + ".gif";
		return GlammUtils.genConstrainedImageLink(imgUrlString, MAX_DIM);
	}

	

	private static String genCpdLink(final GlammSession sm, final String keggId, final String linkText, final String taxonomyId) {
		
		if(keggId == null)
			return linkText;
		
		String cpdLink = "<a href=http://" + sm.getServerConfig().getIsolateHost() + "/cgi-bin/fetchCompound.cgi?keggCid=" + keggId;
		cpdLink += 	
			taxonomyId != null && 
			!taxonomyId.isEmpty() && 
			!taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID) && 
			!sm.isSessionOrganism(taxonomyId) &&
			!taxonomyId.equals("undefined") ? "&taxId=" + taxonomyId : "";
		cpdLink += " target=\"_new\">" + linkText + "</a>";
		return cpdLink;
	}

}
