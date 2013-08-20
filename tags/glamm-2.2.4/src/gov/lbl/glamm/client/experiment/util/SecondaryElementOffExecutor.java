package gov.lbl.glamm.client.experiment.util;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMSVGUseElement;

/**
 * Execute turning off highlighting of selected secondary metabolite SVG elements generically
 * (as for use within generic search algorithms).
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class SecondaryElementOffExecutor
		implements Executor<OMSVGGElement> {
	private static final SecondaryElementOffExecutor instance
			= new SecondaryElementOffExecutor();
	public static SecondaryElementOffExecutor instance() {
		return instance;
	}

	private SecondaryElementOffExecutor() {
	}

	@Override
	public void execute(OMSVGGElement obj) {
		OMNodeList<OMNode> children = obj.getChildNodes();
		for ( int i=0; i< children.getLength(); i++ ) {
			OMNode child = children.getItem(i);
			if ( child.getNodeName().equals("circle") ) {
				OMSVGCircleElement circle = (OMSVGCircleElement) child;
				circle.removeClassNameBaseVal(DrawUtil.HIGHLIGHTED_SYMBOL_CLASS);
				circle.addClassNameBaseVal(DrawUtil.BASE_SYMBOL_CLASS);
			} else if ( child.getNodeName().equals("text") ) {
				OMSVGTextElement text = (OMSVGTextElement) child;
				text.removeClassNameBaseVal(DrawUtil.HIGHLIGHT_TEXT_CLASS);
				text.addClassNameBaseVal(DrawUtil.BASE_TEXT_CLASS);
			} else if ( child.getNodeName().equals("use") ) {
				OMSVGUseElement use = (OMSVGUseElement) child;
				SVGUtil.replaceScaleTransform( use.getOwnerSVGElement()
						, use.getTransform().getBaseVal()
						, DrawUtil.BASE_SECONDARY_SYMBOL_SCALE );
			}
		}
	}
}
