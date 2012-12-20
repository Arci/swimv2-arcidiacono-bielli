package it.polimi.swim.servlet;

import it.polimi.swim.model.User;
import it.polimi.swim.session.AccessManagerRemote;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessServlet extends HttpServlet {

	private static final long serialVersionUID = -7715945730586850060L;

	public AccessServlet() {
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
			Object ref = jndiContext.lookup("AccessManager/remote");
			AccessManagerRemote accessManager = (AccessManagerRemote) ref;
			if (request.getParameter("logout") != null
					&& request.getParameter("logout") == "logout") {
				request.getSession().invalidate();
				response.sendRedirect("./home.jsp");
			}
			User user = accessManager.checkLogin(
					request.getParameter("username"),
					request.getParameter("password"));
			if (user == null) {
				request.getSession().invalidate();
				response.sendRedirect("./home.jsp");
			} else {
				request.getSession().setAttribute("User", user);
				response.sendRedirect("./home.jsp");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
