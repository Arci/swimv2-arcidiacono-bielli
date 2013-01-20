package it.polimi.swim.session;

import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.UserException;

import javax.ejb.Local;

/**
 * Local interface for ProfileManager bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Local
public interface ProfileManagerLocal {

	/**
	 * 
	 * @param username
	 * @return the user with the username passed
	 * @throws UserException
	 *             if no user found
	 */
	public User getUserByUsername(String username) throws UserException;

}
