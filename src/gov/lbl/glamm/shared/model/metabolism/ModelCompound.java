package gov.lbl.glamm.shared.model.metabolism;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelCompound {
	private String modelCompartmentUUID = null,
				   compoundUUID = null,
				   charge = null,
				   formula = null,
				   modDate = null,
				   uuid = null;

	public ModelCompound() { }
	
	@JsonProperty("compound_uuid")
	public String getCompoundUUID() { return compoundUUID; }
	@JsonProperty("compound_uuid")
	public void setCompoundUUID(String compoundUUID) { this.compoundUUID = compoundUUID; }

	@JsonProperty("modelcompartment_uuid")
	public String getModelCompartmentUUID() { return modelCompartmentUUID; }
	@JsonProperty("modelcompartment_uuid")
	public void setModelCompartmentUUID(String modelCompartmentUUID) { this.modelCompartmentUUID = modelCompartmentUUID; }

	@JsonProperty("charge")
	public String getCharge() { return charge; }
	@JsonProperty("charge")
	public void setCharge(String charge) { this.charge = charge; }

	@JsonProperty("formula")
	public String getFormula() { return formula; }
	@JsonProperty("formula")
	public void setFormula(String formula) { this.formula = formula; }

	@JsonProperty("modDate")
	public String getModDate() { return modDate; }
	@JsonProperty("modDate")
	public void setModDate(String modDate) { this.modDate = modDate; }

	@JsonProperty("uuid")
	public String getUUID() { return uuid; }
	@JsonProperty("uuid")
	public void setUUID(String uuid) { this.uuid = uuid; }
}
