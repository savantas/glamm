package gov.lbl.glamm.shared.model.kbase.genome;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Annotation {
	private String comment;
	private String annotator;
	private int annotationTime;
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
	@JsonProperty("comment")
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@JsonProperty("annotator")
	public String getAnnotator() {
		return annotator;
	}
	@JsonProperty("annotator")
	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}
	
	@JsonProperty("annotation_time")
	public int getAnnotationTime() {
		return annotationTime;
	}
	@JsonProperty("annotation_time")
	public void setAnnotationTime(int annotationTime) {
		this.annotationTime = annotationTime;
	}
}
