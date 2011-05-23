package gov.lbl.glamm.server.responsehandlers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class containing static methods for handling a variety of response types.
 * @author jtbates
 *
 */
public abstract class ResponseHandler {

	//********************************************************************************

	public static void asHtml(HttpServletResponse response, 
			Object content, 
			int statusCode) 
	throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(statusCode);
		PrintWriter out = response.getWriter();
		out.println(content);
		
	}

	//********************************************************************************

	public static void asXStreamXml(HttpServletResponse response, 
			Object content, 
			int statusCode) 
	throws IOException {
		
//		XStream xstream = new XStream();
//		xstream.autodetectAnnotations( true );
//		xstream.setMode(XStream.NO_REFERENCES);
//		
//		response.setContentType("application/xml");
//		Writer writer = response.getWriter();
//		writer.write("<?xml version=\"1.0\"?>\n");
//		xstream.toXML(content, writer);
//		
//		xstream = null;

	}

	//********************************************************************************

	public static void asPlainTextAttachment(HttpServletResponse response, 
			Object content, 
			int statusCode, 
			String fileName) 
	throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		PrintWriter out = response.getWriter();
		out.println(content);
	}
	
	//********************************************************************************

}
