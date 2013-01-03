package it.polimi.swim.servlet;

import it.polimi.swim.session.RegistrationManagerRemote;

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
			Object ref = jndiContext.lookup("RegistrationManager/remote");
		
			RegistrationManagerRemote registrationManager = (RegistrationManagerRemote)ref;
			
			if(registrationManager.isUsernameUnique((String) request.getParameter("username")))
				System.out.println("*** [RegistrationServlet] username is unique ***");
			
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
}
