package gov.lbl.glamm.client.experiment.model;

import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.util.Synonym;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class ViewGene extends ViewSymbol<Gene> {

	public ViewGene( Gene baseGene ) {
		super( baseGene );
	}

	// Description methods
	/* (non-Javadoc)
	 * @see gov.lbl.glamm.experiment.client.model.ViewSymbol#toHtml()
	 */
	@Override
	public String toHtml() {
		StringBuilder builder = new StringBuilder();
		builder.append( "<span class='attributeName'>ecNums</span>: <span class='attribute'>" )
				.append( baseObject.getEcNums() )
				.append( "</span><br/><span class='attributeName'>mol taxonomy ids</span>: <span class='attribute'>" )
				.append( baseObject.getMolTaxonomyIds() )
				.append( "</span><br/><span class='attributeName'>meta mol taxonomy ids</span>: <span class='attribute'>" )
				.append( baseObject.getMetaMolTaxonomyIds() );
		builder.append( "</span><br/><span class='attributeName'>synonym</span>: <span class='attribute'>" );
		for ( Synonym synonym : baseObject.getSynonyms() ) {
			builder.append( synonym.getType() ).append( ": " )
					.append( synonym.getName() ).append( "; " );
		}
		builder.append( "</span>" );

		return builder.toString();
	}

}
