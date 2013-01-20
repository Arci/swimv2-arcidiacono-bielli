package it.polimi.swim.session;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.model.HelpRequest;
import it.polimi.swim.model.Message;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.HelpException;
import it.polimi.swim.session.exceptions.UserException;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for HelpsManager bean
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Remote
public interface HelpsManagerRemote {

	/**
	 * Add a help request in the system
	 * 
	 * @param user
	 * @param friend
	 * @throws UserException
	 *             if helper user not found
	 * @throws HelpException
	 *             with error message
	 * @throws AbilityException
	 *             if ability not found
	 */
	public void addRequest(User user, String helper, String ability,
			Date opening_date) throws UserException, HelpException,
			AbilityException;

	/**
	 * 
	 * @param the
	 *            id of the desired help request
	 * @return the help request
	 * @throws HelpException
	 *             if help request not found
	 */
	public HelpRequest loadRequest(int identifier) throws HelpException;

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
	 * close an help request
	 * 
	 * @param closed
	 * @param parseInt
	 * @param parseInt2
	 */
	public void closeHelpRequest(HelpState state, int helpID, int vote);

	/**
	 * add a message to the specified help request
	 * 
	 * @param attribute
	 * @param helpID
	 * @param text
	 *            the message text
	 * @throws HelpException
	 *             if help request not found
	 */
	public void addMessage(User writer, int helpID, String text)
			throws HelpException;

	/**
	 * return the sorted list of messages for the passed help request
	 * 
	 * @param id
	 * @throws HelpException
	 *             if help request not found
	 * @return
	 */
	public List<Message> getMessages(int id) throws HelpException;

}
