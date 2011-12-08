package gov.lbl.glamm.shared;

/**
 * Enumeration of parameters for non-RPC requests.
 * @author jtbates
 *
 */
public enum RequestParameters {
	
	ACTION("action"),
	ALGORITHM("algorithm"),
	AS_TEXT("asText"),
	CPD_SRC("cpdSrc"),
	CPD_DST("cpdDst"),
	DBNAME("dbName"),
	EXPERIMENT("experiment"),
	MAPID("mapId"),
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
