package it.polimi.swim.servlet;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.AdminManagerRemote;

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

public class AddAbilityServlet extends HttpServlet {

	private static final long serialVersionUID = -7715945730586850060L;

	public AddAbilityServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<html><title>Inserting</title>"
				+ "<body><h1>Inserting an ability to a user:</h1>");
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("AdminManager/remote");
			AdminManagerRemote adminManager = (AdminManagerRemote) ref;

			Ability ability = adminManager.loadAbility((String) request
					.getParameter("ability"));
			User user = adminManager.loadUserByUsername((String) request
					.getParameter("user"));
			if (ability == null || user == null) {
				out.println("<b>Problems loading entities</b>");
				return;
			}
			adminManager.addAbilityToUser(user, ability);

			User userRefreshed = adminManager
					.loadUserByUsername((String) request.getParameter("user"));
			out.println("L'utente " + userRefreshed.getUsername()
					+ " ha le seguenti abilitˆ:");
			for (Ability a : userRefreshed.getAbilities()) {
				out.println("<li>" + a.getName() + "</li>");
			}
			out.println("</ol>");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		out.println("</body></html>");
	}

}
