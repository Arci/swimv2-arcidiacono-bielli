package it.polimi.swim.servlet.exception;

import java.util.HashSet;
import java.util.Set;

public class RegistrationParametersException extends Exception {

	private static final long serialVersionUID = 5717303931152442292L;

	private Set<String> missingParameters = new HashSet<String>();
	private Set<String> duplicatedParameters = new HashSet<String>();

	public RegistrationParametersException() {
		super();
	}

	public void addDuplicatedParameter(String parameter) {
		duplicatedParameters.add(parameter);
	}

	public void addMissingParameter(String parameter) {
		missingParameters.add(parameter);
	}

	public boolean hasDuplicatedParameters() {

		if (duplicatedParameters.isEmpty()) {
			return false;
		}

		return true;
	}

	public boolean hasMissingParameters() {

		if (missingParameters.isEmpty()) {
			return false;
		}

		return true;
	}

	public Set<String> getMissingParameters() {
		return missingParameters;
	}

	public Set<String> getDuplicatedParameters() {
		return duplicatedParameters;
	}
}
