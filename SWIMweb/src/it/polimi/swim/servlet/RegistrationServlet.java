package it.polimi.swim.servlet;

import it.polimi.swim.servlet.exception.DuplicatedRegistrationParameterException;
import it.polimi.swim.servlet.exception.MissingRegistrationParametersException;
import it.polimi.swim.session.ProfileManagerRemote;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
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
		//TODO do get
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
			
			this.checkParameters(request, response, profileManager);
			if(!request.getParameter("password").equals(request.getParameter("checkPassword"))){
				request.setAttribute("passwordError", new String("passwordError"));
				
				getServletConfig().getServletContext()
				.getRequestDispatcher("/registration.jsp")
				.forward(request, response);
				
				return;
			}
			
			Map <String, Object> parameters = new Hashtable <String, Object>();
			
			parameters.put("name", request.getParameter("name"));
			parameters.put("surname", request.getParameter("surname"));
			parameters.put("username", request.getParameter("username"));
			parameters.put("email", request.getParameter("email"));
			parameters.put("password", request.getParameter("password"));
			
			profileManager.insertNewUser(parameters);
			
			getServletConfig().getServletContext()
			.getRequestDispatcher("/accessManager")
			.forward(request, response);
			
		} catch (NamingException e){
			e.printStackTrace();
		} catch (MissingRegistrationParametersException e){
			request.setAttribute("missingParameters", e.getMissingParameters());
			getServletConfig().getServletContext()
			.getRequestDispatcher("/registration.jsp")
			.forward(request, response);
		} catch (DuplicatedRegistrationParameterException e){
			request.setAttribute("duplicatedParameters", e.getDuplicatedParameters());
			getServletConfig().getServletContext()
			.getRequestDispatcher("/registration.jsp")
			.forward(request, response);
		}
	}

	private void checkParameters(HttpServletRequest request,
			HttpServletResponse response, ProfileManagerRemote profileManager) 
			throws MissingRegistrationParametersException, DuplicatedRegistrationParameterException{
		
		MissingRegistrationParametersException e1 = new MissingRegistrationParametersException();
		DuplicatedRegistrationParameterException e2 = new DuplicatedRegistrationParameterException();
	
		if ((String) request.getParameter("name") == null ||
				(String) request.getParameter("name") == ""){
			e1.addMissingParameter("name");
		}
		
		if ((String) request.getParameter("username") == null ||
				(String) request.getParameter("username") == ""){
			e1.addMissingParameter("username");
		}else if(!profileManager.isUsernameUnique((String) request.getParameter("username"))){
			e2.addDuplicatedParameter("username");
		}
		
		if ((String) request.getParameter("email") == null ||
				(String) request.getParameter("email") == ""){
			e1.addMissingParameter("email");
		}else if(!profileManager.isEmailUnique((String) request.getParameter("email"))){
			e2.addDuplicatedParameter("email");
		}
		
		if ((String) request.getParameter("password") == null ||
				(String) request.getParameter("password") == ""){	
			e1.addMissingParameter("password");
		}		
	
		if ((String) request.getParameter("checkPassword") == null ||
				(String) request.getParameter("checkPassword") == ""){	
			e1.addMissingParameter("checkPassword");
		}
		
		if (e1.hasMissingParameters()){
			throw e1;
		}
		
		if (e2.hasDuplicatedParameters()){
			throw e2;
		}
	}
}
