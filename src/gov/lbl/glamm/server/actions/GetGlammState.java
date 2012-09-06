package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GroupDataDAO;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.GroupDataDAOImpl;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;
import gov.lbl.glamm.server.externalservice.ExternalDataServiceManager;
import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.GlammState;
import gov.lbl.glamm.shared.model.MetabolicModel;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.OverlayDataGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * glamm/#taxId=XXX&ext=rpOperon|taxonomyId=YYY&...
 * @author wjriehl
 *
 */

public class GetGlammState {
	
	private enum StateParam {
		ORGANISM("taxId"),
		MAP("mapId"),
		ZOOM("v"),
		MODEL("modelId"),
		EXPERIMENT("expId"),
		GROUP("ext");			//external service

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
					state.setViewport(tokenMap.get(p));
					break;
				
				case MODEL :
					// look up the model using its id and send it out.
					String modelInfo = tokenMap.get(p);
					MetabolicModel model = null; // = look up model using data in p
					state.setModel(model);
					break;
					
				case EXPERIMENT :
					// get the experiment from its id and append it to the state.
					String expInfo = tokenMap.get(p);
					// use expInfo to get the Experiment data.
					Experiment exp = null;
					state.setExperiment(exp);
					break;
				
				case GROUP:
					// deal with group data
					ExternalDataService service = buildExternalService(tokenMap.get(p));
					
					// perform the service and get the group data results.
					// then pass them back in.
					
					GroupDataDAO dataDao = new GroupDataDAOImpl(sm);
					Set<OverlayDataGroup> groupData = dataDao.getGroupDataFromService(service);
					state.setGroupData(groupData);
					break;
					
				default :
					// throw an error?
					break;
			}
		}
		
		return state;
	}

	// TODO - for building a current state URL that can be exported. Kind of a poor-man's session save.
	public static String generateHistoryToken(GlammSession sm, Organism organism, String mapId) {
		return "";
	}
	
	private static ExternalDataService buildExternalService(String token) {
		/** token looks like this:
		 *  serviceCallId|param1=XXX|param2=YYY (not sure if | will be the delimiter in the end.
		 *  	maybe || eventually?)
		 */
		
		if (token == null || token.length() == 0)
			return null; // hate to do this, but I can't think of a better way. throw an error?

		// parse token into an ExternalDataService and fill it in with parameters
		
		String[] paramArr = token.split("\\|");
		// the first element is the name of the service, 
		// the remaining elements should all be split on "="
		
		String serviceName = paramArr[0];
		ExternalDataService service = ExternalDataServiceManager.getServiceFromName(serviceName);
		if (service == null)
			return null; 		// now we really should throw an exception.
		
		for (int i=1; i<paramArr.length; i++) {
			String[] kvPair = paramArr[i].split("=");
			if (service.hasParameter(kvPair[0]))
				service.setParameterValue(kvPair[0], kvPair[1]);
			else
				return null; // throw exception
		}
		
		return service;
		
	}
	
	private static Map<StateParam, String> parseHistoryToken(String token) {
		final Map<StateParam, String> tokenMap = new HashMap<StateParam, String>();
		if (token == null || token.length() == 0) {
			return tokenMap; // filled with defaults...
		}
		else {
			// parse token and populate.
			// token looks like: key=value&key=value;...
			String[] paramArr = token.split("&");
			for (int i=0; i<paramArr.length; i++) {
				
				int limiter = paramArr[i].indexOf('=');

				if (limiter == -1 || limiter >= paramArr[i].length())
					return null; //error'd - no delimiter present.
				
				String name = paramArr[i].substring(0, limiter);
				String value = paramArr[i].substring(limiter+1);

				if (name.length() == 0 || value.length() == 0) {
					//Ignore for now, eventually build an error-reporting mechanism.
				}
				else {
					StateParam s = StateParam.fromName(name.toLowerCase());
					if (s == null) {
						//Error'd
					}
					else {
						tokenMap.put(s, value);
					}
				}
			}
		}
		return tokenMap;
	}
	
}
