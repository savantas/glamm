package gov.lbl.glammdb.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Persistent class for storing pathway elements.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="PwyElement")
public class PersistentPwyElement implements Serializable {
	
	/**
	 * Enumerated type of the pathway element.
	 * @author jtbates
	 *
	 */
	public enum Type {
		CPD(new String[]{"cpd", "gl"}),
		EC(new String[]{"ec"}),
		MAP(new String[]{"map"}),
		RXN(new String[]{"rn"});
		
		private Set<String> values;
		
		private Type(final String[] values) {
			this.values = new HashSet<String>();
			for(String value : values)
				this.values.add(value);
		}
		
		/**
		 * Gets the type of a pathway element from its KGML+ string representation.
		 * @param value The KGML+ string representation.
		 * @return The type.
		 */
		public static Type fromValue(final String value) {
			if(value != null) {
				for(Type type : values())
					if(type.values.contains(value))
						return type;
			}
			throw new IllegalArgumentException("Invalid Type: " + value);
		}
	}
	
	@Id
	@Column(name="id")
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pathway_id", nullable = false, unique = false)
	private PersistentPathway pathway;
	
	@Column(name="xrefId", nullable=false)
	private String xrefId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false)
	private Type type;

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
	 * Gets the persistent pathway to which this element belongs.
	 * @return The persistent pathway.
	 */
	public PersistentPathway getPathway() {
		return pathway;
	}

	/**
	 * Sets the persistent pathway to which this element belongs.
	 * @param pathway The persistent pathway.
	 */
	public void setPathway(PersistentPathway pathway) {
		this.pathway = pathway;
	}

	/**
	 * Gets the external reference id for this pathway element.
	 * @return The external reference id.
	 */
	public String getXrefId() {
		return xrefId;
	}

	/**
	 * Sets the external reference id for this pathway element.
	 * @param xrefId The external reference id.
	 */
	public void setXrefId(String xrefId) {
		this.xrefId = xrefId;
	}

	/**
	 * Gets the type of this pathway element.
	 * @return The type of this pathway element.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the type of this pathway element.
	 * @param type The type of this pathway element.
	 */
	public void setType(Type type) {
		this.type = type;
	}	
}
