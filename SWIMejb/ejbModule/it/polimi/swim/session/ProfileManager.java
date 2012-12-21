package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.Hashtable;
import java.util.List;

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

	@Override
	public User updateProfile(User user, Hashtable<String, String> params) {
		// TODO controllare che username
		return user;
	}

	@Override
	public boolean isUsernameUnique(String username) {
		// TODO
		return true;
	}

	@Override
	public boolean isEmailUnique(String email) {
		// TODO
		return true;
	}

}
