package gov.lbl.glamm.server.externalservice;

import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.shared.ExternalDataService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonMappingException;

public interface ServiceJsonParser {

	public Set<OverlayDataGroup> parseJson(ExternalDataService service, InputStream dataStream, GlammSession sm) 
				throws JsonMappingException, IOException;
}
