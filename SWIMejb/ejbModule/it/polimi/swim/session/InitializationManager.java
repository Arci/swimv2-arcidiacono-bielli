package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

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
		String normalUsername = "mouse";
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

}
