package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FBAModel {
	private String fbaModelId;
	private String genomeId;
	private String mappingId;
	private String biochemistryId;
	private String name;
	private String type;
	private String status;
	
	private List<ModelBiomass> biomasses;
	private List<ModelCompartment> compartments;
	private List<ModelReaction> reactions;
	private List<ModelCompound> compounds;
	
	private List<FBAMeta> fbas;
	private List<GapFillMeta> integratedGapFillings;
	private List<GapFillMeta> unintegratedGapFillings;
	private List<GapGenMeta> integratedGapGenerations;
	private List<GapGenMeta> unintegratedGapGenerations;
	
	@JsonProperty("unintegrated_gapgeneration")
	public List<GapGenMeta> getUnintegratedGapGenerations() {
		return unintegratedGapGenerations;
	}
	@JsonProperty("unintegrated_gapgeneration")
	public void setUnintegratedGapGenerations(
			List<GapGenMeta> unintegratedGapGenerations) {
		this.unintegratedGapGenerations = unintegratedGapGenerations;
	}
	
	@JsonProperty("id")
	public String getFbaModelId() {
		return fbaModelId;
	}
	@JsonProperty("id")
	public void setFbaModelId(String fbaModelId) {
		this.fbaModelId = fbaModelId;
	}
	
	@JsonProperty("genome")
	public String getGenomeId() {
		return genomeId;
	}
	@JsonProperty("genome")
	public void setGenomeId(String genomeId) {
		this.genomeId = genomeId;
	}
	
	@JsonProperty("map")
	public String getMappingId() {
		return mappingId;
	}
	@JsonProperty("map")
	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}
	
	@JsonProperty("biochemistry_id")
	public String getBiochemistryId() {
		return biochemistryId;
	}
	@JsonProperty("biochemistry_id")
	public void setBiochemistryId(String biochemistryId) {
		this.biochemistryId = biochemistryId;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonProperty("biomasses")
	public List<ModelBiomass> getBiomasses() {
		return biomasses;
	}
	@JsonProperty("biomasses")
	public void setBiomasses(List<ModelBiomass> biomasses) {
		this.biomasses = biomasses;
	}
	
	@JsonProperty("compartments")
	public List<ModelCompartment> getCompartments() {
		return compartments;
	}
	@JsonProperty("compartments")
	public void setCompartments(List<ModelCompartment> compartments) {
		this.compartments = compartments;
	}
	
	@JsonProperty("reactions")
	public List<ModelReaction> getReactions() {
		return reactions;
	}
	@JsonProperty("reactions")
	public void setReactions(List<ModelReaction> reactions) {
		this.reactions = reactions;
	}
	
	@JsonProperty("compounds")
	public List<ModelCompound> getCompounds() {
		return compounds;
	}
	@JsonProperty("compounds")
	public void setCompounds(List<ModelCompound> compounds) {
		this.compounds = compounds;
	}
	
	@JsonProperty("fbas")
	public List<FBAMeta> getFbas() {
		return fbas;
	}
	@JsonProperty("fbas")
	public void setFbas(List<FBAMeta> fbas) {
		this.fbas = fbas;
	}

	@JsonProperty("integrated_gapfillings")
	public List<GapFillMeta> getIntegratedGapFillings() {
		return integratedGapFillings;
	}
	@JsonProperty("integrated_gapfillings")
	public void setIntegratedGapFillings(List<GapFillMeta> integratedGapFillings) {
		this.integratedGapFillings = integratedGapFillings;
	}
	
	@JsonProperty("unintegrated_gapfillings")
	public List<GapFillMeta> getUnintegratedGapFillings() {
		return unintegratedGapFillings;
	}
	@JsonProperty("unintegrated_gapfillings")
	public void setUnintegratedGapFillings(List<GapFillMeta> unintegratedGapFillings) {
		this.unintegratedGapFillings = unintegratedGapFillings;
	}
	
	@JsonProperty("integrated_gapgenerations")
	public List<GapGenMeta> getIntegratedGapGenerations() {
		return integratedGapGenerations;
	}
	@JsonProperty("integrated_gapgenerations")
	public void setIntegratedGapGenerations(
			List<GapGenMeta> integratedGapGenerations) {
		this.integratedGapGenerations = integratedGapGenerations;
	}
}
