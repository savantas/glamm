package gov.lbl.glamm.shared.model;

import java.io.Serializable;

/**
 * A class describing a Flux Experiment.
 * @author wjriehl
 *
 */
@SuppressWarnings("serial")
public class FluxExperiment implements Serializable {

	private String expId;
	private String modelId;
	
	@SuppressWarnings("unused")
	private FluxExperiment() {}
	
	public FluxExperiment(final String expId, final String modelId) {
		this.expId = expId;
		this.modelId = modelId;
	}
	
	public String getExperimentId() {
		return expId;
	}
	
	public String getModelId() {
		return modelId;
	}
	
}