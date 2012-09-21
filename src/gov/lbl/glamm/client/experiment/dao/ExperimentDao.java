package gov.lbl.glamm.client.experiment.dao;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.shared.model.Sample.DataType;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.List;
import java.util.Map;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public interface ExperimentDao {

	/**
	 * Add experiment and dataType-element data from sharedData.
	 * @param experiments added to by this method
	 * @param dataTypeElementAssocMap added to by this method
	 * @param sharedData input data
	 * @param objCount
	 */
	public void addExperimentData(
			List<Experiment> experiments, Map<DataType,Object[]> dataTypeElementAssocMap
			, PathwayExperimentData sharedData, ObjectCount objCount );
}
