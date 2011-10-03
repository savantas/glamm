package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;

import java.util.List;

public interface AnnotatedMapDescriptorDAO {
	public AnnotatedMapDescriptor getAnnotatedMapDescriptor(final String mapId);
	public List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors();
}
