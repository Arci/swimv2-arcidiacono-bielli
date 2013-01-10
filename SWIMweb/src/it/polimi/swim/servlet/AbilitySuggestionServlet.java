package it.polimi.swim.servlet;

import it.polimi.swim.model.User;
import it.polimi.swim.session.AbilityManagerRemote;
import it.polimi.swim.session.ProfileManagerRemote;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.UserException;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbilitySuggestionServlet extends HttpServlet {

	private static final long serialVersionUID = 3844627354531335877L;

	public AbilitySuggestionServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Not to implement, redirect
		System.out
				.println("*** [AbilitySuggestionServlet] doGet, forwarding to abilitySuggestion.jsp ***");
		reloadUser(request, response);
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/abilitySuggestion.jsp")
				.forward(request, response);
	}

	private void reloadUser(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			User user = (User) request.getSession().getAttribute("User");

			user = profileManager.reloadUser(user);
			request.getSession().removeAttribute("User");
			request.getSession().setAttribute("User", user);

		} catch (UserException ue) {
			request.setAttribute("error", ue.getMessage());
		} catch (NamingException e) {
			request.setAttribute("error", "can't reach the server");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("suggestion") != null
				&& request.getParameter("suggestion") != "") {
			try {

				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.jnp.interfaces.NamingContextFactory");
				env.put(Context.PROVIDER_URL, "localhost:1099");
				InitialContext jndiContext = new InitialContext(env);
				User user = (User) request.getSession().getAttribute("User");

				Object ref = jndiContext.lookup("AbilityManager/remote");
				AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;

				abilityManager.insertSuggestion(user,
						request.getParameter("suggestion"));
				System.out
						.println("*** [AbilitySuggestionServlet] suggestion '"
								+ request.getParameter("suggestion")
								+ "' added ***");
				request.setAttribute("message",
						"your suggestion has been recorded");

			} catch (NamingException e) {
				request.setAttribute("error", "can't reach the server");
			} catch (AbilityException asex) {
				request.setAttribute("error", asex.getMessage());
			}
		} else {
			System.out
					.println("*** [AbilitySuggestionServlet] no suggestion, forwarding to abilitySuggestion.jsp ***");
			request.setAttribute("error", "suggestion can not be empty");
		}

		reloadUser(request, response);

		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/abilitySuggestion.jsp")
				.forward(request, response);

	}

}
