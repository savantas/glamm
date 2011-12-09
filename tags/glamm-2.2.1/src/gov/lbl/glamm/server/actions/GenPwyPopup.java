package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.PathwayDAO;
import gov.lbl.glamm.server.dao.impl.KeggPathwayDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;

import java.util.Set;

/**
 * Service class for generating the HTML content of pathway popups.
 * @author jtbates
 *
 */
public class GenPwyPopup {

	/**
	 * Generates the HTML content of pathway popups.
	 * @param sm The GLAMM session.
	 * @param mapIds The set of map ids for which content is to be generated.
	 * @param taxonomyId The optional organism associated with the map ids.
	 * @param experimentId The optional experiment id.
	 * @param sampleId The optional sample id.
	 * @return The popup's html content.
	 */
	public static String genPwyPopup(final GlammSession sm, 
			final Set<String> mapIds, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) {

		if(mapIds == null || mapIds.isEmpty())
			return "<html>No results found for map.</html>";

		StringBuilder builder = new StringBuilder().append("<html>");

		PathwayDAO pwyDao = new KeggPathwayDAOImpl(sm);

		for(String mapId : mapIds) {
			Pathway pwy = pwyDao.getPathway(mapId);
			if(pwy != null) {
				builder.append(genKeggMapLink(sm, pwy, taxonomyId, experimentId, sampleId));
				builder.append(genImgLink(sm, pwy));
			}
		}

		return builder.append("</html>").toString();
	}

	

	private static String genKeggMapLink(final GlammSession sm, 
			final Pathway pwy, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) { 
		String link = "";
		String mapTitle = pwy.getName();

		if(mapTitle != null && !mapTitle.isEmpty())
			link = "<a href=\"" + 
			genKeggMapUrl(sm, pwy, taxonomyId, experimentId, sampleId) + 
			"\" target = \"_new\">" + 
			mapTitle + 
			"</a>";
		return link;
	}

	

	private static String genKeggMapUrl(final GlammSession sm, 
			final Pathway pwy, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) { 

		String url = "";
		String mapId = pwy.getMapId();

		if(	experimentId != null && !experimentId.equals(Experiment.DEFAULT_EXPERIMENT_ID) &&
				sampleId != null && !sampleId.equals(Sample.DEFAULT_SAMPLE_ID) &&
				taxonomyId != null && !taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID)) {

			url = "http://" + sm.getServerConfig().getIsolateHost() + "/cgi-bin/microarray/reportSet.cgi?disp=3&";
			url += "mapId=" + mapId;
			url += "&taxId=" + taxonomyId;
			url += "&expId=" + experimentId;
			url += "&setId=" + sampleId;
			url += "&z=0.5&n=-1";

		}
		else {

			url = "http://" +  sm.getServerConfig().getIsolateHost() + "/cgi-bin/browseKegg?";
			url += "mapId=map" + mapId;
			url += taxonomyId != null && !taxonomyId.equals(Organism.GLOBAL_MAP_TAXONOMY_ID) ? "&taxId=" + taxonomyId : "";

		}
		return url;
	}

	

	private static String genImgLink(final GlammSession sm, final Pathway pwy) {
		final int MAX_DIM = 250;
		String imgUrlString = "http://" + sm.getServerConfig().getIsolateHost() + "/kegg/map" + pwy.getMapId() + ".png";
		return GlammUtils.genConstrainedImageLink(imgUrlString, MAX_DIM);
	}

	

}
