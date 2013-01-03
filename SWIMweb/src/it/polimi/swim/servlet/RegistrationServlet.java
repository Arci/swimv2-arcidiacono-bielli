package it.polimi.swim.servlet;

import it.polimi.swim.servletException.MissingRegistrationParametersException;
import it.polimi.swim.session.ProfileManagerRemote;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet{
	
	private static final long serialVersionUID = 5019136891262592380L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		//TODO DoGet
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		try{
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
	
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
		
			ProfileManagerRemote profileManager = (ProfileManagerRemote)ref;
			
			this.checkParameters(request, response);
			
			if(profileManager.isUsernameUnique((String) request.getParameter("username")) && 
				profileManager.isEmailUnique((String) request.getParameter("email"))){
				
				
			}			
			
		} catch (NamingException e){
			e.printStackTrace();
		} catch (MissingRegistrationParametersException e){
			request.setAttribute("missingParameters", e.getMissingParameters());
			getServletConfig().getServletContext()
			.getRequestDispatcher("/registration.jsp")
			.forward(request, response);
		}
	}

	private void checkParameters(HttpServletRequest request,
			HttpServletResponse response) throws MissingRegistrationParametersException{
		
		MissingRegistrationParametersException e = new MissingRegistrationParametersException();
	
		if ((String) request.getParameter("name") == null ||
				(String) request.getParameter("name") == ""){
			
			e.addMissingParameter("name");
		}
		
		if ((String) request.getParameter("username") == null ||
				(String) request.getParameter("username") == ""){
			
			e.addMissingParameter("username");
		}
		
		if ((String) request.getParameter("email") == null ||
				(String) request.getParameter("email") == ""){
			
			e.addMissingParameter("email");
		}
		
		if ((String) request.getParameter("password") == null ||
				(String) request.getParameter("password") == ""){
			
			e.addMissingParameter("password");
		}		
	
		if ((String) request.getParameter("checkPassword") == null ||
				(String) request.getParameter("checkPassword") == ""){
			
			e.addMissingParameter("checkPassword");
		}
		
		if (e.hasMissingParameters()){
			throw e;
		}
	}
}
