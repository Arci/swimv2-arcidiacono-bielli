package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class UserManager
 */
@Stateless
public class AdminManager implements AdminManagerRemote, AdminManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager database;

	public AdminManager() {
	}

	public List<Ability> getAbilities() {
		List<Ability> abilities = new ArrayList<Ability>();
		try {
			Query q = database.createQuery("FROM Ability");
			List<?> result = q.getResultList();
			System.out.println("*** list size abilities *** " + result.size());
			for (Object obj : result) {
				abilities.add((Ability) obj);
			}
		} catch (EntityNotFoundException enf) {
		} catch (NonUniqueResultException nure) {
		}
		return abilities;
	}

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		try {
			Query q = database.createQuery("FROM User");
			List<?> result = q.getResultList();
			System.out.println("*** list size users *** " + result.size());
			for (Object obj : result) {
				users.add((User) obj);
			}
		} catch (EntityNotFoundException enf) {
		} catch (NonUniqueResultException nure) {
		}
		return users;
	}

	public void insertUser(User user) {
		database.persist(user);
	}

	public void insertAbility(Ability ability) {
		database.persist(ability);
	}

	public void addAbilityToUser(User user, Ability ability) {
		user.getAbilities().add(ability);
		database.merge(user);
	}

	public void removeAbilityToUser(User user, Ability ability) {
		user.getAbilities().remove(ability);
		database.merge(user);
	}

	public void initDb() {
		// ability
		Ability ability1 = new Ability();
		ability1.setName("giardiniere");
		database.persist(ability1);
		// ability
		Ability ability2 = new Ability();
		ability2.setName("imbianchino");
		database.persist(ability2);
		// user
		User user1 = new User();
		user1.setType(UserType.ADMIN);
		user1.setName("mikey");
		user1.setSurname("topolino");
		user1.setUsername("the_mouse");
		user1.setEmail("topo@fattoria.com");
		user1.setPassword("segreto_topo");
		user1.setCity("Milano");
		user1.setPhone(1234567890);
		database.persist(user1);
		// user
		User user2 = new User();
		user2.setType(UserType.ADMIN);
		user2.setName("paperino");
		user2.setSurname("donald");
		user2.setUsername("the_duck");
		user2.setEmail("papera@fattoria.com");
		user2.setPassword("segreto_papera");
		database.persist(user2);
		// user
		User user3 = new User();
		user3.setType(UserType.ADMIN);
		user3.setName("pluto");
		user3.setUsername("the_dog");
		user3.setEmail("cane@fattoria.com");
		user3.setPassword("segreto_cane");
		database.persist(user3);
	}

	public Ability loadAbility(String name) {
		try {
			Query q = database.createQuery("FROM Ability a WHERE a.name=:name");
			q.setParameter("name", name);
			System.out.println("*** ability name *** " + name);
			System.out.println("*** ability  *** " + q.getSingleResult());
			return (Ability) q.getSingleResult();
		} catch (EntityNotFoundException exc) {
		} catch (javax.persistence.NoResultException exc) {
		} catch (NonUniqueResultException exc) {
		}
		return null;
	}

	public User loadUserByUsername(String username) {
		try {
			Query q = database
					.createQuery("FROM User u WHERE u.username=:username ");
			q.setParameter("username", username);
			System.out.println("*** user username *** " + username);
			System.out.println("*** user  *** " + q.getSingleResult());
			return (User) q.getSingleResult();
		} catch (EntityNotFoundException exc) {
		} catch (javax.persistence.NoResultException exc) {
		} catch (NonUniqueResultException exc) {
		}
		return null;
	}

}
