package it.polimi.swim.session;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RegistrationManager implements RegistrationManagerLocal, RegistrationManagerRemote{
	@PersistenceContext(unitName = "SwimPU") 
	EntityManager manager;
	
	public RegistrationManager(){
		super();
	}

	@Override
	public boolean isEmailUnique(String email) {
		return new ProfileManager().isEmailUnique(email);
	}

	@Override
	public boolean isUsernameUnique(String username) {
		return new ProfileManager().isUsernameUnique(username);
	}

}
