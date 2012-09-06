package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.UserDAO;
import gov.lbl.glamm.server.dao.impl.UserDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glamm.shared.model.User;

/**
 * Service class for authenticating a user.
 * @author jtbates
 *
 */
public class AuthenticateUser {

	/**
	 * Authenticates a user
	 * @param sm The GLAMM session into which this user is logged in.
	 * @param userId The user id for this user.
	 * @param auth The contents of the auth cookie.
	 * @param remoteAddr The address from which the user is connecting.
	 * @return The logged in user, or the guest user if authentication fails.
	 */
	public static User authenticateUser(final GlammSession sm, 
			final String userId, 
			final String auth, 
			final String remoteAddr) {
		
		if(sm == null || userId == null || auth == null || remoteAddr == null)
			return User.guestUser();
		
		if(auth.equals(GlammUtils.genBase64MD5String(userId + "|" + remoteAddr))) {
			UserDAO userDao = new UserDAOImpl(sm);
			User user = userDao.getUserForUserId(userId);
			
			user.setAuth(auth);
			sm.setUser(user);
			
			return user;
		}
		
		return User.guestUser();
		
	}
}
