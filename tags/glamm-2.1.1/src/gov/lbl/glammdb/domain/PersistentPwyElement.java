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

@SuppressWarnings("serial")
@Entity
@Table(name="PwyElement")
public class PersistentPwyElement implements Serializable {
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PersistentPathway getPathway() {
		return pathway;
	}

	public void setPathway(PersistentPathway pathway) {
		this.pathway = pathway;
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
}
