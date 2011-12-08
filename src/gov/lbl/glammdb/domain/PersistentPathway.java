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

/**
 * Persistent class for storing pathways.
 * @author jtbates
 *
 */
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
	
	/**
	 * Constructor.
	 */
	public PersistentPathway() {}
	
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
	 * Gets the pathway title.
	 * @return The title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the pathway title.
	 * @param title The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the pathway external reference id (i.e. a KEGG id.)
	 * @return The external reference id.
	 */
	public String getXrefId() {
		return mapId;
	}
	
	/**
	 * Sets the pathway external reference id (i.e. a KEGG id.)
	 * @param xrefId The external reference id.
	 */
	public void setXrefId(String xrefId) {
		this.mapId = xrefId;
	}

	/**
	 * Sets the set of pathway elements.
	 * @param elements The set of pathway elements.
	 */
	public void setElements(Set<PersistentPwyElement> elements) {
		if(this.elements != elements)
			this.elements.clear();
		if(elements != null && !elements.isEmpty())
			this.elements.addAll(elements);
	}

	/**
	 * Gets the set of pathway elements.
	 * @return The set of pathway elements.
	 */
	public Set<PersistentPwyElement> getElements() {
		return elements;
	}
	
	/**
	 * Adds a pathway element to the pathway.
	 * @param element The pathway element.
	 */
	public void addElement(PersistentPwyElement element) {
		element.setPathway(this);
		elements.add(element);
	}	
}
