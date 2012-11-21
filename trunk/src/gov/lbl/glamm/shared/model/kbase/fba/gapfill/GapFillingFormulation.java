package gov.lbl.glamm.shared.model.kbase.fba.gapfill;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GapFillingFormulation {
	private String mediaId;
	private String notes;
	private String objective;
	private float objFraction;
	private String rxnKo;
	private String uptakeLim;
	private float defaultMaxFlux;
	private float defaultMaxUptake;
	private float defaultMinUptake;
	private boolean noMediaHyp;
	private boolean noBiomassHyp;
	private boolean noGprHyp;
	private boolean noPathwayHyp;
	private boolean allowUnbalanced;
	private float activityBonus;
	private float drainPen;
	private float noStructPen;
	private float unfavorablePen;
	private float noDeltaGPen;
	private float biomassTransPen;
	private float singleTransPen;
	private float transPen;
	private String blacklistedRxns;
	private String guaranteedRxns;
	private String allowedCmps;
	private String probabilisticAnnotation;
	
	@JsonProperty("media_id")
	public String getMediaId() {
		return mediaId;
	}
	@JsonProperty("media_id")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@JsonProperty("notes")
	public String getNotes() {
		return notes;
	}
	@JsonProperty("notes")
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@JsonProperty("objective")
	public String getObjective() {
		return objective;
	}
	@JsonProperty("objective")
	public void setObjective(String objective) {
		this.objective = objective;
	}
	
	@JsonProperty("objfraction")
	public float getObjFraction() {
		return objFraction;
	}
	@JsonProperty("objfraction")
	public void setObjFraction(float objFraction) {
		this.objFraction = objFraction;
	}
	
	@JsonProperty("rxnko")
	public String getRxnKo() {
		return rxnKo;
	}
	@JsonProperty("rxnko")
	public void setRxnKo(String rxnKo) {
		this.rxnKo = rxnKo;
	}
	
	@JsonProperty("uptakelim")
	public String getUptakeLim() {
		return uptakeLim;
	}
	@JsonProperty("uptakelim")
	public void setUptakeLim(String uptakeLim) {
		this.uptakeLim = uptakeLim;
	}
	
	@JsonProperty("defaultmaxflux")
	public float getDefaultMaxFlux() {
		return defaultMaxFlux;
	}
	@JsonProperty("defaultmaxflux")
	public void setDefaultMaxFlux(float defaultMaxFlux) {
		this.defaultMaxFlux = defaultMaxFlux;
	}
	
	@JsonProperty("defaultmaxuptake")
	public float getDefaultMaxUptake() {
		return defaultMaxUptake;
	}
	@JsonProperty("defaultmaxuptake")
	public void setDefaultMaxUptake(float defaultMaxUptake) {
		this.defaultMaxUptake = defaultMaxUptake;
	}
	
	@JsonProperty("defaultminuptake")
	public float getDefaultMinUptake() {
		return defaultMinUptake;
	}
	@JsonProperty("defaultminuptake")
	public void setDefaultMinUptake(float defaultMinUptake) {
		this.defaultMinUptake = defaultMinUptake;
	}
		
	@JsonProperty("nomediahyp")
	public boolean isNoMediaHyp() {
		return noMediaHyp;
	}
	@JsonProperty("nomediahyp")
	public void setNoMediaHyp(boolean noMediaHyp) {
		this.noMediaHyp = noMediaHyp;
	}
	
	@JsonProperty("nobiomasshyp")
	public boolean isNoBiomassHyp() {
		return noBiomassHyp;
	}
	@JsonProperty("nobiomasshyp")
	public void setNoBiomassHyp(boolean noBiomassHyp) {
		this.noBiomassHyp = noBiomassHyp;
	}
	
	@JsonProperty("nogprhyp")
	public boolean isNoGprHyp() {
		return noGprHyp;
	}
	@JsonProperty("nogprhyp")
	public void setNoGprHyp(boolean noGprHyp) {
		this.noGprHyp = noGprHyp;
	}
	
	@JsonProperty("nopathwayhyp")
	public boolean isNoPathwayHyp() {
		return noPathwayHyp;
	}
	@JsonProperty("nopathwayhyp")
	public void setNoPathwayHyp(boolean noPathwayHyp) {
		this.noPathwayHyp = noPathwayHyp;
	}
	
	@JsonProperty("allowunbalanced")
	public boolean isAllowUnbalanced() {
		return allowUnbalanced;
	}
	@JsonProperty("allowunbalanced")
	public void setAllowUnbalanced(boolean allowUnbalanced) {
		this.allowUnbalanced = allowUnbalanced;
	}
	
	@JsonProperty("activitybonus")
	public float getActivityBonus() {
		return activityBonus;
	}
	@JsonProperty("activitybonus")
	public void setActivityBonus(float activityBonus) {
		this.activityBonus = activityBonus;
	}
	
	@JsonProperty("drainpen")
	public float getDrainPen() {
		return drainPen;
	}
	@JsonProperty("drainpen")
	public void setDrainPen(float drainPen) {
		this.drainPen = drainPen;
	}

	@JsonProperty("nostructpen")
	public float getNoStructPen() {
		return noStructPen;
	}
	@JsonProperty("nostructpen")
	public void setNoStructPen(float noStructPen) {
		this.noStructPen = noStructPen;
	}
	
	@JsonProperty("unfavorablepen")
	public float getUnfavorablePen() {
		return unfavorablePen;
	}
	@JsonProperty("unfavorablepen")
	public void setUnfavorablePen(float unfavorablePen) {
		this.unfavorablePen = unfavorablePen;
	}
	
	@JsonProperty("nodeltagpen")
	public float getNoDeltaGPen() {
		return noDeltaGPen;
	}
	@JsonProperty("nodeltagpen")
	public void setNoDeltaGPen(float noDeltaGPen) {
		this.noDeltaGPen = noDeltaGPen;
	}
	
	@JsonProperty("biomasstranspen")
	public float getBiomassTransPen() {
		return biomassTransPen;
	}
	@JsonProperty("biomasstranspen")
	public void setBiomassTransPen(float biomassTransPen) {
		this.biomassTransPen = biomassTransPen;
	}

	@JsonProperty("singletranspen")
	public float getSingleTransPen() {
		return singleTransPen;
	}
	@JsonProperty("singletranspen")
	public void setSingleTransPen(float singleTransPen) {
		this.singleTransPen = singleTransPen;
	}
	
	@JsonProperty("transpen")
	public float getTransPen() {
		return transPen;
	}
	@JsonProperty("transpen")
	public void setTransPen(float transPen) {
		this.transPen = transPen;
	}
	
	@JsonProperty("blacklistedrxns")
	public String getBlacklistedRxns() {
		return blacklistedRxns;
	}
	@JsonProperty("blacklistedrxns")
	public void setBlacklistedRxns(String blacklistedRxns) {
		this.blacklistedRxns = blacklistedRxns;
	}
	
	@JsonProperty("gauranteedrxns")
	public String getGuaranteedRxns() {
		return guaranteedRxns;
	}
	@JsonProperty("gauranteedrxns")
	public void setGuaranteedRxns(String guaranteedRxns) {
		this.guaranteedRxns = guaranteedRxns;
	}
	
	@JsonProperty("allowedcmps")
	public String getAllowedCmps() {
		return allowedCmps;
	}
	@JsonProperty("allowedcmps")
	public void setAllowedCmps(String allowedCmps) {
		this.allowedCmps = allowedCmps;
	}
	
	@JsonProperty("probabilistic_annotation")
	public String getProbabilisticAnnotation() {
		return probabilisticAnnotation;
	}
	@JsonProperty("probabilistic_annotation")
	public void setProbabilisticAnnotation(String probabilisticAnnotation) {
		this.probabilisticAnnotation = probabilisticAnnotation;
	}
}
