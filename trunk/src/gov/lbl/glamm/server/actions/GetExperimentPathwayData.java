package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.PathwayDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.KeggPathwayDAOImpl;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;
import gov.lbl.glamm.server.util.PathwayExperimentTestServerUtil;
import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.Reaction.Participant;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.interfaces.HasMeasurements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import noNamespace.ReactionFunDataDocument;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.http.client.RequestException;

public class GetExperimentPathwayData {

	private static Logger logger = LoggerFactory.getLogger( GetExperimentPathwayData.class );
	
	public static PathwayExperimentData getPathwayData(String pathwayIds, String experimentIds, String fileName) throws IllegalArgumentException, RequestException {
		File file = null;
		file = new File( fileName );

		ReactionFunDataDocument doc = null;
		try {
			doc = ReactionFunDataDocument.Factory.parse( file );
		} catch ( XmlException xe ) {
			logger.error( "getPathwayData: error parsing data xml file.", xe );
			throw new RequestException( xe );
		} catch ( IOException ioe ) {
			logger.error( "getPathwayData: error parsing data xml file.", ioe );
			throw new RequestException( ioe );
		}

		Map<String,Measurement> idMeasurementMap = PathwayExperimentTestServerUtil
				.parseMeasurementDataToSharedObj( doc );
		List<Experiment> experiments = new ArrayList<Experiment>();
		Map<String,Sample> idSampleMap = new HashMap<String,Sample>();
		PathwayExperimentTestServerUtil.parseExperimentDataToSharedObj( experiments, idSampleMap, doc );
		Map<String,Compound> idCompoundMap = new HashMap<String,Compound>();
		Map<String,Gene> idGeneMap = new HashMap<String,Gene>();
		Map<String,Reaction> idReactionMap = new HashMap<String,Reaction>();
		List<Pathway> pathways = PathwayExperimentTestServerUtil
				.parsePathwayDataToSharedObj(
						idCompoundMap, idGeneMap, idReactionMap
						, doc
						, idMeasurementMap, idSampleMap );

		// Convert to shared objects
		PathwayExperimentData peData = new PathwayExperimentData();
		peData.setExperiments( experiments );
		peData.setPathways( pathways );
		peData.setIdCompoundMap( idCompoundMap );
		peData.setIdGeneMap( idGeneMap );
//		peData.setIdOrganismMap( idOrganismMap );

		return peData;
	}
	
	public static PathwayExperimentData getDummyPathwayData(GlammSession sm) {
		
//		String expId = "2956",			// an experiment in Brucella melitensis 16M
//			   taxId = "224914",		// Brucella melitensis 16M
//			   pathId = "map00010";		// Glycolysis
		
		String expId = "46",
			   taxId = "511145",
			   pathId = "map00010";
		
		PathwayExperimentData peData = new PathwayExperimentData();
		
		/* Need:
		 * List of Experiments
		 * List of Pathways
		 * Map<String, Compound> idCompoundMap ...?
		 * Map<String, Gene> idGeneMap ...?
		 */
		List<Experiment> experiments = new ArrayList<Experiment>();
		List<Pathway> pathways = new ArrayList<Pathway>();
		Map<String, Compound> idCompoundMap = new HashMap<String, Compound>();
		Map<String, Gene> idGeneMap = new HashMap<String, Gene>();
		Map<String, Organism> idOrganismMap = new HashMap<String, Organism>();

		ExperimentDAO experimentDao = new ExperimentDAOImpl(sm);
		PathwayDAO pathwayDao = new KeggPathwayDAOImpl(sm);
		
		OrganismDAO organismDao = new OrganismDAOImpl(sm);
		Organism org = organismDao.getOrganismForTaxonomyId(taxId);
		idOrganismMap.put(org.getTaxonomyId(), org);
		
		Experiment exp = experimentDao.getExperiment(expId);
		List<Sample> samples = exp.getSamples();
		experiments.add(exp);
		Pathway pathway = pathwayDao.getPathway(pathId, org);
		pathways.add(pathway);
		
		for (Reaction reaction : pathway.getReactions()) {
			for (Participant participant : reaction.getSubstrates()) {
				Compound compound = participant.getCompound();
				idCompoundMap.put(compound.getGuid(), compound);
			}
			for (Participant participant : reaction.getProducts()) {
				Compound compound = participant.getCompound();
				idCompoundMap.put(compound.getGuid(), compound);
			}
			for (Gene gene : reaction.getGenes()) {
				idGeneMap.put(gene.getVimssId(), gene);
			}
		}
		
		for (Sample sample : samples) {
			Set<? extends HasMeasurements> measurements = GetSample.getMeasurementsForSample(sm, sample);
			Map<String, Measurement> elementIdDataTypeMeasurementMap = sample.getElementIdDataTypeMeasurementMap();
			
			Sample.DataType dataType = sample.getDataType();

			switch (sample.getTargetType()) {
				case COMPOUND :
					for (HasMeasurements element : measurements) {
						Compound cpd = (Compound)element;
						for (Measurement measurement : cpd.getMeasurementSet().getMeasurements())
							elementIdDataTypeMeasurementMap.put(cpd.getGuid() + dataType, measurement);
						idCompoundMap.put(cpd.getGuid(), cpd);
					}
					break;
				
				case REACTION :
					for (HasMeasurements element : measurements) {
						Reaction rxn = (Reaction)element;
						for (Measurement measurement : rxn.getMeasurementSet().getMeasurements())
							elementIdDataTypeMeasurementMap.put(rxn.getGuid() + dataType, measurement);
					}
					break;
				
				case GENE :
					for (HasMeasurements element : measurements) {
						for (Gene gene : ((Reaction)element).getGenes()) {
							for (Measurement measurement : gene.getMeasurementSet().getMeasurements()) {
								elementIdDataTypeMeasurementMap.put(gene.getVimssId() + dataType, measurement);
							}
							idGeneMap.put(gene.getVimssId(), gene);
						}
					}
			}
		}
		
		//224914 = taxonomyID for Brucella melitensis 16M
		peData.setExperiments(experiments);
		peData.setPathways(pathways);
		peData.setIdCompoundMap(idCompoundMap);
		peData.setIdGeneMap(idGeneMap);
		peData.setIdOrganismMap(idOrganismMap);
		
		return peData;
	}
}
