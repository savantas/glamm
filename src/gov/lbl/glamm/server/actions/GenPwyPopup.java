package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.PathwayDAO;
import gov.lbl.glamm.server.dao.impl.KeggPathwayDAOImpl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GenPwyPopup {

	public static String genPwyPopup(final GlammSession sm, 
			final String mapId, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) {
		String html			= "<html>No results found for " + mapId + ".</html>";

		PathwayDAO pwyDao = new KeggPathwayDAOImpl(sm);
		Pathway pwy = pwyDao.getPathway(mapId);

		if(pwy != null) {
			html = "<html>";
			html += genKeggMapLink(sm, pwy, taxonomyId, experimentId, sampleId);
			html += genImgLink(sm, pwy);
			html += "</html>";
		}

		return html;
	}
	
	public static String genPwyPopupFromQueryString(final GlammSession sm, 
			final String query, 
			final String taxonomyId, 
			final String experimentId, 
			final String sampleId) {
		String mapId = null;
		
		for(String token : query.split("&")) {
			String kv[] = token.split("=");
			if(kv.length != 2)
				continue;
			if(kv[0].equals("keggMapId"))
				mapId = kv[1];
		}
		
		return genPwyPopup(sm, mapId, taxonomyId, experimentId, sampleId);
	}

	//********************************************************************************

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

	//********************************************************************************

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

	//********************************************************************************

	private static String genImgLink(final GlammSession sm, final Pathway pwy) {
		String imgLink = "";
		String molImgLink = "http://" + sm.getServerConfig().getIsolateHost() + "/kegg/map" + pwy.getMapId() + ".png";

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
			e.printStackTrace();
		}

		return imgLink;
	}

	//********************************************************************************

}
