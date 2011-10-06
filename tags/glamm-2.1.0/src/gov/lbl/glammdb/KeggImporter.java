package gov.lbl.glammdb;

import org.apache.log4j.Logger;

import gov.lbl.glammdb.kegg.kgml.KgmlDocument;
import gov.lbl.glammdb.kegg.pathway.KeggPathwayHandler;

public class KeggImporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger logger = Logger.getRootLogger();
		String kgmlPath =	"src/resources/kegg/kgml/";
		String svgPath = 	"src/resources/output/svg/";
		String mapInfos[][] = {
				{"map01100",	"Metabolic pathways"},
				{"map01110",	"Biosynthesis of secondary metabolites"},
				{"map01120",	"Microbial metabolism in diverse environments"}
		};
		
		for(String[] mapInfo : mapInfos) {
			String mapId = mapInfo[0];
			String mapTitle = mapInfo[1];
			String kgmlPathAbs = kgmlPath + mapId + ".xml";
			String svgPathAbs = svgPath + mapId + ".svg";
			
			KgmlDocument kgmlDocument = KgmlDocument.create(mapId, mapTitle, kgmlPathAbs, svgPathAbs);
			logger.info("Processing " + mapId + "...");
			kgmlDocument.process();
			logger.info("Done");
			
			logger.info("Committing " + mapId + " to database...");
			kgmlDocument.store();
			logger.info("Done");
		}
		
		String path =	"src/resources/kegg/pathway/";
		String mapListPath = path + "map.list";
		String mapTitlePath = path + "map_title.tab";

		KeggPathwayHandler kmh = KeggPathwayHandler.create(mapTitlePath, mapListPath);
		
		logger.info("Processing map files...");
		kmh.process();
		logger.info("Done");
		
		logger.info("Committing maps to database...");
		kmh.store();
		logger.info("Done");

	}

}