package gov.lbl.glamm.shared.model.kbase.genome;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KBFeature {
	private String featureId;
	private KBLocation location;
	private String featureType;
	private String function;
	private List<KBAlternativeFunction> alternativeFunctions;
	private String proteinTranslation;
	private List<String> aliases;
	private List<KBAnnotation> annotations;
	
	@JsonProperty("id")
	public String getFeatureId() {
		return featureId;
	}
	@JsonProperty("id")
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	@JsonProperty("location")
	public KBLocation getLocation() {
		return location;
	}
	@JsonProperty("location")
	public void setLocation(KBLocation location) {
		this.location = location;
	}
	
	@JsonProperty("type")
	public String getFeatureType() {
		return featureType;
	}
	@JsonProperty("type")
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	@JsonProperty("function")
	public String getFunction() {
		return function;
	}
	@JsonProperty("function")
	public void setFunction(String function) {
		this.function = function;
	}

	@JsonProperty("alternative_function")
	public List<KBAlternativeFunction> getAlternativeFunctions() {
		return alternativeFunctions;
	}
	@JsonProperty("alternative_function")
	public void setAlternativeFunctions(
			List<KBAlternativeFunction> alternativeFunctions) {
		this.alternativeFunctions = alternativeFunctions;
	}
	
	@JsonProperty("protein_translation")
	public String getProteinTranslation() {
		return proteinTranslation;
	}
	@JsonProperty("protein_translation")
	public void setProteinTranslation(String proteinTranslation) {
		this.proteinTranslation = proteinTranslation;
	}

	@JsonProperty("aliases")
	public List<String> getAliases() {
		return aliases;
	}
	@JsonProperty("aliases")
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	@JsonProperty("annotations")
	public List<KBAnnotation> getAnnotations() {
		return annotations;
	}
	@JsonProperty("annotations")
	public void setAnnotations(List<KBAnnotation> annotations) {
		this.annotations = annotations;
	}
}
