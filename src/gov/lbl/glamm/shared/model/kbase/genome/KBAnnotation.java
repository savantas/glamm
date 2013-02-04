package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBAnnotation {
	private String comment;
	private String annotator;
	private int annotationTime;
	
	// Stupid JSON tricks (I hope!)
	public KBAnnotation(List<Object> annotation) {
		if (annotation.size() != 3)
			return;
		else {
			comment = (String)annotation.get(0);
			annotator = (String)annotation.get(1);
			annotationTime = (Integer)annotation.get(2);
		}
	}
	
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
