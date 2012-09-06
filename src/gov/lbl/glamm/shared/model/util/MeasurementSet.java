package gov.lbl.glamm.shared.model.util;

import gov.lbl.glamm.shared.model.Measurement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Class responsible for managing a set of measurements for an object.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class MeasurementSet implements Serializable {
	
	Set<Measurement> measurements;
	
	/**
	 * Constructor
	 */
	public MeasurementSet() {
		measurements = new HashSet<Measurement>();
	}
	
	/**
	 * Adds a measurement to the set.
	 * @param measurement The measurment.
	 */
	public void addMeasurement(final Measurement measurement) {
		measurements.add(measurement);
	}
	
	/**
	 * Gets all measurements in the set.
	 * @return The set of all measurments.
	 */
	public Set<Measurement> getMeasurements() {
		return measurements;
	}
	
	/**
	 * Sets the contents of this measurement set to those of the parameter.
	 * @param measurements The set of all measurements.
	 */
	public void setMeasurements(final Set<Measurement> measurements) {
		this.measurements.clear();
		if(measurements != null && !measurements.isEmpty())
			this.measurements.addAll(measurements);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((measurements == null) ? 0 : measurements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementSet other = (MeasurementSet) obj;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		return true;
	}
	
	
}
