package it.polimi.swim.session;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.HelpRequest;
import it.polimi.swim.model.User;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class HelpsManager implements HelpsManagerRemote, HelpsManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public HelpsManager() {
		super();
	}

	@Override
	public void addRequest(User user, String helper, String ability,
			Date opening_date) {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/local");
			ProfileManagerLocal profileManager = (ProfileManagerLocal) ref;
			User helperUser = profileManager.getUserByUsername(helper);

			ref = jndiContext.lookup("AbilityManager/local");
			AbilityManagerLocal abilityManager = (AbilityManagerLocal) ref;
			Ability helpAbility = abilityManager.getAbilityByName(ability);

			HelpRequest help = new HelpRequest();
			help.setAbility(helpAbility);
			help.setHelper(helperUser);
			help.setOpeningDate(new Date());
			help.setUser(user);
			help.setState(HelpState.PENDING);
			manager.persist(help);

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getGivingHelpRequests(User user) {
		try {
			Query q = manager
					.createQuery("FROM HelpRequest h WHERE h.helper=:user AND h.state=:state");
			q.setParameter("user", user);
			q.setParameter("state", HelpState.OPEN);
			List<HelpRequest> request = (List<HelpRequest>) q.getResultList();
			if (request == null || request.isEmpty()) {
				System.out
						.println("*** [HelpsManager] help request list found ***");
				return null;
			}
			return request;
		} catch (NoResultException exc) {
			System.out
					.println("*** [HelpsManager] help request list not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getOpenHelpRequests(User user) {
		try {
			Query q = manager
					.createQuery("FROM HelpRequest h WHERE h.user=:user AND h.state=:state");
			q.setParameter("user", user);
			q.setParameter("state", HelpState.OPEN);
			List<HelpRequest> request = (List<HelpRequest>) q.getResultList();
			if (request == null || request.isEmpty()) {
				System.out
						.println("*** [HelpsManager] help request list found ***");
				return null;
			}
			return request;
		} catch (NoResultException exc) {
			System.out
					.println("*** [HelpsManager] help request list not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getClosedHelpRequests(User user) {
		try {
			Query q = manager
					.createQuery("FROM HelpRequest h WHERE (h.user=:user OR h.helper=:user) AND h.state=:state");
			q.setParameter("user", user);
			q.setParameter("state", HelpState.CLOSED);
			List<HelpRequest> request = (List<HelpRequest>) q.getResultList();
			if (request == null || request.isEmpty()) {
				System.out
						.println("*** [HelpsManager] help request list found ***");
				return null;
			}
			return request;
		} catch (NoResultException exc) {
			System.out
					.println("*** [HelpsManager] help request list not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getPendingAsAsker(User user) {
		try {
			Query q = manager
					.createQuery("FROM HelpRequest h WHERE h.user=:user AND h.state=:state");
			q.setParameter("user", user);
			q.setParameter("state", HelpState.PENDING);
			List<HelpRequest> request = (List<HelpRequest>) q.getResultList();
			if (request == null || request.isEmpty()) {
				System.out
						.println("*** [HelpsManager] help request list found ***");
				return null;
			}
			return request;
		} catch (NoResultException exc) {
			System.out
					.println("*** [HelpsManager] help request list not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getPendingAsHelper(User user) {
		try {
			Query q = manager
					.createQuery("FROM HelpRequest h WHERE h.helper=:user AND h.state=:state");
			q.setParameter("user", user);
			q.setParameter("state", HelpState.PENDING);
			List<HelpRequest> request = (List<HelpRequest>) q.getResultList();
			if (request == null || request.isEmpty()) {
				System.out
						.println("*** [HelpsManager] help request list found ***");
				return null;
			}
			return request;
		} catch (NoResultException exc) {
			System.out
					.println("*** [HelpsManager] help request list not found ***");
		}
		return null;
	}

	@Override
	public void updateHelpRequest(HelpState state, int helpID) {
		try {
			HelpRequest help = manager.find(HelpRequest.class, helpID);
			if (state.equals(HelpState.REJECTED)) {
				System.out
						.println("*** [HelpsManager] reject remove help request ***");
				manager.remove(help);
			} else if (state.equals(HelpState.OPEN)) {
				System.out
						.println("*** [HelpsManager] accepted set and update help ***");
				help.setState(state);
				manager.merge(help);
			}
			System.out
					.println("*** [HelpsManager] state was pending? don't know what to do ***");
		} catch (NoResultException exc) {
			System.out.println("*** [HelpsManager] help request not found ***");
		}
	}

	@Override
	public Ability isRequestPending(User user1, User user2) {
		// TODO Auto-generated method stub
		return null;
	}

}
