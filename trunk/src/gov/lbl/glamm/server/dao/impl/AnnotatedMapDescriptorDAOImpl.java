package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.AnnotatedMapDescriptorDAO;
import gov.lbl.glammdb.domain.AnnotatedMap;
import gov.lbl.glammdb.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class AnnotatedMapDescriptorDAOImpl implements AnnotatedMapDescriptorDAO {
	
	private GlammSession sm;
	
	public AnnotatedMapDescriptorDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	private List<AnnotatedMap> getAnnotatedMaps() {
		Session session = HibernateUtil.getSessionFactory(sm).getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<AnnotatedMap> result = (List<AnnotatedMap>) session.createCriteria(AnnotatedMap.class)
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
		AnnotatedMap result = (AnnotatedMap) session.createCriteria(AnnotatedMap.class)
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
		List<AnnotatedMap> annotatedMaps = getAnnotatedMaps();
		
		if(annotatedMaps != null && !annotatedMaps.isEmpty()) {
			for(AnnotatedMap am : annotatedMaps)
				amds.add(am.toAnnotatedMapDescriptor());
		}
		
		return amds;
	}

}
