package it.polimi.swim.session;

import it.polimi.swim.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

/**
 * Implementation of stateless bean HomeControl
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
@Stateless
@RemoteBinding(jndiBinding = "HomeControl/remote")
@LocalBinding(jndiBinding = "HomeControl/local")
public class HomeControl implements HomeControlRemote, HomeControlLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public HomeControl() {
		super();
	}

	@Override
	public User checkLogin(String username, String password) {
		try {
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:username");
			q.setParameter("username", username);
			User user = (User) q.getSingleResult();
			System.out.println("*** [HomeControl] user found '" + username
					+ "' ***");
			if (user.getPassword().equals(password)) {
				System.out.println("*** [HomeControl] password equals ("
						+ password + ") ***");
				return user;
			}
			System.out.println("*** [HomeControl] password not equals ("
					+ password + "," + user.getPassword() + ") *** ");
		} catch (NoResultException exc) {
			System.out.println("*** [HomeControl] user not found '" + username
					+ "' ***");
			return null;
		} catch (NonUniqueResultException exc) {
			System.out.println("*** [HomeControl] multiple user found ***");
			return null;
		}
		return null;
	}

}
