package it.polimi.swim.session.exceptions;

public class AbilitySuggestionException extends Exception {

	private static final long serialVersionUID = -4262048111212224583L;
	
	private String message;

	public AbilitySuggestionException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
