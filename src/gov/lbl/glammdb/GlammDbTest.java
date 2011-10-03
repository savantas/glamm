package gov.lbl.glammdb;

import gov.lbl.glammdb.domain.AnnotatedMap;
import gov.lbl.glammdb.domain.Pathway;
import gov.lbl.glammdb.util.HibernateUtil;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class GlammDbTest {

	public static AnnotatedMap loadAnnotatedMap(final String mapId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		AnnotatedMap result = (AnnotatedMap) session.createCriteria(AnnotatedMap.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		return result;
	}

	public static List<AnnotatedMap> loadAnnotatedMaps() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<AnnotatedMap> result = (List<AnnotatedMap>) session.createCriteria(AnnotatedMap.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.setFetchMode("network", FetchMode.JOIN)
		.list();
		session.getTransaction().commit();
		return result;
	}

	public static Pathway loadPathway(final String mapId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Pathway result = (Pathway) session.createCriteria(Pathway.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		return result;
	}
	
	public static List<Pathway> loadPathways() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Pathway> result = (List<Pathway>) session.createCriteria(Pathway.class)
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
		for(AnnotatedMap am : loadAnnotatedMaps()) {
			System.out.println(am.getMapId() + " " + am.getTitle());
		}
	}

}
