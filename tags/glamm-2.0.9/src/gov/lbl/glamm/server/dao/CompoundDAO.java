package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Compound;

import java.util.List;
import java.util.Set;

public interface CompoundDAO {
	public Compound getCompound(String id, String dbName);
	public List<Compound> getCompoundsForSearch(Set<String> dbNames);
}
