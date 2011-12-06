package gov.lbl.glamm.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Describes the configuration of a GLAMM server.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class ServerConfig implements Serializable {
	
	/**
	 * Describes the configuration of the underlying database associated with a GLAMM server.
	 * @author jtbates
	 *
	 */
	public static class DbConfig {
	
		/**
		 * DbConfig attributes and their XML tags.
		 * @author jtbates
		 *
		 */
		public static enum Attribute {
			NAME("name"),
			URI("uri"),
			DRIVER("driver"),
			USER("user"),
			PASSWD("passwd");
			
			private String xmlAttr;
			
			private Attribute(final String xmlAttr) {
				this.xmlAttr = xmlAttr;
			}
			
			@Override
			public String toString() {
				return xmlAttr;
			}
		}
		
		public static final String TAG_NAME		= "db";
		private Map<Attribute, String> map;
		
		private DbConfig() {
			map = new HashMap<Attribute, String>();
		};
		
		/**
		 * Creates a DbConfig from a attribute to string value map.
		 * @param map The attribute map.
		 * @return A DBConfig.
		 */
		public static final DbConfig create(final Map<Attribute, String> map) {
			
			DbConfig dbConfig = new DbConfig();
			dbConfig.map = new HashMap<Attribute, String>(map);
			return dbConfig;
		}

		/**
		 * Gets the database name.
		 * @return The database name.
		 */
		public final String getName() {
			return map.get(Attribute.NAME);
		}

		/**
		 * Gets the database JDBC uri.
		 * @return The uri.
		 */
		public final String getUri() {
			return map.get(Attribute.URI);
		}

		/**
		 * Gets the database driver class name.
		 * @return The driver class name.
		 */
		public final String getDriver() {
			return map.get(Attribute.DRIVER);
		}

		/**
		 * Gets the user name used for this database configuration.
		 * @return The user name.
		 */
		public final String getUser() {
			return map.get(Attribute.USER);
		}

		/**
		 * Gets the password used for this database configuration and user.
		 * @return The password.
		 */
		public final String getPasswd() {
			return map.get(Attribute.PASSWD);
		}
		
		@Override 
		public String toString() {
			StringBuilder builder = new StringBuilder("[");
			for(Entry<Attribute, String> entry : map.entrySet())
				builder.append(entry.getKey().name() + ": " + entry.getValue().toString() + " ");
			builder.append("]");
			return builder.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((map == null) ? 0 : map.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DbConfig other = (DbConfig) obj;
			if (map == null) {
				if (other.map != null)
					return false;
			} else if (!map.equals(other.map))
				return false;
			return true;
		}
	}
	
	/**
	 * ServerConfig attributes and their XML tags.
	 * @author jtbates
	 *
	 */
	public enum Attribute {
		
		SERVER_DOMAIN("serverdomain", true),
		HIBERNATE_CFG("hibernatecfg", true),
		ISOLATE_HOST("isolatehost", true),
		LOGIN_URL("loginurl", true),
		METAGENOME_HOST("metagenomehost", false),
		DB("db", true);
		
		private String xmlAttr;
		private boolean isRequired;
		
		private Attribute(final String xmlAttr, final boolean isRequired) {
			this.xmlAttr = xmlAttr;
			this.isRequired = isRequired;
		}
		
		/**
		 * Indicates whether or not this attribute is required.
		 * @return Flag indicating whether or not this attribute is required.
		 */
		public boolean isRequired() {
			return isRequired;
		}
		
		@Override
		public String toString() {
			return xmlAttr;
		}
		
	}

	public static final String TAG_NAME 			= "server";
	private Map<Attribute, Object> map;
	
	private ServerConfig() {};
	
	public static final ServerConfig create(final Map<Attribute, Object> map) {
		
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.map = new HashMap<Attribute, Object>(map);
		
		return serverConfig;
	}

	/**
	 * Gets the domain for this server config.
	 * @return The domain.
	 */
	public final String getServerDomain() {
		return (String) map.get(Attribute.SERVER_DOMAIN);
	}
	
	/**
	 * Gets the name of the Hibernate configuration file to be used with this server.
	 * @return The Hibernate configuration file name.
	 */
	public final String getHibernateCfg() {
		return (String) map.get(Attribute.HIBERNATE_CFG);
	}

	/** 
	 * Gets the name of the isolate host.
	 * @return The name of the isolate host.
	 */
	public final String getIsolateHost() {
		return (String) map.get(Attribute.ISOLATE_HOST);
	}

	/**
	 * Gets the login URL.
	 * @return The login URL.
	 */
	public final String getLoginUrl() {
		return (String) map.get(Attribute.LOGIN_URL);
	}
	
	/**
	 * Gets the name of the metagenome host.
	 * @return The metagenome host name.
	 */
	public final String getMetagenomeHost() {
		return (String) map.get(Attribute.METAGENOME_HOST);
	}

	/**
	 * Gets the DbConfig associated with this ServerConfig.
	 * @return The DbConfig.
	 */
	public final DbConfig getDbConfig() {
		return (DbConfig) map.get(Attribute.DB);
	}
	
	/**
	 * Indicates whether or not this configuration has a metagenome host.
	 * @return Flag indicating the presence of a metagenome host.
	 */
	public final boolean hasMetagenomeHost() {
		String metagenomeHost = getMetagenomeHost();
		return (metagenomeHost != null && !metagenomeHost.isEmpty());
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		for(Entry<Attribute, Object> entry : map.entrySet())
			builder.append(entry.getKey().name() + ": " + entry.getValue().toString() + " ");
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerConfig other = (ServerConfig) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}
}
