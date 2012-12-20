package it.polimi.swim.session;

import java.util.List;

import it.polimi.swim.model.User;

import javax.ejb.Remote;

@Remote
public interface AccessManagerRemote {
	
	public User checkLogin(String username, String password);
	
	public void addFakeUsers();
	
	public List<User> getAllUsers();
	
}
