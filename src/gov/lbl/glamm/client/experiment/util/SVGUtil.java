package gov.lbl.glamm.client.experiment.util;

import org.vectomatic.dom.svg.OMDocument;
import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTransform;
import org.vectomatic.dom.svg.OMSVGTransformList;
import org.vectomatic.dom.svg.OMSVGUseElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class SVGUtil {

	/**
	 * @param svg
	 * @param svgGroup
	 * @param tx
	 * @param ty
	 * @return OMSVGTransform translation element
	 */
	public static OMSVGTransform translate(
			OMSVGSVGElement svg, OMSVGGElement svgGroup
			, float tx, float ty ) {
		OMSVGTransform translate = svg.createSVGTransform();
		translate.setTranslate(tx, ty);
		svgGroup.getTransform().getBaseVal().appendItem(translate);
		return translate;
	}

	public static OMSVGGElement createTranslatedSVGGroup(
			OMSVGSVGElement svg, OMSVGDocument doc
			, String id, String className
			, float tx, float ty
			, ObjectCount objCount ) {
		OMSVGGElement svgGroup = doc.createSVGGElement();
		objCount.svgObjCount++;
		if ( id != null ) {
			svgGroup.setId(id);
		}
		svgGroup.setClassNameBaseVal(className);
		translate(svg, svgGroup, tx, ty);
		return svgGroup;
	}

	public static void useDef(  OMSVGSVGElement svg, OMSVGDocument doc
			, OMSVGGElement svgGroup, String href
			, float tx, float ty, float rotation, float scale
			, ObjectCount objCount
	) {
		OMSVGUseElement useElement = doc.createSVGUseElement();
		objCount.svgUseCount++;
		useElement.getHref().setBaseVal(href);
	
		OMSVGTransform translateTransform = svg.createSVGTransform();
		translateTransform.setTranslate(tx, ty);
		useElement.getTransform().getBaseVal().appendItem(translateTransform);
		if ( rotation != 0 ) {
			OMSVGTransform rotateTransform = svg.createSVGTransform();
			rotateTransform.setRotate(rotation, 0, 0);
			useElement.getTransform().getBaseVal().appendItem(rotateTransform);
		}
		OMSVGTransform scaleTransform = svg.createSVGTransform();
		scaleTransform.setScale(scale, scale);
		useElement.getTransform().getBaseVal().appendItem(scaleTransform);
	
		svgGroup.appendChild(useElement);
	}

	public static void executeOnSimilarGElements( OMDocument doc
			, String svgClassIdentifier, String elementClassIdentifier
			, Executor<OMSVGGElement> executor ) {
		OMNodeList<OMElement> docChildren
				= doc.getElementsByTagName("svg");
		for ( int i=0; i< docChildren.getLength(); i++ ) {
			OMSVGSVGElement docChildSvg = (OMSVGSVGElement)
			docChildren.getItem(i);
			if ( docChildSvg.getClassName().getBaseVal()
					.equals(svgClassIdentifier) ) {
				SVGUtil.executeOnSimilarGElements( docChildSvg
						, elementClassIdentifier, executor );
			}
		}
	}

	public static void executeOnSimilarGElements( OMSVGSVGElement svg,
			String elementClassIdentifier, Executor<OMSVGGElement> executor
	) {
		OMNodeList<OMElement> gElements = svg.getElementsByTagName("g");
		for ( int i=0; i<gElements.getLength(); i++ ) {
			OMSVGGElement gElement = (OMSVGGElement) gElements.getItem(i);
			if ( gElement.getClassName().getAnimVal()
					.contains(elementClassIdentifier) ) {
				executor.execute(gElement);
			}
		}
	}

	public static OMSVGElement setSymbolStyle( OMSVGElement svgElement
			, String fillCssColor, String strokeCssColor, String strokeWidth ) {
		svgElement.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fillCssColor );
		svgElement.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, strokeCssColor);
		svgElement.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, strokeWidth);
		return svgElement;
	}

	public static void replaceScaleTransform( OMSVGSVGElement svg
			, OMSVGTransformList transformList, float scale
	) {
		int index = findTransformIndex(transformList, OMSVGTransform.SVG_TRANSFORM_SCALE);
		if ( index < 0 ) {
			OMSVGTransform scaleTransform = svg.createSVGTransform();
			scaleTransform.setScale(scale, scale);
			transformList.appendItem(scaleTransform);
		} else {
			OMSVGTransform scaleTransform = transformList.getItem(index);
			scaleTransform.setScale(scale, scale);
		}
	}

	public static int findTransformIndex( OMSVGTransformList transformList
			, int type ) {
		for ( int i=0; i< transformList.getNumberOfItems(); i++ ) {
			if ( type == transformList.getItem(i).getType() ) {
				return i;
			}
		}
		return -1;
	}

	public static String convertToCssColor( int color ) {
		String colorStr = Integer.toHexString(color);
		for ( int k=colorStr.length(); k < 6; k++) {
			colorStr = "0" + colorStr;
		}
		colorStr = "#" + colorStr;
		return colorStr;
	}
}
