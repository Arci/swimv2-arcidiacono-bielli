package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Remote;

/**
 * Remote interface for HomeControl bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Remote
public interface HomeControlRemote {

	/**
	 * check if exist a user with the given parameters
	 * 
	 * @param username
	 * @param password
	 * @return the user if user and password give a result, otherwise null
	 */
	public User checkLogin(String username, String password);

}
