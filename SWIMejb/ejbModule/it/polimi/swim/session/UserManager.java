package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class UserManager
 */
@Stateless
public class UserManager implements UserManagerRemote, UserManagerLocal {
    @PersistenceContext(unitName = "SwimPU")
    EntityManager database;
    
    public UserManager() {
    }
    
    public void initDb(){
    	User user = new User();
    	user.setName("pippo");
    	user.setIdentificationNumber(1);
    	database.persist(user);
    }

}
