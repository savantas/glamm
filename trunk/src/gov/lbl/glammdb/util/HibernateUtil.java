package gov.lbl.glammdb.util;

import gov.lbl.glamm.server.GlammSession;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	private static final Map<String, SessionFactory> s2sf = new HashMap<String, SessionFactory>();

	static {
		try {
			sessionFactory = new AnnotationConfiguration()
			.configure().buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private static SessionFactory initSessionFactory(final GlammSession glammSession) {
		try {
			String hibernateCfg = glammSession.getServerConfig().getHibernateCfg();
			SessionFactory sessionFactory = new AnnotationConfiguration()
			.configure(hibernateCfg)
			.buildSessionFactory();
			s2sf.put(hibernateCfg, sessionFactory);
			return sessionFactory;
		} catch(Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory(final GlammSession glammSession) {
		SessionFactory sessionFactory = s2sf.get(glammSession.getServerConfig().getHibernateCfg());
		if(sessionFactory == null) 
			sessionFactory = initSessionFactory(glammSession);
		return sessionFactory;
	}
}


