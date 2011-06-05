package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.dao.PathwayDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KeggPathwayDAOImpl implements PathwayDAO {

	@Override
	public Pathway getPathway(String mapId) {
		Pathway pathway = null;
		
		String sql 	=	"select distinct mapId, title from glamm.GlammKeggMap where mapId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, mapId);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				pathway = new Pathway();

				pathway.setName(rs.getString("title"));
				pathway.setMapId(rs.getString("mapId"));
			}
			
			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return pathway;
	}

}
