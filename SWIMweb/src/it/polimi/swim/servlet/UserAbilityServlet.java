package it.polimi.swim.servlet;

import it.polimi.swim.model.User;
import it.polimi.swim.session.ProfileManagerRemote;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.UserException;

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

public class UserAbilityServlet extends HttpServlet {

	private static final long serialVersionUID = 3844627354531335877L;

	public UserAbilityServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// not to implement
		sendError("wrong http mode", request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("User");
		boolean haveModifyedSomething = false;
		if (request.getParameterValues("remove") != null
				&& request.getParameterValues("remove").length > 0) {
			String[] remove = request.getParameterValues("remove");
			for (int i = 0; i < remove.length; i++) {
				System.out.println("[UserAbilityServlet] remove ability: "
						+ remove[i]);
				removeAbility(remove[i], user, request, response);
				System.out.println("[UserAbilityServlet] remove ability: "
						+ remove[i]);
			}
			haveModifyedSomething = true;
			System.out.println("[UserAbilityServlet] abilities removed");
		}
		if (request.getParameterValues("add") != null
				&& request.getParameterValues("add").length > 0) {
			String[] add = request.getParameterValues("add");
			for (int i = 0; i < add.length; i++) {
				System.out.println("[UserAbilityServlet] add ability: "
						+ add[i]);
				addAbility(add[i], user, request, response);
				System.out.println("[UserAbilityServlet] add ability: "
						+ add[i]);
			}
			haveModifyedSomething = true;
			System.out.println("[UserAbilityServlet] abilities added");
		}
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext;
			jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			user = profileManager.reloadUser(user);
			request.getSession().removeAttribute("User");
			request.getSession().setAttribute("User", user);

			if (haveModifyedSomething) {
				request.setAttribute("result",
						"abilities chenges applyed succesfull!");
			}
			System.out.println("[UserAbilityServlet] user reloaded");

		} catch (NamingException e) {
			request.setAttribute("error", "can't reach the server!");
		}

		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/modifyProfile.jsp")
				.forward(request, response);
	}

	private void removeAbility(String ability, User user,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext;
			jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			profileManager.removeAbility(user.getUsername(), ability);

		} catch (NamingException e) {
			request.setAttribute("error", "can't reach the server!");
		} catch (AbilityException ae) {
		} catch (UserException ue) {
			request.setAttribute("error", ue.getMessage());
		}
	}

	private void addAbility(String ability, User user,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext;
			jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			profileManager.addAbility(user.getUsername(), ability);

		} catch (NamingException e) {
			request.setAttribute("error", "can't reach the server!");
		} catch (AbilityException ae) {
		} catch (UserException ue) {
			request.setAttribute("error", ue.getMessage());
		}
	}

	private void sendError(String message, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<response>");
		out.println("<result>KO</result>");
		out.println("<error>" + message + "</error>");
		out.println("</response>");
	}
}
