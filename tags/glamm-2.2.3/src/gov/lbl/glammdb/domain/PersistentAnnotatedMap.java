package gov.lbl.glammdb.domain;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.client.model.MetabolicNetwork.MNNode;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Persistent class for storing annotated maps.  Contains paths to the map SVG file and the mini-map icon image file (typically PNG format.)
 * Also contains the metabolic network graph.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AnnotatedMap")
public class PersistentAnnotatedMap implements Serializable {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="mapId", nullable=false)
	private String mapId;

	@Column(name="svg", nullable=false)
	private String svg;

	@Column(name="icon", nullable=false)
	private String icon;

	@Column(name="title", nullable=false)
	private String title;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "am_id")
	@OrderBy("id")
	private Set<PersistentAMRxn> network = new LinkedHashSet<PersistentAMRxn>();

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id The id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the map id - typically a KEGG map id.
	 * @return The map id.
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * Sets the map id - typically a KEGG map id.
	 * @param mapId The map id.
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	/**
	 * Gets the path to the SVG file.
	 * @return The SVG file path.
	 */
	public String getSvg() {
		return svg;
	}

	/**
	 * Sets the SVG file path.
	 * @param svg The SVG file path.
	 */
	public void setSvg(String svg) {
		this.svg = svg;
	}

	/**
	 * Gets the icon file path.
	 * @return The icon file path.
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the icon file path.
	 * @param icon The icon file path.
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the map title.
	 * @return The map title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the map title.
	 * @param title The map title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the set of persistent annotated map reactions that forms the metabolic network.
	 * @return The set of reactions.
	 */
	public Set<PersistentAMRxn> getNetwork() {
		return network;
	}

	/**
	 * Sets the set of persistent annotated map reactions that forms the metabolic network.
	 * @param network The set of reactions.
	 */
	public void setNetwork(Set<PersistentAMRxn> network) {
		if(this.network != network)
			this.network.clear();
		if(network != null && !network.isEmpty())
			this.network.addAll(network);
	}

	/**
	 * Adds a persistent annotated map reaction to the network.
	 * @param rxn The reaction.
	 */
	public void addRxnToNetwork(PersistentAMRxn rxn) {
		rxn.setAnnotatedMap(this);
		network.add(rxn);
	}
	
	/**
	 * Converts the persistent annotated map to an AnnotatedMapDescriptor - the GLAMM model object.
	 * @return The annotated map descriptor.
	 */
	public AnnotatedMapDescriptor toAnnotatedMapDescriptor() {
		return new AnnotatedMapDescriptor(this.getMapId(),
				this.getSvg(),
				this.getIcon(),
				this.getTitle(),
				this.genMetabolicNetwork());
	}
	
	private MetabolicNetwork genMetabolicNetwork() {
		MetabolicNetwork mn = new MetabolicNetwork(this.getTitle());
		
		Set<PersistentAMRxnElement> reactions 	= new LinkedHashSet<PersistentAMRxnElement>();
		Set<PersistentAMRxnElement> substrates 	= new LinkedHashSet<PersistentAMRxnElement>();
		Set<PersistentAMRxnElement> products 		= new LinkedHashSet<PersistentAMRxnElement>();
		
		for(PersistentAMRxn reaction : this.getNetwork()) {
			
			for(PersistentAMRxnElement element : reaction.getElements()) {
				switch(element.getType()) {
					case REACTION: 	reactions.add(element); 	break;
					case SUBSTRATE: substrates.add(element); 	break;
					case PRODUCT: 	products.add(element); 		break;
				}
			}

			for(PersistentAMRxnElement rxn : reactions) {
				for(PersistentAMRxnElement substrate : substrates) {
					for(PersistentAMRxnElement product : products) {
						mn.addNode(new MNNode(rxn.getXrefId(), substrate.getXrefId(), product.getXrefId()));
						mn.addNode(new MNNode(rxn.getXrefId(), product.getXrefId(), substrate.getXrefId()));
					}
				}
			}
			
			reactions.clear();
			products.clear();
			substrates.clear();
		}
		return mn;
	}

}
