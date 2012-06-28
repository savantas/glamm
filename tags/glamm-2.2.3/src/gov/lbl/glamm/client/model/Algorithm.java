package gov.lbl.glamm.client.model;

/**
 * Enumerated type for available retrosynthesis algorithms.
 * @author jtbates
 *
 */
public enum Algorithm {
	
	TW_DFS("Taxon-weighted Depth-first Search", "twdfs", true),
	DFS("Depth-first Search", "dfs", false);
	
	private String fullName;
	private String shortName;
	private boolean requiresOrganism;
	
	private Algorithm(final String caption, final String algorithm, final boolean requiresOrganism) {
		this.fullName = caption;
		this.shortName = algorithm;
		this.requiresOrganism = requiresOrganism;
	}
	
	/**
	 * Gets the short name of this algorithm.
	 * @return The short name.
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * Gets the full name of this algorithm.
	 * @return The full name.
	 */
	public String getFullName() {
		return fullName;
	}
	
	/**
	 * Flag indicating if this algorithm requires a selected organism.
	 * @return The flag.
	 */
	public boolean requiresOrganism() {
		return requiresOrganism;
	}
	
	/**
	 * Gets the algorithm for the given short name.
	 * @param shortName The short name.
	 * @return The algorithm.
	 */
	public static Algorithm getAlgorithmForShortName(final String shortName) {
		if(shortName != null) {
			for(Algorithm algorithm : Algorithm.values())
				if(algorithm.getShortName().equals(shortName))
					return algorithm;
		}
		
		throw new IllegalArgumentException("shortName is null or not supported.");
	}
}
