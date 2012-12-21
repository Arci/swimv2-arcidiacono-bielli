package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class HomeControl implements HomeControlRemote, HomeControlLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public HomeControl() {
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

}
