package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.kbase.MetabolismService;
import gov.lbl.glamm.shared.model.FluxExperiment;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Media;
import gov.lbl.glamm.shared.model.MetabolicModel;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBA;
import gov.lbl.glamm.shared.model.metabolism.deprecated.FbaExperiment;
import gov.lbl.glamm.shared.model.metabolism.deprecated.KBaseMetabolicModel;
import gov.lbl.glamm.shared.model.metabolism.deprecated.ModelVisualization;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	private static final String SERVICE_URL = "http://www.kbase.us/services/fba";
	private static final String SERVICE_VERSION = "1.1";
	private GlammSession sm;
	private MetabolismService metService;
	
	public MetabolicModelDAOImpl(final GlammSession sm) {
		this.sm = sm;
		
		metService = new MetabolismService(SERVICE_URL, SERVICE_VERSION);
	}
	
	@Override
	public MetabolicModel getMetabolicModel(final String id) {
		
		// TODO make this actually do something real.
		// right now, it's just kinda dummy stand-in information.
		// eventually, it should fetch a complete model from KBase as a JSON or whatever object, and repackage it into a MetabolicModel.
		
		if (id.equals("1"))
			return dummyModel(id);
		else {
//			KB
		}
		return null;
	}
	
	private MetabolicModel dummyModel(final String id) {
		final MetabolicModel model = new MetabolicModel(id, "Test Model");
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
	public Set<String> getIdsForOrganism(final Organism organism) {
		return null;
	}

	public Set<Reaction> getReactionsForModel(final String modelId) {
		return null;
	}
	
	@Override
	public Map<Reaction, Set<Measurement>> getReactionFluxes(final FluxExperiment exp) {
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

	@Override
	public Set<Reaction> getReactionFluxes(final KBFBA fba) {
		return new HashSet<Reaction>();
	}
	
	//TODO
	public List<MetabolicModel> getAllMetabolicModels() {
		// make service call. process results. return 'em.
		return new ArrayList<MetabolicModel>();
	}
	
	//TODO
	public List<String> getFbaResultsForModel(final String modelId) {
		return new ArrayList<String>();
	}
	
	//TODO
	public KBFBA getFbaResults(final String fbaId) {
		return new KBFBA();
	}
	
	@Override
	public MetabolicModel getMetabolicModelFromService(String source, String id) {
		
		if (source == null || id == null)
			return null;
		
		if (source.equalsIgnoreCase("kbase")) {
			return getKBaseMetabolicModel(id);
		}
		
		String uri = source;

		try {
			URL url = new URL(uri);
			
			ObjectMapper mapper = new ObjectMapper();
			
			InputStream stream = url.openStream();
			KBaseMetabolicModel kbaseModel = mapper.readValue(stream, KBaseMetabolicModel.class);
			stream.close();
			
			kbaseModel.process();
			return kbase2GlammModel(kbaseModel);

		} catch (MalformedURLException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return null;
	}

	@Override
	public FbaExperiment getFbaExperimentFromService(String source) {

		if (source == null)
			return null;
		
		String uri = source;
		
		try {
			URL url = new URL(uri);
			
			ObjectMapper mapper = new ObjectMapper();
			InputStream stream = url.openStream();
			FbaExperiment exp = mapper.readValue(stream, FbaExperiment.class);
			stream.close();
			
			exp.process();			
			return exp;
			
		} catch (MalformedURLException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return null;
	}
	
	//TODO
	@Override
	public Media getMedia(String mediaId, String biochemistryId) {
		return new Media(mediaId, "dummy media set");
	}
	
	@Override
	public ModelVisualization getModelVisualizationFromService(String source) {
		
		if (source == null)
			return null;
		
		String uri = source;
		
		try {
			URL url = new URL(uri);
			
			ObjectMapper mapper = new ObjectMapper();
			InputStream stream = url.openStream();
			ModelVisualization modelViz = mapper.readValue(stream, ModelVisualization.class);
			stream.close();
			
			return modelViz;
			
		} catch (MalformedURLException e) {
			System.err.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		return null;
	}		
	
	@Override
	public Set<Reaction> getReactionFluxesFromKBase(String fbaId) {
		return new HashSet<Reaction>();
	}
	
	private MetabolicModel kbase2GlammModel(KBaseMetabolicModel kbModel) {
		MetabolicModel model = new MetabolicModel(kbModel.getID(), kbModel.getName());
		
		//TODO need typespec to do the rest...
		return model;
	}

	private MetabolicModel getKBaseMetabolicModel(String id) {
		String uri = "kbase yadda yadda";  // make and invoke a KBase service, maybe? Not sure how this'll get done.
		
		try {
			URL url = new URL(uri);
			ObjectMapper mapper = new ObjectMapper();
			InputStream stream = url.openStream();
			
			KBaseMetabolicModel model = mapper.readValue(stream, KBaseMetabolicModel.class);
			stream.close();
			
			return kbase2GlammModel(model);
		} catch (MalformedURLException e) {
			System.err.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		
		return null;
	}
}