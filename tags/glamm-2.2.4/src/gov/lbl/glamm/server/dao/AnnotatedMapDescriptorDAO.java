package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.AnnotatedMapDescriptor;

import java.util.List;

/**
 * Data access object interface for annotated map descriptor data.
 * @author jtbates
 *
 */
public interface AnnotatedMapDescriptorDAO {
	/**
	 * Gets the annotated map descriptor for a given map id.
	 * @param mapId The map id.
	 * @return The annotated map descriptor.
	 */
	public AnnotatedMapDescriptor getAnnotatedMapDescriptor(final String mapId);
	
	/**
	 * Gets a list of all available annotated map descriptors.
	 * @return The list of annotated map descriptors.
	 */
	public List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors();
}
