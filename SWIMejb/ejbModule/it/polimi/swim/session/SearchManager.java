package it.polimi.swim.session;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.User;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SearchManager implements SearchManagerRemote, SearchManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public SearchManager() {
		super();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchPerson(String pattern) {
		try {
			Query q = manager
					.createQuery("FROM User u WHERE (u.username=:pattern OR "
							+ "u.name=:pattern OR u.surname=:pattern OR u.email=:pattern) "
							+ "AND u.type=:normal");
			q.setParameter("pattern", pattern);
			q.setParameter("normal", UserType.NORMAL);
			List<User> result = (List<User>) q.getResultList();
			return result;
		} catch (NoResultException exc) {
			System.out.println("*** [SearchManager] no results ***");
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchAbility(String pattern) {
		try {
			Query q = manager
					.createQuery("SELECT OBJECT(u) FROM User u JOIN u.abilities AS a WHERE a.name=:pattern AND u.type=:normal");
			q.setParameter("pattern", pattern);
			q.setParameter("normal", UserType.NORMAL);
			List<User> result = (List<User>) q.getResultList();
			return result;
		} catch (NoResultException exc) {
			System.out.println("*** [SearchManager] no results ***");
			return null;
		}
	}

}
