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

@SuppressWarnings("serial")
@Entity
@Table(name="AMRxnElement")
public class PersistentAMRxnElement implements Serializable {

	public enum Type {
		PRODUCT("product"),
		REACTION("reaction"),
		SUBSTRATE("substrate");

		private String value;

		private Type(final String value) {
			this.value = value;
		}

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

	public PersistentAMRxnElement() {}

	public PersistentAMRxnElement(final String xrefId, final Type type) {
		this.xrefId = xrefId;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReaction(PersistentAMRxn reaction) {
		this.reaction = reaction;
	}

	public PersistentAMRxn getReaction() {
		return reaction;
	}

	public String getXrefId() {
		return xrefId;
	}

	public void setXrefId(String xrefId) {
		this.xrefId = xrefId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.getType() + " " + this.getXrefId();
	}
}
