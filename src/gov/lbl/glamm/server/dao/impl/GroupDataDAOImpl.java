package gov.lbl.glamm.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GroupDataServiceManager;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.GroupDataService;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.GroupDataDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;

/**
 * Implementation of the Group Data DAO interface.
 * @author wjriehl
 *
 */
public class GroupDataDAOImpl implements GroupDataDAO {

	private GlammSession sm;
	public GroupDataDAOImpl(GlammSession sm) {
		this.sm = sm;
	}
	
	/**
	 * TODO: this is a stub for now.
	 */
	public Set<OverlayDataGroup> getGroupData(String text) {
		/**
		 * needs to:
		 * parse the text.
		 * craft some kind of web service call using text (later)
		 * invoke the service and get the results.
		 * package the results into an OverlayData
		 * return it.
		 */
		
		if (text == null || text.length() == 0)
			return new HashSet<OverlayDataGroup>();
		
		Set<OverlayDataGroup> data = getDummyData();
		
		return data;
	}
	
	/**
	 * Makes a dummy set of OverlayDataGroups and returns them.
	 * @return
	 */
	private Set<OverlayDataGroup> getDummyData() {
		Set<OverlayDataGroup> dataSet = new HashSet<OverlayDataGroup>();
		
		Set<String> ecNums = new HashSet<String>();
		ecNums.add("1.2.3.1");
		ecNums.add("1.2.3.11");
		ecNums.add("2.7.7.6");
		ecNums.add("1.2.3.4");
		ecNums.add("5.5.1.1");
		String name1 = "test 1";
		
		Set<String> ecNums2 = new HashSet<String>();
		ecNums2.add("2.7.7.7");
		ecNums2.add("6.1.1.17");
		ecNums2.add("2.8.1.2");
		ecNums2.add("2.3.3.1");
		String name2 = "test 2";

		ReactionDAO reactionDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> rxns = reactionDao.getReactionsForEcNums(ecNums);
		OverlayDataGroup group1 = new OverlayDataGroup("1", name1, "http://glamm.lbl.gov", "RegPrecise");

		for (Reaction rxn : rxns) {
			group1.addGroupElement(rxn);
		}
		
		OverlayDataGroup group2 = new OverlayDataGroup("2", name2, "http://regprecise.lbl.gov/", "RegPrecise");
		Set<Reaction> rxns2 = reactionDao.getReactionsForEcNums(ecNums2);
		for (Reaction rxn : rxns2) {
			group2.addGroupElement(rxn);
		}
		
		dataSet.add(group1);
		dataSet.add(group2);
		return dataSet;
	}

	/**
	 * Gets group data from a data service.
	 * TODO:
	 * This is still a horrible, yet functional, prototype. It should ultimately:
	 * 	- call the service with the right parameters.
	 *  - craft a proper error (or throw a proper exception) if necessary
	 *  - invoke the DataService's parser to properly construct the DataGroups
	 * 
	 * Right now, well, one of out three ain't bad. The parser is wrapped up in the code below, and this only works for
	 * Pavel's RegPrecise service.
	 * 
	 * @param serviceName the name of the service to invoke.
	 * @param parameters the parameters for the service, where the key is the parameter name and the value is its value.
	 * @return a Set of OverlayDataGroups from the service
	 */
	@Override
	public Set<OverlayDataGroup> getGroupDataFromService(String serviceName, Map<String, String> parameters) {
		
		Set<OverlayDataGroup> dataSet = new HashSet<OverlayDataGroup>();

		GroupDataService service = GroupDataServiceManager.getServiceFromName(serviceName);
		
		if (service == null)
			return dataSet;
		
		String uri = service.getUrl();
		uri += buildParameterString(service, parameters);

		// Horrible hack for prototyping this up for now.
		// Later, should make a separate parser for each service, and invoke through reflection.
		String taxId = "";
		if (parameters.containsKey("MO taxonomy id"))
			taxId = parameters.get("MO taxonomy id");
		
		try {
			URL url = new URL(uri);
			
			ObjectMapper mapper = new ObjectMapper();
			
			InputStream stream = url.openStream();
			ElementSet dataElements = mapper.readValue(stream, ElementSet.class);
			stream.close();
			
/** Maybe this structure would work better?
 * 			{
				[
					{ 
					  "groupName" : "name",
					  "groupId"   : "id",
					  "URL"		  : "url",
					  "source"    : "source"
					  [ 
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss  : "##"
					  	},
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss" : "##"
					  	}, ...
					  ]
					},
					{ 
					  "groupName" : "name",
					  "groupId"   : "id",
					  "URL"		  : "url",
					  "source"    : "source"
					  [ 
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss  : "##"
					  	},
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss" : "##"
					  	}, ...
					  ]
					}					
				]
			}
**/			

			/**
			 *  Shuffle around elements and restructure into a Set<OverlayDataGroup>
			 *  
			 *  OverlayData works on a set of reactions or compounds.
			 *  In this case, we're dealing with reactions (not always, though).
			 *  So we want to go from either VIMSS ids (if that's the only option) to reactions
			 *  Or form EC nums to reactions.
			 *  
			 *  First, break things up into sets.
			 *  	group id -> set of VIMSS ids
			 *      group id -> set of EC nums
			 *  
			 *  Look up the Genes for the VIMSS ids in each group. Now we should have JUST
			 *      group id -> set of EC nums
			 *  
			 *  Look up the Reactions for the EC nums.
			 *  
			 *  Now make one OverlayDataGroup for each group id/name, populate with reactions.
			 */

			// List of VIMSS locus ids to look up.
			Set<String> locusIds = new HashSet<String>();
			
			// List of EC numbers for all elements.
			Set<String> ecNums = new HashSet<String>();
			
			// Map of all data groups by their group id.
			Map<String, OverlayDataGroup> id2DataGroup = new HashMap<String, OverlayDataGroup>();
			
			// Map of all ec numbers by the group id they belong to (eventually turn these into Reactions and put them in the data groups).
			Map<String, Set<String>> id2EcNums = new HashMap<String, Set<String>>();

			for (Element elem : dataElements.getGlammElement()) {
				
				// If there isn't a data group container yet, initialize a new one.
				if (!id2DataGroup.containsKey(elem.getGroupId()))
					id2DataGroup.put(elem.getGroupId(), new OverlayDataGroup(elem.getGroupId(), elem.getGroupName(), elem.getCallbackURL(), service.getName()));
				
				// If this element has an ec number, put it in the list so we don't have to look it up via VIMSS ids
				if (elem.getEcNumber() != null && !elem.getEcNumber().isEmpty()) {
					if (!id2EcNums.containsKey(elem.getGroupId()))
						id2EcNums.put(elem.getGroupId(), new HashSet<String>());
					id2EcNums.get(elem.getGroupId()).add(elem.getEcNumber());
					ecNums.add(elem.getEcNumber());
				}
				
				// If it has a VIMSS id, put that in the locus ids list.
				else if (elem.getVimssId() != null && !elem.getVimssId().isEmpty()) {
					locusIds.add(elem.getVimssId());
				}
			}

			// Look up the set of genes by VIMSS id to get their EC numbers
			GeneDAO geneDao = new GeneDAOImpl(sm);
			Set<Gene> genes = geneDao.getGenesForVimssIds(taxId, locusIds);

			// Map from VIMSS ids to the set of EC numbers
			// Also continue to populate the list of all ec numbers
			Map<String, Set<String>> locus2Ecs = new HashMap<String, Set<String>>();
			for (Gene g : genes) {
				locus2Ecs.put(g.getVimssId(), g.getEcNums());
				ecNums.addAll(g.getEcNums());
			}

			// Do the mapping from VIMSS ids to EC numbers for all elements.
			for (Element elem : dataElements.getGlammElement()) {
				if (elem.getVimssId() != null && !elem.getVimssId().isEmpty() && locus2Ecs.containsKey(elem.getVimssId())) {
					if (!id2EcNums.containsKey(elem.getGroupId()))
						id2EcNums.put(elem.getGroupId(), new HashSet<String>());
					id2EcNums.get(elem.getGroupId()).addAll(locus2Ecs.get(elem.getVimssId()));
				}
			}
			
			// now id2EcNums is fully populated. so is ecNums, so use that to get the reactions. then we can map it back and
			// populate everything.
			ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
			Set<Reaction> rxns = rxnDao.getReactionsForEcNums(ecNums);

			for (Reaction r : rxns) {
				// Each reaction has a set of EC nums
				Set<String> rxnEcs = r.getEcNums();
				
				for (String groupId : id2EcNums.keySet()) {
					// Each group has a set of EC nums
					Set<String> groupEcs = id2EcNums.get(groupId);
					
					// If there's any overlap between the group and reaction ECs,
					// then the reaction belongs in the group.
					for (String groupEc : groupEcs) {
						if (rxnEcs.contains(groupEc)) {
							id2DataGroup.get(groupId).addGroupElement(r);
							continue;
						}
					}
				}
			}
			
			dataSet.addAll(id2DataGroup.values());
			
		} catch (MalformedURLException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return dataSet;
	}
	
	/**
	 * 
	 * @param service
	 * @param parameters
	 * @return
	 */
	private String buildParameterString(GroupDataService service, Map<String, String> parameters) {
		if (parameters == null || parameters.size() == 0)
			return "";

		String paramString = "?";
		List<String> pairs = new ArrayList<String>();
		for (String p : parameters.keySet()) {
			String urlParamName = service.getUrlParamName(p);
			if (urlParamName != null)
				pairs.add(urlParamName + "=" + parameters.get(p));
		}
		
		paramString += pairs.get(0);
		for (int i=1; i<pairs.size(); i++) {
			paramString += "&" + pairs.get(i);
		}
		return paramString;
	}
}

/**
 * A simple class used by Jackson for unmarshalling the JSON-encoded results from RegPrecise
 */
class ElementSet {
	private Collection<Element> glammElement;
	
	public ElementSet() { }
	
	public void setGlammElement(Collection<Element> glammElement) {
		this.glammElement = glammElement;
	}
	
	public Collection<Element> getGlammElement() { return glammElement; }
}		

/**
 * A simple class used by Jackson for unmarshalling the JSON-encoded results from RegPrecise
 */
class Element {
	private String vimssId;
	private String ecNumber;
	private String groupId;
	private String groupName;
	private String callbackURL;

	public Element() {
		vimssId = "";
		ecNumber = "";
		groupId = "";
		groupName = "";
		callbackURL = "";
	}
	
	public void setVimssId(String vimssId) {
		this.vimssId = vimssId;
	}
	
	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	public String getVimssId() { return vimssId; }
	
	public String getEcNumber() { return ecNumber; }
	
	public String getGroupId() { return groupId; }
	
	public String getGroupName() { return groupName; }
	
	public String getCallbackURL() { return callbackURL; }
}