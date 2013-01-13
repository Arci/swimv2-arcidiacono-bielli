package it.polimi.swim.session;
import javax.ejb.Remote;


@Remote
public interface InitializationManagerRemote {

	public void addFakeUsers();

	public void addFakeAbilities();

	public void addAbilitiesToUsers();

	public void addFakeHelpRequest();

	public void addFakeFriendship();

}
