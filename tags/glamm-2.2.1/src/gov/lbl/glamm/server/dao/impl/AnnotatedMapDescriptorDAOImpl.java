package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.AnnotatedMapDescriptorDAO;
import gov.lbl.glammdb.domain.PersistentAnnotatedMap;
import gov.lbl.glammdb.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Implementation of AnnotatedMapDescriptorDAO interface.  Wraps Hibernate calls.
 * @author jtbates
 *
 */
public class AnnotatedMapDescriptorDAOImpl implements AnnotatedMapDescriptorDAO {
	
	private GlammSession sm;
	
	/**
	 * Constructor
	 * @param sm The GLAMM session.
	 */
	public AnnotatedMapDescriptorDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	private List<PersistentAnnotatedMap> getAnnotatedMaps() {
		Session session = HibernateUtil.getSessionFactory(sm).getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<PersistentAnnotatedMap> result = (List<PersistentAnnotatedMap>) session.createCriteria(PersistentAnnotatedMap.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.setFetchMode("network", FetchMode.JOIN)
		.list();
		session.getTransaction().commit();
		return result;
	}
	
	@Override
	public AnnotatedMapDescriptor getAnnotatedMapDescriptor(final String mapId) {
		Session session = HibernateUtil.getSessionFactory(sm).getCurrentSession();
		session.beginTransaction();
		PersistentAnnotatedMap result = (PersistentAnnotatedMap) session.createCriteria(PersistentAnnotatedMap.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		
		if(result == null)
			return null;
		
		return result.toAnnotatedMapDescriptor();
	}
	
	@Override
	public List<AnnotatedMapDescriptor> getAnnotatedMapDescriptors() {
		List<AnnotatedMapDescriptor> amds = new ArrayList<AnnotatedMapDescriptor>();
		List<PersistentAnnotatedMap> annotatedMaps = getAnnotatedMaps();
		
		if(annotatedMaps != null && !annotatedMaps.isEmpty()) {
			for(PersistentAnnotatedMap am : annotatedMaps)
				amds.add(am.toAnnotatedMapDescriptor());
		}
		
		return amds;
	}

}
