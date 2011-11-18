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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name="AMRxn")
public class PersistentAMRxn implements Serializable {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "am_id", nullable = false, unique = false)
	private PersistentAnnotatedMap annotatedMap;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "amRxn_id")
	@OrderBy("id")
	private Set<PersistentAMRxnElement> elements = new LinkedHashSet<PersistentAMRxnElement>();

	@Column(name="reversible", nullable=false)
	@Type(type="yes_no")
	private boolean reversible;

	
	public PersistentAMRxn() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PersistentAnnotatedMap getAnnotatedMap() {
		return annotatedMap;
	}

	public void setAnnotatedMap(PersistentAnnotatedMap annotatedMap) {
		this.annotatedMap = annotatedMap;
	}

	public void addElement(final PersistentAMRxnElement element) {
		element.setReaction(this);
		elements.add(element);
	}

	public Set<PersistentAMRxnElement> getElements() {
		return elements;
	}

	public void setElements(Set<PersistentAMRxnElement> elements) {
		if(this.elements != elements) {
			this.elements.clear();
		}
		if(elements != null && !elements.isEmpty()) {
			this.elements.addAll(elements);
		}
	}

	public boolean isReversible() {
		return reversible;
	}

	public void setReversible(boolean reversible) {
		this.reversible = reversible;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id: ").append(this.getId()).append(" reversible: ").append(this.isReversible());
		builder.append(" [ ");
		for(PersistentAMRxnElement element : this.getElements())
			builder.append(element.toString()).append(" ");
		builder.append(" ]");
		return builder.toString();
	}	
}
