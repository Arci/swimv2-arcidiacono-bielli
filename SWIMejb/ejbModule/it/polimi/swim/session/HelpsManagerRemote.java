package it.polimi.swim.session;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface HelpsManagerRemote {

	/**
	 * Add a help request in the system
	 * 
	 * @param user
	 * @param friend
	 */
	public void addRequest(User user, User helper, Ability ability,
			Date opening_date);

	/**
	 * set the ID of the passed friendship request to the passed state
	 * 
	 * @param state
	 * @param string
	 */
	public void updateFriendship(HelpState state, int friendshipID);

	/**
	 * @param user1
	 * @param user2
	 * @return the ability on witch the user are in a hel request
	 */
	public Ability isRequestPending(User user1, User user2);
}
