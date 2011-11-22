package gov.lbl.glamm.server;

import gov.lbl.glamm.server.ServerConfig.DbConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class GlammDbConnectionPool {

	private static Map<DbConfig, ComboPooledDataSource> map;
	
	static {
		map = new HashMap<DbConfig, ComboPooledDataSource>();
	}

	private GlammDbConnectionPool() {}

	private static ComboPooledDataSource getDataSourceForDbConfig(final DbConfig dbConfig) {
		
		ComboPooledDataSource dataSource = map.get(dbConfig);
		if(dataSource == null) {
			try {
				dataSource = new ComboPooledDataSource();
				dataSource.setDriverClass(dbConfig.getDriver());
				dataSource.setJdbcUrl(dbConfig.getUri());
				dataSource.setUser(dbConfig.getUser());
				dataSource.setPassword(dbConfig.getPasswd());
				
				map.put(dbConfig, dataSource);
			} catch(Exception e) {
				throw new RuntimeException("Problem setting up data source for dbConfig " + dbConfig.getName());
			}
		}
		return dataSource;
	}

	public static Connection getConnection(final DbConfig dbConfig) 
	throws SQLException {
		return getDataSourceForDbConfig(dbConfig).getConnection();
	}
	
	public static Connection getConnection(final GlammSession sm) 
	throws SQLException {
		return getConnection(sm.getServerConfig().getDbConfig());
	}
	
	//********************************************************************************

	public static void destroy() {
		try {
			for(ComboPooledDataSource dataSource : map.values())
				DataSources.destroy(dataSource);
		} catch (SQLException e) {
			System.err.println (e.getMessage());
			e.printStackTrace();
		}
	}

	//********************************************************************************

}
