package it.polimi.swim.session;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class AbilitySuggestion implements AbilitySuggestionRemote,
		AbilitySuggestionLocal {
	@PersistenceContext(unitName = "SwimPU")
	EntityManager manager;

	public AbilitySuggestion() {
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

}
