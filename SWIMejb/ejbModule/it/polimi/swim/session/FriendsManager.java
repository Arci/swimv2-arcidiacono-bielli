package it.polimi.swim.session;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.Friendship;
import it.polimi.swim.model.User;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
	public void addRequest(User user, User friend) {
		Friendship friendship = new Friendship();
		friendship.setUser(user);
		friendship.setFriend(friend);
		friendship.setState(RequestState.PENDING);
		manager.persist(friendship);
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
	public void updateFriendship(RequestState state, Friendship friendshipID) {
		try {
			Friendship friendship = manager
					.find(Friendship.class, friendshipID);
			friendship.setState(state);
			manager.merge(friendship);
			System.out
					.println("*** [FriendsManager] friendship request found and updated ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [FriendsManager] friendship request not found ***");
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
		}
		return false;
	}

}
