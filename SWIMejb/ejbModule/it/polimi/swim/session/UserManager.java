package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
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

	public void initDb() {
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setType(UserType.ADMIN);
			user.setName("pippo"+i);
			user.setSurname("topolino");
			user.setUsername("the gozzest "+ user.getName());
			user.setEmail("gigio." + i + "@fantasia.com");
			user.setPassword("segreto");
			user.setCity("Milano");
			user.setPhone(1234567890);
			database.persist(user);
		}
	}

}
