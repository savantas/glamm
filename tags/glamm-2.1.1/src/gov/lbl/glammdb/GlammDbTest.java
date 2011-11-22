package gov.lbl.glammdb;

import gov.lbl.glammdb.domain.PersistentAnnotatedMap;
import gov.lbl.glammdb.domain.PersistentPathway;
import gov.lbl.glammdb.util.HibernateUtil;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class GlammDbTest {

	public static PersistentAnnotatedMap loadAnnotatedMap(final String mapId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		PersistentAnnotatedMap result = (PersistentAnnotatedMap) session.createCriteria(PersistentAnnotatedMap.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		return result;
	}

	public static List<PersistentAnnotatedMap> loadAnnotatedMaps() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<PersistentAnnotatedMap> result = (List<PersistentAnnotatedMap>) session.createCriteria(PersistentAnnotatedMap.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.setFetchMode("network", FetchMode.JOIN)
		.list();
		session.getTransaction().commit();
		return result;
	}

	public static PersistentPathway loadPathway(final String mapId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		PersistentPathway result = (PersistentPathway) session.createCriteria(PersistentPathway.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		return result;
	}
	
	public static List<PersistentPathway> loadPathways() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<PersistentPathway> result = (List<PersistentPathway>) session.createCriteria(PersistentPathway.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.setFetchMode("elements", FetchMode.JOIN)
		.list();
		session.getTransaction().commit();
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(PersistentAnnotatedMap am : loadAnnotatedMaps()) {
			System.out.println(am.getMapId() + " " + am.getTitle());
		}
	}

}
