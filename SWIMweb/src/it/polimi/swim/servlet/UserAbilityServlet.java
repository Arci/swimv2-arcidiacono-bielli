package it.polimi.swim.servlet;

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
		if (haveToAddAbility(request, response)) {
			System.out.println("*** [UserAbilityServlet] add ability ***");
			addAbility(request, response);
		} else if (haveToRemoveAbility(request, response)) {
			System.out.println("*** [UserAbilityServlet] remove ability ***");
			removeAbility(request, response);
		} else {
			sendError("operation not recognized", request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// not to implement
		sendError("wrong http mode", request, response);
	}

	private boolean haveToAddAbility(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("action") != null
				&& request.getParameter("action") != ""
				&& request.getParameter("action").equals("add")) {
			return true;
		}
		return false;
	}

	private void addAbility(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<response>");
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			profileManager.addAbility(request.getParameter("username"),
					request.getParameter("ability"));

			out.println("<result>OK</result>");
		} catch (UserException uex) {
			out.println("<result>KO</result>");
			out.println("<error>" + uex.getMessage() + "</error>");
		} catch (NamingException nex) {
			out.println("<result>KO</result>");
			out.println("<error>can't reach the server</error>");
		} catch (AbilityException ae) {
			out.println("<result>KO</result>");
			out.println("<error>" + ae.getMessage() + "</error>");
		}
		out.println("</response>");
	}

	private boolean haveToRemoveAbility(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("action") != null
				&& request.getParameter("action") != ""
				&& request.getParameter("action").equals("remove")) {
			return true;
		}
		return false;
	}

	private void removeAbility(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<response>");
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("ProfileManager/remote");
			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			profileManager.removeAbility(request.getParameter("username"),
					request.getParameter("ability"));

			out.println("<result>OK</result>");
		} catch (UserException uex) {
			out.println("<result>KO</result>");
			out.println("<error>" + uex.getMessage() + "</error>");
		} catch (NamingException nex) {
			out.println("<result>KO</result>");
			out.println("<error>can't reach the server</error>");
		} catch (AbilityException ae) {
			out.println("<result>KO</result>");
			out.println("<error>" + ae.getMessage() + "</error>");
		}
		out.println("</response>");

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
