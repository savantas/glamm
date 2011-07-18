package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.GlammUser;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.UserDAO;
import gov.lbl.glamm.server.dao.impl.UserDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;


public class AuthenticateUser {

	public static GlammUser authenticateUser(final GlammSession sm, 
			final String userId, 
			final String auth, 
			final String remoteAddr) {
		
		if(sm == null || userId == null || auth == null || remoteAddr == null)
			return GlammUser.guestUser();
		
		if(auth.equals(GlammUtils.genBase64MD5String(userId + "|" + remoteAddr))) {
			UserDAO userDao = new UserDAOImpl(sm);
			GlammUser user = userDao.getUserForUserId(userId);
			
			user.setAuth(auth);
			sm.setUser(user);
			
			return user;
		}
		
		return GlammUser.guestUser();
		
	}
}
