package it.polimi.swim.servlet;

import it.polimi.swim.model.User;
import it.polimi.swim.session.AbilitySuggestionRemote;

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
		System.out
				.println("*** [AbilitySuggestionServlet] doGet - forwarding to abilitySuggestion.jsp ***");
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/abilitySuggestion.jsp")
				.forward(request, response);
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
				Object ref = jndiContext.lookup("AbilitySuggestion/remote");
				AbilitySuggestionRemote abilitySuggestion = (AbilitySuggestionRemote) ref;

				User user = (User) request.getSession().getAttribute("User");
				abilitySuggestion.insertSuggestion(user,
						request.getParameter("suggestion"));
				user = abilitySuggestion.reloadUser(user);
				request.getSession().removeAttribute("User");
				request.getSession().setAttribute("User", user);

			} catch (NamingException e) {
				e.printStackTrace();
			}
			System.out.println("*** [AbilitySuggestionServlet] suggestion '"
					+ request.getParameter("suggestion") + "' added ***");
			request.setAttribute("message", "your suggestion has been recorded");
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/abilitySuggestion.jsp")
					.forward(request, response);
		} else {
			System.out
					.println("*** [AbilitySuggestionServlet] no suggestion forwarding to abilitySuggestion.jsp ***");
			request.setAttribute("error", "suggestion can not be empty");
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/abilitySuggestion.jsp")
					.forward(request, response);
		}
	}

}
