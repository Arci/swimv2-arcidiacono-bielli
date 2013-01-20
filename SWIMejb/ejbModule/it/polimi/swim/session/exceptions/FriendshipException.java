package it.polimi.swim.session.exceptions;

/**
 * FriendshipException
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
public class FriendshipException extends Exception {

	private static final long serialVersionUID = 7642939581449795539L;

	private String message;

	public FriendshipException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
