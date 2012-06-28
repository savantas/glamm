package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.GlammState;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;

import java.util.HashMap;
import java.util.Map;

public class GetGlammState {
	
	private enum StateParam {
		ORGANISM("taxId"),
		MAP("mapId"),
		ZOOM("v"),
		MODEL("modelId"),
		EXPERIMENT("expId");

		private static final Map<String, StateParam> string2State = new HashMap<String, StateParam>();
		static{
			for (StateParam s : values()) {
				string2State.put(s.getName().toLowerCase(), s);
			}
		}
		
		private String name;
		private StateParam(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static StateParam fromName(String name) {
			return string2State.get(name);
		}
	}
	
	
	public static GlammState getStateFromHistoryToken(GlammSession sm, String token) {
		Map<StateParam, String> tokenMap = parseHistoryToken(token);
		
		GlammState state = GlammState.defaultState();
		
		for (StateParam p : tokenMap.keySet()) {
			switch (p) {
				case ORGANISM :
					OrganismDAO orgDao = new OrganismDAOImpl(sm);
					Organism org = orgDao.getOrganismForTaxonomyId(tokenMap.get(p));
					if (org == null)
						org = Organism.globalMap();
					state.setOrganism(org);
					break;
				
				case MAP :
					state.setAMDId(tokenMap.get(p));
					break;
					
				case ZOOM :
					// some kind of zoom state class, or a map or something.
					break;
				
				case MODEL :
					// look up the model using its id and send it out.
					break;
					
				case EXPERIMENT :
					// get the experiment from its id and append it to the state.
					break;
					
				default :
					// throw an error?
					break;
			}
		}
		
		return state;
	}

	public static String generateHistoryToken(GlammSession sm, Organism organism, String mapId) {
		return "";
	}
	
	private static Map<StateParam, String> parseHistoryToken(String token) {
		final Map<StateParam, String> tokenMap = new HashMap<StateParam, String>();
		if (token.length() == 0) {
			return tokenMap; // filled with defaults...
		}
		else {
			// parse token and populate.
			// token looks like: key=value&key=value;...
			String[] paramArr = token.split("&");
			for (int i=0; i<paramArr.length; i++) {
				final String[] kvPair = paramArr[i].split("=");
				if (kvPair.length != 2) {
					//Ignore for now, eventually build an error-reporting mechanism.
				}
				else {
					StateParam s = StateParam.fromName(kvPair[0].toLowerCase());
					if (s == null) {
						//Error'd
					}
					else {
						tokenMap.put(s, kvPair[1]);
					}
				}
			}
		}
		return tokenMap;
	}
	
}
