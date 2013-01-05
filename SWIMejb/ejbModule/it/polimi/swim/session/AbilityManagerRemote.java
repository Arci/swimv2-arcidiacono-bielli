package it.polimi.swim.session;

import java.util.List;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.model.User;

import javax.ejb.Remote;

@Remote
public interface AbilityManagerRemote {

	/**
	 * Insert a new suggestion for the specified user
	 * 
	 * @param user
	 * @param text
	 */
	public void insertSuggestion(User user, String text);

	/**
	 * 
	 * @param user
	 * @return the user reloaded
	 */
	public User reloadUser(User user);

	/**
	 * Check if an ability already exists
	 * 
	 * @param name
	 * @return true if name correspond to an existing ability, false otherwise
	 */
	public boolean existAbility(String name);

	/**
	 * @return list of all ability in the system
	 */
	public List<Ability> getAbilityList();
	
	/**
	 * 
	 * @return list of all suspended ability requests.
	 */
	public List<AbilityRequest> getAbilityRequests();
}
