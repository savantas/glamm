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
public class AnnotatedMap implements Serializable {

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
	private Set<AMRxn> network = new LinkedHashSet<AMRxn>();

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

	public Set<AMRxn> getNetwork() {
		return network;
	}

	public void setNetwork(Set<AMRxn> network) {
		if(this.network != network)
			this.network.clear();
		if(network != null && !network.isEmpty())
			this.network.addAll(network);
	}

	public void addRxnToNetwork(AMRxn rxn) {
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
		
		Set<AMRxnElement> reactions 	= new LinkedHashSet<AMRxnElement>();
		Set<AMRxnElement> substrates 	= new LinkedHashSet<AMRxnElement>();
		Set<AMRxnElement> products 		= new LinkedHashSet<AMRxnElement>();
		
		for(AMRxn reaction : this.getNetwork()) {
			
			for(AMRxnElement element : reaction.getElements()) {
				switch(element.getType()) {
					case REACTION: 	reactions.add(element); 	break;
					case SUBSTRATE: substrates.add(element); 	break;
					case PRODUCT: 	products.add(element); 		break;
				}
			}

			for(AMRxnElement rxn : reactions) {
				for(AMRxnElement substrate : substrates) {
					for(AMRxnElement product : products) {
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
