package gov.lbl.glamm.client.experiment.model;

import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.util.Synonym;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class ViewCompound extends ViewSymbol<Compound> {

	public ViewCompound( Compound baseCompound ) {
		super( baseCompound );
	}

	public static final String MAIN_ROLE = "main";

	public static boolean isMainRole(String roleStr) {
		if ( roleStr != null && roleStr.equals( MAIN_ROLE ) ) {
			return true;
		} else {
			return false;
		}
	}

	// Description methods
	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.model.ViewSymbol#toHtml()
	 */
	@Override
	public String toHtml() {
		StringBuilder builder = new StringBuilder();
		builder.append("<span class='title'>").append( this.baseObject.getName() )
			.append("</span><br/><span class='subtitle'>")
			.append( this.baseObject.getFormula() )
			.append("</span><br/><span class='attributeName'>mass:</span> <span class='attribute'>")
			.append( this.baseObject.getMass() )
			.append("</span><br/><span class='attributeName'>inchi:</span> <span class='attribute'>")
			.append( this.baseObject.getInchi() )
			.append("</span><br/><span class='attributeName'>smiles:</span> <span class='attribute'>")
			.append( this.baseObject.getSmiles() )
			.append("</span><br/><span class='attributeName'>guid:</span> <span class='attribute'>")
			.append( this.baseObject.getGuid() )
			.append("</span>")
			.append("</span><br/><span class='attributeName'>synonyms:</span> <span class='attribute'>");
		for ( Synonym synonym : baseObject.getSynonyms() ) {
			builder.append( synonym.getType() ).append( ": " )
				.append( synonym.getName() ).append( "; " );
		}
		builder
			.append("</span>")
			.append("</span><br/><span class='attributeName'>xrefs:</span> <span class='attribute'>")
			.append( this.baseObject.getXrefSet().getXrefs() )
			.append("</span>")
			;
		if ( this.baseObject.getPathwayLinks().size() > 0 ) {
			builder.append("<br/><span class='attributeName'>Pathway links</span>:");
			for ( gov.lbl.glamm.shared.model.Pathway pathwayLink
					: this.baseObject.getPathwayLinks() ) {
				builder.append( "<br/>" )
					.append( pathwayLink.getMapId() ).append( ": " )
					.append( pathwayLink.getName() ).append( "; " )
				;
			}
		}
		return builder.toString();
	}
}
