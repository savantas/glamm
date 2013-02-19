package gov.lbl.glamm.shared;

import java.io.Serializable;


public enum DeploymentDomain implements Serializable {

	LBL("lbl"),
	KBASE("kbase");
	
	private String name;
	private DeploymentDomain(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static DeploymentDomain fromString(String name) {
		name = name.toLowerCase();
		for (DeploymentDomain domain : DeploymentDomain.values()) {
			if (domain.getName().equals(name))
				return domain;
		}
		return null;	// returns null if we give it an unknown name
	}

}
