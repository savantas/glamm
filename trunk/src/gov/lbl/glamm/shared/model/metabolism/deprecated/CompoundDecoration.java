package gov.lbl.glamm.shared.model.metabolism.deprecated;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompoundDecoration {
	
	private ElementDecoration globalDecoration,
							  annotationDecoration,
							  gapFillDecoration,
							  growMatchDecoration;
	
	
	public CompoundDecoration() { 
		globalDecoration = new ElementDecoration();
		annotationDecoration = new ElementDecoration();
		gapFillDecoration = new ElementDecoration();
		growMatchDecoration = new ElementDecoration();
	}
	
	@JsonProperty("color")
	public void setGlobalColor(String color) {
		globalDecoration.setColor(color);
	}
	@JsonProperty("color")
	public String getGlobalColor() {
		return globalDecoration.getColor();
	}
	
	@JsonProperty("shape")
	public void setGlobalShape(String shape) {
		globalDecoration.setShape(shape);
	}
	@JsonProperty("shape")
	public String getGlobalShape() {
		return globalDecoration.getShape();
	}
	
	@JsonProperty("annotationCompounds")
	public void setAnnotationDecoration(ElementDecoration annotationDecoration) {
		this.annotationDecoration = annotationDecoration;
	}
	@JsonProperty("annotationCompounds")
	public ElementDecoration getAnnotationDecoration() {
		return annotationDecoration;
	}
	
	@JsonProperty("gapFillCompounds")
	public void setGapFillDecoration(ElementDecoration gapFillDecoration) {
		this.gapFillDecoration = gapFillDecoration;
	}
	@JsonProperty("gapFillCompounds")
	public ElementDecoration getGapFillDecoration() {
		return gapFillDecoration;
	}
	
	@JsonProperty("growMatchCompounds")
	public void setGrowMatchDecoration(ElementDecoration growMatchDecoration) {
		this.growMatchDecoration = growMatchDecoration;
	}
	@JsonProperty("growMatchCompounds")
	public ElementDecoration getGrowMatchDecoration() {
		return growMatchDecoration;
	}
}
