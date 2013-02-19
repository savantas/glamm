package gov.lbl.glamm.server.kbase.dao.impl;

import gov.doe.kbase.MOTranslationService.MOTranslation;
import gov.lbl.glamm.server.ConfigurationManager;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.kbase.dao.KBTranslationDAO;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KBTranslationDAOImpl implements KBTranslationDAO {

	private static final String TRANSLATION_URL = ConfigurationManager.getKBaseServiceURL("translation"); 
	private GlammSession sm;

	private static MOTranslation translationClient; 
	
	static {
		try {
			translationClient = new MOTranslation(TRANSLATION_URL);

		} catch (MalformedURLException e) {
			translationClient = null;
		}
	}
	
	public KBTranslationDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public Map<String, List<String>> locusIds2Fids(List<String> locusIds) {
		if (1==1)
			return new HashMap<String, List<String>>();
		
		// Keith, why do you hate me?
		List<Integer> intLocusIds = new ArrayList<Integer>();
		
		// I mean, seriously. These are all strings all throughout GLAMM.
		for (String locusId : locusIds) {
			try {
				// And now you want integers? What did I do to you?
				intLocusIds.add(Integer.parseInt(locusId));
			}
			catch (NumberFormatException e) {
				// do nothing if something got muddled up. guess we don't get an id for that one!
			}
		}

		try {
			Map<Integer, List<String>> intLocusId2Fids = translationClient.moLocusIds_to_fids(intLocusIds);
			Map<String, List<String>> locusIds2Fids = new HashMap<String, List<String>>();
			for (Integer intLocusId : intLocusId2Fids.keySet()) {
				locusIds2Fids.put(intLocusId.toString(), intLocusId2Fids.get(intLocusId));
			}
			return locusIds2Fids;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
