package it.polimi.swim.session;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.Friendship;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.FriendshipException;
import it.polimi.swim.session.exceptions.UserException;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for FriendsManager bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Remote
public interface FriendsManagerRemote {

	/**
	 * Add a request of friendship from user to friend
	 * 
	 * @param user
	 * @param string
	 * @throws FriendshipException
	 *             with the error message
	 * @throws UserException
	 */
	public void addRequest(User user, String string)
			throws FriendshipException, UserException;

	/**
	 * Get the friends list of the passed user
	 * 
	 * @param user
	 * @return the list of friend
	 */
	public List<User> getFriends(User user);

	/**
	 * Get the list of friendship request for the passed user
	 * 
	 * @param user
	 * @return the list of friendship request associated to the passed user
	 */
	public List<Friendship> getRequest(User user);

	/**
	 * Get the friendship request started by the passed user
	 * 
	 * @param user
	 * @return the list of friendship without response
	 */
	public List<Friendship> getPending(User user);

	/**
	 * set the ID of the passed friendship request to the passed state
	 * 
	 * @param state
	 * @param friendshipID
	 * @throws FriendshipException
	 *             with the error message
	 */
	public void updateFriendship(RequestState state, int friendshipID)
			throws FriendshipException;

	/**
	 * @param user1
	 * @param user2
	 * @return true if the user are friends, false otherwise
	 */
	public boolean areFriends(User user1, User user2);

	/**
	 * @param user1
	 * @param user2
	 * @return true if exist a pending friendship request from user1 to user2
	 */
	public boolean isRequestPending(User user1, User user2);

}
