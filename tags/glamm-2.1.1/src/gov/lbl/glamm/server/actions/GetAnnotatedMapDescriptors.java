package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.AnnotatedMapDescriptorDAO;
import gov.lbl.glamm.server.dao.impl.AnnotatedMapDescriptorDAOImpl;

import java.util.List;

public class GetAnnotatedMapDescriptors {
	public static List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors(final GlammSession glammSession) {
		AnnotatedMapDescriptorDAO dao = new AnnotatedMapDescriptorDAOImpl(glammSession);
		return dao.getAnnotatedMapDescriptors();
	}
}
