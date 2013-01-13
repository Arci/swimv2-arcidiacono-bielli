package it.polimi.swim.servlet;
import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.FriendsManagerRemote;
import it.polimi.swim.session.ProfileManagerRemote;
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


public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = -4806101806094670071L;

	public ProfileServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (!haveToShowSessionUserProfile(request, response)) {
			try {
				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.jnp.interfaces.NamingContextFactory");
				env.put(Context.PROVIDER_URL, "localhost:1099");
				InitialContext jndiContext = new InitialContext(env);
				Object ref = jndiContext.lookup("ProfileManager/remote");
				ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;
				ref = jndiContext.lookup("FriendsManager/remote");
				FriendsManagerRemote friendsManager = (FriendsManagerRemote) ref;

				User userLoaded = profileManager.getUserByUsername(request
						.getParameter("username"));
				request.setAttribute("userLoaded", userLoaded);
				getUserInformation(userLoaded, request, response);

				if (friendsManager.areFriends((User) request.getSession()
						.getAttribute("User"), userLoaded)) {
					request.setAttribute("friendState", "friend");
				} else if (friendsManager.isRequestPending((User) request
						.getSession().getAttribute("User"), userLoaded)
						|| friendsManager.isRequestPending(userLoaded,
								(User) request.getSession()
										.getAttribute("User"))) {
					request.setAttribute("friendState", "pending");
				}
			} catch (NamingException e) {
				request.setAttribute("error", "can't reach the server");
			} catch (UserException ue) {
				request.setAttribute("error", ue.getMessage());
			}
			request.setAttribute("from", request.getParameter("from"));
			System.out
					.println("*** [ProfileServlet] forwarding to profile.jsp ***");
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/profile.jsp")
					.forward(request, response);
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
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO manage modify profile
		// then reload user and replace it in the session
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			User user = (User) request.getSession().getAttribute("User");

			Hashtable<String, String> params = new Hashtable<String, String>();
			params.put("name", request.getParameter("name"));
			params.put("surname", request.getParameter("surname"));
			params.put("username", request.getParameter("username"));
			params.put("email", request.getParameter("email"));
			params.put("city", request.getParameter("city"));
			params.put("phone", request.getParameter("phone"));
			// params.put("password", request.getParameter("password"));

			request.getSession().removeAttribute("User");
			request.getSession().setAttribute("User",
					profileManager.updateProfile(user, params));
			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/modifyProfile.jsp")
					.forward(request, response);

		} catch (NamingException e) {
			e.printStackTrace();
		}
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
			System.out.println("*** [ProfileServlet] user rating set ***");
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

	private boolean haveToShowSessionUserProfile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("username") != null
				&& request.getParameter("username") != "") {
			System.out.println("*** [ProfileServlet] doGet, show '"
					+ request.getParameter("username") + "' profile ***");
			return false;
		} else {
			return true;
		}
	}
}
