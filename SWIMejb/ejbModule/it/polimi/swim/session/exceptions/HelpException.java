package it.polimi.swim.session.exceptions;

public class HelpException extends Exception {

	private static final long serialVersionUID = 8832782238654562243L;

	private String message;

	public HelpException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
