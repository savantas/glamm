package gov.lbl.glamm.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class ServerConfig implements Serializable {
	
	public static class DbConfig {
		
		public enum Attribute {
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
		
		public static final DbConfig create(final Map<Attribute, String> map) {
			
			DbConfig dbConfig = new DbConfig();
			dbConfig.map = new HashMap<Attribute, String>(map);
			return dbConfig;
		}

		public final String getName() {
			return map.get(Attribute.NAME);
		}

		public final String getUri() {
			return map.get(Attribute.URI);
		}

		public final String getDriver() {
			return map.get(Attribute.DRIVER);
		}

		public final String getUser() {
			return map.get(Attribute.USER);
		}

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

	public final String getServerDomain() {
		return (String) map.get(Attribute.SERVER_DOMAIN);
	}
	
	public final String getHibernateCfg() {
		return (String) map.get(Attribute.HIBERNATE_CFG);
	}

	public final String getIsolateHost() {
		return (String) map.get(Attribute.ISOLATE_HOST);
	}

	public final String getLoginUrl() {
		return (String) map.get(Attribute.LOGIN_URL);
	}
	
	public final String getMetagenomeHost() {
		return (String) map.get(Attribute.METAGENOME_HOST);
	}

	public final DbConfig getDbConfig() {
		return (DbConfig) map.get(Attribute.DB);
	}
	
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
