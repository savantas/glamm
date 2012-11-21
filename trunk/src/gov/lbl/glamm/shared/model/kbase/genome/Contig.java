package gov.lbl.glamm.shared.model.kbase.genome;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contig {
	private String contigId;
	private String dna;
	
	@JsonProperty("id")
	public String getContigId() {
		return contigId;
	}
	@JsonProperty("id")
	public void setContigId(String contigId) {
		this.contigId = contigId;
	}
	
	@JsonProperty("dna")
	public String getDna() {
		return dna;
	}
	@JsonProperty("dna")
	public void setDna(String dna) {
		this.dna = dna;
	}
}
