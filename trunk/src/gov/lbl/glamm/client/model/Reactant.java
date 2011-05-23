package gov.lbl.glamm.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class Reactant extends ReactionParticipant implements Serializable {
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();

	@SuppressWarnings("unused")
	private Reactant() {}
	
	public Reactant(String compoundId, String coefficient, String role) {
		super(compoundId, coefficient, role);
	}
	
	@Override
	public Type getType() {
		return TYPE;
	}

}
