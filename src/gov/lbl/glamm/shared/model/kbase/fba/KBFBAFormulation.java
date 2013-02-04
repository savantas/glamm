package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class KBFBAFormulation implements Serializable {    
	private String media;
	private List<String> additionalcpds;
	private String prommodel;
	private String prommodel_workspace;
	private String media_workspace;
	private Float objfraction;
	private boolean allreversible;
	private boolean maximizeObjective;
	private List<KBFBAConstraintTerm> objectiveTerms;
	private List<String> geneko;
	private List<String> rxnko;
	private List<KBBound> bounds;
	private List<KBFBAConstraint> constraints;
	private Map<String, Float> uptakelim;
	private Float defaultmaxflux;
	private Float defaultminuptake;
	private Float defaultmaxuptake;
	private boolean simplethermoconst;
	private boolean thermoconst;
	private boolean nothermoerror;
	private boolean minthermoerror;

	public KBFBAFormulation() {
	}
	
	public String getMediaId() {
		return media;
	}
	public void setMediaId(String media) {
		this.media = media;
	}
	public List<String> getAdditionalCompounds() {
		return additionalcpds;
	}
	public void setAdditionalCompounds(List<String> additionalcpds) {
		this.additionalcpds = additionalcpds;
	}
	public String getPromModelId() {
		return prommodel;
	}
	public void setPromModelId(String prommodel) {
		this.prommodel = prommodel;
	}
	public String getPromModelWorkspace() {
		return prommodel_workspace;
	}
	public void setPromModelWorkspace(String prommodel_workspace) {
		this.prommodel_workspace = prommodel_workspace;
	}
	public String getMediaWorkspace() {
		return media_workspace;
	}
	public void setMediaWorkspace(String media_workspace) {
		this.media_workspace = media_workspace;
	}
	public Float getObjectiveFraction() {
		return objfraction;
	}
	public void setObjectiveFraction(Float objfraction) {
		this.objfraction = objfraction;
	}
	public boolean getAllReversible() {
		return allreversible;
	}
	public void setAllReversible(boolean allreversible) {
		this.allreversible = allreversible;
	}
	public boolean maximizeObjective() {
		return maximizeObjective;
	}
	public void setMaximizeObjective(boolean maximizeObjective) {
		this.maximizeObjective = maximizeObjective;
	}
	public List<KBFBAConstraintTerm> getObjectiveTerms() {
		return objectiveTerms;
	}
	public void setObjectiveTerms(List<KBFBAConstraintTerm> objectiveTerms) {
		this.objectiveTerms = objectiveTerms;
	}
	public List<String> getGeneKO() {
		return geneko;
	}
	public void setGeneKO(List<String> geneko) {
		this.geneko = geneko;
	}
	public List<String> getReactionKO() {
		return rxnko;
	}
	public void setReactionKO(List<String> rxnko) {
		this.rxnko = rxnko;
	}
	public List<KBBound> getBounds() {
		return bounds;
	}
	public void setBounds(List<KBBound> bounds) {
		this.bounds = bounds;
	}
	public List<KBFBAConstraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<KBFBAConstraint> constraints) {
		this.constraints = constraints;
	}
	public Map<String, Float> getUptakeLimits() {
		return uptakelim;
	}
	public void setUptakeLimits(Map<String, Float> uptakelim) {
		this.uptakelim = uptakelim;
	}
	public Float getDefaultMaxFlux() {
		return defaultmaxflux;
	}
	public void setDefaultMaxFlux(Float defaultmaxflux) {
		this.defaultmaxflux = defaultmaxflux;
	}
	public Float getDefaultMinUptake() {
		return defaultminuptake;
	}
	public void setDefaultMinUptake(Float defaultminuptake) {
		this.defaultminuptake = defaultminuptake;
	}
	public Float getDefaultMaxUptake() {
		return defaultmaxuptake;
	}
	public void setDefaultMaxUptake(Float defaultmaxuptake) {
		this.defaultmaxuptake = defaultmaxuptake;
	}
	public boolean useSimpleThermodynamicConstraints() {
		return simplethermoconst;
	}
	public void setUseSimpleThermodynamicConstraints(boolean simplethermoconst) {
		this.simplethermoconst = simplethermoconst;
	}
	public boolean useThermodynamicConstraints() {
		return thermoconst;
	}
	public void setUseThermodynamicConstraints(boolean thermoconst) {
		this.thermoconst = thermoconst;
	}
	public boolean getNoThermodynamicsError() {
		return nothermoerror;
	}
	public void setNoThermodynamicsError(boolean nothermoerror) {
		this.nothermoerror = nothermoerror;
	}
	public boolean getMinThermodynamicsError() {
		return minthermoerror;
	}
	public void setMinThermodynamicsError(boolean minthermoerror) {
		this.minthermoerror = minthermoerror;
	}



}
