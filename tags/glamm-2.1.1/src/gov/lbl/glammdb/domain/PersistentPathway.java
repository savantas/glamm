package gov.lbl.glammdb.domain;

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
@Table(name="Pathway")
public class PersistentPathway implements Serializable {

	@Id
	@Column(name="id")
	@GeneratedValue
	private Long id;
	
	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="mapId", nullable=false)
	private String mapId;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "pathway_id")
    @OrderBy("id")
	private Set<PersistentPwyElement> elements = new LinkedHashSet<PersistentPwyElement>();
	
	public PersistentPathway() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getXrefId() {
		return mapId;
	}

	public void setXrefId(String xrefId) {
		this.mapId = xrefId;
	}

	public void setElements(Set<PersistentPwyElement> elements) {
		if(this.elements != elements)
			this.elements.clear();
		if(elements != null && !elements.isEmpty())
			this.elements.addAll(elements);
	}

	public Set<PersistentPwyElement> getElements() {
		return elements;
	}
	
	public void addElement(PersistentPwyElement element) {
		element.setPathway(this);
		elements.add(element);
	}	
}
