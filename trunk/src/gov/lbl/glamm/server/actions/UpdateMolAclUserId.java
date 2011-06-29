package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateMolAclUserId {
	
	public static void updateMolAclUserId(SessionManager sm, final String molAclUserId) {
		
		sm.setMolAclUserId(molAclUserId);
		
		if(molAclUserId == null || molAclUserId.isEmpty()) {
			sm.addMolAclGroupId("1");
			return;
		}
		
		//mysql> select u.userId, g.groupId, u.email from Users u join GroupUsers g using(userId) where userId=1104 and active=1;

		String sql = "select g.groupId from GroupUsers g where userId=? and active=1;";
		
		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, molAclUserId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				sm.addMolAclGroupId(rs.getString("groupId"));
			}
			
			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
