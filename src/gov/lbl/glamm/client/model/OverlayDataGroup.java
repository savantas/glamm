package gov.lbl.glamm.client.model;

import gov.lbl.glamm.client.model.interfaces.HasType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Class for describing overlay data that has been separated into sets.
 * For example, if someone wanted to visualize sets of reactions that are regulated by multiple regulators.
 * @author wjriehl
 *
 */
@SuppressWarnings("serial")
public class OverlayDataGroup implements Serializable {
	
	/**
	 * A list of CSS colors that gets automatically cycled through and used to colorize sets.
	 * TODO: this will likely be woefully inadequate when (for example) getting all regulators
	 * for a single species.
	 * 
	 * So I'll need to make some kind of color-picker element. Or maybe just a larger list.
	 */
	private static final List<String> cssColors = new ArrayList<String>();
	static {
		cssColors.add("cyan");
		cssColors.add("yellow");
		cssColors.add("red");
		cssColors.add("green");
		cssColors.add("blue");
		cssColors.add("orange");
		cssColors.add("violet");
		cssColors.add("magenta");
		cssColors.add("gray");
	}
	private static int colorPointer = 0;

	private String id = "";
	private String name = "";
	private String source = "";
	private String cssColor = "white";
	private int taxId;
	private String genomeName = "";
	private Set<Gene> geneGroup;
	private Set<Compound> compoundGroup;
	private Set<Reaction> reactionGroup;
	
	private Map<String, Reaction> def2Reaction; // reaction definition --> reaction. should be 1 to 1, right?
	private String url;
	
	/**
	 * Key provider. 
	 */
	public static final transient ProvidesKey<OverlayDataGroup> KEY_PROVIDER = new ProvidesKey<OverlayDataGroup>() {
		public Object getKey(OverlayDataGroup item) {
			return item == null ? null : item.getName() + "_" + item.getId();
		}
	};

	@SuppressWarnings("unused")
	private OverlayDataGroup() { }

	/**
	 * Constructor. When initializing a set, it needs an id and a name.
	 * @param id
	 * @param name
	 */
	public OverlayDataGroup(String id, String name) {
		this.id = id;
		this.name = name;
		geneGroup = new HashSet<Gene>();
		compoundGroup = new HashSet<Compound>();
		reactionGroup = new HashSet<Reaction>();
		def2Reaction = new HashMap<String, Reaction>();
		cssColor = cssColors.get(colorPointer++ % cssColors.size());
		url = null;
	}
	
	/**
	 * Constructor that includes a callback URL for the set, and a name for the source.
	 * For example, the source name might be RegPrecise and the URL might be http://regprecise.lbl.gov/
	 * @param id
	 * @param name
	 * @param url
	 * @param source
	 */
	public OverlayDataGroup(String id, String name, String url, String source) {
		this(id, name);
		this.url = url;
		this.source = source;
	}

	/**
	 * Sets the CSS color string.
	 * TODO: error checking / validation
	 * @param cssColor
	 */
	public void setCssColor(String cssColor) {
		this.cssColor = cssColor;
	}
	
	/**
	 * Gets the current valid css color of this set.
	 * @return the CSS color string for this data group.
	 */
	public String getCssColor() {
		return cssColor;
	}
	
	/**
	 * Gets the name of this data group.
	 * @return the name of the data group.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the ID of this data group.
	 * @return the group ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gets the set of all elements of the group
	 * @return a Set of all elements in the data group as HasTypes
	 */
//	public Set<HasType> getElementSet() {
//		return dataGroup;
//	}
	
	/**
	 * Adds the given element to the group
	 * @param element the element to add to the group
	 */
	public void addElement(HasType element) {
		if (element.getType() == Reaction.TYPE)
			addReaction((Reaction)element);
		else if (element.getType() == Compound.TYPE)
			addCompound((Compound)element);
		else if (element.getType() == Gene.TYPE)
			addGene((Gene)element);
//		dataGroup.add(element);
	}
	
	public void addReaction(Reaction rxn) {
		reactionGroup.add(rxn);
		def2Reaction.put(rxn.getDefinition(), rxn);
	}
	
	public void addCompound(Compound cpd) {
		compoundGroup.add(cpd);
	}
	
	public void addGene(Gene gene) {
		geneGroup.add(gene);
	}
	
	/**
	 * Returns true if the data group has no elements.
	 * @return true if the data group is empty.
	 */
	public boolean isEmpty() {
//		return dataGroup.isEmpty();
		return (geneGroup.isEmpty() && compoundGroup.isEmpty() && reactionGroup.isEmpty());
	}
	
	/**
	 * Gets the URL associated with this data group.
	 * @return the data group's callback URL String, or an empty String if one is not set.
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Sets a new URL for this data group.
	 * TODO: checking/URL validation
	 * @param url the URL to set for this data group
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Gets the source String for this data group.
	 * @return the source String for this data group, or an empty String if one is not set.
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * Sets the source String for this data group.
	 * @param source the source for this data group.
	 */
	public void setSource(String source) {
		if (source == null)
			this.source = "";
		this.source = source;
	}
	
	public Set<HasType> getAllElements() {
		Set<HasType> elements = new HashSet<HasType>();
		elements.addAll(reactionGroup);
		elements.addAll(geneGroup);
		elements.addAll(compoundGroup);
		return elements;
	}
	
	/**
	 * Gets a set of all Reactions that are associated with this data group. This can be an empty set if there are no Reactions given.
	 * @return the set of all Reactions in this data group.
	 */
	public Set<Reaction> getAllReactions() {
		return reactionGroup;
	}

	/**
	 * Gets a set of all Compounds in this data group. This can be an empty set if there are no Compounds present.
	 * Note that adding a Reaction doesn't add its Compounds.
	 * TODO: should this be the case?
	 * @return the set of all Compounds in this data group
	 */
	public Set<Compound> getAllCompounds() {
		return compoundGroup;
	}
	
	
	public Set<Gene> getAllGenes() {
		return geneGroup;
	}
	
	/**
	 * TODO: Make this run in (more or less) constant time. Problems with how Reactions and Compounds are hashed.
	 * @param element
	 * @return
	 */
	public boolean containsElement(HasType element) {
		if (element.getType() == Reaction.TYPE)
			return containsReaction((Reaction) element);
		else if (element.getType() == Compound.TYPE)
			return containsCompound((Compound) element);
		else if (element.getType() == Gene.TYPE)
			return containsGene((Gene) element);
		else
			return false;
	}
	
	public boolean containsReaction(Reaction rxn) {
		return def2Reaction.containsKey(rxn.getDefinition());
	}
	
	public boolean containsCompound(Compound cpd) {
		return compoundGroup.contains(cpd);
	}
	
	public boolean containsGene(Gene gene) {
		return geneGroup.contains(gene);
	}
	
	public int size() {
		return geneGroup.size() +
			   reactionGroup.size() + 
			   compoundGroup.size();
	}

	public Set<Gene> getGenesForReaction(Reaction reaction) {
		Set<Gene> genes = new HashSet<Gene>();
		if (def2Reaction.containsKey(reaction.getDefinition())) {
			Reaction rxn = def2Reaction.get(reaction.getDefinition());
			for (Gene gene : rxn.getGenes()) {
				if (containsGene(gene))
					genes.add(gene);
			}
		}
		return genes;
	}

	public String getGenomeName() {
		return genomeName;
	}
	
	public void setGenomeName(String genomeName) {
		if (genomeName == null)
			this.genomeName = "";
		else
			this.genomeName = genomeName;
	}
}
