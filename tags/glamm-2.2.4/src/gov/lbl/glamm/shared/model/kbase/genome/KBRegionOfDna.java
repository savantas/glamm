package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBRegionOfDna {
	private String contigId;
	private int begin;
	private String strand;
	private int length;

	public KBRegionOfDna() {
		contigId = null;
		strand = null;
	}
	
	public KBRegionOfDna(List<Object> region) {
		if (region.size() != 4) 
			return;
		else {
			contigId = (String)region.get(0);
			begin = (Integer)region.get(1);
			strand = (String)region.get(2);
			length = (Integer)region.get(3);
		}
	}
	
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
