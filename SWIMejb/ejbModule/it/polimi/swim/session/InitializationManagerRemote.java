package it.polimi.swim.session;

import javax.ejb.Remote;

@Remote
public interface InitializationManagerRemote {

	public void addFakeUsers();
	
}
