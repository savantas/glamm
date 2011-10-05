package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.PathwayDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KeggPathwayDAOImpl implements PathwayDAO {
	
	private GlammSession sm;
	
	public KeggPathwayDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public Pathway getPathway(String mapId) {
		Pathway pathway = null;
		
		String sql 	=	"select distinct mapId, title from glamm.GlammKeggMap where mapId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);
			
			if(mapId.startsWith("map"))
				mapId = mapId.substring(3);
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
