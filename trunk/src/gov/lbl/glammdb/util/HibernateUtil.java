package gov.lbl.glammdb.util;

import gov.lbl.glamm.server.GlammSession;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Hibernate utility class, primarily used for getting Hibernate SessionFactory instances.
 * @author jtbates
 *
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	private static final Map<String, SessionFactory> s2sf = new HashMap<String, SessionFactory>();

	static {
		sessionFactory = null;
//		try {
//			sessionFactory = new AnnotationConfiguration()
//			.configure().buildSessionFactory();
//		} catch (Throwable ex) {
//			throw new ExceptionInInitializerError(ex);
//		}
	}

	/**
	 * Gets the default SessionFactory instance.
	 * @return The instance.
	 */
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

	/**
	 * Gets the SessionFactory instance associated with a GlammSession.
	 * @param glammSession The session.
	 * @return The instance.
	 */
	public static SessionFactory getSessionFactory(final GlammSession glammSession) {
		SessionFactory sessionFactory = s2sf.get(glammSession.getServerConfig().getHibernateCfg());
		if(sessionFactory == null) 
			sessionFactory = initSessionFactory(glammSession);
		return sessionFactory;
	}
}


