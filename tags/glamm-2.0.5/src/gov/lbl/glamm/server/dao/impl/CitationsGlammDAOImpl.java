package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Citation;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.dao.CitationsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CitationsGlammDAOImpl implements CitationsDAO {

	@Override
	public String getCitationsTableName() {

		String tableName = null;

		String sql 	=	"select table_name " +
		"from information_schema.tables " +
		"where table_schema = \"glamm\" and table_name = \"GlammCitation\";";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if(rs.next())
				tableName = rs.getString("table_name");

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return tableName;
	}


	@Override
	public ArrayList<Citation> getCitations() {

		ArrayList<Citation> citations = null;

		String sql = "select DS.description, DS.dbVersion, C.citation " +
		"from glamm.GlammDataSource DS " +
		"join glamm.GlammCitation C on (DS.guid=C.dataSourceGuid);";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				
				String dbVersion 	= rs.getString("dbVersion");
				String description 	= rs.getString("description");
				String text			= rs.getString("citation");

				if(dbVersion != null && 
						description != null && 
						text != null) {
					Citation citation = new Citation(dbVersion, description, text);
					if(citations == null)
						citations = new ArrayList<Citation>();
					citations.add(citation);
				}
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return citations;
	}

}
