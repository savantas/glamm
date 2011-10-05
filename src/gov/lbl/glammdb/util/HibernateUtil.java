package gov.lbl.glammdb.util;

import gov.lbl.glamm.server.GlammSession;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	private static final Map<GlammSession, SessionFactory> s2sf = new HashMap<GlammSession, SessionFactory>();

	static {
		try {
			sessionFactory = new AnnotationConfiguration()
			.configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Log exception!
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private static SessionFactory initSessionFactory(final GlammSession glammSession) {
		try {
			SessionFactory sessionFactory = new AnnotationConfiguration()
			.configure(glammSession.getServerConfig().getHibernateCfg())
			.buildSessionFactory();
			s2sf.put(glammSession, sessionFactory);
			return sessionFactory;
		} catch(Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory(final GlammSession glammSession) {
		SessionFactory sessionFactory = s2sf.get(glammSession);
		if(sessionFactory == null) 
			sessionFactory = initSessionFactory(glammSession);
		return sessionFactory;
	}
}


