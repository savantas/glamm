package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Abstract base class for GLAMM primitives
 * @author jtbates
 *
 */

@SuppressWarnings({ "unused", "serial" })
public abstract class GlammPrimitive implements Serializable {
	
	public static class Type implements Serializable {
		private static int nextHashCode;
		private final int index;
		
		public Type() {
			index = ++nextHashCode;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Type other = (Type) obj;
			if (index != other.index)
				return false;
			return true;
		}
		
		
	}

	//********************************************************************************

	public static class Reference implements Serializable {

		//********************************************************************************

		private String refId = null;

		//********************************************************************************
		
		private Reference() {}
		
		public Reference(final String id) {
			this.refId = id;
		}
		
		//********************************************************************************

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((refId == null) ? 0 : refId.hashCode());
			return result;
		}

		//********************************************************************************

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Reference other = (Reference) obj;
			if (refId == null) {
				if (other.refId != null)
					return false;
			} else if (!refId.equals(other.refId))
				return false;
			return true;
		}
		
		//********************************************************************************
		
	}
	
	//********************************************************************************
	
	public static class Xref implements Serializable {
		
		//********************************************************************************
		
		private String xrefId		= null;
		private String xrefDbName 	= null;
		
		//********************************************************************************
		
		private Xref() {}
		
		public Xref(final String xrefId, final String xrefDbName) {
			this.xrefId = xrefId;
			this.xrefDbName = xrefDbName;
		}
			
		//********************************************************************************

		public String getXrefId() {
			return xrefId;
		}
		
		//********************************************************************************

		public String getXrefDbName() {
			return xrefDbName;
		}

		//********************************************************************************

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((xrefDbName == null) ? 0 : xrefDbName.hashCode());
			result = prime * result
					+ ((xrefId == null) ? 0 : xrefId.hashCode());
			return result;
		}
		
		//********************************************************************************

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Xref other = (Xref) obj;
			if (xrefDbName == null) {
				if (other.xrefDbName != null)
					return false;
			} else if (!xrefDbName.equals(other.xrefDbName))
				return false;
			if (xrefId == null) {
				if (other.xrefId != null)
					return false;
			} else if (!xrefId.equals(other.xrefId))
				return false;
			return true;
		}
		
		//********************************************************************************
		
	}
	
	public static class Synonym implements Serializable {
		private String name = "";
		private String type = "";
		
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Synonym other = (Synonym) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
		
		
		
	}
	
	//********************************************************************************
	
		
	private	String id = null;
	private String source = null;
	private String order = null;
	private HashSet<Xref> xrefs = null;
	private HashSet<Synonym> synonyms = null;
	private HashSet<Measurement> measurements = null;
	
	//********************************************************************************

	public void addMeasurement(Measurement measurement) {
		if(measurements == null)
			measurements = new HashSet<Measurement>();
		measurements.add(measurement);
	}
	
	//********************************************************************************

	public void addSynonym(String name, String type) {
		if(synonyms == null)
			synonyms = new HashSet<Synonym>();
		synonyms.add(new Synonym(name, type));
	}
	
	//********************************************************************************

	public void addXref(String xrefId, String xrefDbName) {
		Xref xref = new Xref(xrefId, xrefDbName);
		if(xrefs == null)
			xrefs = new HashSet<Xref>();
		xrefs.add(xref);
	}
	
	//********************************************************************************
	
	public Reference createReference() {
		if(this.id != null)
			return new Reference(id);
		return null;
	}
	
	//********************************************************************************
	
	public abstract Type getType();
	
	public String getId() {
		return id;
	}
	
	//********************************************************************************

	public HashSet<Measurement> getMeasurements() {
		return measurements;
	}
	
	//********************************************************************************

	public String getSource() {
		return source;
	}
	
	//********************************************************************************

	public HashSet<Synonym> getSynonyms() {
		return synonyms;
	}
	
	//********************************************************************************

	public Synonym getSynonymOfPreferredType(final String[] types) {
		for(int i = 0; i < types.length; i++) {
			Synonym synonym = getSynonymOfType(types[i]);
			if(synonym != null)
				return synonym;
		}
		return null;
	}
	
	//********************************************************************************

	public Synonym getSynonymOfType(final String type) {
		if(getSynonyms() != null) 
			for(Synonym synonym : getSynonyms()) 
				if(synonym.type.equals(type)) 
					return synonym;
		return null;
	}
	
	//********************************************************************************

	public HashSet<Xref> getXrefs() {
		return xrefs;
	}
	
	//********************************************************************************

	public Xref getXrefForDbName(String dbName) {
		for(Xref xref : xrefs) {
			if(xref.getXrefDbName().equals(dbName))
				return xref;
		}
		return null;
	}
	
	//********************************************************************************
	
	public Xref getXrefForDbNames(Collection<String> dbNames) {
		for(Xref xref : xrefs) {
			if(dbNames.contains(xref.getXrefDbName()))
				return xref;
		}
		return null;
	}
	
	//********************************************************************************

	public void setId(final String id) {
		this.id = id;
	}
	
	//********************************************************************************

	public void setOrder(final int order) {
		this.order = Integer.toString(order);
	}
	
	//********************************************************************************

	public void setSource(final String source) {
		this.source = source;
	}
	
	//********************************************************************************

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	//********************************************************************************

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlammPrimitive other = (GlammPrimitive) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	//********************************************************************************
	
}