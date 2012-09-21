/**
 *
 */
package gov.lbl.glamm.client.experiment.model;

import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.util.XrefSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Model object representing a reaction.
 * @author DHAP Digital, Inc - angie
 *
 */
public class Reaction {
	// Information
	private String guid = null;
	private Set<String> ecNums = null;
	private String definition = null;
	private XrefSet xrefSet = null;

	// Children
	private ArrayList<Compound> mainReactants = null;
	private ArrayList<Compound> secondaryReactants = null;
	private ArrayList<Compound> mainProducts = null;
	private ArrayList<Compound> secondaryProducts = null;
	private ArrayList<Gene> genes = null;

	// Bean methods
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Set<String> getEcNums() {
		if ( ecNums == null ) {
			ecNums = new HashSet<String>();
		}
		return ecNums;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public XrefSet getXrefSet() {
		if ( xrefSet == null ) {
			xrefSet = new XrefSet();
		}
		return xrefSet;
	}

	public ArrayList<Compound> getMainReactants() {
		return getMainReactants(false);
	}
	public ArrayList<Compound> getMainReactants(boolean reset) {
		if ( reset == true || mainReactants == null ) {
			mainReactants = new ArrayList<Compound>();
		}
		return mainReactants;
	}
	public ArrayList<Compound> getSecondaryReactants() {
		return getSecondaryReactants(false);
	}
	public ArrayList<Compound> getSecondaryReactants(boolean reset) {
		if ( reset == true || secondaryReactants == null ) {
			secondaryReactants = new ArrayList<Compound>();
		}
		return secondaryReactants;
	}
	public ArrayList<Compound> getMainProducts() {
		return getMainProducts(false);
	}
	public ArrayList<Compound> getMainProducts(boolean reset) {
		if ( reset == true || mainProducts == null ) {
			mainProducts = new ArrayList<Compound>();
		}
		return mainProducts;
	}
	public ArrayList<Compound> getSecondaryProducts() {
		return getSecondaryProducts(false);
	}
	public ArrayList<Compound> getSecondaryProducts(boolean reset) {
		if ( reset == true || secondaryProducts == null ) {
			secondaryProducts = new ArrayList<Compound>();
		}
		return secondaryProducts;
	}
	public ArrayList<Gene> getGenes() {
		return getGenes(false);
	}
	public ArrayList<Gene> getGenes(boolean reset) {
		if ( reset == true || genes == null ) {
			genes = new ArrayList<Gene>();
		}
		return genes;
	}
}
