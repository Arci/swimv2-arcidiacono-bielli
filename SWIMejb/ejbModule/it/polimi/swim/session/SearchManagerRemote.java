package it.polimi.swim.session;

import it.polimi.swim.model.User;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SearchManagerRemote {

	/**
	 * Given some text, search it in all the fields of a user
	 * 
	 * @param pattern
	 * @return the list of user matching the pattern
	 */
	public List<User> searchPerson(String pattern);

	/**
	 * Given some text, search it in the abilities of the users
	 * 
	 * @param pattern
	 * @return the list of user having that ability
	 */
	public List<User> searchAbility(String pattern);

}
