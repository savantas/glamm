package gov.lbl.glamm.shared.model.kbase.fba;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class KBMinimalMediaPrediction implements Serializable {
	private List<String> optionalNutrients; //compound_id
	private List<String> essentialNutrients; //compound_id
	
	@JsonProperty("optionalNutrients")
	public List<String> getOptionalNutrients() {
		return optionalNutrients;
	}
	@JsonProperty("optionalNutrients")
	public void setOptionalNutrients(List<String> optionalNutrients) {
		this.optionalNutrients = optionalNutrients;
	}
	
	@JsonProperty("essentialNutrients")
	public List<String> getEssentialNutrients() {
		return essentialNutrients;
	}
	@JsonProperty("essentialNutrients")
	public void setEssentialNutrients(List<String> essentialNutrients) {
		this.essentialNutrients = essentialNutrients;
	}
}
