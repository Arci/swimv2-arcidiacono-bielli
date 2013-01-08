package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.AbilityException;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AbilityManagerRemote {

	/**
	 * Insert a new suggestion for the specified user
	 * 
	 * @param user
	 * @param text
	 * @throws AbilityException
	 *             with the error message
	 */
	public void insertSuggestion(User user, String text)
			throws AbilityException;

	/**
	 * 
	 * @param user
	 * @return the user reloaded
	 */
	public User reloadUser(User user);

	/**
	 * @return list of all ability in the system
	 */
	public List<Ability> getAbilityList();

	/**
	 * 
	 * @return list of all suspended ability requests.
	 */
	public List<AbilityRequest> getAbilityRequests();

	/**
	 * update the state about an ability request.
	 * 
	 * @param id
	 *            about an ability request
	 * @param state
	 *            about an ability request
	 */
	public void updateAbilityRequestState(String id, String state);

	/**
	 * Insert a new ability in the sistem.
	 * 
	 * @param name
	 */
	public void insertNewAbility(String name);
}
