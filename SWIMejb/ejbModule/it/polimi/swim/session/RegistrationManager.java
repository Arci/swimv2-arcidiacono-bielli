package it.polimi.swim.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RegistrationManager implements RegistrationManagerLocal, RegistrationManagerRemote{
	@PersistenceContext(unitName = "SwimPU") 
	EntityManager manager;
	
	public RegistrationManager(){
		super();
	}
	
}
