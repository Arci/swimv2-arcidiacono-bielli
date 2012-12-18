package it.polimi.swim.servlet;

import it.polimi.swim.model.Ability;
import it.polimi.swim.model.User;
import it.polimi.swim.session.AdminManagerRemote;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListServlet extends HttpServlet {

	private static final long serialVersionUID = -8417343838002808981L;

	public ListServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showList(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showList(request, response);
	}

	private void showList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<html><title>Get list</title>"
				+ "<body><h1>Database records:</h1>");

		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("AdminManager/remote");
			AdminManagerRemote adminManager = (AdminManagerRemote) ref;
			List<Ability> abilities = adminManager.getAbilities();
			out.println("Abilities:</br>");
			out.println("<ol>");
			for (Ability ability : abilities) {
				out.println("<li><a href='./ability.html?name="
						+ ability.getName() + "'>" + ability.getName()
						+ "</a></li>");
			}
			out.println("</ol>");
			out.println("Users:</br>");
			List<User> users = adminManager.getUsers();
			out.println("<ol>");
			for (User user : users) {
				out.println("<li><a href='./users.html?name="
						+ user.getUsername() + "'>" + user.getName() + " "
						+ user.getSurname() + "</a></li>");
			}
			out.println("</ol>");

		} catch (NamingException e) {
			e.printStackTrace();
		}
		out.println("</body></html>");
	}

}
