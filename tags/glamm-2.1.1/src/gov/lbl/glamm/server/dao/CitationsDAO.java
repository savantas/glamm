package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Citation;

import java.util.List;

public interface CitationsDAO {
	public String getCitationsTableName();
	public List<Citation> getCitations();
}
