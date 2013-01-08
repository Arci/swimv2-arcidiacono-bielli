package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.session.exceptions.AbilityException;

import javax.ejb.Local;

@Local
public interface AbilityManagerLocal {

	/**
	 * Search for an ability starting from the name passed
	 * 
	 * @param name
	 * @return null if no ability exist, the ability otherwise
	 * @throws AbilityException
	 */
	public Ability getAbilityByName(String name) throws AbilityException;

}
