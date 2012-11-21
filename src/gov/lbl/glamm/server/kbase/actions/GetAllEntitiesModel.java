package gov.lbl.glamm.server.kbase.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;


public class GetAllEntitiesModel {
	
	private String version = "1.1";
	private String method = "CDMI_EntityAPI.all_entities_Model";
	private List<String> fields = null;
	private int first;
	private int count;
	
	public GetAllEntitiesModel(int first, int count, List<String> fields) {
		this.first = first;
		this.count = count;
		this.fields = fields;
	}

	public void doJsonCall(OutputStream out) throws JsonGenerationException,
													JsonMappingException, IOException {
		JsonGenerator g = new JsonFactory().createJsonGenerator(out);
		
		g.writeStartObject();
		
			/* the 'params' field must look like this:
			 * "params" : [ start, count, [ field1, field2, field3, field4, ... ] ]
			 * 
			 * Since it's a non-standard array, we can't just use Jackson's usual ObjectMapper idiom.
			 */
			g.writeFieldName("params");
			g.writeStartArray();
				g.writeNumber(first);
				g.writeNumber(count);
				g.writeStartArray();
					for (String s : fields) {
						g.writeString(s);
					}
				g.writeEndArray();
			g.writeEndArray();
			
			g.writeStringField("version", getVersion());
			
			g.writeStringField("method", getMethod());
			
		g.writeEndObject();
		g.close();
	}

	public String getMethod() {
		return method;
	}

	public String getVersion() {
		return version;
	}
}
