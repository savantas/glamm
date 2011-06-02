package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings({"unused", "serial"})
public class Organism extends GlammPrimitive implements Serializable {
	
	public static transient final String GLOBAL_MAP_NAME			= "Global Map";
	public static transient final String GLOBAL_MAP_TAXONOMY_ID		= "0";
	public static transient final long MIN_METAGENOME_TAXID = 1000000000000l;
	
	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	
	public static class OrganismComparator implements Comparator<Organism> {

		@Override
		public int compare(Organism o0, Organism o1) {
			String name0 = o0.getName();
			String name1 = o1.getName();
			return name0.compareTo(name1);
		}
		
	}
	
	private String taxonomyId = "";
	private String name = "";
	
	//********************************************************************************

	private Organism() {}
	
	//********************************************************************************

	public static Organism globalMap() {
		return new Organism(GLOBAL_MAP_TAXONOMY_ID, GLOBAL_MAP_NAME);
	}
	
	//********************************************************************************
	
	public Organism(String taxonomyId, String name) {		
		this.taxonomyId = taxonomyId;
		this.name = name;
	}
	
	//********************************************************************************
	
	public String getName() {
		return this.name;
	}
	
	//********************************************************************************

	public String getTaxonomyId() {
		return this.taxonomyId;
	}
	
	@Override
	public Type getType() {
		return TYPE;
	}
	
	//********************************************************************************

	public final boolean isGlobalMap() {
		return taxonomyId.equals(GLOBAL_MAP_TAXONOMY_ID);
	}
	
	//********************************************************************************

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((taxonomyId == null) ? 0 : taxonomyId.hashCode());
		return result;
	}

	//********************************************************************************

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organism other = (Organism) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taxonomyId == null) {
			if (other.taxonomyId != null)
				return false;
		} else if (!taxonomyId.equals(other.taxonomyId))
			return false;
		return true;
	}
	
	//********************************************************************************
	
	

}