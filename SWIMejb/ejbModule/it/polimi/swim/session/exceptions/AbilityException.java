package it.polimi.swim.session.exceptions;

public class AbilityException extends Exception {

	private static final long serialVersionUID = -4262048111212224583L;

	private String message;

	public AbilityException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
