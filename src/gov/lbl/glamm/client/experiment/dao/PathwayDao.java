package gov.lbl.glamm.client.experiment.dao;

import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.client.experiment.util.ObjectCount;

import java.util.List;

public interface PathwayDao {

	/**
	 * Add peData pathway data to the pathways and secondaryMetaboliteSet containers.
	 *
	 * @param pathways added to by this method
	 * @param viewSecondaryMetaboliteSet added to by this method
	 * @param sharedData input data
	 * @param objCount
	 */
	public void addPathwayData(
			List<ViewPathway> pathways
			, BinarySortedSet<ViewCompound> viewSecondaryMetaboliteSet
			, PathwayExperimentData sharedData, ObjectCount objCount );
}
