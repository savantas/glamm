package gov.lbl.glamm.shared;

public enum RequestParameters {
	
	ACTION("action"),
	ALGORITHM("algorithm"),
	AS_TEXT("asText"),
	CPD_SRC("cpdSrc"),
	CPD_DST("cpdDst"),
	DBNAME("dbName"),
	EXPERIMENT("experiment"),
	MAP_TITLE("mapTitle"),
	SAMPLE("sample"),
	TAXONOMY_ID("taxonomyId");
	
	private String name;
	
	private RequestParameters(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
