package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.AnnotatedMapDescriptorDAO;
import gov.lbl.glamm.server.dao.impl.AnnotatedMapDescriptorDAOImpl;

import java.util.List;

/**
 * Service class for getting the list of available annotated map descriptors.
 * @author jtbates
 *
 */
public class GetAnnotatedMapDescriptors {
	
	/**
	 * Gets the list of available annotated map descriptors.
	 * @param glammSession The GLAMM session.
	 * @return The list of annotated map descriptors.
	 */
	public static List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors(final GlammSession glammSession) {
		AnnotatedMapDescriptorDAO dao = new AnnotatedMapDescriptorDAOImpl(glammSession);
		return dao.getAnnotatedMapDescriptors();
	}
}
