package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AccessManager implements AccessManagerRemote, AccessManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public AccessManager() {
		super();
	}

	public User checkLogin(String username, String password) {
		try {
			System.out.println("*** [login] search user *** " + username);
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:username");
			q.setParameter("username", username);
			User user = (User) q.getSingleResult();
			System.out.println("*** [login] user found *** " + user);
			if (user.getPassword().equals(password)) {
				System.out.println("*** [login] password equals *** ("
						+ password + ")");
				return user;
			}
			System.out.println("*** [login] password not equals *** ("
					+ password + "," + user.getPassword() + ")");
		} catch (NoResultException exc) {
			System.out.println("*** [login] user not found *** " + username);
			return null;
		} catch (NonUniqueResultException exc) {
			System.out.println("*** [login] multiple user found *** ");
			return null;
		}
		return null;
	}

	public void addFakeUsers() {
		String adminUsername = "duck";
		String normalUsername = "mouse";
		try {
			System.out.println("*** [fake] search ADMIN user ***");
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:admin");
			q.setParameter("admin", adminUsername);
			q.getSingleResult();
			System.out.println("*** [fake] ADMIN users found no action ***");
		} catch (NoResultException exc) {
			System.out.println("*** [fake] ADMIN users not found ***");
			User admin = new User();
			admin.setType(UserType.ADMIN);
			admin.setName("donald");
			admin.setSurname("paperino");
			admin.setUsername(adminUsername);
			admin.setEmail("papera@fattoria.com");
			admin.setPassword("duck");
			admin.setCity("Torino");
			admin.setPhone(1234567890);
			manager.persist(admin);
			System.out.println("*** [fake] ADMIN user inserted ***");
		}
		try {
			System.out.println("*** [fake] search NORMAL user ***");
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:normal");
			q.setParameter("normal", normalUsername);
			q.getSingleResult();
			System.out.println("*** [fake] NORMAL users found no action ***");
		} catch (NoResultException exc) {
			System.out.println("*** [fake] NORMAL users not found ***");
			User normal = new User();
			normal.setType(UserType.NORMAL);
			normal.setName("mikey");
			normal.setSurname("topolino");
			normal.setUsername(normalUsername);
			normal.setEmail("topo@fattoria.com");
			normal.setPassword("mouse");
			normal.setCity("Milano");
			normal.setPhone(1234567890);
			manager.persist(normal);
			System.out.println("*** [fake] NORMAL user inserted ***");
		}
	}

}
