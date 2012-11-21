package gov.lbl.glamm.shared.model.metabolism.deprecated;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBaseMetabolicModel {

	private String status,
				   uuid,
				   growth,
				   defaultNameSpace,
				   current,
				   modDate,
				   isPublic,
				   id,
				   mappingUUID,
				   isLocked,
				   version,
				   name,
				   biochemistryUUID,
				   modelAnalysisUUID,
				   type,
				   annotationUUID;
	
	private List<Biomass> biomasses;
	private List<ModelCompound> modelCompounds;
	private List<ModelCompartment> modelCompartments;
	private List<ModelReaction> modelReactions;
	private List<String> ancestorUUIDs;
	
	private Map<String, Biomass> id2Biomass;
	private Map<String, ModelCompound> id2Compound;
	private Map<String, ModelCompartment> id2Compartment;
	private Map<String, ModelReaction> id2Reaction;
	
	private FbaExperiment experiment;
	
	public KBaseMetabolicModel() 
	{
		status = "";
		uuid = "";
		growth = "";
		defaultNameSpace = "";
		current = "";
		modDate = "";
		isPublic = "";
		id = "";
		mappingUUID = "";
		isLocked = "";
		version = "";
		name = "";
		biochemistryUUID = "";
		modelAnalysisUUID = "";
		type = "";
		annotationUUID = "";
		
		biomasses = new ArrayList<Biomass>();
		modelCompounds = new ArrayList<ModelCompound>();
		modelCompartments = new ArrayList<ModelCompartment>();
		modelReactions = new ArrayList<ModelReaction>();
		ancestorUUIDs = new ArrayList<String>();
		
		id2Biomass = new HashMap<String, Biomass>();
		id2Compound = new HashMap<String, ModelCompound>();
		id2Compartment = new HashMap<String, ModelCompartment>();
		id2Reaction = new HashMap<String, ModelReaction>();
	}
	
	@JsonProperty("status")
	public String getStatus() { return status; }
	@JsonProperty("status")
	public void setStatus(String status) { this.status = status; }

	@JsonProperty("uuid")
	public String getUUID() { return uuid; }
	@JsonProperty("uuid")
	public void setUUID(String uuid) { this.uuid = uuid; }

	@JsonProperty("growth")
	public String getGrowth() { return growth; }
	@JsonProperty("growth")
	public void setGrowth(String growth) { this.growth = growth; }

	@JsonProperty("defaultNameSpace")
	public String getDefaultNameSpace() { return defaultNameSpace; }
	@JsonProperty("defaultNameSpace")
	public void setDefaultNameSpace(String defaultNameSpace) { this.defaultNameSpace = defaultNameSpace; }

	@JsonProperty("current")
	public String getCurrent() { return current; }
	@JsonProperty("current")
	public void setCurrent(String current) { this.current = current; }

	@JsonProperty("modDate")
	public String getModDate() { return modDate; }
	@JsonProperty("modDate")
	public void setModDate(String modDate) { this.modDate = modDate; }

	@JsonProperty("public")
	public String getPublic() { return isPublic; }
	@JsonProperty("public")
	public void setPublic(String isPublic) { this.isPublic = isPublic; }

	@JsonProperty("id")
	public String getID() { return id; }
	@JsonProperty("id")
	public void setID(String id) { this.id = id; }

	@JsonProperty("mapping_uuid")
	public String getMappingUUID() { return mappingUUID; }
	@JsonProperty("mapping_uuid")
	public void setMappingUUID(String mappingUUID) { this.mappingUUID = mappingUUID; }

	@JsonProperty("locked")
	public String getLocked() { return isLocked; }
	@JsonProperty("locked")
	public void setLocked(String isLocked) { this.isLocked = isLocked; }

	@JsonProperty("version")
	public String getVersion() { return version; }
	@JsonProperty("version")
	public void setVersion(String version) { this.version = version; }

	@JsonProperty("name")
	public String getName() { return name; }
	@JsonProperty("name")
	public void setName(String name) { this.name = name; }

	@JsonProperty("biochemistry_uuid")
	public String getBiochemistryUUID() { return biochemistryUUID; }
	@JsonProperty("biochemistry_uuid")
	public void setBiochemistryUUID(String biochemistryUUID) { this.biochemistryUUID = biochemistryUUID; }

	@JsonProperty("modelanalysis_uuid")
	public String getModelAnalysisUUID() { return modelAnalysisUUID; }
	@JsonProperty("modelanalysis_uuid")
	public void setModelAnalysisUUID(String modelAnalysisUUID) { this.modelAnalysisUUID = modelAnalysisUUID; }

	@JsonProperty("type")
	public String getType() { return type; }
	@JsonProperty("type")
	public void setType(String type) { this.type = type; }

	@JsonProperty("annotation_uuid")
	public String getAnnotationUUID() { return annotationUUID; }
	@JsonProperty("annotation_uuid")
	public void setAnnotationUUID(String annotationUUID) { this.annotationUUID = annotationUUID; }

	@JsonProperty("biomasses")
	public List<Biomass> getBiomasses() { return biomasses; }
	@JsonProperty("biomasses")
	public void setBiomasses(List<Biomass> biomasses) { this.biomasses = biomasses; }

	@JsonProperty("modelcompounds")
	public List<ModelCompound> getModelCompounds() { return modelCompounds; }
	@JsonProperty("modelcompounds")
	public void setModelCompounds(List<ModelCompound> modelCompounds) { this.modelCompounds = modelCompounds; }

	@JsonProperty("modelcompartments")
	public List<ModelCompartment> getModelCompartments() { return modelCompartments; }
	@JsonProperty("modelcompartments")
	public void setModelCompartments(List<ModelCompartment> modelCompartments) { this.modelCompartments = modelCompartments; }

	@JsonProperty("ancestor_uuids")
	public List<String> getAncestorUUIDs() { return ancestorUUIDs; }
	@JsonProperty("ancestor_uuids")
	public void setAncestorUUIDs(List<String> ancestorUUIDs) { this.ancestorUUIDs = ancestorUUIDs; }

	@JsonProperty("modelreactions")
	public List<ModelReaction> getModelReactions() { return modelReactions; }
	@JsonProperty("modelreactions")
	public void setModelReactions(List<ModelReaction> modelReactions) { this.modelReactions = modelReactions; }
	
	public void process() {
		// turn Lists into Maps by UUID.
		id2Biomass = new HashMap<String, Biomass>();
		for (Biomass biomass : biomasses) {
			id2Biomass.put(biomass.getUUID(), biomass);
		}
		
		id2Compartment = new HashMap<String, ModelCompartment>();
		for (ModelCompartment c : modelCompartments) {
			id2Compartment.put(c.getUUID(), c);
		}

		id2Compound = new HashMap<String, ModelCompound>();
		for (ModelCompound c : modelCompounds) {
			id2Compound.put(c.getUUID(), c);
		}

		id2Reaction = new HashMap<String, ModelReaction>();
		for (ModelReaction r : modelReactions) {
			id2Reaction.put(r.getUUID(), r);
		}
	}
	
	public void setExperiment(FbaExperiment exp) { this.experiment = exp; }
	public FbaExperiment getExperiment() { return experiment; }
	
	public ModelReaction getReaction(String uuid) { return id2Reaction.get(uuid); }
	public ModelCompound getCompound(String uuid) { return id2Compound.get(uuid); }
	public ModelCompartment getCompartment(String uuid) { return id2Compartment.get(uuid); }
	public Biomass getBiomass(String uuid) { return id2Biomass.get(uuid); }
}