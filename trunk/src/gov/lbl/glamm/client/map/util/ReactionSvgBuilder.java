package gov.lbl.glamm.client.map.util;

import java.util.Set;
import java.util.HashSet;

import gov.lbl.glamm.shared.model.AnnotatedMapData;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.AnnotatedMapData.Attribute;
import gov.lbl.glamm.shared.model.AnnotatedMapData.ElementClass;
import gov.lbl.glamm.shared.model.AnnotatedMapData.State;
import gov.lbl.glamm.shared.model.interfaces.HasXrefs;
import gov.lbl.glamm.shared.model.util.Xref;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGEllipseElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

public class ReactionSvgBuilder {

	private static final float CPD_RADIUS = 7.0f;
	private static final String CPD_STROKE_WIDTH = "1";
	private static final String RXN_STROKE_WIDTH = "1";
	private static final float MARGIN_X = 50.0f; // SVG units
	private static final float MARGIN_Y = 50.0f; // SVG units 
	private static final float REACTION_WIDTH = CPD_RADIUS * 4 + 200; // SVG units
	private static final float REACTION_HEIGHT_MULT = 80;
	private static final String DEFAULT_CPD_COLOR = SVGConstants.CSS_WHITE_VALUE;
	private static final String DEFAULT_RXN_COLOR = SVGConstants.CSS_WHITE_VALUE;
	private static final String PREFERRED_RXN_DB = "LIGAND-RXN";
	private static final String PREFERRED_CPD_DB = "LIGAND-CPD";
	private static final String RXN_MITER_LIMIT = "10";
	
	public static final String USER_GROUP_ID = "user_reactions";
	
	public static OMElement buildReactionSvg(Set<Reaction> reactions, AnnotatedMapData mapData, 
											 float canvasX, float canvasY, float maxWidth, float maxHeight) {
		/**
		 * Assume that the given set of reactions is NOT a part of mapData. That check should be done elsewhere.
		 * 
		 * 1. Build SVG elements for all reactions.
		 * 2. Attach the root of our new SVG to mapData.getViewport()
		 * 3. Integrate the set of reactions for lookup by the mapData.
		 */
		
		/**
		 * canvasX, canvasY, maxWidth & maxHeight represent the additional "canvas" space for adding reactions.
		 * eventually, all added reactions should fit in that space.
		 * for now, keep max height, but ignore width.
		 */
		
		// make new svg "root" (g element) for this
		OMElement group = ((OMSVGDocument)(mapData.getSvg().getOwnerDocument())).createSVGGElement();
		group.setAttribute(Attribute.ID, USER_GROUP_ID);
		
		float curX = canvasX + MARGIN_X;
		float curY = canvasY + MARGIN_Y;
		float width = REACTION_WIDTH;
		
		for (Reaction reaction : reactions) {
			float height = buildReactionSvg(group, reaction, mapData, curX, curY, width);
			curY += height + MARGIN_Y;
			if (curY > maxHeight - MARGIN_Y)
			{
				curY = canvasY + MARGIN_Y;
				curX += width + MARGIN_X;
			}
		}
		
		group.setAttribute(Attribute.WIDTH, String.valueOf(curX + width + MARGIN_X));
		group.setAttribute(Attribute.HEIGHT, String.valueOf(maxHeight));
		
		
		return group;
	}
	
	/** Builds an SVG for a reaction (and its compounds)
	 *  @return the height that the new reaction occupies
	 */
	private static float buildReactionSvg(OMElement group, Reaction reaction, AnnotatedMapData mapData,
									     float x, float y, float width) {
		Set<Reaction.Participant> substrates = reaction.getSubstrates();
		Set<Reaction.Participant> products = reaction.getProducts();
		
		float height = (float)(Math.max(Math.floor(substrates.size()/2), Math.floor(products.size()/2))) * REACTION_HEIGHT_MULT;
		
		OMSVGDocument owner = (OMSVGDocument)(mapData.getSvg().getOwnerDocument());
	
		// centered join point for reaction path
		float joinX = x + width/2,
			  joinY = y + height/2;
		
		OMElement rxnPathGroup = owner.createSVGGElement();
		rxnPathGroup.setAttribute(Attribute.CLASS, ElementClass.RXN.getCssClass());
		rxnPathGroup.setAttribute(Attribute.REACTION, getXrefId(reaction, PREFERRED_RXN_DB));
		String ecSet = "";
		Set<String> ecNums = reaction.getEcNums();
		if (ecNums.size() > 0) {
			int i=0;
			for (String ecNum : ecNums) {
				if (i == ecNums.size() - 1)
					ecSet += ecNum;
				else
					ecSet += ecNum + "+";
				i++;
			}
		}
		if (ecSet.length() != 0) {
			rxnPathGroup.setAttribute(Attribute.ENZYME, ecSet);
		}

		Set<OMElement> cpdElements = new HashSet<OMElement>();
		
		
		populateReactionSide(owner, joinX, joinY, x, substrates, rxnPathGroup, cpdElements);
		populateReactionSide(owner, joinX, joinY, x + width, products, rxnPathGroup, cpdElements);
		
		bindMouseHandlers(rxnPathGroup, SVGConstants.SVG_PATH_TAG);
		group.appendChild(rxnPathGroup);
		for (OMElement cpd : cpdElements) {
			bindMouseHandlers(cpd, SVGConstants.SVG_ELLIPSE_TAG);
			group.appendChild(cpd);
		}
		
		return height;
	}
	
	private static void populateReactionSide(OMSVGDocument owner, float centerX, float centerY, float x, Set<Reaction.Participant> cpds, OMElement rxnPathGroup, Set<OMElement> cpdElements) {
		Reaction.Participant[] cpdArr = cpds.toArray(new Reaction.Participant[0]);
		
		for (int i=0; i<cpdArr.length-1; i+=2) {
			// Do above and below substrates
			float hFromCenter = 20*i + 40;
			rxnPathGroup.appendChild(buildReactionPathSvg(owner, centerX, centerY, x, centerY - hFromCenter));
			cpdElements.add(buildCompoundSvg(owner, cpdArr[i], x, centerY - hFromCenter));
			
			rxnPathGroup.appendChild(buildReactionPathSvg(owner, centerX, centerY, x, centerY + hFromCenter));
			cpdElements.add(buildCompoundSvg(owner, cpdArr[i+1], x, centerY + hFromCenter));
		}
		if (cpdArr.length % 2 != 0) {
			// Do middle
			rxnPathGroup.appendChild(buildReactionPathSvg(owner, centerX, centerY, x, centerY));
			cpdElements.add(buildCompoundSvg(owner, cpdArr[cpdArr.length-1], x, centerY));
		}
	}
	
	private static String getXrefId(HasXrefs obj, String preferred)
	{
		int i=0;
		String id = "";
		
		// by default, return either the preferred id, or combine all the rest (if there are any).
		
		Xref prefXref = obj.getXrefSet().getXrefForDbName(preferred);
		if (prefXref == null) {
			Set<Xref> xRefs = obj.getXrefSet().getXrefs();
			for (Xref ref : xRefs)
			{
				if (i == xRefs.size()-1)
					id += ref.getXrefId();
				else
					id += ref.getXrefId() + "+";
				i++;
			}
		}
		else
			id = prefXref.getXrefId();
		
		return id;
	}
	
	private static OMSVGPathElement buildReactionPathSvg(OMSVGDocument owner, float startX, float startY, float endX, float endY) {
		OMSVGPathElement p = owner.createSVGPathElement();
		p.setAttribute(Attribute.CLASS, ElementClass.RXN.getCssClass());
		p.setAttribute(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_NONE_VALUE);
		p.setAttribute(Attribute.STATE, State.DEFAULT);
		p.setAttribute(SVGConstants.CSS_STROKE_PROPERTY, DEFAULT_RXN_COLOR);
		p.setAttribute(SVGConstants.CSS_STROKE_LINECAP_PROPERTY, SVGConstants.CSS_SQUARE_VALUE);
		p.setAttribute(SVGConstants.CSS_STROKE_MITERLIMIT_PROPERTY, RXN_MITER_LIMIT);
		p.setAttribute(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, RXN_STROKE_WIDTH);
	
		
		String d = buildSCurveBezier(startX, startY, endX, endY);
		
		p.setAttribute(SVGConstants.SVG_D_ATTRIBUTE, d);
		
		return p;
	}

	private static String buildLinePath(float startX, float startY, float endX, float endY) {
		return "M " + startX + " " + startY + " L " + endX + " " + endY;
	}
	
	private static String buildSCurveBezier(float startX, float startY, float endX, float endY) {
		
		// If there's not enough room to make the curve, just do a straight line.
		if (Math.abs(endY-startY) < 40 || Math.abs(endX-startX) < 40)
			return buildLinePath(startX, startY, endX, endY);

		/* Constructing (up to) 8 points.
		 * 4 bezier control points
		 * from 1 to 4 line points
		 *
		 * Each bezier curve is a 90 degree turn through a 20 x 20 area, with control points at the halfway mark of each side.
		 * Up to 3 lines (1 vertical, and 2 horizontal) are used to keep the curves in a specific position
		 */

		float xDir = 1;
		if (endX < startX)
			xDir = -1;
		
		float yDir = 1;
		if (endY < startY)
			yDir = -1;
		
		float wBar = 0;
		if (Math.abs(endX-startX) > 40)
			wBar = (Math.abs(endX-startX) - 40)/2;
		
		float hBar = 0;
		if (Math.abs(endY-startY) > 40)
			hBar = (Math.abs(endY-startY) - 40);
		
		StringBuilder builder = new StringBuilder("M " + startX + " " + startY + " ");
		
		float curX = startX;
		float curY = startY;
		
		if (wBar > 0) {
			curX += wBar * xDir;
			builder.append("L " + curX + " " + curY + " ");
		}
		
		float ctrlX1 = curX + 10 * xDir;
		float ctrlY1 = curY;
		float ctrlX2 = curX + 20 * xDir;
		float ctrlY2 = curY + 10 * yDir;
		
		curX += 20 * xDir;
		curY += 20 * yDir;
		builder.append("C " + ctrlX1 + " " + ctrlY1 + " " + ctrlX2 + " " + ctrlY2 + " " + curX + " " + curY + " ");
		
		if (hBar > 0) {
			curY += hBar * yDir;
			builder.append("L " + curX + " " + curY + " ");
		}
		
		ctrlX1 = curX;
		ctrlY1 = curY + 10 * yDir;
		ctrlX2 = curX + 10 * xDir;
		ctrlY2 = curY + 20 * yDir;
		
		curX += 20 * xDir;
		curY += 20 * yDir;
		builder.append("C " + ctrlX1 + " " + ctrlY1 + " " + ctrlX2 + " " + ctrlY2 + " " + curX + " " + curY + " ");
		
		if (wBar > 0) {
			curX += wBar * xDir;
			builder.append("L " + curX + " " + curY + " ");
		}
		
		return builder.toString();
	}
	
	private static OMElement buildCompoundSvg(OMSVGDocument owner, Reaction.Participant cpd, float x, float y) {
		OMElement g = owner.createSVGGElement();
		g.setAttribute(Attribute.CLASS, ElementClass.CPD.getCssClass());
		g.setAttribute(Attribute.COMPOUND, getXrefId(cpd.getCompound(), PREFERRED_CPD_DB));
		
		OMSVGEllipseElement ellipse = owner.createSVGEllipseElement(x, y, CPD_RADIUS, CPD_RADIUS);
		ellipse.setAttribute(Attribute.DEFAULT_COLOR, SVGConstants.CSS_WHITE_VALUE);
		ellipse.setAttribute(Attribute.CLASS, ElementClass.CPD.getCssClass());
		ellipse.setAttribute(SVGConstants.CSS_FILL_PROPERTY, DEFAULT_CPD_COLOR);
		ellipse.setAttribute(SVGConstants.CSS_STROKE_PROPERTY, DEFAULT_CPD_COLOR);
		ellipse.setAttribute(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, CPD_STROKE_WIDTH);
		ellipse.setAttribute(Attribute.STATE, State.DEFAULT);
		
		g.appendChild(ellipse);
		
		return g;
	}
	
	private static void bindMouseHandlers(OMElement g, final String tagName) {
		((HasMouseOverHandlers)g).addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				Element element = event.getRelativeElement();
				if (element != null) {
					
					NodeList<Element> siblings = element.getElementsByTagName(tagName);
					for (int i=0; i<siblings.getLength(); i++) {
						final Element sibling = siblings.getItem(i);
						if(!sibling.hasAttribute(AnnotatedMapData.Attribute.STATE) ||
								!sibling.getAttribute(AnnotatedMapData.Attribute.STATE).equals(State.SELECTED))
							sibling.setAttribute(AnnotatedMapData.Attribute.STATE, State.MOUSEOVER);
					}
				}
			}
		});
		
		((HasMouseOutHandlers)g).addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				Element element = event.getRelativeElement();
				if(element != null) {
					NodeList<Element> siblings = element.getElementsByTagName(tagName);
					for(int i = 0; i < siblings.getLength(); i++) {
						final Element sibling = siblings.getItem(i);
						if(!sibling.hasAttribute(AnnotatedMapData.Attribute.STATE) || 
								!sibling.getAttribute(AnnotatedMapData.Attribute.STATE).equals(State.SELECTED))
							sibling.setAttribute(AnnotatedMapData.Attribute.STATE, State.DEFAULT);
					}
				}
			}
		});
	}
}