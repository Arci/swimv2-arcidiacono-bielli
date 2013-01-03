package it.polimi.swim.servletException;

import java.util.HashSet;
import java.util.Set;

public class MissingRegistrationParametersException extends Exception {
	
	private static final long serialVersionUID = 5717303931152442292L;
	
	private Set <String> missingParameters = new HashSet <String>();
	
	public MissingRegistrationParametersException() {
		super ();
	}
	
	public void addMissingParameter(String parameter){
		missingParameters.add(parameter);
	}
	
	public boolean hasMissingParameters(){
		
		if (missingParameters.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	public Set <String> getMissingParameters(){
		return missingParameters;
	}
}
