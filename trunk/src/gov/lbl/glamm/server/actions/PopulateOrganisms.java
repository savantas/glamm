package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;

import java.util.ArrayList;

public class PopulateOrganisms {
	
	public static ArrayList<Organism> populateOrganisms(SessionManager sm, String dataType) {
	
		OrganismDAO dao = new OrganismDAOImpl(sm);
		ArrayList<Organism> organisms = dao.getAllOrganismsWithDataForType(dataType);

		return organisms;
	}

}
