package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.UserException;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProfileManager implements ProfileManagerRemote,
		ProfileManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public ProfileManager() {
		super();
	}

	@Override
	public User getUserByUsername(String username) throws UserException {
		try {
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:username");
			q.setParameter("username", username);
			User user = (User) q.getSingleResult();
			System.out.println("*** [ProfileManager] requested user found ***");
			return user;
		} catch (NoResultException exc) {
			System.out
					.println("*** [ProfileManager] requested user not found ***");
			throw new UserException("user not found");
		}
	}

	@Override
	public User updateProfile(User user, Hashtable<String, String> params) {
		// TODO ricevuti i parametri aggiorna user se new!=old, poi fa
		// manager.merge(user)
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isUsernameUnique(String username) {
		try {
			Query q = manager.createQuery("SELECT u.username FROM User AS u");
			List<String> usernames = (List<String>) q.getResultList();
			Iterator<String> itr = usernames.iterator();
			while (itr.hasNext()) {
				String name = itr.next();
				if (username.equals(name)) {
					System.out.println("*** [ProfileManager] username '"
							+ username + "' was already used ***");
					return false;
				}
			}
			System.out.println("*** [ProfileManager] username '" + username
					+ "' is unique ***");
			return true;
		} catch (NoResultException exc) {
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isEmailUnique(String email) {
		try {
			Query q = manager.createQuery("SELECT u.email FROM User AS u");
			List<String> mails = (List<String>) q.getResultList();
			Iterator<String> itr = mails.iterator();
			while (itr.hasNext()) {
				String mail = itr.next();
				if (email.equals(mail)) {
					System.out.println("*** [ProfileManager] email '" + email
							+ "' was already used ***");
					return false;
				}
			}
			System.out.println("*** [ProfileManager] email '" + email
					+ "' is unique ***");
			return true;
		} catch (NoResultException exc) {
		}
		return false;
	}

	@Override
	public Double getUserRating(User user) {
		try {
			Query q = manager
					.createQuery("SELECT coalesce(avg(h.vote), 0) FROM HelpRequest h WHERE h.helper=:user");
			q.setParameter("user", user);
			Double vote = (Double) q.getSingleResult();
			System.out.println("*** [ProfileManager] rating for '"
					+ user.getUsername() + "' is '" + vote + "' ***");
			return vote;
		} catch (NoResultException exc) {
			System.out.println("*** [ProfileManager] rating not found ***");
		}
		return -1d;
	}

	@Override
	public Double getAbilityRating(User user, Ability ability) {
		try {
			Query q = manager
					.createQuery("SELECT coalesce(avg(h.vote), 0) FROM HelpRequest h WHERE h.helper=:user AND h.ability=:ability");
			q.setParameter("user", user);
			q.setParameter("ability", ability);
			Double vote = (Double) q.getSingleResult();
			System.out.println("*** [ProfileManager] rating for ability '"
					+ ability.getName() + "' of '" + user.getUsername()
					+ "' is '" + vote + "' ***");
			return vote;
		} catch (NoResultException exc) {
			System.out
					.println("*** [ProfileManager] ability rating not found ***");
		}
		return -1d;
	}

	@Override
	public User insertNewUser(Map<String, Object> params) {

		String normalUsername = (String) params.get("username");
		try {
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:normal");
			q.setParameter("normal", normalUsername);
			q.getSingleResult();
			System.out
					.println("*** [ProfileManager] NORMAL users found no action ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [ProfileManager] NORMAL users not found ***");
			User normal = new User();
			normal.setType(UserType.NORMAL);
			normal.setName((String) params.get("name"));
			normal.setSurname((String) params.get("surname"));
			normal.setUsername((String) normalUsername);
			normal.setEmail((String) params.get("email"));
			normal.setPassword((String) params.get("password"));
			// normal.setCity((String) params.get("city"));
			// normal.setPhone((int) params.get("phone"));
			manager.persist(normal);
			System.out.println("*** [ProfileManager] NORMAL user inserted ***");
			return normal;
		}
		return null;
	}

}
