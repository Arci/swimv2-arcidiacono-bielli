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
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchPerson(String pattern, String cityPattern) {
		try {
			Query q = manager
					.createQuery("FROM User u WHERE (u.username=:pattern OR "
							+ "u.name=:pattern OR u.surname=:pattern OR u.email=:pattern) "
							+ "AND u.type=:normal AND u.city=:city");
			q.setParameter("pattern", pattern);
			q.setParameter("city", cityPattern);
			q.setParameter("normal", UserType.NORMAL);
			List<User> result = (List<User>) q.getResultList();
			return result;
		} catch (NoResultException exc) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchAbility(String pattern, String cityPattern) {
		try {
			Query q = manager
					.createQuery("SELECT OBJECT(u) FROM User u JOIN u.abilities AS a WHERE a.name=:pattern AND u.type=:normal AND u.city=:city");
			q.setParameter("pattern", pattern);
			q.setParameter("city", cityPattern);
			q.setParameter("normal", UserType.NORMAL);
			List<User> result = (List<User>) q.getResultList();
			return result;
		} catch (NoResultException exc) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchByCity(String cityPattern) {
		try {
			Query q = null;
			if (cityPattern.equals("allCities")) {
				q = manager
						.createQuery("SELECT OBJECT(u) FROM User u WHERE u.type=:normal");
			} else {
				q = manager
						.createQuery("SELECT OBJECT(u) FROM User u WHERE u.city=:city AND u.type=:normal");
				q.setParameter("city", cityPattern);
			}
			q.setParameter("normal", UserType.NORMAL);
			List<User> result = (List<User>) q.getResultList();
			return result;
		} catch (NoResultException exc) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getCities() {
		try {
			Query q = manager
					.createQuery("SELECT DISTINCT u.city FROM User u WHERE u.type=:normal");
			q.setParameter("normal", UserType.NORMAL);
			List<String> result = (List<String>) q.getResultList();
			return result;
		} catch (NoResultException exc) {
			return null;
		}
	}

}
