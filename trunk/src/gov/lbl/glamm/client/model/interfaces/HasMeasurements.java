package gov.lbl.glamm.client.model.interfaces;

import gov.lbl.glamm.client.model.Measurement;

import java.util.Set;

public interface HasMeasurements {
	public void addMeasurement(final Measurement measurement);
	public Set<Measurement> getMeasurements();
	public void setMeasurements(final Set<Measurement> measurements);
}
