package gov.lbl.glamm.server.kbase.dao;

import java.util.List;
import java.util.Map;

public interface KBTranslationDAO {
	public Map<String, List<String>> locusIds2Fids(List<String> locusIds);
}
