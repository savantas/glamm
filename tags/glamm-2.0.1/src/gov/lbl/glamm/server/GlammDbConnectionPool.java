package gov.lbl.glamm.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class GlammDbConnectionPool {

	public class GlammDbConfig {

		private static final String ATTR_DEBUG 			= "debug";
		private static final String ATTR_DRIVER 		= "driver";		
		private static final String ATTR_FILTER_ON_ACL 	= "filterOnAcl";
		private static final String ATTR_PASSWD 		= "passwd";
		private static final String ATTR_URI 			= "uri";
		private static final String ATTR_USER 			= "user";

		private boolean debug 		= false;
		private String 	driver 		= null;
		private boolean filterOnAcl	= true;
		private String 	passwd 		= null;
		private String 	uri 		= null;
		private String 	user 		= null;

		public GlammDbConfig(Element dbElement) {
			if(dbElement.hasAttribute(ATTR_DEBUG))
				debug = Boolean.parseBoolean(dbElement.getAttribute(ATTR_DEBUG));
			driver = dbElement.getAttribute(ATTR_DRIVER);
			if(dbElement.hasAttribute(ATTR_FILTER_ON_ACL))
				filterOnAcl = Boolean.parseBoolean(dbElement.getAttribute(ATTR_FILTER_ON_ACL));
			passwd = dbElement.getAttribute(ATTR_PASSWD);
			uri = dbElement.getAttribute(ATTR_URI);
			user = dbElement.getAttribute(ATTR_USER);
		}

		public final boolean isDebug() {
			return debug;
		}

		public final String getDriver() {
			return driver;
		}

		public final boolean isFilterOnAcl() {
			return filterOnAcl;
		}

		public final String getPasswd() {
			return passwd;
		}

		public final String getUri() {
			return uri;
		}

		public final String getUser() {
			return user;
		}


	}

	//********************************************************************************

	private static GlammDbConnectionPool INSTANCE = null;

	private static final String MY_HOSTNAME_PREFIX		= "jtbates-m";

	//private static final String GLAMM_JBEI_HOSTNAME_PREFIX = "glamm";

	//********************************************************************************

	private ComboPooledDataSource	dataSource 		= null;

	private GlammDbConfig			debugDbConfig	= null;
	private GlammDbConfig			defaultDbConfig	= null;
	private GlammDbConfig			dbConfig 		= null;

	//********************************************************************************

	private GlammDbConnectionPool() {

	}

	//********************************************************************************

	public static void init(String dbConfigFileName) {

		if(INSTANCE == null) {
			
			INSTANCE = new GlammDbConnectionPool();
			INSTANCE.parseConfigFile(dbConfigFileName);

			try {
				java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
				String hostName = localMachine.getHostName();

				if(hostName.startsWith(MY_HOSTNAME_PREFIX)) 
					INSTANCE.dbConfig = INSTANCE.debugDbConfig;
				else
					INSTANCE.dbConfig = INSTANCE.defaultDbConfig;

				INSTANCE.dataSource = new ComboPooledDataSource();
				INSTANCE.dataSource.setDriverClass(INSTANCE.dbConfig.getDriver());
				INSTANCE.dataSource.setJdbcUrl(INSTANCE.dbConfig.getUri());
				INSTANCE.dataSource.setUser(INSTANCE.dbConfig.getUser());
				INSTANCE.dataSource.setPassword(INSTANCE.dbConfig.getPasswd());

			}
			catch(Exception e) {
				System.err.println (e.getMessage());
				e.printStackTrace();
			}
		}

	}

	//********************************************************************************

	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = INSTANCE.dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	//********************************************************************************

	public static GlammDbConfig getDbConfig() {
		if(INSTANCE != null)
			return INSTANCE.dbConfig;
		return null;
	}
	
	//********************************************************************************

	public static ArrayList<HashMap<String, Object>> executeQuery(String query) {

		ArrayList<HashMap<String, Object>> results = null;

		if(query != null) {

			try {

				Connection connection = INSTANCE.dataSource.getConnection();

				Statement	statement	= connection.createStatement();
				ResultSet	rs			= statement.executeQuery( query );

				results = getQueryArrayListFromResultSet( rs );

				rs.close();
				statement.close();
				connection.close();

			} catch ( SQLException e ) {
				System.err.println ( e.getMessage() );
				e.printStackTrace();
			} catch ( NullPointerException e ) {
				System.err.println ( e.getMessage() );
				e.printStackTrace();
			}
		}

		return results;
	}

	//********************************************************************************

	private static ArrayList<HashMap<String, Object>> getQueryArrayListFromResultSet ( ResultSet rs ) {

		ArrayList<HashMap<String, Object>> results = null;

		if ( rs != null ) {

			try {

				ResultSetMetaData	rsmd 			= rs.getMetaData();
				int					columnCount		= rsmd.getColumnCount();

				results = new ArrayList<HashMap<String, Object>>();

				rs.beforeFirst();

				while ( rs.next() ) {
					HashMap<String, Object> record = new HashMap<String, Object>();

					for ( int i = 1; i <= columnCount; i++ ) {
						String key 		= rsmd.getColumnLabel( i );
						Object value 	= rs.getObject( i );

						record.put( key, value );
					}

					results.add ( record );
				}

			} catch ( SQLException e ) {
				System.err.println ( e.getMessage() );
				e.printStackTrace();
			}	
		}
		return results;
	}

	//********************************************************************************

	public static void destroy() {
		try {
			DataSources.destroy(INSTANCE.dataSource);
		} catch (SQLException e) {
			System.err.println (e.getMessage());
			e.printStackTrace();
		}
	}

	//********************************************************************************

	private void parseConfigFile(String uri) {
		
		final String TAG_DATABASE = "database";
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(uri);
				
				NodeList nodeList = doc.getElementsByTagName(TAG_DATABASE);
				for(int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) nodeList.item(i);
					GlammDbConfig dbConfig = new GlammDbConfig(element);
					
					if(dbConfig.isDebug())
						this.debugDbConfig = dbConfig;
					else
						this.defaultDbConfig = dbConfig;
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
		
	}

	//********************************************************************************

}