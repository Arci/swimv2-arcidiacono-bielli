package it.polimi.swim.session;

import it.polimi.swim.model.User;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for SearchManager bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
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
	 * Given some text, search it in the abilities of the users and restrict the
	 * research by the city
	 * 
	 * @param string
	 * @param cityPattern
	 * @return the list of user matching the pattern
	 */
	public List<User> searchPerson(String pattern, String cityPattern);

	/**
	 * Given some text, search it in the abilities of the users
	 * 
	 * @param pattern
	 * @return the list of user having that ability
	 */
	public List<User> searchAbility(String pattern);

	/**
	 * Given some text, search it in all the fields of a user and restrict the
	 * research by the city
	 * 
	 * @param string
	 * @param cityPattern
	 * @return the list of user having that ability
	 */
	public List<User> searchAbility(String pattern, String cityPattern);

	/**
	 * Given a city search user in that city
	 * 
	 * @param cityPattern
	 * @return the list of user matching the pattern
	 */
	public List<User> searchByCity(String cityPattern);

	/**
	 * 
	 * @return the list of the cities registred in the system
	 */
	public List<String> getCities();

}
