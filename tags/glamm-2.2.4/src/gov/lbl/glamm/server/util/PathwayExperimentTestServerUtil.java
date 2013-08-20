package gov.lbl.glamm.server.util;

import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.Reaction.Direction;
import gov.lbl.glamm.shared.model.Reaction.Participant;
import gov.lbl.glamm.shared.model.Reaction.Participant.KeggRpairRole;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.Sample.DataType;
import gov.lbl.glamm.shared.model.Sample.TargetType;
import gov.lbl.glamm.shared.model.util.Synonym;
import gov.lbl.glamm.shared.model.util.Xref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noNamespace.CompoundType;
import noNamespace.ExperimentType;
import noNamespace.GeneReferenceType;
import noNamespace.GeneType;
import noNamespace.MeasurementReferenceType;
import noNamespace.MeasurementType;
import noNamespace.PathwayLinkType;
import noNamespace.PathwayStepType;
import noNamespace.PathwayType;
import noNamespace.ProductType;
import noNamespace.ReactantType;
import noNamespace.ReactionFunDataDocument;
import noNamespace.ReactionType;
import noNamespace.SampleType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathwayExperimentTestServerUtil {

	private static Logger logger = LoggerFactory.getLogger( PathwayExperimentTestServerUtil.class );

	public final static String GLAMM_DB_NAME = "glamm";

	public static Map<String,Measurement> parseMeasurementDataToSharedObj( ReactionFunDataDocument doc ) {
		Map<String,Measurement> idMeasurementMap = new HashMap<String,Measurement>();
		for ( ExperimentType docExperiment : doc.getReactionFunData().getExperiments().getExperimentArray() ) {
			for ( SampleType docSample : docExperiment.getSamples().getSampleArray() ) {
				for ( MeasurementType docMeasurement : docSample.getMeasurements().getMeasurementArray() ) {
					Measurement measurement = new Measurement(
							docExperiment.getId()
							, docSample.getId()
							, docMeasurement.getValue()
							, docMeasurement.getConfidence()
							, null
							);
					idMeasurementMap.put( docMeasurement.getId(), measurement );
				}
			}
		}
		return idMeasurementMap;
	}

	public static void parseExperimentDataToSharedObj(
			List<Experiment> experiments
			, Map<String, Sample> idSampleMap
			, ReactionFunDataDocument doc ) {
		Map<String,DataType> dummyXMLStringDataTypeMap = new HashMap<String,DataType>();
		dummyXMLStringDataTypeMap.put( "mRNA", DataType.RNA );
		dummyXMLStringDataTypeMap.put( "protein", DataType.PROTEIN );
		dummyXMLStringDataTypeMap.put( "flux", DataType.FITNESS );
		dummyXMLStringDataTypeMap.put( "metabolite", DataType.SESSION );
		Map<DataType,TargetType> dummyXMLDataTypeTargetTypeMap = new HashMap<DataType,TargetType>();
		dummyXMLDataTypeTargetTypeMap.put( DataType.RNA, TargetType.GENE );
		dummyXMLDataTypeTargetTypeMap.put( DataType.PROTEIN, TargetType.GENE );
		dummyXMLDataTypeTargetTypeMap.put( DataType.FITNESS, TargetType.REACTION );
		dummyXMLDataTypeTargetTypeMap.put( DataType.SESSION, TargetType.COMPOUND );
		for ( ExperimentType docExperiment : doc.getReactionFunData().getExperiments().getExperimentArray() ) {
			Experiment experiment = new Experiment( docExperiment.getId() );
			for ( SampleType docSample : docExperiment.getSamples().getSampleArray() ) {
				MeasurementType docFirstMeasurement = null;
				if ( docSample.getMeasurements() != null && docSample.getMeasurements().getMeasurementArray() != null ) {
					docFirstMeasurement = docSample.getMeasurements().getMeasurementArray(0);
				}
				DataType dataType = null;
				if ( docFirstMeasurement != null ) {
					dataType = dummyXMLStringDataTypeMap.get( docFirstMeasurement.getDataType() );
				}
				Sample sample = new Sample(
						experiment.getExperimentId(), docSample.getId()
						, dataType
						);
				if ( docFirstMeasurement != null ) {
					sample.setConfidenceType( docFirstMeasurement.getConfidenceType() );
				}
				sample.setConfidenceType( docSample.getCFactor() );
				sample.setFactorUnits( docSample.getFactorUnit() );
				sample.setControl( docSample.getCFactor(), docSample.getCTime() );
				sample.setTreatment( docSample.getTFactor(), docSample.getTTime() );
				sample.setUnits( docSample.getFactorUnit() );
				sample.setStress( docExperiment.getStress() );
				sample.setTargetType( dummyXMLDataTypeTargetTypeMap.get( dataType ) );
				idSampleMap.put( sample.getSampleId(), sample );

				experiment.addSample( sample );
			}
			experiments.add( experiment );
		}
	}

	public static List<Pathway> parsePathwayDataToSharedObj(
			Map<String, Compound> idCompoundMap
			, Map<String, Gene> idGeneMap
			, Map<String, Reaction> idReactionMap
			, ReactionFunDataDocument doc
			, Map<String,Measurement> idMeasurementMap, Map<String, Sample> idSampleMap ) {
		CompoundType[] docCompounds = doc.getReactionFunData().getCompounds().getCompoundArray();
		for ( CompoundType docCompound : docCompounds ) {
			// Create compound object
			Compound compound = new Compound();
			compound.setGuid( docCompound.getId() );
			compound.setFormula( docCompound.getFormula() );
			compound.setMass( Float.toString( docCompound.getMass() ) );
			compound.setName( docCompound.getName() );
			compound.setInchi( docCompound.getInchi() );
			compound.setSmiles( docCompound.getSmiles() );
			Synonym synonym = new Synonym( docCompound.getExtId(), docCompound.getExtIdName() );
			compound.addSynonym( synonym );
			if ( docCompound.getPathwayLinks() != null ) {
				for ( PathwayLinkType docPathwayLink : docCompound.getPathwayLinks().getPathwayLinkArray() ) {
					Pathway pathwayLink = new Pathway();
					pathwayLink.setMapId( docPathwayLink.getId() );
					pathwayLink.setName( docPathwayLink.getName() );
					compound.getPathwayLinks().add( pathwayLink );
				}
			}

			// Place compound object in map for easy reference
			idCompoundMap.put(  docCompound.getId(), compound );

			// Measurements
			if ( docCompound.getMeasurementReferences() != null ) {
				for ( MeasurementReferenceType docMeasRef : docCompound.getMeasurementReferences().getMeasurementReferenceArray() ) {
					Measurement measurement = idMeasurementMap.get( docMeasRef.getRefId() );
					Sample sample = idSampleMap.get( measurement.getSampleId() );
					sample.getElementIdDataTypeMeasurementMap().put(
							compound.getGuid() + sample.getDataType(), measurement );
				}
			}
		}

		GeneType[] docGenes = doc.getReactionFunData().getGenes().getGeneArray();
		for ( GeneType docGene : docGenes ) {
			// Create gene object
			Gene gene = new Gene();
			gene.addEcNum( docGene.getEcNum() );
			Synonym synonym = new Synonym( docGene.getSynonym(), "synonym" );
			gene.addSynonym( synonym );
			synonym = new Synonym( docGene.getExtId(), docGene.getExtIdName() );
			gene.addSynonym( synonym );
			synonym = new Synonym( docGene.getId(), Gene.SYNONYM_TYPE_VIMSS );
			gene.addSynonym( synonym );
			gene.addMetaMolTaxonomyId( docGene.getSynonym() );
			gene.addMolTaxonomyId( docGene.getName() );
			// Place gene object in map for easy reference
			idGeneMap.put( docGene.getId(), gene );

			// Measurements
			if ( docGene.getMeasurementReferences() != null ) {
				for ( MeasurementReferenceType docMeasRef : docGene.getMeasurementReferences().getMeasurementReferenceArray() ) {
					Measurement measurement = idMeasurementMap.get( docMeasRef.getRefId() );
					Sample sample = idSampleMap.get( measurement.getSampleId() );
					sample.getElementIdDataTypeMeasurementMap().put(
							gene.getVimssId() + sample.getDataType(), measurement );
				}
			}
		}

		ReactionType[] docReactions = doc.getReactionFunData().getReactions().getReactionArray();
		for ( ReactionType docReaction : docReactions ) {
			Reaction reaction = new Reaction();
			ReactantType[] docReactants = docReaction.getReactants().getReactantArray();
			for ( ReactantType docReactant : docReactants ) {
				Compound compound = idCompoundMap.get( docReactant.getCompoundRef() );
				Participant participant = new Participant( compound
						, docReactant.getCoefficient()
						, KeggRpairRole.fromString( docReactant.getRole() )
						);
				reaction.addSubstrate( participant );
			}
			ProductType[] docProducts = docReaction.getProducts().getProductArray();
			for ( ProductType docProduct : docProducts ) {
				Compound compound = idCompoundMap.get( docProduct.getCompoundRef() );
				Participant participant = new Participant( compound
						, docProduct.getCoefficient()
						, KeggRpairRole.fromString( docProduct.getRole() )
						);
				reaction.addProduct( participant );
			}
			if ( docReaction.getGeneReferences() == null ) {
				logger.debug( "No genes for reaction with id " + docReaction.getId() );
			} else {
				GeneReferenceType[] docGeneRefs = docReaction.getGeneReferences().getGeneReferenceArray();
				for ( GeneReferenceType docGeneRef : docGeneRefs ) {
					reaction.addGene( idGeneMap.get( docGeneRef.getRefId() ) );
				}
			}
			reaction.addEcNum( docReaction.getEcNum() );
			reaction.setDefinition( docReaction.getDefinition() );
			reaction.setGuid( docReaction.getId() );
			reaction.getXrefSet().addXref( new Xref( docReaction.getId(), GLAMM_DB_NAME ) );
			// Note: Reaction is not in a pathway, so there is no direction data

			// Place reaction object in map for easy reference
			idReactionMap.put( docReaction.getId(), reaction );

			// Measurements
			if ( docReaction.getMeasurementReferences() != null ) {
				for ( MeasurementReferenceType docMeasRef : docReaction.getMeasurementReferences().getMeasurementReferenceArray() ) {
					Measurement measurement = idMeasurementMap.get( docMeasRef.getRefId() );
					if ( measurement == null ) {
						logger.warn( "Missing in xml document: measurement with refId " + docMeasRef.getRefId() );
					} else {
						Sample sample = idSampleMap.get( measurement.getSampleId() );
						sample.getElementIdDataTypeMeasurementMap().put(
								reaction.getGuid() + sample.getDataType(), measurement );
					}
				}
			}
		}

		List<Pathway> pathways = new ArrayList<Pathway>();
		PathwayType[] docPathways = doc.getReactionFunData().getPathways().getPathwayArray();
		for ( PathwayType docPathway : docPathways ) {
			Pathway pathway = new Pathway();
			pathway.setName( docPathway.getName() );
			List<PathwayStepType> orderingStepList = new ArrayList<PathwayStepType>( docPathway.getPathwayStepArray().length );
			for ( PathwayStepType docPathStep : docPathway.getPathwayStepArray() ) {
				orderingStepList.add( docPathStep );
			}
			Collections.sort( orderingStepList
					, new Comparator<PathwayStepType>() {
				@Override
				public int compare( PathwayStepType step1, PathwayStepType step2 ) {
					return step1.getOrder() - step2.getOrder();
				}
					}
			);
			for ( PathwayStepType docPathStep : orderingStepList ) {
				logger.debug( "parsePathwayDataToSharedObj(): pathway = " + pathway.getName() + "; path step reaction ref = " + docPathStep.getReactionRef() );
				Reaction genericReaction = idReactionMap.get( docPathStep.getReactionRef() );
				Reaction pathwayStep = genericReaction.shallowClone();
				Direction direction = Direction.UNSPECIFIED;
				if ( docPathStep.getDirection().equals( "both" ) ) {
					direction = Direction.BOTH;
				} else if ( docPathStep.getDirection().equals( "forward" ) ) {
					direction = Direction.FORWARD;
				} else if ( docPathStep.getDirection().equals( "reverse" ) ) {
					direction = Direction.REVERSE;
				}
				pathwayStep.setDirection( direction );
				docPathStep.getOrder();
				pathway.addReaction( pathwayStep );
			}
			pathway.setName( docPathway.getName() );
			pathways.add( docPathway.getOrder(), pathway );
		}

		return pathways;
	}
}
