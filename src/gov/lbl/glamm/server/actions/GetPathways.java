package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.PathwayDAO;
import gov.lbl.glamm.server.dao.impl.KeggPathwayDAOImpl;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Sample;

import java.util.HashSet;
import java.util.Set;


/**
 * Service class for fetching a set of Pathways.
 * 
 * @author wjriehl Bill Riehl - wjriehl@lbl.gov
 *
 */
public class GetPathways {

	public static Set<Pathway> getPathways(final GlammSession sm,
										   final Set<String> ids) {
		if (ids == null || ids.isEmpty())
			return new HashSet<Pathway>();
		
		return getPathways(sm, ids, null, null);
	}
	
	public static Set<Pathway> getPathways(final GlammSession sm,
										   final Set<String> ids,
										   final Organism organism,
										   final Sample sample) {
		if (ids == null || ids.isEmpty())
			return new HashSet<Pathway>();
		
		PathwayDAO pwyDao = new KeggPathwayDAOImpl(sm);
		Set<Pathway> pwySet = new HashSet<Pathway>();

		for(String mapId : ids) {
			Pathway pwy = pwyDao.getPathway(mapId, organism);
			if(pwy != null)
				pwySet.add(pwy);
		}
		return pwySet;
	}
}
