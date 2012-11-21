package gov.lbl.glamm.shared.model.kbase.genome;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionOfDna {
	private String contigId;
	private int begin;
	private String strand;
	private int length;
	
	@JsonProperty("contig_id")
	public String getContigId() {
		return contigId;
	}
	@JsonProperty("contig_id")
	public void setContigId(String contigId) {
		this.contigId = contigId;
	}
	
	@JsonProperty("begin")
	public int getBegin() {
		return begin;
	}
	@JsonProperty("begin")
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	@JsonProperty("strand")
	public String getStrand() {
		return strand;
	}
	@JsonProperty("strand")
	public void setStrand(String strand) {
		this.strand = strand;
	}

	@JsonProperty("length")
	public int getLength() {
		return length;
	}
	@JsonProperty("length")
	public void setLength(int length) {
		this.length = length;
	}
}
