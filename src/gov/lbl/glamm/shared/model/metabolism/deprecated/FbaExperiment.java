package gov.lbl.glamm.shared.model.metabolism.deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FbaExperiment {

	private String fbaFormulationUUID,
				   objectiveValue,
				   name,
				   modDate,
				   uuid,
				   resultNotes;
	private List<FbaBiomassVariables> biomassVars;
	private List<FbaReactionVariables> reactionVars;
	private List<FbaCompoundVariables> compoundVars;
	
	private Map<String, FbaBiomassVariables> id2BiomassVars;
	private Map<String, FbaReactionVariables> id2ReactionVars;
	private Map<String, FbaCompoundVariables> id2CompoundVars;
	
	public FbaExperiment() {
		fbaFormulationUUID = null;
		objectiveValue = null;
		name = null;
		modDate = null;
		uuid = null;
		resultNotes = null;
		
		biomassVars = new ArrayList<FbaBiomassVariables>();
		reactionVars = new ArrayList<FbaReactionVariables>();
		compoundVars = new ArrayList<FbaCompoundVariables>();
		
		id2BiomassVars = new HashMap<String, FbaBiomassVariables>();
		id2ReactionVars = new HashMap<String, FbaReactionVariables>();
		id2CompoundVars = new HashMap<String, FbaCompoundVariables>();
	}
	
	@JsonProperty("fbaformulation_uuid")
	public String getFbaFormulationUUID() { return fbaFormulationUUID; }
	@JsonProperty("fbaformulation_uuid")
	public void setFbaFormulationUUID(String fbaFormulationUUID) { this.fbaFormulationUUID = fbaFormulationUUID; }
	
	@JsonProperty("objectiveValue")
	public String getObjectiveValue() { return objectiveValue; }
	@JsonProperty("objectiveValue")
	public void setObjectiveValue(String objectiveValue) { this.objectiveValue = objectiveValue; }
	
	@JsonProperty("name")
	public String getName() { return name; }
	@JsonProperty("name")
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("modDate")
	public String getModDate() { return modDate; }
	@JsonProperty("modDate")
	public void setModDate(String modDate) { this.modDate = modDate; }
	
	@JsonProperty("uuid")
	public String getUUID() { return uuid; }
	@JsonProperty("uuid")
	public void setUUID(String uuid) { this.uuid = uuid; }
	
	@JsonProperty("resultNotes")
	public String getResultNotes() { return resultNotes; }
	@JsonProperty("resultNotes")
	public void setResultNotes(String resultNotes) { this.resultNotes = resultNotes; }
	
	@JsonProperty("fbaBiomassVariables")
	public List<FbaBiomassVariables> getFbaBiomassVariables() { return biomassVars; }
	@JsonProperty("fbaBiomassVariables")
	public void setFbaBiomassVariables(List<FbaBiomassVariables> biomassVars) { this.biomassVars = biomassVars; }
	
	@JsonProperty("fbaReactionVariables")
	public List<FbaReactionVariables> getFbaReactionVariables() { return reactionVars; }
	@JsonProperty("fbaReactionVariables")
	public void setFbaReactionVariables(List<FbaReactionVariables> reactionVars) { this.reactionVars = reactionVars; }
	
	@JsonProperty("fbaCompoundVariables")
	public List<FbaCompoundVariables> getFbaCompoundVariables() { return compoundVars; }
	@JsonProperty("fbaCompoundVariables")
	public void setFbaCompoundVariables(List<FbaCompoundVariables> compoundVars) { this.compoundVars = compoundVars; }
	
	public void process() {
		id2ReactionVars = new HashMap<String, FbaReactionVariables>();
		for (FbaReactionVariables r : reactionVars) {
			id2ReactionVars.put(r.getReactionUUID(), r);
		}

		id2CompoundVars = new HashMap<String, FbaCompoundVariables>();
		for (FbaCompoundVariables c : compoundVars) {
			id2CompoundVars.put(c.getCompoundUUID(), c);
		}
		
		id2BiomassVars = new HashMap<String, FbaBiomassVariables>();
		for (FbaBiomassVariables b : biomassVars) {
			id2BiomassVars.put(b.getBiomassUUID(), b);
		}
	}
	
	public FbaReactionVariables getReactionVariables(String uuid) { return id2ReactionVars.get(uuid); }
	public FbaCompoundVariables getCompoundVariables(String uuid) { return id2CompoundVars.get(uuid); }
	public FbaBiomassVariables getBiomassVariables(String uuid) { return id2BiomassVars.get(uuid); }
}
