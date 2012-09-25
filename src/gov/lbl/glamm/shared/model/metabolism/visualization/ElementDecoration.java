package gov.lbl.glamm.shared.model.metabolism.visualization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElementDecoration {

	private String color,
				   shape;
	
	public ElementDecoration() {
		color = "";
		shape = "";
	}
	
	@JsonProperty("color")
	public void setColor(String color) { this.color = color; }
	@JsonProperty("color")
	public String getColor() { return color; }
	
	@JsonProperty("shape")
	public void setShape(String shape) { this.shape = shape; }
	@JsonProperty("shape")
	public String getShape() { return shape; }
}
