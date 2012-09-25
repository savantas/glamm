package gov.lbl.glamm.shared.model.metabolism;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelReactionStoichiometry {
	private String modelCompoundUUID = null;
	private String coefficient = null;
	
	public ModelReactionStoichiometry() { }
	
	@JsonProperty("modelcompound_uuid")
	public String getModelCompoundUUID() { return modelCompoundUUID; }
	@JsonProperty("modelcompound_uuid")
	public void setModelCompoundUUID(String modelCompoundUUID) { this.modelCompoundUUID = modelCompoundUUID; }

	@JsonProperty("coefficient")
	public String getCoefficient() { return coefficient; }
	@JsonProperty("coefficient")
	public void setCoefficient(String coefficient) { this.coefficient = coefficient; }
}
