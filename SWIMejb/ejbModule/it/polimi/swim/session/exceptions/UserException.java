package it.polimi.swim.session.exceptions;

public class UserException extends Exception {

	private static final long serialVersionUID = 8282131031427780586L;

	private String message;

	public UserException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
