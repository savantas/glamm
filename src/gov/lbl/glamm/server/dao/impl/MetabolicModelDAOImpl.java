package gov.lbl.glamm.server.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.lbl.glamm.client.model.FluxExperiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.MetabolicModel;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;

/**
 * Implementation of the Metabolic Model DAO.
 * This first prototype just cobbles together a fake model from a pseudo-random set of
 * reaction IDs that (mostly) aren't in the main map.
 * 
 * A real implementation should make a database call to get Metabolic Model info, 
 * either through GLAMM/MO (unlikely) or KBase APIs (possibly) or however Chris will
 * be doing this with SHOCK (most likely)
 * @author wjriehl
 *
 */
public class MetabolicModelDAOImpl implements MetabolicModelDAO {
	
	private GlammSession sm;
	
	public MetabolicModelDAOImpl(GlammSession sm) {
		this.sm = sm;
	}
	
	@Override
	public MetabolicModel getMetabolicModel(String id) {
		
		// TODO make this actually do something real.
		// right now, it's just kinda dummy stand-in information.
		// eventually, it should fetch a complete model from KBase as a JSON or whatever object, and repackage it into a MetabolicModel.
		
		if (id.equals("1"))
			return dummyModel(id);
		return null;
	}
	
	private MetabolicModel dummyModel(String id) {
		final MetabolicModel model = new MetabolicModel(id);
		Set<String> ids = new HashSet<String>();
		ids.add("R09204");
		ids.add("R09175");
		ids.add("R09220");
		ids.add("R09221");
		ids.add("R09222");
		ids.add("R09223");
		ids.add("R09224");
		ids.add("R09225");
		ids.add("R09226");
		ids.add("R09227");
		ids.add("R09228");
		ids.add("R09229");
		ids.add("R09230");
		ids.add("R09231");
		ids.add("R09232");
		ids.add("R09233");
		ids.add("R01281");
		ids.add("R01954");
		ids.add("R01085");
		
		ReactionDAO reactionDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> reactions = reactionDao.getReactions(ids);
		model.addReactions(reactions);
		return model;		
	}
	
	@Override
	public Set<String> getIdsForOrganism(Organism organism) {
		return null;
	}

	public Set<Reaction> getReactionsForModel(String modelId) {
		return null;
	}
	
	@Override
	public Map<Reaction, Set<Measurement>> getFluxes(FluxExperiment exp) {
		Map<Reaction, Set<Measurement>> resultSet = new HashMap<Reaction, Set<Measurement>>();
		MetabolicModel model = getMetabolicModel(exp.getModelId());
		Set<Reaction> reactions = model.getReactions();
		
		for (Reaction rxn : reactions) {
			Set<Measurement> measurements = new HashSet<Measurement>();
			Measurement m = new Measurement(exp.getExperimentId(), "", (float)(Math.random()*20-10), (float)Math.random(), rxn.getGuid());
			measurements.add(m);
			resultSet.put(rxn, measurements);
		}
		return resultSet;
	}
	
	public MetabolicModel getMetabolicModelFromService(String serviceUrl) {
		
//		if (serviceUrl == null)
//			return null;
//		
//		String uri = serviceUrl + buildParameterString(parameters);
//
//		try {
//			URL url = new URL(uri);
//			
//			ObjectMapper mapper = new ObjectMapper();
//			
//			InputStream stream = url.openStream();
//			KBaseMetabolicModel kbaseModel = mapper.readValue(stream, KBaseMetabolicModel.class);
//			stream.close();
//		}
//		catch (MalformedURLException e) {
//			System.out.println(e.getLocalizedMessage());
//		} catch (IOException e) {
//			System.out.println(e.getLocalizedMessage());
//		}
		
		return new MetabolicModel("");
	}
		
}
