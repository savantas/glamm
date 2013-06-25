package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasSynonyms;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GetSample  {
	
	public static Set<? extends HasMeasurements> getMeasurementsForSample(final GlammSession sm, final Sample sample) {
		
		if(sample == null)
			return new HashSet<HasMeasurements>();
		
		// set up DAOs
		ExperimentDAO expDao = new ExperimentDAOImpl(sm);
		GeneDAO geneDao = new GeneDAOImpl(sm);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		
		// get taxonomyId and id2Measurements mapping for experiment
		String taxonomyId = expDao.getTaxonomyIdForExperimentId(sample.getExperimentId()); 
		Map<String, Set<Measurement>> id2Measurements = expDao.getMeasurements(sample.getExperimentId(), sample.getSampleId());
		
		// bail early if empty
		if(id2Measurements == null)
			return new HashSet<HasMeasurements>();
		
		// get the genes corresponding to these measurements
		Set<Gene> genes = null;
		if(sm.isSessionExperiment(sample.getExperimentId()))
			genes = geneDao.getGenesForSynonyms(taxonomyId, id2Measurements.keySet());
		else
			genes = geneDao.getGenesForVimssIds(taxonomyId, id2Measurements.keySet());
		
		// add measurements to genes
		for(Gene gene : genes) {
			Set<Synonym> synonyms = ((HasSynonyms) gene).getSynonyms();
			for(Synonym synonym : synonyms) {
				Set<Measurement> measurements = id2Measurements.get(synonym.getName());
				if(measurements != null)
					for(Measurement measurement : measurements)
						gene.addMeasurement(measurement);
			}
		}
		
		// assemble map of ecNums to Genes
		Map<String, Set<Gene>> ecNum2Genes = new HashMap<String, Set<Gene>>();
		for(Gene gene : genes) {
			for(String ecNum : gene.getEcNums()) {
				Set<Gene> genes4EcNum = ecNum2Genes.get(ecNum);
				if(genes4EcNum == null) {
					genes4EcNum = new HashSet<Gene>();
					ecNum2Genes.put(ecNum, genes4EcNum);
				}
				genes4EcNum.add(gene);
			}
		}
		
		// get set of reactions corresponding to those ecNums
		Set<Reaction> rxns = rxnDao.getReactionsForEcNums(ecNum2Genes.keySet());
		
		// add genes to rxns
		for(Reaction rxn : rxns) {
			for(String ecNum : rxn.getEcNums()) {
				Set<Gene> genes4EcNum = ecNum2Genes.get(ecNum);
				if(genes4EcNum != null)
					for(Gene gene : genes4EcNum)
						rxn.addGene(gene);
			}
		}
		
		return rxns;
	}
	
}