package gov.lbl.glamm.client.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class AnnotatedMapDescriptor implements Serializable {
	
	private String mapId;
	private String svg;
	private String icon;
	private String title;
	private MetabolicNetwork network;
	
	@SuppressWarnings("unused")
	private AnnotatedMapDescriptor() {}
	
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

	public String getMapId() {
		return mapId;
	}

	public String getSvg() {
		return svg;
	}

	public String getIcon() {
		return icon;
	}

	public String getTitle() {
		return title;
	}
	
	public MetabolicNetwork getMetabolicNetwork() {
		return network;
	}
	
}
