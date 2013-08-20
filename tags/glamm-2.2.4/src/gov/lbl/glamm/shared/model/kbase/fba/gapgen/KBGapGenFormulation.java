package gov.lbl.glamm.shared.model.kbase.fba.gapgen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBGapGenFormulation {
	private String mediaId;
	private String refMedia;
	private String notes;
	private String objective;
	private float objFraction;
	private String rxnKo;
	private String geneKo;
	private String uptakeLim;
	private float defaultMaxFlux;
	private float defaultMaxUptake;
	private float defaultMinUptake;
	private boolean noMediaHyp;
	private boolean noBiomassHyp;
	private boolean noGprHyp;
	private boolean noPathwayHyp;
	
	
	@JsonProperty("media")
	public String getMediaId() {
		return mediaId;
	}
	@JsonProperty("media")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@JsonProperty("refmedia")
	public String getRefMedia() {
		return refMedia;
	}
	@JsonProperty("refmedia")
	public void setRefMedia(String refMedia) {
		this.refMedia = refMedia;
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
	
	@JsonProperty("geneko")
	public String getGeneKo() {
		return geneKo;
	}
	@JsonProperty("geneko")
	public void setGeneKo(String geneKo) {
		this.geneKo = geneKo;
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
}
