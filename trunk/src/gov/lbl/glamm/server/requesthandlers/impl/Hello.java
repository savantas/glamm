package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Hello implements RequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ResponseHandler.asHtml(response, "Hello, world!", HttpServletResponse.SC_OK);

	}

}
