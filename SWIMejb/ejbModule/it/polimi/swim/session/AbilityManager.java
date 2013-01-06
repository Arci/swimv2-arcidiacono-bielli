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
			System.out.println("*** [AbilityManager] ability '" + name
					+ "' found ***");
			if (ability != null) {
				return true;
			} else {
				return false;
			}
		} catch (NoResultException exc) {
			System.out.println("*** [AbilityManager] ability '" + name
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
			System.out.println("*** [AbilityManager] abilities found ***");
			return abilities;
		} catch (NoResultException exc) {
			System.out.println("*** [AbilityManager] abilities not found ***");
		}
		return null;
	}

	@Override
	public Ability getAbilityByName(String name) {
		try {
			Query q = manager.createQuery("FROM Ability a WHERE a.name=:name");
			q.setParameter("name", name);
			Ability ability = (Ability) q.getSingleResult();
			System.out.println("*** [AbilityManager] ability '" + name
					+ "' found ***");
			return ability;
		} catch (NoResultException exc) {
			System.out.println("*** [AbilityManager] ability '" + name
					+ "' not found ***");
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AbilityRequest> getAbilityRequests() {
		try {
			Query q = manager.createQuery("FROM AbilityRequest a WHERE a.state='pending'");
			List <AbilityRequest> result = q.getResultList();
			if (result.size() > 0){
				System.out.println("*** [AbilityManager] there are ability requests ***");
				return result;
			} else throw new NoResultException();
		} catch (NoResultException e){
			System.out.println("*** [AbilityManager] there aren't ability requests ***");
		}
		
		return null;
	}

	@Override
	public void updateAbilityRequestState(String id, String state) {
		try {
			int abilityRequestID = Integer.parseInt(id);
			AbilityRequest request = manager.find(AbilityRequest.class, abilityRequestID);
			
			if (state.equals(RequestState.ACCEPTED.toString())){
				request.setState(RequestState.ACCEPTED);
				System.out.println("*** [AbilityManager] state of '" + request.getText() + "' became accepted ***");
				Ability ability =  new Ability();
				ability.setName(request.getText());
				manager.persist(ability);
				System.out.println("*** [AbilityManager] ability added'");
			} else if (state.equals(RequestState.REJECTED.toString())){
				request.setState(RequestState.REJECTED);
				System.out.println("*** [AbilityManager] state of '" + request.getText() + "' became refused ***");
			} else System.out.println("*** [AbilityManager] error state ***");
			manager.merge(request);
		
		} catch (NoResultException exc){
			System.out.println("*** [AbilityManager] ability request not found ***");
		}
	}

	@Override
	public void insertNewAbility(String name) {
		Ability ability = new Ability();
		ability.setName(name);
		manager.persist(ability);
		System.out.println("*** [AbilityManager] new ability added ***");
	}
}
