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

	/**
	 * Packages content in a response as html text.
	 * @param response The response.
	 * @param content The content object - should override toString.
	 * @param statusCode The status code of the response.
	 * @throws IOException
	 */
	public static void asHtml(HttpServletResponse response, 
			Object content, 
			int statusCode) 
	throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(statusCode);
		PrintWriter out = response.getWriter();
		out.println(content);
		
	}

	/**
	 * Packages content in a response as plain text for download.
	 * @param response The response.
	 * @param content The content object - should override toString.
	 * @param statusCode The status code of the response.
	 * @param fileName The name of the file to be downloaded.
	 * @throws IOException
	 */
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
}
