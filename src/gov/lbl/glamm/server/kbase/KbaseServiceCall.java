package gov.lbl.glamm.server.kbase;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface KbaseServiceCall {

	public String getMethod();
	public String getVersion();
	public void doJsonCall(OutputStream out) throws JsonGenerationException, JsonMappingException, IOException;
	
}
