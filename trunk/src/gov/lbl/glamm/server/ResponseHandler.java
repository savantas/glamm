package gov.lbl.glamm.server;

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
