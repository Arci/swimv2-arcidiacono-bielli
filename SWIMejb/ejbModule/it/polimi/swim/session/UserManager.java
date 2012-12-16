package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.Ability;
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
		Ability ability1 = new Ability();
		ability1.setName("giardiniere");
		database.persist(ability1);
		Ability ability2 = new Ability();
		ability2.setName("imbianchino");
		database.persist(ability2);
		// user with 2 ability
		User user1 = new User();
		user1.setType(UserType.ADMIN);
		user1.setName("mikey");
		user1.setSurname("topolino");
		user1.setUsername("the mouse");
		user1.setEmail("topo@fattoria.com");
		user1.setPassword("segreto_topo");
		user1.setCity("Milano");
		user1.setPhone(1234567890);
		database.persist(user1);
		// user with 1 ability
		User user2 = new User();
		user2.setType(UserType.ADMIN);
		user2.setName("paperino");
		user2.setSurname("donald");
		user2.setUsername("the duck");
		user2.setEmail("papera@fattoria.com");
		user2.setPassword("segreto_papera");
		database.persist(user2);
		// user without ability
		User user3 = new User();
		user3.setType(UserType.ADMIN);
		user3.setName("pluto");
		user3.setUsername("the dog");
		user3.setEmail("cane@fattoria.com");
		user3.setPassword("segreto_cane");
		database.persist(user3);
	}

}
