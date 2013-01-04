package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.Hashtable;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface ProfileManagerRemote {
	
	/**
	 * insert in the database the parameters about a new user.
	 * 
	 * @param params
	 * @return the new user
	 */
	
	public User insertNewUser(Map <String, Object> params);

	/**
	 * get the user from his username
	 * 
	 * @param parameter
	 * @return null if no user found, the user object otherwise
	 */
	public User getUserByUsername(String parameter);

	/**
	 * Check the params and update the user
	 * 
	 * @param user
	 *            to update
	 * @param params
	 *            hashtable with (paramName, value)
	 * @return the updated user
	 */
	public User updateProfile(User user, Hashtable<String, String> params);

	/**
	 * 
	 * @param username
	 *            to check
	 * @return true iff username is unique
	 */
	public boolean isUsernameUnique(String username);

	/**
	 * 
	 * @param email
	 *            to check
	 * @return true iff email is unique
	 */
	public boolean isEmailUnique(String email);

	/**
	 * 
	 * @param user
	 * @return the average of all the feedbacks given to the user, return -1d if
	 *         some problems occurs
	 */
	public Double getUserRating(User user);

	/**
	 * 
	 * @param user
	 * @param ability
	 * @return the average of all the feedbaks given to the user for the
	 *         requested ability, return -1d if some problems occurs
	 */
	public Double getAbilityRating(User user, Ability ability);

}
