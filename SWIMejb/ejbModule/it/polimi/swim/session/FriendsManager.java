package it.polimi.swim.session;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.Friendship;
import it.polimi.swim.model.User;
import it.polimi.swim.session.exceptions.FriendshipException;
import it.polimi.swim.session.exceptions.UserException;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FriendsManager implements FriendsManagerRemote,
		FriendsManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public FriendsManager() {
		super();
	}

	@Override
	public void addRequest(User user, String friend)
			throws FriendshipException, UserException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/local");
			ProfileManagerLocal profileManager = (ProfileManagerLocal) ref;

			User friendUser = profileManager.getUserByUsername(friend);
			if (!friendshipAlreadyExist(user, friendUser)) {
				Friendship friendship = new Friendship();
				friendship.setFriend(friendUser);
				friendship.setState(RequestState.PENDING);
				friendship.setUser(user);
				manager.persist(friendship);
			} else {
				throw new FriendshipException("friendship already exists!");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UserException e) {
			throw new UserException("can't found 'friend' user: " + friend);
		}
	}

	private boolean friendshipAlreadyExist(User user, User friendUser) {
		try {
			Query q = manager
					.createQuery("SELECT OBJECT(f) FROM Friendship f WHERE f.user=:user AND f.friend=:friend");
			q.setParameter("user", user);
			q.setParameter("friend", friendUser);
			Friendship friendship = (Friendship) q.getSingleResult();
			if (friendship == null) {
				System.out
						.println("*** [FriendsManager] friendship not exists ***");
				return false;
			}
			return true;
		} catch (NonUniqueResultException exc) {
			System.out.println("*** [FriendsManager] friendship exists ***");
			return true;
		} catch (NoResultException nrex) {
			System.out
					.println("*** [FriendsManager] friendship not exists ***");
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getFriends(User user) {
		try {
			Query q = manager
					.createQuery("SELECT f.friend FROM Friendship f WHERE f.user=:user AND f.state=:accepted");
			q.setParameter("user", user);
			q.setParameter("accepted", RequestState.ACCEPTED);
			List<User> friends = (List<User>) q.getResultList();
			if (friends == null || friends.isEmpty()) {
				System.out
						.println("*** [FriendsManager] friends list not found ***");
				return null;
			}
			System.out.println("*** [FriendsManager] friends list found ***");
			return friends;
		} catch (NoResultException exc) {
			System.out
					.println("*** [FriendsManager] friends list not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Friendship> getRequest(User user) {
		try {
			Query q = manager
					.createQuery("FROM Friendship f WHERE f.friend=:user AND f.state=:pending");
			q.setParameter("user", user);
			q.setParameter("pending", RequestState.PENDING);
			List<Friendship> request = (List<Friendship>) q.getResultList();
			if (request == null || request.isEmpty()) {
				System.out
						.println("*** [FriendsManager] friendship request list found ***");
				return null;
			}
			return request;
		} catch (NoResultException exc) {
			System.out
					.println("*** [FriendsManager] friendship request list not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Friendship> getPending(User user) {
		try {
			Query q = manager
					.createQuery("FROM Friendship f WHERE f.user=:user AND f.state=:pending");
			q.setParameter("user", user);
			q.setParameter("pending", RequestState.PENDING);
			List<Friendship> pending = (List<Friendship>) q.getResultList();
			if (pending == null || pending.isEmpty()) {
				System.out
						.println("*** [FriendsManager] pending list found ***");
				return null;
			}
			return pending;
		} catch (NoResultException exc) {
			System.out
					.println("*** [FriendsManager] pending list not found ***");
		}
		return null;
	}

	@Override
	public void updateFriendship(RequestState state, int friendshipID)
			throws FriendshipException {
		try {
			Friendship friendship = manager
					.find(Friendship.class, friendshipID);
			if (friendship == null) {
				System.out
						.println("*** [FriendsManager] friendship request not found ***");
				throw new FriendshipException(
						"you are trying to manage a friendship that does not exists!");
			}
			if (state.equals(RequestState.REJECTED)) {
				System.out
						.println("*** [FriendsManager] reject remove friendship ***");
				manager.remove(friendship);
			} else if (state.equals(RequestState.ACCEPTED)) {
				if (friendship.getState() != RequestState.ACCEPTED) {
					System.out
							.println("*** [FriendsManager] accepted set and update friendship ***");
					friendship.setState(state);
					manager.merge(friendship);
					Friendship viceversa = new Friendship();
					viceversa.setFriend(friendship.getUser());
					viceversa.setUser(friendship.getFriend());
					viceversa.setState(RequestState.ACCEPTED);
					manager.persist(viceversa);
					System.out
							.println("*** [FriendsManager] viceversa friendhsip added ***");
				} else {
					System.out
							.println("*** [FriendsManager] accept 2 times the same requet ***");
					throw new FriendshipException(
							"you have already accepted this friendship request!");
				}
			} else {
				System.out
						.println("*** [FriendsManager] maybe have selected pending or something else ***");
				throw new FriendshipException("action not recognized!");
			}
		} catch (NoResultException exc) {
			System.out
					.println("*** [FriendsManager] friendship request not found ***");
			throw new FriendshipException(
					"trying to manage a friendship that does not exists!");
		}
	}

	@Override
	public boolean areFriends(User user1, User user2) {
		return searchFriendship(user1, user2, RequestState.ACCEPTED);

	}

	@Override
	public boolean isRequestPending(User user1, User user2) {
		return searchFriendship(user1, user2, RequestState.PENDING);
	}

	private boolean searchFriendship(User user1, User user2, RequestState state) {
		try {
			Query q = manager
					.createQuery("FROM Friendship f WHERE f.user=:user AND f.friend=:friend AND f.state=:state");
			q.setParameter("user", user1);
			q.setParameter("friend", user2);
			q.setParameter("state", state);
			Friendship friendship = (Friendship) q.getSingleResult();
			System.out.println("*** [ProfileManager] friendship '"
					+ user1.getUsername() + "' and '" + user2.getUsername()
					+ "' in state '" + state + "' found ***");
			if (friendship != null) {
				return true;
			} else {
				return false;
			}
		} catch (NoResultException exc) {
			System.out.println("*** [ProfileManager] friendship '"
					+ user1.getUsername() + "' and '" + user2.getUsername()
					+ "' in state '" + state + "' not found ***");
		} catch (NonUniqueResultException exc) {
			System.out.println("*** [ProfileManager] friendship '"
					+ user1.getUsername() + "' and '" + user2.getUsername()
					+ "' in state '" + state + "' multiple found ***");
		}
		return false;
	}

}
