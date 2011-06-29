package gov.lbl.glamm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadHandler {

	//********************************************************************************

	public interface LineParser {
		public String parseLine(String line);
	}

	//********************************************************************************

	private static final int MAX_MSGS = 10;

	protected static final String FILE_PARSE_WARNING_INVALID_FORM_DATA		= "Invalid form data - please resubmit.";
	protected static final String FILE_PARSE_WARNING_COULD_NOT_OPEN			= "Could not open file - please resubmit.";

	//********************************************************************************

	private Map<String, String> 	formFields 			= null;
	private List<String>			errorMsgs			= null;

	//********************************************************************************

	public FileUploadHandler(HttpServletRequest request, LineParser lineParser) {
		processRequest(request, lineParser);
	}

	//********************************************************************************

	public String getErrorMessages() {
		String result = "";

		if(errorMsgs != null) {
			int numMsgs = 0;
			for(String msg : errorMsgs) {
				result += msg + "\n";
				numMsgs++;

				if(numMsgs >= MAX_MSGS) {
					result += "and " + (numMsgs - MAX_MSGS) + " more.\n";
					break;
				}
			}
		}

		return result;
	}

	//********************************************************************************

	public String getFormField(String name) {
		String value = null;

		if(formFields != null && name != null)
			value = formFields.get(name);

		return value;
	}

	//********************************************************************************

	private void logError(String msg) {
		if(errorMsgs == null)
			errorMsgs = new ArrayList<String>();
		errorMsgs.add(msg);
	}

	//********************************************************************************

	private void logError(int lineNumber, String msg) {
		logError(lineNumber + ": " + msg);
	}

	//********************************************************************************

	private void processRequest(HttpServletRequest request, LineParser lineParser) {
		try {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);

			for(FileItem item : items) {
				if(item.isFormField()) {
					processFormField(item);
				} 
				else {
					processUploadedFile(item, lineParser);
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//********************************************************************************

	private void processFormField(FileItem item) {
		String name = item.getFieldName();
		String value = item.getString();

		if(formFields == null) 
			formFields = new HashMap<String, String>();

		formFields.put(name, value);
	}

	//********************************************************************************

	private void processUploadedFile(FileItem item, LineParser lineParser) {
		if(item != null) {
			try {
				InputStream 		inputStream 	= item.getInputStream();
				InputStreamReader 	isr 			= new InputStreamReader(inputStream);
				BufferedReader		reader			= new BufferedReader(isr);

				String	line 		= null;
				int 	lineNumber 	= 0;

				do {

					line = reader.readLine();
					lineNumber++;

					if(line == null)
						break;

					if(line.length() <= 0)
						continue;

					String errorMsg = lineParser.parseLine(line);
					if(errorMsg != null)
						logError(lineNumber, errorMsg);

				} while(line != null);

				inputStream.close();
			}
			catch(IOException e) {
				logError(FILE_PARSE_WARNING_COULD_NOT_OPEN);
			}
		}
		else {
			logError(FILE_PARSE_WARNING_COULD_NOT_OPEN);
		}
	}

	//********************************************************************************

}
