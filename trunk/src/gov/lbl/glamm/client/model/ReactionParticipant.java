package gov.lbl.glamm.client.model;


import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class ReactionParticipant extends GlammPrimitive implements Serializable {

	public static final String TYPE_REACTANT	= "REACTANT";
	public static final String TYPE_PRODUCT 	= "PRODUCT";

	public static final String ROLE_MAIN		= "main";
	public static final String ROLE_OTHER		= "other";

	private String compoundId;
	private String coefficient;
	private String role				= ROLE_OTHER;

	protected ReactionParticipant() {}

	public ReactionParticipant(String compoundId, String coefficient, String role ) {
		this.compoundId 	= compoundId;
		this.coefficient 	= coefficient;
		this.role			= role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((coefficient == null) ? 0 : coefficient.hashCode());
		result = prime * result
		+ ((compoundId == null) ? 0 : compoundId.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReactionParticipant other = (ReactionParticipant) obj;
		if (coefficient == null) {
			if (other.coefficient != null)
				return false;
		} else if (!coefficient.equals(other.coefficient))
			return false;
		if (compoundId == null) {
			if (other.compoundId != null)
				return false;
		} else if (!compoundId.equals(other.compoundId))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	public String getExtIdName() {
		return null;
	}

	public String getIdPrefix() {
		return null;
	}
}
