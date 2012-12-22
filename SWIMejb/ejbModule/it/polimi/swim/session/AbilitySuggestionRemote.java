package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Remote;

@Remote
public interface AbilitySuggestionRemote {

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

}
