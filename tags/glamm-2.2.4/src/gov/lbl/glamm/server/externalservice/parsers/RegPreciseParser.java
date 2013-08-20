package gov.lbl.glamm.server.externalservice.parsers;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;
import gov.lbl.glamm.server.externalservice.ServiceJsonParser;
import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.ExternalServiceParameter;
import gov.lbl.glamm.shared.model.DataGroupElement;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.OverlayDataGroup;
import gov.lbl.glamm.shared.model.Reaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegPreciseParser implements ServiceJsonParser {
	
	public static final String MONOCHROME = "cyan";

	@Override
	public Set<OverlayDataGroup> parseJson(ExternalDataService service, InputStream dataStream, GlammSession sm) 
				throws JsonMappingException, IOException {

		Set<OverlayDataGroup> dataSet = new HashSet<OverlayDataGroup>();
		
		ObjectMapper mapper = new ObjectMapper();
		ElementSet dataElements = mapper.readValue(dataStream, ElementSet.class);
		
		if (dataElements == null)
			return dataSet;
		
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
		 *  OverlayData works on a set of reactions, compounds, or genes.
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

		// This is a RegPrecise parser, using a RegPrecise-based service.
		// So, we should check for the colorizer parameter, and build our groups based on that.
		// ALL RegPrecise services (maybe all external services?) have a boolean parameter with 
		// state name = "c" for color. 
		
		boolean useColor = true;
		for (ExternalServiceParameter param : service.getParameters())
			if (param.getStateUrlName().equalsIgnoreCase("c"))
				useColor = !param.getValue().equals("0");
		
		
		// Map of all data groups by their group id.
		Map<String, OverlayDataGroup> id2DataGroup = new HashMap<String, OverlayDataGroup>();
		Map<String, Set<String>> locus2GroupIds = new HashMap<String, Set<String>>();
		Map<String, Set<String>> ecNum2GroupIds = new HashMap<String, Set<String>>();
		Map<String, Set<Gene>> ecNum2Genes = new HashMap<String, Set<Gene>>();
		
		Map<String, Map<String, Element>> id2Metadata = new HashMap<String, Map<String, Element>>();
		
		/** Initial setup - parse info that was downloaded **/
		for (Element elem : dataElements.getGlammElements()) {
			
			// Check the groupId for format (if no group Id, move on to the next Element)
			String groupId = elem.getGroupId();
			if (groupId == null || groupId.isEmpty())
				continue;

			String ecNum = elem.getEcNumber();
			String locusId = elem.getVimssId();
			String genomeName = elem.getGenomeName();
			
			if (ecNum != null && ecNum.length() > 0) {
				if (!id2Metadata.containsKey(ecNum))
					id2Metadata.put(ecNum, new HashMap<String, Element>());
				id2Metadata.get(ecNum).put(groupId, elem);
			}
			
			if (locusId != null && locusId.length() > 0) {
				if (!id2Metadata.containsKey(locusId))
					id2Metadata.put(locusId, new HashMap<String, Element>());
				id2Metadata.get(locusId).put(groupId, elem);
			}
			
			// If there's no OverlayDataGroup yet, make one.
			if (!id2DataGroup.containsKey(groupId)) {
				OverlayDataGroup newGroup = new OverlayDataGroup(groupId, elem.getGroupName(), elem.getCallbackURL(), service.getServiceName());
				if (!useColor)
					newGroup.setCssColor(MONOCHROME);
				newGroup.setGenomeName(genomeName);
				id2DataGroup.put(groupId, newGroup);
			}
			
			// Put the EC num in that group.
			if (ecNum != null && !ecNum.isEmpty()) {
				if (!ecNum2GroupIds.containsKey(ecNum))
					ecNum2GroupIds.put(ecNum, new HashSet<String>());
				ecNum2GroupIds.get(ecNum).add(groupId);
			}
			
			// Put the locusId in that group.
			if (locusId != null && !locusId.isEmpty()) {
				if (!locus2GroupIds.containsKey(locusId))
					locus2GroupIds.put(locusId, new HashSet<String>());
				locus2GroupIds.get(locusId).add(groupId);
			}
		}

		// Get all genes from VIMSS locus IDs.
		GeneDAO geneDao = new GeneDAOImpl(sm);
		
		Set<Gene> genes = geneDao.getGenesForVimssIds(locus2GroupIds.keySet());
		
		// Shuffle them around to be in the right groups.
		for (Gene gene : genes) {
			String locusId = gene.getVimssId();
			if (!locus2GroupIds.containsKey(locusId))
				continue;
			
			for (String groupId : locus2GroupIds.get(locusId)) {
				if (id2Metadata.containsKey(locusId) && id2Metadata.get(locusId).containsKey(groupId)) {
					Element elem = id2Metadata.get(locusId).get(groupId);
					id2DataGroup.get(groupId).addElement(gene, elem.getCallbackURL(), elem.getStrength());
				} else {
					id2DataGroup.get(groupId).addElement(gene);
				}
			}
			
			for (String ecNum : gene.getEcNums()) {
				if (!ecNum2GroupIds.containsKey(ecNum))
					ecNum2GroupIds.put(ecNum, new HashSet<String>());
				ecNum2GroupIds.get(ecNum).addAll(locus2GroupIds.get(gene.getVimssId()));
				
				if (!ecNum2Genes.containsKey(ecNum))
					ecNum2Genes.put(ecNum, new HashSet<Gene>());
				ecNum2Genes.get(ecNum).add(gene);
			}
		}
		
		// NOW we need gene->EC
		// get all ECs.
		// get all Reactions for those ECs.
		// now we can map (for all genes), which reactions they belong to (reaction -> EC set, EC -> gene set, gene -> Group)
		
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> reactions = rxnDao.getReactions(rxnDao.getRxnIdsForEcNums(ecNum2GroupIds.keySet()));

		for (Reaction rxn : reactions) {
			for (String ecNum : rxn.getEcNums()) {
				Set<Gene> geneSet = ecNum2Genes.get(ecNum);
				if (geneSet != null && !geneSet.isEmpty()) {
					for (Gene g : geneSet) {
						rxn.addGene(g);
					}
				}
				
				Set<String> groupIds = ecNum2GroupIds.get(ecNum);
				if (groupIds == null || groupIds.isEmpty())
					continue;
				
				for (String id : groupIds) {
					id2DataGroup.get(id).addReaction(rxn);
				}
			}
		}
		
		dataSet.addAll(id2DataGroup.values());

		return dataSet;
	
	}
	
	public Set<OverlayDataGroup> parse(ExternalDataService service, InputStream dataStream, GlammSession sm) 
			throws JsonMappingException, IOException {

//		Set<OverlayDataGroup> dataSet = new HashSet<OverlayDataGroup>();
		
		ObjectMapper mapper = new ObjectMapper();
		ElementSet dataElements = mapper.readValue(dataStream, ElementSet.class);
		
		Map<String, OverlayDataGroup> id2DataGroup = new HashMap<String, OverlayDataGroup>();
		Map<String, DataGroupElement> groupId2Element = new HashMap<String, DataGroupElement>();
		
		for (Element elem : dataElements.getGlammElements()) {
			
			// Make a new Data Group if we don't have one yet.
			if (!id2DataGroup.containsKey(elem.getGroupId())) {
				id2DataGroup.put(elem.getGroupId(), new OverlayDataGroup(elem.getGroupId(), elem.getGroupName(), elem.getCallbackURL(), service.getServiceName()));
			}
			
			// Build some incomplete DataGroupElements, linked to their data group
			DataGroupElement groupElem = new DataGroupElement(elem.getVimssId());
			groupElem.setCallbackUrl(elem.getCallbackURL());
			groupElem.setGenomeName(elem.getGenomeName());
			groupElem.setTaxonomyId(elem.getTaxonomyId());
			groupElem.setStrength(elem.getStrength());
			
			groupId2Element.put(elem.getGroupId(), groupElem);
		}
		
		return null;
	}
		
		

}

/**
 * A simple class used by Jackson for unmarshalling the JSON-encoded results from RegPrecise
 */
class ElementSet {
	private Collection<Element> glammElements;
	
	public ElementSet() { }
	
	@JsonProperty("glammElement")
	public void setGlammElements(Collection<Element> glammElement) {
		this.glammElements = glammElement;
	}
	
	@JsonProperty("glammElement")
	public Collection<Element> getGlammElements() { return glammElements; }
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
	private String genomeName;
	private String taxonomyId;
	private float strength;

//	String genomeName;	// new, name of a genome
//	Integer taxonomyId;	// new, taxonomy id of a genome
//	Float strength;		// strength (from 0 to 1)

	
	public Element() {
		vimssId = "";
		ecNumber = "";
		groupId = "";
		groupName = "";
		callbackURL = "";
		genomeName = "";
		taxonomyId = "";
		strength = 0;
	}
	
	@JsonProperty("genomeName")
	public void setGenomeName(String genomeName) {
		this.genomeName = genomeName;
	}
	
	@JsonProperty("taxonomyId")
	public void setTaxonomyId(String taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
	
	@JsonProperty("strength")
	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	@JsonProperty("vimssId")
	public void setVimssId(String vimssId) {
		this.vimssId = vimssId;
	}
	
	@JsonProperty("ecNumber")
	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}
	
	@JsonProperty("groupId")
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@JsonProperty("groupName")
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@JsonProperty("callbackURL")
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	@JsonProperty("vimssId")
	public String getVimssId() { return vimssId; }
	
	@JsonProperty("ecNumber")
	public String getEcNumber() { return ecNumber; }
	
	@JsonProperty("groupId")
	public String getGroupId() { return groupId; }
	
	@JsonProperty("groupName")
	public String getGroupName() { return groupName; }
	
	@JsonProperty("callbackURL")
	public String getCallbackURL() { return callbackURL; }
	
	@JsonProperty("genomeName")
	public String getGenomeName() { return genomeName; }
	
	@JsonProperty("taxonomyId")
	public String getTaxonomyId() { return taxonomyId; }
	
	@JsonProperty("strength")
	public float getStrength() { return strength; }
}