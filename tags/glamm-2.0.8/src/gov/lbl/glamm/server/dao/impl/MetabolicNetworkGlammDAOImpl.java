package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.MNNode;
import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicNetworkDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MetabolicNetworkGlammDAOImpl implements MetabolicNetworkDAO {
	
	private GlammSession sm;
	
	public MetabolicNetworkGlammDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}
	
	@Override
	public MetabolicNetwork getNetworkForMapId(String mapId) {
		
		MetabolicNetwork mn = null;
		String sql 	=	"select * from glamm.GlammMapConn where mapTitle=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, mapId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				if(mn == null) 
					mn = new MetabolicNetwork(mapId);
				
				mn.addNode(new MNNode(	rs.getString("rxnExtId"), 
										rs.getString("cpd0ExtId"), 
										rs.getString("cpd1ExtId")));
				
				mn.addNode(new MNNode(	rs.getString("rxnExtId"), 
						rs.getString("cpd1ExtId"), 
						rs.getString("cpd0ExtId")));

			}



			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return mn;
	}
	
}
