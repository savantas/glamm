package gov.lbl.glamm.server.kbase;

import gov.lbl.glamm.shared.model.kbase.biochemistry.Biochemistry;
import gov.lbl.glamm.shared.model.kbase.biochemistry.Compound;
import gov.lbl.glamm.shared.model.kbase.biochemistry.Media;
import gov.lbl.glamm.shared.model.kbase.biochemistry.Reaction;
import gov.lbl.glamm.shared.model.kbase.fba.FBA;
import gov.lbl.glamm.shared.model.kbase.fba.FBAFormulation;
import gov.lbl.glamm.shared.model.kbase.fba.gapfill.GapFill;
import gov.lbl.glamm.shared.model.kbase.fba.gapfill.GapFillingFormulation;
import gov.lbl.glamm.shared.model.kbase.fba.gapgen.GapGen;
import gov.lbl.glamm.shared.model.kbase.fba.gapgen.GapGenFormulation;
import gov.lbl.glamm.shared.model.kbase.fba.model.FBAModel;
import gov.lbl.glamm.shared.model.kbase.genome.Genome;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MetabolismService {
	private String serviceURL;
	private String version;
	private String servicePrefix = "fba";

	public class ServiceResponse {
		String version;
		List<String> result;
		
		public ServiceResponse() {
		}
		
		@JsonProperty("version")
		public void setVersion(String version) {
			this.version = version;
		}
		
		@JsonProperty("version")
		public String getVersion() {
			return version;
		}
		
		@JsonProperty("result")
		public void setResult(List<String> result) {
			this.result = result;
		}
		
		@JsonProperty("result")
		public List<String> getResult() {
			return result;
		}
	}
	
	public MetabolismService(String serviceURL, String version) {
		this.serviceURL = serviceURL;
		this.version = version;
	}
	
	public MetabolismService() {
		serviceURL = "http://www.kbase.us/services/cdmi_api";
		version = "1.1";
		
	}

	private String doServiceCall(List<Object> params, String method) throws IOException {
		StringWriter sw = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();

		sw.append("{\"params\":");
		
		mapper.writeValue(sw, params);
		
		sw.append(", \"version\":\"");
		sw.append(version);
		sw.append("\", \"method\":\"");
		sw.append(method);
		sw.append("\"}");
		
		String jsonStr = sw.toString();
		System.out.println(jsonStr);
		
//		String jsonStr = "{\"params\":[" + jsonParams + "]," + 
//						 "\"version\":\"" + version + "\"," +
//						 "\"method\":\"" + method + "\"}";
		HttpURLConnection conn = (HttpURLConnection) new URL(serviceURL).openConnection();
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(jsonStr.getBytes().length));
		conn.setRequestProperty("Content-Language", "en-US");
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		
		DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
		writer.writeBytes(jsonStr);
		writer.flush();
		writer.close();
		
		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			conn.disconnect();
			throw new IOException("KBase service call '" + method + "' failed! Server responded with code " + responseCode + " " + conn.getResponseMessage());
		}
		
//		HttpClient client = new DefaultHttpClient();
//		HttpPost request = new HttpPost(serviceURL);
//		
//		StringEntity json = new StringEntity(jsonStr);
//		request.addHeader("content-type", "application/json");
//		request.setEntity(json);
//		HttpResponse httpResponse = client.execute(request);
//		
//		String response = EntityUtils.toString(httpResponse.getEntity());
//		EntityUtils.consume(httpResponse.getEntity());
//		client.getConnectionManager().shutdown();


		/** Encoding the HTTP response into JSON format */
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String response = "";
		while ((response = br.readLine()) != null) {
			builder.append(response);
		}
		response = builder.toString();
		br.close();
		conn.disconnect();
		/**
		 * The reason for this messy halfway parsing of the JSON result is to just
		 * whittle out the 'result' field of the returned JSON object. The calling
		 * method will know how to turn this into the right kind of return object,
		 * so just extract that from the stream and return it.
		 */
		JsonFactory jFactory = new JsonFactory();
		JsonParser jParser = jFactory.createParser(response);

		String result = "";
		try {
			int resultStart = 1,
			    resultEnd = 2;
			while (jParser.nextToken() != JsonToken.END_OBJECT) {
				String fieldName = jParser.getCurrentName();
				
				if ("result".equals(fieldName)) {
					//current token = "result"
					// next = [
					jParser.nextToken();
					resultStart = jParser.getCurrentLocation().getColumnNr();
					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						// just keep going until we get to the end of the array.
					}
					resultEnd = jParser.getCurrentLocation().getColumnNr();
					
					break;
				}
				
				if ("error".equals(fieldName)) {
					// throw an IOException or something.
				}
			}
			result = response.substring(resultStart-1, resultEnd-2);
		} catch (JsonParseException e) {
			throw new IOException("Error in parsing JSON response from server: " + e.getLocalizedMessage());
		}

		return "[" + result + "]";
	}
	
	public String getAllEntitiesGenome() throws IOException {
		int first = 0;
		int count = 10000;
		List<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("scientific_name");
		fields.add("domain");
		
//		StringWriter sw = new StringWriter();
//		sw.append(String.valueOf(first) + ", " + String.valueOf(count) + ", ");
//		ObjectMapper mapper = new ObjectMapper();

//		mapper.writeValue(sw, fields);
		
		List<Object> params = new ArrayList<Object>();
		params.add(first);
		params.add(count);
		params.add(fields);
		String response = doServiceCall(params, "CDMI_EntityAPI.all_entities_Genome");
		// then parse out the JSON
		return response;
		// System.out.println("response:\n" + response);
	}
	
	public String getEntityGenome(List<String> ids) throws IOException {
		List<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("scientific_name");
		fields.add("domain");

//		StringWriter sw = new StringWriter();
//		ObjectMapper mapper = new ObjectMapper();
//		
//		mapper.writeValue(sw, ids);
//		sw.append(", ");
//		mapper.writeValue(sw, fields);

		List<Object> params = new ArrayList<Object>();
		params.add(ids);
		params.add(fields);
		String response = doServiceCall(params, "CDMI_EntityAPI.get_entity_Genome");
		return response;
	}
	
	/* This command accepts a KBase genome ID and returns the requested genome typed object */
	public Genome getGenomeObject(String genomeId, boolean asNewGenome) throws IOException {
//		typedef structure {
//		    bool as_new_genome;
//		} Get_GenomeObject_Opts;
//
//		funcdef get_genomeobject (genome_id id, Get_GenomeObject_Opts options) returns (GenomeObject genome);
		//TODO
		return null;
	}
	
	/* This function creates a new metabolic model given an input genome id */
	public String genomeToFbaModel(String genomeId) throws IOException {
//		funcdef genome_to_fbamodel (genome_id in_genome) returns (fbamodel_id out_model);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(genomeId);
		String response = doServiceCall(params, servicePrefix + ".genome_to_fbamodel");
		return mapper.readValue(response, String.class);
	}
	
	/* This function converts a metabolic model into an SBML file. */
	public String fbaModelToSbml(String fbaModelId) throws IOException {
//		typedef string SBML;
//		funcdef fbamodel_to_sbml(fbamodel_id in_model) returns (SBML sbml_string);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaModelId);
		String response = doServiceCall(params, servicePrefix + ".fba_model_to_sbml");
		return mapper.readValue(response, String.class);
	}

	/* This function converts an input object into HTML format. */
	public String fbaModelToHtml(String fbaModelId) throws IOException {
//		typedef string HTML;
//		funcdef fbamodel_to_html(fbamodel_id in_model) returns (HTML html_string);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaModelId);
		String response = doServiceCall(params, servicePrefix + ".fba_model_to_html");
		return mapper.readValue(response, String.class);
	}

	/* This function runs flux balance analysis on the input FBAModel and produces HTML as output */
	public String runFba(String fbaModelId, FBAFormulation formulation) throws IOException {
//		funcdef runfba (fbamodel_id in_model,FBAFormulation formulation) returns (fba_id out_fba);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaModelId);
		params.add(formulation);
		
		String response = doServiceCall(params, servicePrefix + ".runfba");
		return mapper.readValue(response, String.class);
	}
	
	public boolean fbaCheckResults(String fbaId) throws IOException {
//		funcdef fba_check_results (fba_id in_fba) returns (bool is_done);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaId);
		String response = doServiceCall(params, servicePrefix + ".fba_check_results");
		return mapper.readValue(response, Boolean.class);
	}
	
	public String fbaResultsToHtml(String fbaId) throws IOException {
//		funcdef fba_results_to_html (fba_id in_fba) returns (HTML html_string);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaId);
		String response = doServiceCall(params, servicePrefix + ".fba_results_to_html");
		return mapper.readValue(response, String.class);
	}
	
	/* These functions run gapfilling on the input FBAModel and produce gapfill objects as output */
	public String gapFillModel(String fbaModelId, GapFillingFormulation formulation) throws IOException {
//		funcdef gapfill_model (fbamodel_id in_model, GapfillingFormulation formulation) returns (gapfill_id out_gapfill);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaModelId);
		params.add(formulation);
		
		String response = doServiceCall(params, servicePrefix + ".gapfill_model");
		return mapper.readValue(response, String.class);
	}
	
	public boolean gapFillCheckResults(String gapFillId) throws IOException {
//		funcdef gapfill_check_results (gapfill_id in_gapfill) returns (bool is_done);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(gapFillId);
		String response = doServiceCall(params, servicePrefix + ".gapfill_check_results");
		return mapper.readValue(response, Boolean.class);
	}
	
	public String gapFillToHtml(String gapFillId) throws IOException {
//	funcdef gapfill_to_html (gapfill_id in_gapfill) returns (HTML html_string);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();

		params.add(gapFillId);
		String response = doServiceCall(params, servicePrefix + ".gapfill_to_html");
		
		return mapper.readValue(response, String.class);
	}

	public void gapFillIntegrate(String gapFillId, String fbaModelId) throws IOException {
//		funcdef gapfill_integrate (gapfill_id in_gapfill,fbamodel_id in_model) returns ();
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(gapFillId);
		params.add(fbaModelId);
		
		String response = doServiceCall(params, servicePrefix + ".gapfill_integrate");
	}
	

	/* These functions run gapgeneration on the input FBAModel and produce gapgen objects as output */
	public String gapGenModel(String fbaModelId, GapGenFormulation formulation) throws IOException {
//		funcdef gapgen_model (fbamodel_id in_model, GapgenFormulation formulation) returns (gapgen_id out_gapgen);
		
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaModelId);
		params.add(formulation);
		
		String response = doServiceCall(params, servicePrefix + ".gapgen_model");
		return mapper.readValue(response, String.class);
	}
	
	public boolean gapGenCheckResults(String gapGenId) throws IOException {
//		funcdef gapgen_check_results (gapgen_id in_gapgen) returns (bool is_done);
		
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(gapGenId);
		String response = doServiceCall(params, servicePrefix + ".gapgen_check_results");
		return mapper.readValue(response, Boolean.class);
	}
	
	public String gapGenToHtml(String gapGenId) throws IOException {
//		funcdef gapgen_to_html (gapgen_id in_gapgen) returns (HTML html_string);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(gapGenId);
		String response = doServiceCall(params, servicePrefix + ".gapgen_to_html");
		return mapper.readValue(response, String.class);
		
	}
	
	public void gapGenIntegrate(String gapGenId, String fbaModelId) throws IOException {
//	funcdef gapgen_integrate (gapgen_id in_gapgen,fbamodel_id in_model) returns ();
		List<Object> params = new ArrayList<Object>();

		params.add(gapGenId);
		params.add(fbaModelId);
		String response = doServiceCall(params, servicePrefix + ".gapgen_integrate");
	}
	
	/* This function returns model data for input ids */
	public List<FBAModel> getModels(List<String> modelIds) throws IOException {
//		funcdef get_models(list<fbamodel_id> in_model_ids) returns (list<FBAModel> out_models);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(modelIds);
		String response = doServiceCall(params, servicePrefix + ".get_models");
		return mapper.readValue(response, new TypeReference<List<FBAModel>>() { } );
		
	}

	/* This function returns fba data for input ids */
	public List<FBA> getFbas(List<String> fbaIds) throws IOException {
//	funcdef get_fbas(list<fba_id> in_fba_ids) returns (list<FBA> out_fbas);
		List<Object> params = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		params.add(fbaIds);
		String response = doServiceCall(params, servicePrefix + ".get_fbas");
		return mapper.readValue(response, new TypeReference<List<FBA>>() { } );
	}
	
	/* This function returns gapfill data for input ids */
	public List<GapFill> getGapFills(List<String> gapFillIds) throws IOException {
//	funcdef get_gapfills(list<gapfill_id> in_gapfill_ids) returns (list<GapFill> out_gapfills);
		List<Object> params = new ArrayList<Object>();
		params.add(gapFillIds);
		
		String response = doServiceCall(params, servicePrefix + ".get_gapfills");
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response, new TypeReference<List<GapFill>>() { });
	}
	
	/* This function returns gapgen data for input ids */
	public List<GapGen> getGapGens(List<String> gapGenIds) throws IOException {
//	funcdef get_gapgens(list<gapgen_id> in_gapgen_ids) returns (list<GapGen> out_gapgens);
		List<Object> params = new ArrayList<Object>();
		params.add(gapGenIds);
		
		String response = doServiceCall(params, servicePrefix + ".get_gapgens");
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response, new TypeReference<List<GapGen>>() { } );
	}
	
	/* This function returns reaction data for input ids */
	public List<Reaction> getReactions(List<String> reactionIds, String biochemistryId) throws IOException {
//	funcdef get_reactions(list<reaction_id> in_reaction_ids,biochemistry_id biochemistry) returns (list<Reaction> out_reactions);
		List<Object> params = new ArrayList<Object>();
		params.add(reactionIds);
		params.add(biochemistryId);
		String response = doServiceCall(params, servicePrefix + ".get_reactions");
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response, new TypeReference<List<Reaction>>() { });
		
	}
	
	/* This function returns compound data for input ids */
	public List<Compound> getCompounds(List<String> compoundIds, String biochemistryId) throws IOException {
//	funcdef get_compounds(list<compound_id> in_compound_ids,biochemistry_id biochemistry) returns (list<Compound> out_compounds);
		List<Object> params = new ArrayList<Object>();
		params.add(compoundIds);
		params.add(biochemistryId);
		
		String response = doServiceCall(params, servicePrefix + ".get_compounds");
		ObjectMapper mapper = new ObjectMapper();
		List<Compound> compoundList = mapper.readValue(response, new TypeReference<List<Compound>>() { });
		return compoundList;
	}
	
	/* This function returns media data for input ids */
	public List<Media> getMedia(List<String> mediaIds, String biochemistryId) throws IOException {
//		funcdef get_media(list<media_id> in_media_ids,biochemistry_id biochemistry) returns (list<Media> out_media);
		
		List<Object> params = new ArrayList<Object>();
		params.add(mediaIds);
		params.add(biochemistryId);
		String response = doServiceCall(params, servicePrefix + ".get_media");

		ObjectMapper mapper = new ObjectMapper();
		List<Media> mediaList = mapper.readValue(response, new TypeReference<List<Media>>() { });
		return mediaList;
	}

	/* This function returns biochemistry object */
	public Biochemistry getBiochemistry(String biochemistryId) throws IOException {
		//	funcdef get_biochemistry(biochemistry_id biochemistry) returns (Biochemistry out_biochemistry);
		List<Object> params = new ArrayList<Object>();
		params.add(biochemistryId);
		String response = stripArray(doServiceCall(params, servicePrefix + ".get_biochemistry"));
		
		Biochemistry biochemistry = new ObjectMapper().readValue(response, Biochemistry.class);
		return biochemistry;
	}
	
	private String stripArray(String s) {
		if (s.charAt(0) == '[' && s.charAt(s.length()-1) == ']')
			return s.substring(1, s.length()-2);
		return s;
	}
	
	public static void main(String[] args) {
		try {
			MetabolismService service = new MetabolismService();
			
			List<String> ids = new ArrayList<String>();
			ids.add("kb|g.20848");
			ids.add("kb|g.161");
			
			String response;
			
//			String response = service.getAllEntitiesGenome();
//			System.out.println(response);
			
			response = service.getEntityGenome(ids);
			System.out.println(response);
			
			service.getCompounds(ids, "biochemistry!");
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}