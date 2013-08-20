package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.UserDAO;
import gov.lbl.glamm.shared.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the User DAO interface granting access to users stored in MicrobesOnline.  All GLAMM login credentials are 
 * supplied by MicrobesOnline.
 * @author jtbates
 *
 */
public class UserDAOImpl implements UserDAO {

	private GlammSession sm;

	/**
	 * Constructor.
	 * @param sm The GLAMM Session.
	 */
	public UserDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public String getPasswordHashForEmail(final String email) {
		
		if(email == null || email.isEmpty())
			throw new IllegalArgumentException("email is empty or null");
		
		final String sql = "select pwhash from Users where email=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				return rs.getString("pwhash");
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User getUserForUserId(final String userId) {

		if(userId == null || userId.isEmpty())
			throw new IllegalArgumentException("userId is empty or null");

		Set<String> groupIds = new HashSet<String>();
		String email = null;

		final String sql = "select g.groupId, u.email " +
		"from Users u " +
		"join GroupUsers g using(userId) " +
		"where userId=? and active=1;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				email = rs.getString("email");
				groupIds.add(rs.getString("groupId"));
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return new User(userId, groupIds, email);
	}

	@Override
	public User getUserForEmail(final String email) {
		
		if(email == null || email.isEmpty())
			throw new IllegalArgumentException("email is empty or null");

		Set<String> groupIds = new HashSet<String>();
		String userId = null;

		final String sql = "select u.userId, g.groupId from Users u join GroupUsers g using(userId) where email=? and active=1;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				userId = rs.getString("userId");
				groupIds.add(rs.getString("groupId"));
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return new User(userId, groupIds, email);
	}

}
