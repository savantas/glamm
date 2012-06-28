package gov.lbl.glammdb.domain;

import java.io.Serializable;

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
 * Persistent class for storing reaction elements.  A persistent reaction element may be a product, a reaction, or a substrate.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AMRxnElement")
public class PersistentAMRxnElement implements Serializable {

	/**
	 * Type of reaction element.
	 * @author jtbates
	 *
	 */
	public enum Type {
		PRODUCT("product"),
		REACTION("reaction"),
		SUBSTRATE("substrate");

		private String value;

		private Type(final String value) {
			this.value = value;
		}

		/**
		 * Gets a Type from a string representation.
		 * @param value The string representation of the type.
		 * @return The type.
		 */
		public static Type fromValue(final String value) {
			if(value != null) {
				for(Type type : values())
					if(type.value.equals(value))
						return type;
			}
			throw new IllegalArgumentException("Invalid Type: " + value);
		}

		@Override
		public String toString() {
			return value;
		}
	
	}

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "amRxn_id", nullable = false, unique = false)
	private PersistentAMRxn reaction;

	@Column(name="xrefId", nullable=false)
	private String xrefId;

	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false)
	private Type type;

	/**
	 * Constructor
	 */
	public PersistentAMRxnElement() {}

	/**
	 * Constructor
	 * @param xrefId The external reference id (e.g. KEGG id) of the element.
	 * @param type The type of the element.
	 */
	public PersistentAMRxnElement(final String xrefId, final Type type) {
		this.xrefId = xrefId;
		this.type = type;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id The id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the reaction.
	 * @param reaction The reaction.
	 */
	public void setReaction(PersistentAMRxn reaction) {
		this.reaction = reaction;
	}

	/**
	 * Gets the reaction.
	 * @return The reaction.
	 */
	public PersistentAMRxn getReaction() {
		return reaction;
	}

	/**
	 * Gets the external reference id.
	 * @return The external reference id.
	 */
	public String getXrefId() {
		return xrefId;
	}

	/**
	 * Sets the external reference id.
	 * @param xrefId The external reference id.
	 */
	public void setXrefId(String xrefId) {
		this.xrefId = xrefId;
	}

	/**
	 * Gets the reaction element type.
	 * @return The type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the reaction element type.
	 * @param type The type.
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.getType() + " " + this.getXrefId();
	}
}
