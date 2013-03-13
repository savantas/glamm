package gov.lbl.glamm.server.actions;

import java.util.HashSet;

import gov.doe.kbase.auth.AuthService;
import gov.doe.kbase.auth.AuthToken;
import gov.lbl.glamm.server.ConfigurationManager;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.UserDAO;
import gov.lbl.glamm.server.dao.impl.UserDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glamm.shared.DeploymentDomain;
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
		
		if(sm == null || userId == null || auth == null || 
				(ConfigurationManager.getDeploymentDomain() == DeploymentDomain.LBL && remoteAddr == null))
			return User.guestUser();
		
		UserDAO userDao = new UserDAOImpl(sm);

		if (ConfigurationManager.getDeploymentDomain() == DeploymentDomain.LBL) {
			if(auth.equals(GlammUtils.genBase64MD5String(userId + "|" + remoteAddr))) {
				User user = userDao.getUserForUserId(userId);
				
				user.setAuth(auth);
				sm.setUser(user);
				
				return user;
			}
		}
		if (ConfigurationManager.getDeploymentDomain() == DeploymentDomain.KBASE) {
			try {
				AuthToken token = new AuthToken(auth);
				if (AuthService.validateToken(token)) {
					User user = new User(userId, new HashSet<String>(), userId);
					user.setAuth(auth);
					user.setSessionId(token.getSignature());
					return user;
				}
			}
			catch (Exception e) {
				return User.guestUser();
			}
		}
		return User.guestUser();
	}
}
