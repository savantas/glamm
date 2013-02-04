package gov.lbl.glamm.shared.model.kbase.fba.model;

import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Reaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class KBMetabolicModel implements Serializable {
	
	private String id;
	private String name;
	private String workspace;
	private String genomeName;
	private String genomeWorkspace;
	private String mapName;
	private String mapWorkspace;
	private String biochemistryName;
	private String biochemistryWorkspace;
	private String type;
	private String status;
	private List<KBModelBiomass> biomassesList;
	private List<KBModelCompartment> compartments;
	private Map<String, Compound> compoundMap;
	private Map<String, Reaction> reactionMap;
	
	public KBMetabolicModel() {
		biomassesList = new ArrayList<KBModelBiomass>();
		compartments = new ArrayList<KBModelCompartment>();
		compoundMap = new HashMap<String, Compound>();
		reactionMap = new HashMap<String, Reaction>();
	}
	
	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getGenomeName() {
		return genomeName;
	}

	public void setGenomeName(String genomeName) {
		this.genomeName = genomeName;
	}

	public String getGenomeWorkspace() {
		return genomeWorkspace;
	}

	public void setGenomeWorkspace(String genomeWorkspace) {
		this.genomeWorkspace = genomeWorkspace;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getMapWorkspace() {
		return mapWorkspace;
	}

	public void setMapWorkspace(String mapWorkspace) {
		this.mapWorkspace = mapWorkspace;
	}

	public String getBiochemistryName() {
		return biochemistryName;
	}

	public void setBiochemistryName(String biochemistryName) {
		this.biochemistryName = biochemistryName;
	}

	public String getBiochemistryWorkspace() {
		return biochemistryWorkspace;
	}

	public void setBiochemistryWorkspace(String biochemistryWorkspace) {
		this.biochemistryWorkspace = biochemistryWorkspace;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<KBModelBiomass> getBiomassesList() {
		return biomassesList;
	}

	public void setBiomassesList(List<KBModelBiomass> biomassesList) {
		this.biomassesList = biomassesList;
	}

	public List<KBModelCompartment> getCompartments() {
		return compartments;
	}

	public void setCompartments(List<KBModelCompartment> compartments) {
		this.compartments = compartments;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Compound> getCompounds() {
		Set<Compound> cpdSet = new HashSet<Compound>(compoundMap.values());
		return cpdSet;
	}
	
	public Set<Reaction> getReactions() {
		Set<Reaction> rxnSet = new HashSet<Reaction>(reactionMap.values());
		return rxnSet;
	}
	
	public void addCompounds(Collection<Compound> compounds) {
		for (Compound cpd : compounds) {
			if (cpd != null && (cpd.getGuid() != null && cpd.getGuid().length() > 0))
				compoundMap.put(cpd.getGuid(), cpd);
		}
	}
	
	public void addReactions(Collection<Reaction> reactions) {
		for (Reaction rxn : reactions) {
			if (rxn != null && (rxn.getGuid() != null && rxn.getGuid().length() > 0))
				reactionMap.put(rxn.getGuid(), rxn);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
}
