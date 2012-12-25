package it.polimi.swim.session;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.enums.RequestState;
import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.Friendship;
import it.polimi.swim.model.HelpRequest;
import it.polimi.swim.model.User;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class InitializationManager implements InitializationManagerLocal,
		InitializationManagerRemote {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public InitializationManager() {
		super();
	}

	@Override
	public void addFakeUsers() {
		String adminUsername = "duck";
		try {
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:admin");
			q.setParameter("admin", adminUsername);
			q.getSingleResult();
			System.out
					.println("*** [InitializationManager] ADMIN users found no action ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [InitializationManager] ADMIN users not found ***");
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
			System.out
					.println("*** [InitializationManager] ADMIN user inserted ***");
		}
		String normalUsername = "mouse";
		try {
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:normal");
			q.setParameter("normal", normalUsername);
			q.getSingleResult();
			System.out
					.println("*** [InitializationManager] NORMAL users found no action ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [InitializationManager] NORMAL users not found ***");
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
			System.out
					.println("*** [InitializationManager] NORMAL user inserted ***");
		}
		String normalUsername2 = "dog";
		try {
			Query q = manager
					.createQuery("FROM User u WHERE u.username=:normal");
			q.setParameter("normal", normalUsername2);
			q.getSingleResult();
			System.out
					.println("*** [InitializationManager] NORMAL users found no action ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [InitializationManager] NORMAL users not found ***");
			User normal = new User();
			normal.setType(UserType.NORMAL);
			normal.setName("pippo");
			normal.setSurname("cane");
			normal.setUsername(normalUsername2);
			normal.setEmail("cane@fattoria.com");
			normal.setPassword("dog");
			normal.setCity("Milano");
			normal.setPhone(1234567890);
			manager.persist(normal);
			System.out
					.println("*** [InitializationManager] NORMAL user inserted ***");
		}
	}

	@Override
	public void addFakeAbilities() {
		String giardiniere = "giardiniere";
		String imbianchino = "imbianchino";
		try {
			Query q = manager.createQuery("FROM Ability a WHERE a.name=:name");
			q.setParameter("name", giardiniere);
			q.getSingleResult();
			System.out
					.println("*** [InitializationManager] GIARDINIERE ability found no action ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [InitializationManager] GIARDINIERE ability not found ***");
			Ability ability = new Ability();
			ability.setName(giardiniere);
			manager.persist(ability);
			System.out
					.println("*** [InitializationManager] GIARDINIERE ability inserted ***");
		}
		try {
			Query q = manager.createQuery("FROM Ability a WHERE a.name=:name");
			q.setParameter("name", imbianchino);
			q.getSingleResult();
			System.out
					.println("*** [InitializationManager] IMBIANCHINO ability found no action ***");
		} catch (NoResultException exc) {
			System.out
					.println("*** [InitializationManager] IMBIANCHINO ability not found ***");
			Ability ability = new Ability();
			ability.setName(imbianchino);
			manager.persist(ability);
			System.out
					.println("*** [InitializationManager] IMBIANCHINO ability inserted ***");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addAbilitiesToUsers() {
		try {
			Query q = manager.createQuery("FROM User u");
			List<User> users = (List<User>) q.getResultList();
			q = manager.createQuery("FROM Ability a");
			List<Ability> abilities = (List<Ability>) q.getResultList();
			Iterator<User> itr = users.iterator();
			while (itr.hasNext()) {
				User normal = itr.next();
				if (normal.getType().equals(UserType.NORMAL)) {
					normal.addAbility(abilities.get(0));
					normal.addAbility(abilities.get(1));
					System.out
							.println("*** [InitializationManager] Added abilities to '"
									+ normal.getUsername() + "' ***");
				}
			}
		} catch (NoResultException exc) {
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addFakeHelpRequest() {
		// creati nuovi ad ogni reload della pagina
		// sempre uguale ma con rating generato random [0,5]
		try {
			Query q = manager.createQuery("FROM User u");
			List<User> users = (List<User>) q.getResultList();
			q = manager.createQuery("FROM Ability a");
			List<Ability> abilities = (List<Ability>) q.getResultList();
			HelpRequest help = new HelpRequest();
			for (User u : users) {
				if (u.getType().equals(UserType.NORMAL)) {
					help.setHelper(u);
					System.out
							.println("*** [InitializationManager] helper is '"
									+ u.getUsername() + "' ***");
				}
			}
			for (User u : users) {
				if (u.getType().equals(UserType.NORMAL)
						&& !u.equals(help.getHelper())) {
					help.setUser(u);
					System.out
							.println("*** [InitializationManager] requester is '"
									+ u.getUsername() + "' ***");
				}
			}
			Random generator = new Random();
			help.setVote(generator.nextInt(6));
			help.setOpeningDate(new Date());
			help.setState(HelpState.OPEN);
			Ability ability = abilities.get(0);
			help.setAbility(ability);
			System.out
					.println("*** [InitializationManager] ability is set to '"
							+ ability.getName() + "' ***");
			manager.persist(help);
		} catch (NoResultException exc) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polimi.swim.session.InitializationManagerRemote#addFakeFriendship()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addFakeFriendship() {
		try {
			Query q = manager.createQuery("SELECT COUNT(f) FROM Friendship f");
			long count = (Long) q.getSingleResult();
			if (count == 0l) {
				q = manager.createQuery("FROM User u WHERE u.type=:normal");
				q.setParameter("normal", UserType.NORMAL);
				List<User> normals = q.getResultList();
				User user1 = normals.get(0);
				User user2 = normals.get(1);
				Friendship accepted1to2 = new Friendship();
				accepted1to2.setFriend(user2);
				accepted1to2.setUser(user1);
				accepted1to2.setState(RequestState.ACCEPTED);
				manager.persist(accepted1to2);
				Friendship accepted2to1 = new Friendship();
				accepted2to1.setFriend(user1);
				accepted2to1.setUser(user2);
				accepted2to1.setState(RequestState.ACCEPTED);
				manager.persist(accepted2to1);
				System.out
						.println("*** [InitializationManager] accepted friend '"
								+ user2.getUsername()
								+ "' user '"
								+ user1.getUsername() + "' and viceversa ***");
				Friendship pending = new Friendship();
				pending.setFriend(user2);
				pending.setUser(user1);
				pending.setState(RequestState.PENDING);
				manager.persist(pending);
				System.out
						.println("*** [InitializationManager] pending friend '"
								+ user2.getUsername() + "' user '"
								+ user1.getUsername() + "' ***");
			} else {
				System.out
						.println("*** [InitializationManager] friendship already inserted ***");
			}
		} catch (NoResultException exc) {
		}
	}

}
