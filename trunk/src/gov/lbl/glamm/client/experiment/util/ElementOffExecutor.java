package gov.lbl.glamm.client.experiment.util;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;

/**
 * Execute turning off highlighting of selected primary metabolite SVG elements generically
 * (as for use within generic search algorithms).
 * 
 * @author DHAP Digital, Inc - angie
 *
 */
public class ElementOffExecutor implements Executor<OMSVGGElement> {

	private static final ElementOffExecutor instance = new ElementOffExecutor();
	public static ElementOffExecutor instance() {
		return instance;
	}

	private ElementOffExecutor() {
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
			}
		}
	}
}
