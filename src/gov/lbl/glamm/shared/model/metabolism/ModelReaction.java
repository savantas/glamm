package gov.lbl.glamm.shared.model.metabolism;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelReaction {
	private String direction = null,
				   modelCompartmentUUID = null,
				   modDate = null,
				   uuid = null,
				   reactionInstanceUUID = null,
				   protons = null;
	private List<ModelReactionStoichiometry> modelReactionReagents;
	private List<ModelReactionProtein> reactionProteins;
	
	public ModelReaction() { }
	
	@JsonProperty("direction")
	public String getDirection() { return direction; }	
	@JsonProperty("direction")
	public void setDirection(String direction) { this.direction = direction; }

	@JsonProperty("modelcompartment_uuid")
	public String getModelCompartmentUUID() { return modelCompartmentUUID; }
	@JsonProperty("modelcompartment_uuid")
	public void setModelCompartmentUUID(String modelCompartmentUUID) { this.modelCompartmentUUID = modelCompartmentUUID; }

	@JsonProperty("modDate")
	public String getModDate() { return modDate; }
	@JsonProperty("modDate")
	public void setModDate(String modDate) { this.modDate = modDate; }

	@JsonProperty("uuid")
	public String getUUID() { return uuid; }
	@JsonProperty("uuid")
	public void setUUID(String uuid) { this.uuid = uuid; }

	@JsonProperty("reactioninstance_uuid")
	public String getReactionInstanceUUID() { return reactionInstanceUUID; }
	@JsonProperty("reactioninstance_uuid")
	public void setReactionInstanceUUID(String reactionInstanceUUID) { this.reactionInstanceUUID = reactionInstanceUUID; }

	@JsonProperty("protons")
	public String getProtons() { return protons; }
	@JsonProperty("protons")
	public void setProtons(String protons) { this.protons = protons; }

	@JsonProperty("modelReactionReagents")
	public List<ModelReactionStoichiometry> getModelReactionReagents() { return modelReactionReagents; }
	@JsonProperty("modelReactionReagents")
	public void setModelReactionReagents(List<ModelReactionStoichiometry> modelReactionReagents) { this.modelReactionReagents = modelReactionReagents; }

	@JsonProperty("modelReactionProteins")
	public List<ModelReactionProtein> getReactionProteins() { return reactionProteins; }
	@JsonProperty("modelReactionProteins")
	public void setReactionProteins(List<ModelReactionProtein> reactionProteins) { this.reactionProteins = reactionProteins; }
}
