package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.FluxExperiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicModelDAOImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service class for retrieving fluxes associated with a FluxExperiment.
 * TODO: should probably be rolled into the GetSample / Experiment paradigm. 
 * 		This will likely get solved with the release of Chris' database/document storage thing.
 * @author wjriehl
 *
 */
public class GetFluxes {
	
	public static Set<Reaction> getFluxes(GlammSession sm, FluxExperiment exp) {

		MetabolicModelDAO mmDao = new MetabolicModelDAOImpl(sm);
		Map<Reaction, Set<Measurement>> result = mmDao.getFluxes(exp);
		Set<Reaction> reactions = new HashSet<Reaction>();
		
		for (Reaction rxn : result.keySet()) {
			rxn.getMeasurementSet().setMeasurements(result.get(rxn));
			reactions.add(rxn);
		}
		
		return reactions;
		
	}
}
