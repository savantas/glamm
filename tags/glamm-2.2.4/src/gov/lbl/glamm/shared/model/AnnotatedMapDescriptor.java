package gov.lbl.glamm.shared.model;

import java.io.Serializable;

/**
 * A compact description of the satellite data associated with a GLAMM-annotated SVG map - it's effectively a data transfer object.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class AnnotatedMapDescriptor implements Serializable {
	
	private String mapId;
	private String svg;
	private String icon;
	private String title;
	private MetabolicNetwork network;
	
	@SuppressWarnings("unused")
	private AnnotatedMapDescriptor() {}
	
	/**
	 * Constructor
	 * @param mapId The id for this map.
	 * @param svg The name of the SVG file for this map.
	 * @param icon The name of the image (PNG) file for this map.
	 * @param title The title of this map.
	 * @param network The metabolic network for this map.
	 */
	public AnnotatedMapDescriptor(final String mapId, 
			final String svg, 
			final String icon, 
			final String title,
			final MetabolicNetwork network) {
		this.mapId = mapId;
		this.svg = svg;
		this.icon = icon;
		this.title = title;
		this.network = network;
	}

	/**
	 * Gets the map id.
	 * @return The map id.
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * Gets the SVG file name.
	 * @return The file name.
	 */
	public String getSvg() {
		return svg;
	}

	/**
	 * Gets the icon image file name.
	 * @return The file name.
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Gets the map title.
	 * @return The map title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Gets the metabolic network for this map.
	 * @return The metabolic network.
	 */
	public MetabolicNetwork getMetabolicNetwork() {
		return network;
	}
	
}
