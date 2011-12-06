package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;

import java.util.List;

/**
 * Service class for getting the list of available organisms.
 * @author jtbates
 *
 */
public class PopulateOrganisms {
	
	/**
	 * Gets the list of available organisms, filtered by data type.  The content of this list depends on the access level of the user.
	 * @param sm The GLAMM session.
	 * @param dataType The data type.
	 * @return The list of available organisms.
	 */
	public static List<Organism> populateOrganisms(GlammSession sm, Sample.DataType dataType) {
	
		OrganismDAO dao = new OrganismDAOImpl(sm);
		List<Organism> organisms = dao.getAllOrganismsWithDataForType(dataType);

		return organisms;
	}

}
