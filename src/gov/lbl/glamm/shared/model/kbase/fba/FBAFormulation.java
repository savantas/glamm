package gov.lbl.glamm.shared.model.kbase.fba;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FBAFormulation {
	private String mediaId;
	private String fbaModelId;
	private String regModelId;
	private String expressionDataId;
	private String objectiveStr;
	private float objective;
	private String description;
	private String type;
	private String uptakeLimits;
	private float objectiveConstraintFraction;
	private boolean allReversible;
	private float defaultMaxFlux;
	private float defaultMaxDrainFlux;
	private float defaultMinDrainFlux;
	private int numberOfSolutions;
	private boolean fva;
	private int comboDeletions;
	private boolean fluxMinimization;
	private boolean findMinimalMedia;
	private boolean simpleThermoConstraints;
	private boolean thermodynamicConstraints;
	private boolean noErrorThermodynamicConstraints;
	private boolean minimizeErrorThermodynamicConstraints;
	private List<String> featureKO; //feature_id
	private List<String> reactionKO; //modelreaction_id
	private List<String> constraints;
	private List<String> bounds;
	
	@JsonProperty("media_id")
	public String getMediaId() {
		return mediaId;
	}
	@JsonProperty("media_id")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@JsonProperty("fbamodel_id")
	public String getFbaModelId() {
		return fbaModelId;
	}
	@JsonProperty("fbamodel_id")
	public void setFbaModelId(String fbaModelId) {
		this.fbaModelId = fbaModelId;
	}
	
	@JsonProperty("regmodel_id")
	public String getRegModelId() {
		return regModelId;
	}
	@JsonProperty("regmodel_id")
	public void setRegModelId(String regModelId) {
		this.regModelId = regModelId;
	}
	
	@JsonProperty("expression_id")
	public String getExpressionDataId() {
		return expressionDataId;
	}
	@JsonProperty("expression_id")
	public void setExpressionDataId(String expressionDataId) {
		this.expressionDataId = expressionDataId;
	}
	
	@JsonProperty("objective")
	public String getObjectiveStr() {
		return objectiveStr;
	}
	@JsonProperty("objective")
	public void setObjectiveStr(String objectiveStr) {
		this.objectiveStr = objectiveStr;
	}

	@JsonProperty("objective")
	public float getObjective() {
		return objective;
	}
	@JsonProperty("objective")
	public void setObjective(float objective) {
		this.objective = objective;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonProperty("uptakelimits")
	public String getUptakeLimits() {
		return uptakeLimits;
	}
	@JsonProperty("uptakelimits")
	public void setUptakeLimits(String uptakeLimits) {
		this.uptakeLimits = uptakeLimits;
	}

	@JsonProperty("objectiveConstraintFraction")
	public float getObjectiveConstraintFraction() {
		return objectiveConstraintFraction;
	}
	@JsonProperty("objectiveConstraintFraction")
	public void setObjectiveConstraintFraction(float objectiveConstraintFraction) {
		this.objectiveConstraintFraction = objectiveConstraintFraction;
	}

	@JsonProperty("allReversible")
	public boolean isAllReversible() {
		return allReversible;
	}
	@JsonProperty("allReversible")
	public void setAllReversible(boolean allReversible) {
		this.allReversible = allReversible;
	}

	@JsonProperty("defaultMaxFlux")
	public float getDefaultMaxFlux() {
		return defaultMaxFlux;
	}
	@JsonProperty("defaultMaxFlux")
	public void setDefaultMaxFlux(float defaultMaxFlux) {
		this.defaultMaxFlux = defaultMaxFlux;
	}

	@JsonProperty("defaultMaxDrainFlux")
	public float getDefaultMaxDrainFlux() {
		return defaultMaxDrainFlux;
	}
	@JsonProperty("defaultMaxDrainFlux")
	public void setDefaultMaxDrainFlux(float defaultMaxDrainFlux) {
		this.defaultMaxDrainFlux = defaultMaxDrainFlux;
	}

	@JsonProperty("defaultMinDrainFlux")
	public float getDefaultMinDrainFlux() {
		return defaultMinDrainFlux;
	}
	@JsonProperty("defaultMinDrainFlux")
	public void setDefaultMinDrainFlux(float defaultMinDrainFlux) {
		this.defaultMinDrainFlux = defaultMinDrainFlux;
	}

	@JsonProperty("numberOfSolutions")
	public int getNumberOfSolutions() {
		return numberOfSolutions;
	}
	@JsonProperty("numberOfSolutions")
	public void setNumberOfSolutions(int numberOfSolutions) {
		this.numberOfSolutions = numberOfSolutions;
	}
	
	@JsonProperty("fva")
	public boolean isFva() {
		return fva;
	}
	@JsonProperty("fva")
	public void setFva(boolean fva) {
		this.fva = fva;
	}
	
	@JsonProperty("comboDeletions")
	public int getComboDeletions() {
		return comboDeletions;
	}
	@JsonProperty("comboDeletions")
	public void setComboDeletions(int comboDeletions) {
		this.comboDeletions = comboDeletions;
	}
	
	@JsonProperty("fluxMinimization")
	public boolean isFluxMinimization() {
		return fluxMinimization;
	}
	@JsonProperty("fluxMinimization")
	public void setFluxMinimization(boolean fluxMinimization) {
		this.fluxMinimization = fluxMinimization;
	}
	
	@JsonProperty("findMinimalMedia")
	public boolean isFindMinimalMedia() {
		return findMinimalMedia;
	}
	@JsonProperty("findMinimalMedia")
	public void setFindMinimalMedia(boolean findMinimalMedia) {
		this.findMinimalMedia = findMinimalMedia;
	}
	
	@JsonProperty("simpleThermoConstraints")
	public boolean isSimpleThermoConstraints() {
		return simpleThermoConstraints;
	}
	@JsonProperty("simpleThermoConstraints")
	public void setSimpleThermoConstraints(boolean simpleThermoConstraints) {
		this.simpleThermoConstraints = simpleThermoConstraints;
	}
	
	@JsonProperty("thermodynamicConstraints")
	public boolean isThermodynamicConstraints() {
		return thermodynamicConstraints;
	}
	@JsonProperty("thermodynamicConstraints")
	public void setThermodynamicConstraints(boolean thermodynamicConstraints) {
		this.thermodynamicConstraints = thermodynamicConstraints;
	}
	
	@JsonProperty("noErrorThermodynamicConstraints")
	public boolean isNoErrorThermodynamicConstraints() {
		return noErrorThermodynamicConstraints;
	}
	@JsonProperty("noErrorThermodynamicConstraints")
	public void setNoErrorThermodynamicConstraints(
			boolean noErrorThermodynamicConstraints) {
		this.noErrorThermodynamicConstraints = noErrorThermodynamicConstraints;
	}
	
	@JsonProperty("minimizeErrorThermodynamicConstraints")
	public boolean isMinimizeErrorThermodynamicConstraints() {
		return minimizeErrorThermodynamicConstraints;
	}
	@JsonProperty("minimizeErrorThermodynamicConstraints")
	public void setMinimizeErrorThermodynamicConstraints(
			boolean minimizeErrorThermodynamicConstraints) {
		this.minimizeErrorThermodynamicConstraints = minimizeErrorThermodynamicConstraints;
	}
	
	@JsonProperty("featureKO")
	public List<String> getFeatureKO() {
		return featureKO;
	}
	@JsonProperty("featureKO")
	public void setFeatureKO(List<String> featureKO) {
		this.featureKO = featureKO;
	}
	
	@JsonProperty("reactionKO")
	public List<String> getReactionKO() {
		return reactionKO;
	}
	@JsonProperty("reactionKO")
	public void setReactionKO(List<String> reactionKO) {
		this.reactionKO = reactionKO;
	}
	
	@JsonProperty("constraints")
	public List<String> getConstraints() {
		return constraints;
	}
	@JsonProperty("constraints")
	public void setConstraints(List<String> constraints) {
		this.constraints = constraints;
	}
	
	@JsonProperty("bounds")
	public List<String> getBounds() {
		return bounds;
	}
	@JsonProperty("bounds")
	public void setBounds(List<String> bounds) {
		this.bounds = bounds;
	}
}
