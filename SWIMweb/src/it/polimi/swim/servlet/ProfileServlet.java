package it.polimi.swim.servlet;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
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

public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = -4806101806094670071L;

	public ProfileServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("username") != null
				&& request.getParameter("username") != "") {
			System.out.println("*** [ProfileServlet] doGet, show '"
					+ request.getParameter("username") + "' profile ***");
			try {
				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.jnp.interfaces.NamingContextFactory");
				env.put(Context.PROVIDER_URL, "localhost:1099");
				InitialContext jndiContext = new InitialContext(env);
				Object ref = jndiContext.lookup("ProfileManager/remote");
				ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

				User userLoaded = profileManager
						.getUserByUsername((String) request
								.getParameter("username"));
				request.setAttribute("userLoaded", userLoaded);
				getUserInformation(userLoaded, request, response);
				System.out
						.println("*** [ProfileServlet] forwarding to profile.jsp ***");
				getServletConfig().getServletContext()
						.getRequestDispatcher("/user/profile.jsp")
						.forward(request, response);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else {
			System.out
					.println("*** [ProfileServlet] doGet, no params, show current user profile ***");
			getUserInformation(
					(User) request.getSession().getAttribute("User"), request,
					response);
			System.out
					.println("*** [ProfileServlet] forwarding to profile.jsp ***");
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/profile.jsp")
					.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out
				.println("*** [ProfileServlet] doPost, show current user profile ***");
		getUserInformation((User) request.getSession().getAttribute("User"),
				request, response);
		System.out
				.println("*** [ProfileServlet] forwarding to profile.jsp ***");
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/profile.jsp")
				.forward(request, response);
	}

	private void getUserInformation(User user, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			Double rating = profileManager.getUserRating(user);
			request.setAttribute("rating", (int) Math.rint(rating));
			System.out.println("*** [ProfileServlet] rating set ***");
			for (Ability ability : user.getAbilities()) {
				Double abilityRating = profileManager.getAbilityRating(user,
						ability);
				request.setAttribute(ability.getName(),
						(int) Math.rint(abilityRating));
			}
			System.out.println("*** [ProfileServlet] abilities rating set ***");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
