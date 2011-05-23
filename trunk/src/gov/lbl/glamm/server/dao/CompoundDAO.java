package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Compound;

import java.util.ArrayList;

public interface CompoundDAO {
	public Compound getCompound(String id, String dbName);
	public ArrayList<Compound> getCompoundsForSearch(String dbName);
}
