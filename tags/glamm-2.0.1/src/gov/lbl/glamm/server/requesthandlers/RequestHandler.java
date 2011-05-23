package gov.lbl.glamm.server.requesthandlers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler {
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
