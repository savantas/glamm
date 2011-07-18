package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;

import java.util.List;
import java.util.Set;

public class PopulateCompoundSearch {

	public static List<Compound> populateCompoundSearch(final GlammSession sm, final Set<String> dbNames) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl(sm);
		return cpdDao.getCompoundsForSearch(dbNames);
	}

}
