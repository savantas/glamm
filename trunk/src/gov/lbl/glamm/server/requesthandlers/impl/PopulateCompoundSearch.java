package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;

import java.util.ArrayList;

public class PopulateCompoundSearch {

	public static ArrayList<Compound> populateCompoundSearch(String extIdName) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl();
		return cpdDao.getCompoundsForSearch(extIdName);
	}

}
