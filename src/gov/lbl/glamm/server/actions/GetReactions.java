package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service class for getting sets of reactions.
 * @author jtbates
 *
 */
public class GetReactions {

	/**
	 * Gets a set of reactions.
	 * @param sm The GLAMM session.
	 * @param ids The list of reaction ids.
	 * @param organism The optional organism for which these reactions belong - adds genes to the reactions, if specified.
	 * @param sample The optional sample - adds measurements to the genes, if specified.
	 * @return The set of reactions.
	 */
	public static Set<Reaction> getReactions(final GlammSession sm, 
			final Set<String> ids, 
			final Organism organism, 
			final Sample sample) {

		if(ids == null || ids.isEmpty())
			return new HashSet<Reaction>();
		
		
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> rxns = rxnDao.getReactions(ids);

		if(organism != null && !organism.isGlobalMap()) {
			// add genes for non-null organisms
			GeneDAO geneDao = new GeneDAOImpl(sm);
			Set<String> geneSynonyms = new HashSet<String>();

			for(Reaction rxn : rxns) {
				Set<Gene> genes = geneDao.getGenesForEcNums(organism.getTaxonomyId(), rxn.getEcNums());
				if(genes == null)
					continue;
				for(Gene gene : genes) {
					rxn.addGene(gene);
					if(sample != null) {
						for(Synonym synonym : gene.getSynonyms())
							geneSynonyms.add(synonym.getName());
					}
				}
			}
			
			if(sample != null && !geneSynonyms.isEmpty()) {
				ExperimentDAO expDao = new ExperimentDAOImpl(sm);
				Map<String, Set<Measurement>> measurementMap = expDao.getMeasurementsForIds(sample.getExperimentId(), 
						sample.getSampleId(), geneSynonyms);
				for(Reaction rxn : rxns) {
					for(Gene gene : rxn.getGenes()) {
						for(Synonym synonym : gene.getSynonyms()) {
							Set<Measurement> measurements = measurementMap.get(synonym.getName());
							if(measurements != null)
								for(Measurement measurement : measurements)
									gene.getMeasurementSet().addMeasurement(measurement);
						}
					}
				}
			}
		}

		return rxns;
	}
}
