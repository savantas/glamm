package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Citation;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.dao.CitationsDAO;
import gov.lbl.glamm.server.dao.impl.CitationsGlammDAOImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenCitationsPopup implements RequestHandler {
	
	//********************************************************************************

	private final String PREAMBLE = "GLAMM: The Genome-Linked Application for Metabolic Maps";

	@SuppressWarnings("unused")
	private final String CITATION = "";

	private final String ACKNOWLEDGEMENTS = "This work was part of the DOE <a href=\"http://www.jbei.org\">Joint BioEnergy Institute</a>, " +
	"the ENIGMA Scientific Focus Area Program, and Genomics:GTL Foundational " +
	"Science supported by the U. S. Department of Energy, Office of Science, Office of Biological " +
	"and Environmental Research, through contract DE-AC02-05CH11231 between Lawrence Berkeley National " +
	"Laboratory and the U. S. Department of Energy.";

	//********************************************************************************
	
	private HashMap<String, ArrayList<Integer>> description2CitationsTextIndices = new HashMap<String, ArrayList<Integer>>();
	private ArrayList<String> descriptions = new ArrayList<String>();
	private ArrayList<String> citationsText = new ArrayList<String>();
	
	//********************************************************************************


	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		
		CitationsDAO citationsDao = new CitationsGlammDAOImpl();

		String html = "<html>";
		
		html += "<div style=\"font-family: Arial Unicode MS, Arial, sans-serif;font-size: 11px;\">";
		html += PREAMBLE;
		html += "<br><br>";
		html += ACKNOWLEDGEMENTS;
		
		if(citationsDao.getCitationsTableName() != null) {
			
			ArrayList<Citation> citations = citationsDao.getCitations();
			
			processCitations(citations);
			
			html += "<br><br>";
			html += "This web service uses data from the following sources:";
			html += "<br><br>";
			html += descriptionsToString();
			html += "<br><br>";
			html += citationsToString();
		}
		
		html += "</div>";
		html += "</html>";
		
		ResponseHandler.asHtml(response, html, HttpServletResponse.SC_OK);
	}
	
	//********************************************************************************
	
	private void processCitations(ArrayList<Citation> citations) {
		if(citations != null) {
			for(Citation citation : citations) {
				String text = citation.getText();
				String description = citation.getDescription() + " (" + citation.getDbVersion() + ")";

				if(!descriptions.contains(description))
					descriptions.add(description);

				if(!citationsText.contains(text))
					citationsText.add(text);

				Integer citationsTextIndex = Integer.valueOf(citationsText.indexOf(text));

				ArrayList<Integer> citationsTextIndices = description2CitationsTextIndices.get(description);
				if(citationsTextIndices == null) {
					citationsTextIndices = new ArrayList<Integer>();
					description2CitationsTextIndices.put(description, citationsTextIndices);
				}

				if(!citationsTextIndices.contains(citationsTextIndex)) 
					citationsTextIndices.add(citationsTextIndex);
			}
		}
	}
	
	//********************************************************************************

	private String descriptionToString(final String description, final ArrayList<Integer> citationIndices) {
		String result = "";

		result += description;

		if(citationIndices != null) {
			for(Integer citationIndex : citationIndices) {
				result += " <sup>" + (citationIndex.intValue() + 1) + "</sup>";
			}
		}

		return result;
	}

	//********************************************************************************

	private String descriptionsToString() {
		String result = "";
		if(descriptions != null) {
			for(String description : descriptions) {
				ArrayList<Integer> citationIndices = description2CitationsTextIndices.get(description);
				result += descriptionToString(description, citationIndices) + "<br>";
			}
		}
		return result;
	}

	//********************************************************************************

	private String citationsToString() {
		String result = "";

		if(citationsText != null) {
			for(int i = 0; i < citationsText.size(); i++) {
				result += "<sup>" + (i + 1) + " </sup>" + citationsText.get(i) + "<br>";
			}
		}

		return result;
	}

	//********************************************************************************


}
