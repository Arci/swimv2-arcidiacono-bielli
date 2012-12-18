package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AdminManagerRemote {

	public void initDb();
	
	public List<Ability> getAbilities();
	
	public List<User> getUsers();
	
	public void insertUser(User user);
	
	public void insertAbility(Ability abiity);
	
	public void addAbilityToUser(User user, Ability ability);
	
	public void removeAbilityToUser(User user, Ability ability);
	
	public Ability loadAbility(String name);
	
	public User loadUserByUsername(String username);
	
}
