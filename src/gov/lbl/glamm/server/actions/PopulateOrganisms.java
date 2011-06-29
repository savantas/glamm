package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;

import java.util.List;

public class PopulateOrganisms {
	
	public static List<Organism> populateOrganisms(SessionManager sm, Sample.DataType dataType) {
	
		OrganismDAO dao = new OrganismDAOImpl(sm);
		List<Organism> organisms = dao.getAllOrganismsWithDataForType(dataType);

		return organisms;
	}

}
