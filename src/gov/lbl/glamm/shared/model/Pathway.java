package gov.lbl.glamm.shared.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Pathways - ordered lists of reactions.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class Pathway implements Serializable {
	
	private String name;
	private String mapId;
	private List<Reaction> reactions;
	
	/**
	 * Construction
	 */
	public Pathway() {
		reactions = new ArrayList<Reaction>();
	}
	
	/**
	 * Adds a reaction to the pathway.
	 * @param reaction The reaction to add.
	 */
	public void addReaction(Reaction reaction) {
		reactions.add(reaction);
	}
	
	/**
	 * Gets the name of the pathway.
	 * @return The name of the pathway.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the list of the reactions in the pathway.
	 * @return The list of reactions.
	 */
	public List<Reaction> getReactions() {
		return reactions;
	}
	
	/**
	 * Sets the name of the pathway.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the id of the pathway - typically a KEGG map id.
	 * @return The map id.
	 */
	public String getMapId() {
		return mapId;
	}
	
	/**
	 * Sets the id of the pathway - typically a KEGG map id.
	 * @param mapId The map id.
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	
}