package it.polimi.swim.session;

import java.util.List;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AbilityManager implements AbilityManagerRemote,
		AbilityManagerLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public AbilityManager() {
		super();
	}

	@Override
	public void insertSuggestion(User user, String text) {
		AbilityRequest request = new AbilityRequest();
		request.setUser(user);
		request.setText(text);
		request.setState(RequestState.PENDING);
		manager.persist(request);
	}

	@Override
	public User reloadUser(User user) {
		try {
			return manager.find(User.class, user.getId());
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public boolean existAbility(String name) {
		try {
			Query q = manager.createQuery("FROM Ability a WHERE a.name=:name");
			q.setParameter("name", name);
			Ability ability = (Ability) q.getSingleResult();
			System.out.println("*** [ProfileManager] ability '" + name
					+ "' found ***");
			if (ability != null) {
				return true;
			} else {
				return false;
			}
		} catch (NoResultException exc) {
			System.out.println("*** [ProfileManager] ability '" + name
					+ "' not found ***");
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Ability> getAbilityList() {
		try {
			Query q = manager.createQuery("FROM Ability a");
			List<Ability> abilities = (List<Ability>) q.getResultList();
			System.out.println("*** [ProfileManager] abilities found ***");
			return abilities;
		} catch (NoResultException exc) {
			System.out.println("*** [ProfileManager] abilities not found ***");
		}
		return null;
	}

}
