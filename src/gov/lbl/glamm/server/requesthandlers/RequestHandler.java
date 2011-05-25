package gov.lbl.glamm.server.requesthandlers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler {
	
	public static final String PARAM_ACTION						= "action";
	public static final String PARAM_ALGORITHM					= "algorithm";
	public static final String PARAM_AS_TEXT					= "asText";
	public static final String PARAM_CPD_SRC					= "cpdSrc";
	public static final String PARAM_CPD_DST					= "cpdDst";
	public static final String PARAM_EXPERIMENT					= "experiment";
	public static final String PARAM_EXP_SOURCE					= "expSource";
	public static final String PARAM_EXTID						= "extId";
	public static final String PARAM_MAP_TITLE					= "mapTitle";
	public static final String PARAM_SAMPLE						= "sample";
	public static final String PARAM_TAXONOMY_ID				= "taxonomyId";
	
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
