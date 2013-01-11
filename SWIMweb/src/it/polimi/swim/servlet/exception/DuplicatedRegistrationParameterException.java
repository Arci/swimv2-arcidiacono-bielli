package it.polimi.swim.servlet.exception;

import java.util.HashSet;
import java.util.Set;

public class DuplicatedRegistrationParameterException extends Exception {

	private static final long serialVersionUID = -8950334255751612227L;
	private Set<String> duplicatedParameters = new HashSet<String>();

	public DuplicatedRegistrationParameterException() {
		super();
	}

	public void addDuplicatedParameter(String parameter) {
		duplicatedParameters.add(parameter);
	}

	public boolean hasDuplicatedParameters() {

		if (duplicatedParameters.isEmpty()) {
			return false;
		}

		return true;
	}

	public Set<String> getDuplicatedParameters() {
		return duplicatedParameters;
	}
}
