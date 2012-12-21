package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Remote;

@Remote
public interface AccessManagerRemote {
	
	public User checkLogin(String username, String password);
	
	public void addFakeUsers();
		
}
