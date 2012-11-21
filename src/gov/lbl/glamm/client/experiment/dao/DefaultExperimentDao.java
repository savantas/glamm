package gov.lbl.glamm.client.experiment.dao;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.Sample.DataType;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class DefaultExperimentDao implements ExperimentDao {

	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.dao.ExperimentDao#getExperimentData(gov.lbl.glamm.experiment.shared.model.PathwayExperimentData)
	 * During processing, populate dataTypeElementAssocMap
	 * with the dataType mapped to an array [ order, element type ].
	 * If there are multiple associations, only the first is stored.
	 */
	@Override
	public void addExperimentData(
			List<Experiment> experiments
			, Map<DataType,Object[]> dataTypeElementAssocMap
			, PathwayExperimentData sharedData
			, ObjectCount objCount ) {
		int dataTypeOrder = 0;
		for ( Experiment sharedExperiment : sharedData.getExperiments() ) {
			Experiment experiment = new Experiment();
			objCount.expObjCount++;
			experiment.setExperimentId( sharedExperiment.getExperimentId() );

			for ( Sample sharedSample : sharedExperiment.getSamples() ) {
				Sample sample = createSample( sharedSample, experiment, objCount );
				experiment.getSamples().add( sample );

				for ( Entry<String, Measurement>
						sharedEntry : sharedSample.getElementIdDataTypeMeasurementMap().entrySet() ) {
					Measurement sharedMeasurement = sharedEntry.getValue();
					Measurement measurement = createMeasurement(
							sharedMeasurement, sample, objCount );
					sample.getElementIdDataTypeMeasurementMap().put( sharedEntry.getKey(), measurement );

				}

				// record dataType element associations
				Object[] elementAssoc = dataTypeElementAssocMap.get(
						sample.getDataType() );
				if ( elementAssoc == null ) {
					elementAssoc = new Object[2];
					elementAssoc[0] = dataTypeOrder;
					String element = sharedSample.getTargetType().toString();
					elementAssoc[1] = element;
					dataTypeElementAssocMap.put(
							sample.getDataType(), elementAssoc );
					dataTypeOrder++;
				}
			}

			experiments.add( experiment );
		}
	}

	/**
	 * Sets experiment reference in returned Sample object, but does not add sample to experiment.
	 *
	 * @param sharedSample
	 * @param experiment
	 * @param objCount
	 * @return
	 */
	private Sample createSample( gov.lbl.glamm.shared.model.Sample sharedSample
			, Experiment experiment
			, ObjectCount objCount ) {
		Sample sample = new Sample();
		objCount.expObjCount++;
		sample.setExperiment( experiment );
		sample.setDataType( DataType.dataTypeForMolExpType(
				sharedSample.getDataType().getMolExpType() ) )
				;

		sample.setId( sharedSample.getSampleId() );
		sample.getAttributesMap().put( "clampMin", Float.toString( sharedSample.getClampMin() ) );
		sample.getAttributesMap().put( "clampMid", Float.toString( sharedSample.getClampMid() ) );
		sample.getAttributesMap().put( "clampMax", Float.toString( sharedSample.getClampMax() ) ) ;
		sample.getAttributesMap().put( "confidenceType", sharedSample.getConfidenceType() );
		sample.getAttributesMap().put( "control", sharedSample.getControl() );
		sample.getAttributesMap().put( "cTime", sharedSample.getcTime() );
		sample.getAttributesMap().put( "stress", sharedSample.getStress() );
		sample.getAttributesMap().put( "summary", sharedSample.getSummary() );
		sample.getAttributesMap().put( "treatment", sharedSample.getTreatment() );
		sample.getAttributesMap().put( "tTime", sharedSample.gettTime() );
		sample.getAttributesMap().put( "units", sharedSample.getUnits() );
		sample.getAttributesMap().put( "factorUnit", sharedSample.getFactorUnit() );
		sample.getAttributesMap().put( "unitString", sharedSample.getUnitString() );
		sample.getAttributesMap().put( "targetType", sharedSample.getTargetType().toString() );
		return sample;
	}

	/**
	 * Sets sample in measurement, but does not add measurement to sample's map.
	 *
	 * @param sharedMeasurement
	 * @param objCount
	 * @return
	 */
	private Measurement createMeasurement(
			gov.lbl.glamm.shared.model.Measurement sharedMeasurement
			, Sample sample
			, ObjectCount objCount ) {
		Measurement measurement = new Measurement();
		objCount.expObjCount++;
		measurement.setSample( sample );

		measurement.setConfidence( sharedMeasurement.getConfidence() );
		measurement.setValue( sharedMeasurement.getValue() );
		return measurement;
	}
}
