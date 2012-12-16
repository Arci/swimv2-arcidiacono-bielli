package it.polimi.swim.session;

import javax.ejb.Remote;

@Remote
public interface UserManagerRemote {

	public void initDb();
	
}
