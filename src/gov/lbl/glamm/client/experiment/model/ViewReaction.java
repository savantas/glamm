package gov.lbl.glamm.client.experiment.model;

import java.util.ArrayList;

/**
 * View object class with in-pathway reactant/product types, length calculations,
 * and directionality of the reaction.
 * Contains a reference to the base Reaction on which the view is modeled.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class ViewReaction {
	/**
	 * View placement: note that measurements are generally taken from centers of drawn objects
	 */
	private float totalLength = 0;
	/**
	 * Relative Y for (first) gene placement, whether there is a gene or not.
	 */
	private float genePlacementY = 0;

	// Main data
	public static final int BOTH_DIRECTION = 0;
	public static final int REVERSE_DIRECTION = -1;
	public static final int FORWARD_DIRECTION = 1;
	public static final int UNSPECIFIED_DIRECTION = Integer.MAX_VALUE;
	private Reaction baseReaction = null;
	private int direction = 0;

	// Children
	private ViewCompound mainReactant = null;
	private ArrayList<ViewCompound> otherMainReactants = null;
	private ArrayList<ViewCompound> secondaryReactants = null;

	private ViewCompound mainProduct = null;
	private ArrayList<ViewCompound> otherMainProducts = null;
	private ArrayList<ViewCompound> secondaryProducts = null;

	private ArrayList<ViewGene> genes = null;

	// Instantiation methods

	// Description methods
	public String toHtml(boolean includeGenes) {
		StringBuilder builder = new StringBuilder();
		builder.append("<span class='type'>ecNums</span>: <span class='title'>")
			.append( baseReaction.getEcNums() )
			.append("</span><br/><span class='attributeName'>definition</span>: <span class='attribute'>")
			.append( baseReaction.getDefinition() )
			.append("</span><br/><span class='attributeName'>id</span>: <span class='attribute'>")
			.append( baseReaction.getGuid() )
			.append("</span><br/><span class='attributeName'>xrefs</span>: <span class='attribute'>")
			.append( baseReaction.getXrefSet().getXrefs() )
			.append("</span>")
			;
		if ( includeGenes ) {
			builder.append("<br/><div class='child'><span class='type'>Genes</span>:");
			for ( ViewGene gene : genes ) {
				builder.append("<br/>").append(gene.toHtml());
			}
			builder.append("</div>");
		}
		return builder.toString();
	}

	// Helper methods
	public void clearMainReactant() {
		this.mainReactant = null;
	}
	public void setMainReactant( ViewCompound viewReactant ) {
		this.mainReactant = viewReactant;
	}
	public void addOtherMainReactant( ViewCompound viewReactant ) {
		this.getOtherMainReactants().add( viewReactant );
	}

	public void clearMainProduct() {
		this.mainProduct = null;
	}
	public void addOtherMainProduct( ViewCompound viewProduct ) {
		this.getOtherMainProducts().add( viewProduct );
	}

	// Bean methods
	public float getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(float totalLength) {
		this.totalLength = totalLength;
	}

	public float getGenePlacementY() {
		return genePlacementY;
	}
	public void setGenePlacementY(float genePlacementY) {
		this.genePlacementY = genePlacementY;
	}

	public Reaction getBaseReaction() {
		return baseReaction;
	}

	public void setBaseReaction(Reaction baseReaction) {
		this.baseReaction = baseReaction;
	}

	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public void setDirection(String directionStr) {
		this.direction = UNSPECIFIED_DIRECTION;
		if ( directionStr.equals("forward") ) {
			this.direction = FORWARD_DIRECTION;
		} else if ( directionStr.equals("reverse") ) {
			this.direction = REVERSE_DIRECTION;
		} else if ( directionStr.equals("both") ) {
			this.direction = BOTH_DIRECTION;
		}
	}

	public ViewCompound getMainReactant() {
		return mainReactant;
	}

	public void clearOtherMainReactants() {
		otherMainReactants = new ArrayList<ViewCompound>();
	}
	public ArrayList<ViewCompound> getOtherMainReactants() {
		return getOtherMainReactants(false);
	}
	private ArrayList<ViewCompound> getOtherMainReactants(boolean reset) {
		if ( reset == true || otherMainReactants == null ) {
			otherMainReactants = new ArrayList<ViewCompound>();
		}
		return otherMainReactants;
	}
	public ArrayList<ViewCompound> getSecondaryReactants() {
		return this.getSecondaryReactants(false);
	}
	public ArrayList<ViewCompound> getSecondaryReactants(boolean reset) {
		if ( reset == true || secondaryReactants == null ) {
			secondaryReactants = new ArrayList<ViewCompound>();
		}
		return secondaryReactants;
	}

	public ViewCompound getMainProduct() {
		return mainProduct;
	}
	public void setMainProduct(ViewCompound mainProduct) {
		this.mainProduct = mainProduct;
	}

	public void clearOtherMainProducts() {
		otherMainProducts = new ArrayList<ViewCompound>();
	}
	public ArrayList<ViewCompound> getOtherMainProducts() {
		return this.getOtherMainProducts(false);
	}
	private ArrayList<ViewCompound> getOtherMainProducts(boolean reset) {
		if ( reset == true || otherMainProducts == null ) {
			otherMainProducts = new ArrayList<ViewCompound>();
		}
		return otherMainProducts;
	}
	public ArrayList<ViewCompound> getSecondaryProducts() {
		return this.getSecondaryProducts(false);
	}
	public ArrayList<ViewCompound> getSecondaryProducts(boolean reset) {
		if ( reset == true || secondaryProducts == null ) {
			secondaryProducts = new ArrayList<ViewCompound>();
		}
		return secondaryProducts;
	}
	public ArrayList<ViewGene> getGenes() {
		if ( genes == null ) {
			genes = new ArrayList<ViewGene>();
		}
		return genes;
	}
}
