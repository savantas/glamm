package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CitationsDAO;
import gov.lbl.glamm.shared.model.Citation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementations of the CitationsDAO interface.
 * @author jtbates
 *
 */
public class CitationsGlammDAOImpl implements CitationsDAO {

	private GlammSession sm;
	
	/**
	 * Constructor
	 * @param sm The GLAMM session.
	 */
	public CitationsGlammDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}
	
	@Override
	public String getCitationsTableName() {

		String tableName = null;

		String sql 	=	"select table_name " +
		"from information_schema.tables " +
		"where table_schema = \"glamm\" and table_name = \"GlammCitation\";";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
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
	public List<Citation> getCitations() {

		List<Citation> citations = null;

		String sql = "select DS.description, DS.dbVersion, C.citation " +
		"from glamm.GlammDataSource DS " +
		"join glamm.GlammCitation C on (DS.guid=C.dataSourceGuid);";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
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
