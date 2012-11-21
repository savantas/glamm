package gov.lbl.glamm.shared.model.metabolism.deprecated;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperimentDecoration {

	public enum GradientColor {
		BLUE_YELLOW("blue-yellow"),
		RED_GREEN("red-green");
		
		private String name;
		
		GradientColor(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static GradientColor toGradient(String s) {
			s = s.toLowerCase();
			for (GradientColor color : GradientColor.values()) {
				if (color.getName().equals(s))
					return color;
			}
			
			return BLUE_YELLOW;
		}
	}
	
	private GradientColor reactionGradient,
				   		  compoundGradient;
	
	public ExperimentDecoration() {
		reactionGradient = GradientColor.BLUE_YELLOW;
		compoundGradient = GradientColor.BLUE_YELLOW;
	}
	
	@JsonProperty("reactions")
	public void setReactionGradient(String gradient) {
		reactionGradient = GradientColor.toGradient(gradient);
	}
	@JsonProperty("reactions")
	public GradientColor getReactionGradient() {
		return reactionGradient;
	}
	
	@JsonProperty("compounds")
	public void setCompoundGradient(String gradient) {
		compoundGradient = GradientColor.toGradient(gradient);
	}
	@JsonProperty("compounds")
	public GradientColor getCompoundGradient() {
		return compoundGradient;
	}
}
