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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getSvg() {
		return svg;
	}

	public void setSvg(String svg) {
		this.svg = svg;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<PersistentAMRxn> getNetwork() {
		return network;
	}

	public void setNetwork(Set<PersistentAMRxn> network) {
		if(this.network != network)
			this.network.clear();
		if(network != null && !network.isEmpty())
			this.network.addAll(network);
	}

	public void addRxnToNetwork(PersistentAMRxn rxn) {
		rxn.setAnnotatedMap(this);
		network.add(rxn);
	}
	
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
