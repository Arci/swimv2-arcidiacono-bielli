package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Local;

@Local
public interface ProfileManagerLocal {

	public User getUserByUsername(String username);

}
