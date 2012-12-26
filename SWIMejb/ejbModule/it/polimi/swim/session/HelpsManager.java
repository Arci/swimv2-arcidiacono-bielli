package it.polimi.swim.session;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.Date;

import javax.ejb.Stateless;

@Stateless
public class HelpsManager implements HelpsManagerRemote, HelpsManagerLocal {

	public HelpsManager() {
		super();
	}

	@Override
	public void addRequest(User user, User helper, Ability ability,
			Date opening_date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFriendship(HelpState state, int friendshipID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Ability isRequestPending(User user1, User user2) {
		// TODO Auto-generated method stub
		return null;
	}

}
