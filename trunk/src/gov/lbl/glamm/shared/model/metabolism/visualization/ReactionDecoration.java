package gov.lbl.glamm.shared.model.metabolism.visualization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactionDecoration {
	private ElementDecoration globalDecoration,
							  annotationDecoration,
							  gapFillDecoration,
							  growMatchDecoration;
	
	public ReactionDecoration() 
	{ 
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
		globalDecoration.setColor(shape);
	}
	@JsonProperty("shape")
	public String getGlobalShape() {
		return globalDecoration.getShape();
	}
	
	@JsonProperty("annotationReactions")
	public void setAnnotationDecoration(ElementDecoration annotationDecoration) { 
		this.annotationDecoration = annotationDecoration;
	}
	@JsonProperty("annotationReactions")
	public ElementDecoration getAnnotationDecoration() { return annotationDecoration; }
	
	@JsonProperty("gapFillReactions")
	public void setGapFillDecoration(ElementDecoration gapFillDecoration) {
		this.gapFillDecoration = gapFillDecoration;
	}
	@JsonProperty("gapFillReactions")
	public ElementDecoration getGapFillDecoration() { return gapFillDecoration; }
	
	@JsonProperty("growMatchReactions")
	public void setGrowMatchDecoration(ElementDecoration growMatchDecoration) {
		this.growMatchDecoration = growMatchDecoration;
	}
	@JsonProperty("growMatchReactions")
	public ElementDecoration getGrowMatchDecoration() { return growMatchDecoration; }

}
