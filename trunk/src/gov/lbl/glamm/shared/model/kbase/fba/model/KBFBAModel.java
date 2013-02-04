package gov.lbl.glamm.shared.model.kbase.fba.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBFBAModel {
	private String fbaModelId;
	private String genomeId;
	private String mappingId;
	private String biochemistryId;
	private String name;
	private String type;
	private String status;
	private String workspaceId;
	private String genomeWorkspaceId;
	private String mapWorkspaceId;
	private String biochemistryWorkspaceId;
	
	private List<KBModelBiomass> biomasses;
	private List<KBModelCompartment> compartments;
	private List<KBModelReaction> reactions;
	private List<KBModelCompound> compounds;
	
	private List<KBFBAMeta> fbas;
	private List<KBGapFillMeta> integratedGapFillings;
	private List<KBGapFillMeta> unintegratedGapFillings;
	private List<KBGapGenMeta> integratedGapGenerations;
	private List<KBGapGenMeta> unintegratedGapGenerations;
	
	@JsonProperty("workspace")
	public String getWorkspaceId() {
		return workspaceId;
	}
	@JsonProperty("workspace")
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	@JsonProperty("genome_workspace")
	public String getGenomeWorkspaceId() {
		return genomeWorkspaceId;
	}
	@JsonProperty("genome_workspace")
	public void setGenomeWorkspaceId(String genomeWorkspaceId) {
		this.genomeWorkspaceId = genomeWorkspaceId;
	}

	@JsonProperty("map_workspace")
	public String getMapWorkspaceId() {
		return mapWorkspaceId;
	}
	@JsonProperty("map_workspace")
	public void setMapWorkspaceId(String mapWorkspaceId) {
		this.mapWorkspaceId = mapWorkspaceId;
	}

	@JsonProperty("biochemistry_workspace")
	public String getBiochemistryWorkspaceId() {
		return biochemistryWorkspaceId;
	}
	@JsonProperty("biochemistry_workspace")
	public void setBiochemistryWorkspaceId(String biochemistryWorkspaceId) {
		this.biochemistryWorkspaceId = biochemistryWorkspaceId;
	}

	@JsonProperty("unintegrated_gapgeneration")
	public List<KBGapGenMeta> getUnintegratedGapGenerations() {
		return unintegratedGapGenerations;
	}
	@JsonProperty("unintegrated_gapgeneration")
	public void setUnintegratedGapGenerations(
			List<KBGapGenMeta> unintegratedGapGenerations) {
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
	public List<KBModelBiomass> getBiomasses() {
		return biomasses;
	}
	@JsonProperty("biomasses")
	public void setBiomasses(List<KBModelBiomass> biomasses) {
		this.biomasses = biomasses;
	}
	
	@JsonProperty("compartments")
	public List<KBModelCompartment> getCompartments() {
		return compartments;
	}
	@JsonProperty("compartments")
	public void setCompartments(List<KBModelCompartment> compartments) {
		this.compartments = compartments;
	}
	
	@JsonProperty("reactions")
	public List<KBModelReaction> getReactions() {
		return reactions;
	}
	@JsonProperty("reactions")
	public void setReactions(List<KBModelReaction> reactions) {
		this.reactions = reactions;
	}
	
	@JsonProperty("compounds")
	public List<KBModelCompound> getCompounds() {
		return compounds;
	}
	@JsonProperty("compounds")
	public void setCompounds(List<KBModelCompound> compounds) {
		this.compounds = compounds;
	}
	
	@JsonProperty("fbas")
	public List<KBFBAMeta> getFbas() {
		return fbas;
	}
	@JsonProperty("fbas")
	public void setFbas(List<KBFBAMeta> fbas) {
		this.fbas = fbas;
	}

	@JsonProperty("integrated_gapfillings")
	public List<KBGapFillMeta> getIntegratedGapFillings() {
		return integratedGapFillings;
	}
	@JsonProperty("integrated_gapfillings")
	public void setIntegratedGapFillings(List<KBGapFillMeta> integratedGapFillings) {
		this.integratedGapFillings = integratedGapFillings;
	}
	
	@JsonProperty("unintegrated_gapfillings")
	public List<KBGapFillMeta> getUnintegratedGapFillings() {
		return unintegratedGapFillings;
	}
	@JsonProperty("unintegrated_gapfillings")
	public void setUnintegratedGapFillings(List<KBGapFillMeta> unintegratedGapFillings) {
		this.unintegratedGapFillings = unintegratedGapFillings;
	}
	
	@JsonProperty("integrated_gapgenerations")
	public List<KBGapGenMeta> getIntegratedGapGenerations() {
		return integratedGapGenerations;
	}
	@JsonProperty("integrated_gapgenerations")
	public void setIntegratedGapGenerations(
			List<KBGapGenMeta> integratedGapGenerations) {
		this.integratedGapGenerations = integratedGapGenerations;
	}
}
