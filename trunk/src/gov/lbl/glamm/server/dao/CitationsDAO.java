package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Citation;

import java.util.ArrayList;

public interface CitationsDAO {
	public String getCitationsTableName();
	public ArrayList<Citation> getCitations();
}
