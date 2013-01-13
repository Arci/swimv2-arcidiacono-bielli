package it.polimi.swim.session;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.UserException;

import java.util.Hashtable;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface ProfileManagerRemote {

	/**
	 * remove the specified ability from the user
	 * 
	 * @param user
	 * @param abilityName
	 * @throws UserException
	 *             if user not found
	 * @throws AbilityException
	 *             if ability not found or user doesn't have the ability
	 */
	public void removeAbility(String username, String abilityName)
			throws UserException, AbilityException;

	/**
	 * remove the specified ability from the user
	 * 
	 * @param username
	 * @param abilityName
	 * @throws UserException
	 *             if user not found
	 * @throws AbilityException
	 *             if ability not found or user doesn't have the ability
	 */
	public void addAbility(String username, String abilityName)
			throws UserException, AbilityException;

	/**
	 * insert in the database the parameters about a new user.
	 * 
	 * @param params
	 * @return the new user
	 */
	public User insertNewUser(Map<String, Object> params);

	/**
	 * 
	 * @param username
	 * @return the user with the username passed
	 * @throws UserException
	 *             if no user found
	 */
	public User getUserByUsername(String username) throws UserException;

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

	/**
	 * 
	 * @param user
	 * @return the user reloaded
	 * @throws UserException
	 *             if problems
	 */
	public User reloadUser(User user) throws UserException;

}
