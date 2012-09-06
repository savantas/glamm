package gov.lbl.glamm.shared.model.interfaces;

import gov.lbl.glamm.shared.model.util.MeasurementSet;

/**
 * Interface indicating the implementor has a set of measurements associated with it.
 * @author jtbates
 *
 */
public interface HasMeasurements {
	/**
	 * Gets the set of measurements associated with this object.
	 * @return The MeasurementSet
	 */
	public MeasurementSet getMeasurementSet();
}
