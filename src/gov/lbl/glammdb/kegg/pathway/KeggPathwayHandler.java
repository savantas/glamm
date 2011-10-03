package gov.lbl.glammdb.kegg.pathway;

import gov.lbl.glammdb.domain.Pathway;
import gov.lbl.glammdb.domain.PwyElement;
import gov.lbl.glammdb.util.HibernateUtil;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.hibernate.Session;

public class KeggPathwayHandler {
	private Map<String, Pathway> id2Maps;
	private File mapTitleFile;
	private File mapListFile;

	private KeggPathwayHandler(final File mapTitleFile, final File mapListFile) {
		this.mapTitleFile = mapTitleFile;
		this.mapListFile = mapListFile;
		id2Maps = new LinkedHashMap<String, Pathway>();
	}

	public static KeggPathwayHandler create(final String mapTitlePath, final String mapListPath) {
		try {
			File mapTitleFile = new File(mapTitlePath);
			File mapListFile = new File(mapListPath);
			return new KeggPathwayHandler(mapTitleFile, mapListFile);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void process() {
		processMapTitleFile();
		processMapListFile();
	}

	private void processMapTitleFile() {
		try {
			for(Scanner scanner = new Scanner(mapTitleFile);
			scanner.hasNextLine();) {
				String line = scanner.nextLine();
				String[] tokens = line.split("\\t");

				String xrefId = tokens[0];
				String title = tokens[1];
				
				// map ids are unique in this file
				Pathway map = new Pathway();
				
				
				map.setXrefId(xrefId);
				map.setTitle(title);
				
				id2Maps.put(xrefId, map);
			}	
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void processMapListFile() {
		try {
			for(Scanner scanner = new Scanner(mapListFile);
			scanner.hasNextLine();) {
				String line = scanner.nextLine();
				String[] tokens = line.split("\\t");
				
				if(tokens.length != 2)
					continue;
				
				String mapXrefId = tokens[0].split("\\:")[1];
				Pathway pm = id2Maps.get(mapXrefId);
				
				if(pm == null)
					continue;
				
				String[] elementTokens = tokens[1].split("\\:");
				String elementXrefId = elementTokens[1];
				
				try {
					PwyElement.Type elementType = PwyElement.Type.fromValue(elementTokens[0]);
					PwyElement element = new PwyElement();
					element.setType(elementType);
					element.setXrefId(elementXrefId);
					
					pm.addElement(element);
				}
				catch(IllegalArgumentException e) {
					continue;
				}		
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Long storeMap(final Pathway pm) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Long pmId = (Long) session.save(pm);

		session.getTransaction().commit();
		return pmId;
	}
	
	public void store() {
		for(Pathway pm : this.id2Maps.values()) {
			storeMap(pm);
		}
	}

}
