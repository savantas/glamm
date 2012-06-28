package gov.lbl.glamm.server.kbase;

import gov.lbl.glamm.client.model.MetabolicModel;

import java.util.HashMap;
import java.util.Map;

public class KbaseService {

	@SuppressWarnings("unused")
	private static final String KBASE_URL = "http://bio-data-1.mcs.anl.gov/services/cdmi_api";
	
	public static Map<String, String> getMetabolicModelIds() {
		Map<String, String> modelInfo = new HashMap<String, String>();
		
		return modelInfo;
	}
	
	public static MetabolicModel getModelFromId(String modelId) {
		return null;
	}
	
}
