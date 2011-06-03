package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;

import java.util.ArrayList;
import java.util.HashSet;

public class PopulateCompoundSearch {

	public static ArrayList<Compound> populateCompoundSearch(HashSet<String> dbNames) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl();
		return cpdDao.getCompoundsForSearch(dbNames);
	}

}
