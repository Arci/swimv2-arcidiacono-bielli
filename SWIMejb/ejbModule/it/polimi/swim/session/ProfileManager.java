package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.UserException;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
	public User reloadUser(User user) throws UserException {
		try {
			return manager.find(User.class, user.getId());
		} catch (NoResultException e) {
			throw new UserException("can't reload user");
		}
	}

	@Override
	public User updateProfile(User user, Hashtable<String, String> params) {
		// TODO ricevuti i parametri aggiorna user se new!=old, poi fa
		// manager.merge(user)

		user.setName(params.get("name"));
		user.setSurname(params.get("surname"));
		user.setUsername(params.get("username"));
		user.setEmail(params.get("email"));
		user.setCity(params.get("city"));
		user.setPhone(Integer.parseInt(params.get("phone")));
//		user.setPassword(params.get("password"));
		manager.merge(user);
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

	@Override
	public void removeAbility(String username, String abilityName)
			throws UserException, AbilityException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext;
			jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("AbilityManager/local");
			AbilityManagerLocal abilityManager = (AbilityManagerLocal) ref;

			Ability ability = abilityManager.getAbilityByName(abilityName);

			ref = jndiContext.lookup("ProfileManager/local");
			ProfileManagerLocal profileManager = (ProfileManagerLocal) ref;
			User user = profileManager.getUserByUsername(username);

			if (user.getAbilities().contains(ability)) {
				user.removeAbility(ability);
			} else {
				throw new AbilityException(
						"you can't delete an ability you don't have!");
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addAbility(String username, String abilityName)
			throws UserException, AbilityException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext;
			jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("AbilityManager/local");
			AbilityManagerLocal abilityManager = (AbilityManagerLocal) ref;

			Ability ability = abilityManager.getAbilityByName(abilityName);

			ref = jndiContext.lookup("ProfileManager/local");
			ProfileManagerLocal profileManager = (ProfileManagerLocal) ref;
			User user = profileManager.getUserByUsername(username);

			if (!user.getAbilities().contains(ability)) {
				user.addAbility(ability);
			} else {
				throw new AbilityException("you already have this ability!");
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
