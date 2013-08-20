package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBGenomeObject {
	private String scientificName;
	private String domain;
	private int geneticCode;
	private String source;
	private String sourceId;
	private List<KBContig> contigs;
	private List<KBFeature> features;
	
	@JsonProperty("scientific_name")
	public String getScientificName() {
		return scientificName;
	}
	@JsonProperty("scientific_name")
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	
	@JsonProperty("domain")
	public String getDomain() {
		return domain;
	}
	@JsonProperty("domain")
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@JsonProperty("genetic_code")
	public int getGeneticCode() {
		return geneticCode;
	}
	@JsonProperty("genetic_code")
	public void setGeneticCode(int geneticCode) {
		this.geneticCode = geneticCode;
	}

	@JsonProperty("source")
	public String getSource() {
		return source;
	}
	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
	}
	
	@JsonProperty("source_id")
	public String getSourceId() {
		return sourceId;
	}
	@JsonProperty("source_id")
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@JsonProperty("contigs")
	public List<KBContig> getContigs() {
		return contigs;
	}
	@JsonProperty("contigs")
	public void setContigs(List<KBContig> contigs) {
		this.contigs = contigs;
	}

	@JsonProperty("features")
	public List<KBFeature> getFeatures() {
		return features;
	}
	@JsonProperty("features")
	public void setFeatures(List<KBFeature> features) {
		this.features = features;
	}
}