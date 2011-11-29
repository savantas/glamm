package gov.lbl.glamm.client.model.util;

import gov.lbl.glamm.client.model.Measurement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class MeasurementSet implements Serializable {
	
	Set<Measurement> measurements;
	
	public MeasurementSet() {
		measurements = new HashSet<Measurement>();
	}
	
	public void addMeasurement(final Measurement measurement) {
		measurements.add(measurement);
	}
	
	public Set<Measurement> getMeasurements() {
		return measurements;
	}
	
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
