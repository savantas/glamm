package gov.lbl.glamm.client.experiment.dao;

import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;
import gov.lbl.glamm.client.experiment.model.Reaction;
import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.model.ViewGene;
import gov.lbl.glamm.client.experiment.model.ViewReaction;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.client.experiment.util.ObjectCount;
import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Gene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class DefaultPathwayDao implements PathwayDao {

	@Override
	public void addPathwayData( List<ViewPathway> pathways
			, BinarySortedSet<ViewCompound> viewSecondaryMetaboliteSet
			, PathwayExperimentData sharedData
			, ObjectCount objCount ) {
		BinarySortedSet<Compound> secondaryMetaboliteSet = new BinarySortedSet<Compound>(
				new Comparator<Compound>() {
					@Override
					public int compare( Compound o1, Compound o2 ) {
						return o1.getGuid().compareTo(o2.getGuid());
					}
				}
		);

		objCount.primaryObjCount += sharedData.getIdCompoundMap().size();

		objCount.primaryObjCount += sharedData.getIdGeneMap().size();

		for ( gov.lbl.glamm.shared.model.Pathway sharedPathway : sharedData.getPathways() ) {
			ViewPathway pathway = new ViewPathway();
			objCount.primaryObjCount++;
			pathway.setId( sharedPathway.getMapId() );
			pathway.setName( sharedPathway.getName() );

			List<ViewReaction> viewReactions = pathway.getViewReactions();
			objCount.listCount++;
			for ( gov.lbl.glamm.shared.model.Reaction sharedReaction : sharedPathway.getReactions() ) {
				// base reaction
				Reaction baseReaction = createReaction(
						secondaryMetaboliteSet, sharedReaction
						, sharedData.getIdCompoundMap()
						, sharedData.getIdGeneMap()
						, objCount );

				// view reaction
				ViewReaction viewReaction = new ViewReaction();
				objCount.viewObjCount++;
				viewReaction.setBaseReaction( baseReaction );
				viewReaction.setDirection( sharedReaction.getDirection().toString() );

				// don't populate view reaction compounds now;
				//		do it after all viewReaction objects are created

				// populate view genes
				populateViewGenes( viewReaction, objCount );

				viewReactions.add( viewReaction );

			}

			// populate view reaction compounds
			ViewReaction before = null;
			ViewReaction after = null;
			for ( int i=0; i < viewReactions.size(); i++ ) {
				ViewReaction viewReaction = viewReactions.get(i);
				if ( i+1 < viewReactions.size() ) {
					after = viewReactions.get(i+1);
				} else {
					after = null;
				}
				populateViewReactionCompounds( viewReaction, before, after, objCount );

				before = viewReaction;
			}
			pathways.add( pathway );
		}

		// Convert secondary metabolite set of Compounds to set of ViewCompounds
		for ( Compound secondaryMetabolite : secondaryMetaboliteSet ) {
			ViewCompound viewCompound = new ViewCompound( secondaryMetabolite );
			objCount.viewObjCount++;
			viewSecondaryMetaboliteSet.add( viewCompound );
		}
	}

	/**
	 * Main entry point for populating ViewReaction compounds.
	 * Handles macro logic regarding mainReactants and mainProducts
	 * and calls the other populate compound methods.
	 * <br />
	 * Assumption: before (ViewReaction object) already had found it's
	 *  mainProduct if one exists (matching reactant in this viewReaction).
	 * @param viewReaction
	 * @param before
	 * @param after
	 * @param objCount
	 */
	protected void populateViewReactionCompounds( ViewReaction viewReaction
			, ViewReaction before
			, ViewReaction after
			, ObjectCount objCount
	) {
		Reaction baseReaction = viewReaction.getBaseReaction();

		ArrayList<Compound> mainReactants = baseReaction.getMainReactants();
		objCount.listCount++;
		populateMainViewReactants( viewReaction
				, mainReactants
				, (before == null)	// isFirstReaction
				, (before == null ? null : before.getMainProduct())
				, objCount
		);
		populateSecondaryViewReactants( viewReaction, objCount );
		ArrayList<Compound> mainProducts = baseReaction.getMainProducts();
		populateMainViewProducts( viewReaction
				, mainProducts
				, (after == null)	// isLastReaction
				, (after == null ? null : after.getBaseReaction().getMainReactants())
				, objCount
		);
		populateSecondaryViewProducts( viewReaction, objCount );
	}
	private void populateMainViewReactants( ViewReaction viewReaction
			, List<Compound> mainReactants
			, boolean isFirstReaction
			, ViewCompound beforeMainProduct
			, ObjectCount objCount
	) {
		viewReaction.clearMainReactant();
		viewReaction.clearOtherMainReactants();
		objCount.listCount++;
		if ( isFirstReaction ) {
			// just pick the first main reactant as mainline one
				int index = 0;
				for ( Compound mainReactant : mainReactants ) {
					ViewCompound viewMainReactant = new ViewCompound( mainReactant );
					objCount.viewObjCount++;
					if ( index == 0 ) {
						// Main in-line reactant
						viewReaction.setMainReactant( viewMainReactant );
					} else {
						// Other main reactant
						viewReaction.addOtherMainReactant( viewMainReactant );
					}
					index++;
				}
		} else if ( beforeMainProduct == null ) {
			for ( Compound mainReactant : mainReactants ) {
				ViewCompound viewMainReactant = new ViewCompound( mainReactant );
				viewReaction.addOtherMainReactant( viewMainReactant );
				objCount.viewObjCount++;
			}
		} else {
			for ( Compound reactant : mainReactants ) {
				objCount.viewObjCount++;
				if ( reactant.getGuid().equals(beforeMainProduct.getBaseObject().getGuid()) ) {
					ViewCompound viewMainReactant = new ViewCompound( reactant );
					viewReaction.setMainReactant( viewMainReactant );
					objCount.viewObjCount++;
				} else {
					ViewCompound viewMainReactant = new ViewCompound( reactant );
					viewReaction.addOtherMainReactant( viewMainReactant );
					objCount.viewObjCount++;
				}
			}
		}
	}
	private void populateMainViewProducts( ViewReaction viewReaction
			, List<Compound> mainProducts
			, boolean isLastReaction
			, List<Compound> afterReactants, ObjectCount objCount
	) {
		viewReaction.clearMainProduct();
		viewReaction.clearOtherMainProducts();
		objCount.listCount++;

		if ( isLastReaction ) {
			// just pick the first main product as mainline one
				int index = 0;
				for ( Compound mainProduct : mainProducts ) {
					ViewCompound viewMainProduct = new ViewCompound( mainProduct );
					objCount.viewObjCount++;
					if ( index == 0 ) {
						viewReaction.setMainProduct( viewMainProduct );
					} else {
						viewReaction.addOtherMainProduct( viewMainProduct );
					}
					index++;
				}
		} else if ( afterReactants.size() == 0 ) {
			for ( Compound mainProduct : mainProducts ) {
				ViewCompound viewMainProduct = new ViewCompound( mainProduct );
				viewReaction.addOtherMainProduct( viewMainProduct );
				objCount.viewObjCount++;
			}
		} else {
			boolean mainlineProductFound = false;
			for ( Compound product : mainProducts ) {
				ViewCompound viewMainProduct = new ViewCompound( product );
				objCount.viewObjCount++;

				// look for first mainline product match in next reaction's reactants
				boolean isOtherMainProduct = true;
				if ( !mainlineProductFound ) {
					for ( Compound afterReactant : afterReactants ) {
						if ( product.getGuid().equals(afterReactant.getGuid()) ) {
							// if a match is found
							// set it as the main product
							viewReaction.setMainProduct(
									viewMainProduct
							);
							objCount.viewObjCount++;
							// don't add to "other main product" list
							isOtherMainProduct = false;
							// indicate that no further mainline product searches needed
							mainlineProductFound = true;
							break;
						}
					}
				}

				if ( isOtherMainProduct ) {
					viewReaction.addOtherMainProduct( viewMainProduct );
				}
			}
		}
	}
	private void populateSecondaryViewReactants( ViewReaction viewReaction
			, ObjectCount objCount ) {
		ArrayList<ViewCompound> secondaryReactants = viewReaction.getSecondaryReactants(true);
		objCount.listCount++;
		ArrayList<Compound> baseSecondaryReactants = viewReaction.getBaseReaction().getSecondaryReactants();
		addViewCompounds( secondaryReactants, baseSecondaryReactants, objCount );
	}
	private void populateSecondaryViewProducts( ViewReaction viewReaction
			, ObjectCount objCount ) {
		ArrayList<ViewCompound> secondaryProducts = viewReaction.getSecondaryProducts(true);
		objCount.listCount++;
		ArrayList<Compound> baseSecondaryProducts = viewReaction.getBaseReaction().getSecondaryProducts();
		addViewCompounds( secondaryProducts, baseSecondaryProducts, objCount );
	}
	private void addViewCompounds( ArrayList<ViewCompound> viewCompounds
			, List<Compound> baseCompounds, ObjectCount objCount ) {
		for ( Compound baseCompound : baseCompounds ) {
			viewCompounds.add( new ViewCompound( baseCompound ) );
			objCount.viewObjCount++;
		}
	}

	public static void populateViewGenes( ViewReaction viewReaction
			, ObjectCount objCount ) {
		for ( Gene gene : viewReaction.getBaseReaction().getGenes() ) {
			viewReaction.getGenes().add( new ViewGene(gene) );
			objCount.viewObjCount++;
		}
	}

	/**
	 * @param secondaryMetaboliteSet TODO
	 * @param sharedReaction
	 * @param idCompoundMap
	 * @param idGeneMap
	 * @param objCount for counting created objects, including the Reaction object returned
	 * @return
	 */
	private Reaction createReaction(
			BinarySortedSet<Compound> secondaryMetaboliteSet
			, gov.lbl.glamm.shared.model.Reaction sharedReaction
			, Map<String,Compound> idCompoundMap
			, Map<String,Gene> idGeneMap, ObjectCount objCount
	) {
		// base reaction
		Reaction baseReaction = new Reaction();
		objCount.primaryObjCount++;
		baseReaction.setDefinition( sharedReaction.getDefinition() );
		baseReaction.getEcNums().addAll( sharedReaction.getEcNums() );
		baseReaction.getXrefSet().addXrefs( sharedReaction.getXrefSet().getXrefs() );
		baseReaction.setGuid( sharedReaction.getGuid() );

		objCount.listCount++;
		for ( gov.lbl.glamm.shared.model.Reaction.Participant sharedSubstrate : sharedReaction.getSubstrates() ) {
			Compound baseSubstrate = idCompoundMap.get( sharedSubstrate.getCompound().getGuid() );
			if ( sharedSubstrate.getRole() == gov.lbl.glamm.shared.model.Reaction.Participant.KeggRpairRole.MAIN ) {
				baseReaction.getMainReactants().add( baseSubstrate );
			} else {
				baseReaction.getSecondaryReactants().add( baseSubstrate );
				secondaryMetaboliteSet.add( baseSubstrate );
			}
		}

		objCount.listCount++;
		for ( gov.lbl.glamm.shared.model.Reaction.Participant sharedProduct : sharedReaction.getProducts() ) {
			Compound baseProduct = idCompoundMap.get( sharedProduct.getCompound().getGuid() );
			if ( sharedProduct.getRole() == gov.lbl.glamm.shared.model.Reaction.Participant.KeggRpairRole.MAIN ) {
				baseReaction.getMainProducts().add( baseProduct );
			} else {
				baseReaction.getSecondaryProducts().add( baseProduct );
				secondaryMetaboliteSet.add( baseProduct );
			}
		}

		objCount.listCount++;
		for ( gov.lbl.glamm.shared.model.Gene sharedGene : sharedReaction.getGenes() ) {
			Gene gene = idGeneMap.get( sharedGene.getVimssId() );
			baseReaction.getGenes().add( gene );
		}

		return baseReaction;
	}
}
