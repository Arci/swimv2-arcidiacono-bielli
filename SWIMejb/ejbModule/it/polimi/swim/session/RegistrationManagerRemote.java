package it.polimi.swim.session;

import javax.ejb.Remote;

@Remote
public interface RegistrationManagerRemote {
	
	/**
	 * 
	 * @param email
	 *            to check
	 * @return true iff email is unique
	 */
	public boolean isEmailUnique(String email);
	
	/**
	 * 
	 * @param username
	 *            to check
	 * @return true iff username is unique
	 */
	public boolean isUsernameUnique(String username);
}
