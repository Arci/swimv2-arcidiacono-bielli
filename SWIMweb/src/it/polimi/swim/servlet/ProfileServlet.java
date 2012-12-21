package it.polimi.swim.servlet;

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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			User user = (User) request.getSession().getAttribute("User");
			if (user == null) {
				System.out.println("*** [ProfileServlet] user not in session ***");
				response.sendRedirect("/SWIMweb");
			}
			Hashtable<String, String> params = new Hashtable<String, String>();
			if (request.getSession().getAttribute("name") != null) {
				params.put("name",
						(String) request.getSession().getAttribute("name"));
			} else {
				System.out.println("*** [ProfileServlet] not name specified ***");
				// TODO redirect con error
			}
			if (request.getSession().getAttribute("surname") != null) {
				params.put("surname", (String) request.getSession()
						.getAttribute("surname"));
			}
			if (request.getSession().getAttribute("username") != null) {
				if (!user.getUsername().equals(
						request.getSession().getAttribute("username"))
						&& profileManager.isUsernameUnique((String) request
								.getSession().getAttribute("username"))) {
					params.put("username", (String) request.getSession()
							.getAttribute("username"));
				} else {
					System.out.println("*** [ProfileServlet] not unique username ***");
					// TODO redirect con error
				}
			} else {
				System.out.println("*** [ProfileServlet] not username spedified ***");
				// TODO redirect con error
			}
			if (request.getSession().getAttribute("email") != null) {
				if (!user.getEmail().equals(
						request.getSession().getAttribute("email"))
						&& profileManager.isEmailUnique((String) request
								.getSession().getAttribute("email"))) {
					params.put("email", (String) request.getSession()
							.getAttribute("email"));
				} else {
					System.out.println("*** [ProfileServlet] not unique email ***");
					// TODO redirect con error
				}
			} else {
				System.out.println("*** [ProfileServlet] not email specified ***");
				// TODO redirect con error
			}
			if (request.getSession().getAttribute("city") != null) {
				params.put("city",
						(String) request.getSession().getAttribute("city"));
			}
			if (request.getSession().getAttribute("phone") != null) {
				params.put("phone",
						(String) request.getSession().getAttribute("phone"));
			}
			// TODO abilitˆ
			user = profileManager.updateProfile(user, params);

			request.getSession().setAttribute("User", user);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
