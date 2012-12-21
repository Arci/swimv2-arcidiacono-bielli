package it.polimi.swim.session;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ProfileManagerRemote {

	public List<Ability> getAbilityList();

	public User updateProfile(User user, Hashtable<String, String> params);

	public boolean isUsernameUnique(String username);
	
	public boolean isEmailUnique(String email);

}
