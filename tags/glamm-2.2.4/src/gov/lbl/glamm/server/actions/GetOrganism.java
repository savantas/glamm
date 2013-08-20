package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;
import gov.lbl.glamm.shared.model.Organism;

public class GetOrganism {

	public static Organism getOrganismForTaxId(GlammSession sm, String taxId) {
		OrganismDAO dao = new OrganismDAOImpl(sm);
		return dao.getOrganismForTaxonomyId(taxId);
	}
}
