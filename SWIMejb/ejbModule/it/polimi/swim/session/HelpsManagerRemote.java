package it.polimi.swim.session;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.HelpRequest;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.UserException;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface HelpsManagerRemote {

	/**
	 * Add a help request in the system
	 * 
	 * @param user
	 * @param friend
	 * @throws UserException
	 */
	public void addRequest(User user, String helper, String ability,
			Date opening_date) throws UserException;

	/**
	 * Get the list of help that user is giving
	 * 
	 * @param user
	 * @return the list of help user is giving
	 */
	public List<HelpRequest> getGivingHelpRequests(User user);

	/**
	 * Get the user asked open help requests
	 * 
	 * @param user
	 * @return the open help requests
	 */
	public List<HelpRequest> getOpenHelpRequests(User user);

	/**
	 * Get the closed help request for the passed user
	 * 
	 * @param user
	 * @return the closed helps list
	 */
	public List<HelpRequest> getClosedHelpRequests(User user);

	/**
	 * Get the pending help request as asker for the passed user
	 * 
	 * @param user
	 * @return the pending helps list
	 */
	public List<HelpRequest> getPendingAsAsker(User user);

	/**
	 * Get the pending help request as helper for the passed user
	 * 
	 * @param user
	 * @return
	 */
	public List<HelpRequest> getPendingAsHelper(User user);

	/**
	 * delete the request if state is REJECTED otherwise if is OPEN, open the
	 * request
	 * 
	 * @param state
	 * @param helpID
	 */
	public void updateHelpRequest(HelpState state, int helpID);

	/**
	 * @param user1
	 * @param user2
	 * @return the ability on witch the user are in a hel request
	 */
	public Ability isRequestPending(User user1, User user2);

}
