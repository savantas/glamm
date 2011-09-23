package gov.lbl.glamm.client.model.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Synonym implements Serializable {
	
	private String name;
	private String type;
	
	@SuppressWarnings("unused")
	private Synonym() {}
	
	public Synonym(final String name, final String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
}
