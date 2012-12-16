package it.polimi.swim.servlet;
import it.polimi.swim.session.UserManagerRemote;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 5431182455535450291L;

	public InitServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initialization(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initialization(request, response);
	}
	
	private void initialization(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<html><title>Databse Initializzation</title>" +
				"<body><h1>Inserting some user</h1>");

		try {
				Hashtable<String,String> env = new Hashtable<String,String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
				env.put(Context.PROVIDER_URL,"localhost:1099");
				InitialContext jndiContext = new InitialContext(env);
				Object ref = jndiContext.lookup("UserManager/remote");
				UserManagerRemote userManager = (UserManagerRemote) ref;
				userManager.initDb();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		out.println("Inserted!</body></html>");
	}


}
